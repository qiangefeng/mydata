package com.company;


import javassist.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.company.RunPut.addressFileName;
import static com.company.RunPut.myAddressRand;
import static java.lang.Thread.sleep;

public class Main {
    private String filePath;
    private String sheetName;
    private String tableNameCell;
    private String fieldNameCell;
    private String expressCell;
    private List<DataTree> allData = new ArrayList();
    private List<String> expressReadFromExcel; //try{}catch{};
    private Constructor RunPutConstructor;
    private int maxItemCount;

    public Main(String filePath, String sheetName, String tableNameCell, String fieldNameCell, String expressCell) {
        this.filePath = filePath;
        this.sheetName = sheetName;
        this.tableNameCell = tableNameCell;
        this.fieldNameCell = fieldNameCell;
        this.expressCell = expressCell;
    }

    public void writeAddress() throws IOException, InterruptedException {
        BufferedWriter fw = new BufferedWriter(new FileWriter(Main.class.getClassLoader().getResource(addressFileName).getPath(), true));
        for (int i = 0; i < 1000; i++) {
            fw.write(myAddressRand());
            fw.newLine();
            sleep(1000);
            if (i % 10 == 0) {
                fw.flush();
            }
        }
        fw.close();
    }


    private List<String> readExcel(Workbook workbook, String sheetName, String tableNameCell, String fieldNameCell, String expressCell) {
        List<String> expresses = new ArrayList<>();
        //2.获取要解析的表格
        Sheet sheet = workbook.getSheet(sheetName);
        //获得最后一行的行号
        int lastRowNum = sheet.getLastRowNum();
        for (int i = 0; i <= lastRowNum; i++) {//遍历每一行
            //3.获得要解析的行
            Row row = sheet.getRow(i);
            //4.获得每个单元格中的内容（String）
            String tableName = row.getCell(tableNameCell.toLowerCase().charAt(0) - 'a').getStringCellValue();
            String fieldName = row.getCell(fieldNameCell.toLowerCase().charAt(0) - 'a').getStringCellValue();
            String express = row.getCell(expressCell.toLowerCase().charAt(0) - 'a').getStringCellValue();
            if ((express == null) || "".equals(express)) {
                continue;
            }
            String key = tableName.toLowerCase() + "." + fieldName.toLowerCase();
            expresses.add(String.format("try{if(data.get(\"%s\") == null) data.put(\"%s\", %s); } catch (Exception e){}", key, key, express));
        }
        return expresses;
    }

    private List<String> getExpresses(String filePath, String sheetName, String tableNameCell, String fieldNameCell, String expressCell) throws Exception {
        String execlVersion = filePath.substring(filePath.lastIndexOf('.') + 1);
        Workbook workbook = null;
        switch (execlVersion) {
            case "xlsx":
                try {
                    workbook = new XSSFWorkbook(new FileInputStream(filePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case "xls":
                try {
                    workbook = new HSSFWorkbook(new FileInputStream(filePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("cant read *." + execlVersion);
                throw new Exception();
        }
        assert workbook != null;
        return readExcel(workbook, sheetName, tableNameCell, fieldNameCell, expressCell);

    }

    private Constructor getRunPutConstructor(List<String> expressReadFromExcel) {
        Constructor constructor = null;
        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass ctClass = pool.getCtClass("com.company.RunPut");
            CtMethod putData = ctClass.getDeclaredMethod("putData");
            for (String express : expressReadFromExcel) {
                putData.insertAfter(express);
            }
            constructor = ctClass.toClass().getDeclaredConstructor(new Class[]{List.class});
            constructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return constructor;
    }

    private void init() throws Exception {
        this.expressReadFromExcel = getExpresses(filePath, sheetName, tableNameCell, fieldNameCell, expressCell);
        this.RunPutConstructor = getRunPutConstructor(this.expressReadFromExcel);
        this.maxItemCount = 100;
    }

    private void start() throws InterruptedException, IllegalAccessException, InvocationTargetException, InstantiationException {
        ExecutorService ex = Executors.newFixedThreadPool(50);
        for (int i = 0; i < this.maxItemCount; i++) {
            ex.submit((Runnable) this.RunPutConstructor.newInstance(new Object[]{this.allData}));
        }

        ex.shutdown();
        while (true) {
            if (ex.isTerminated()) {
                break;
            }
            Thread.sleep(2000);
        }
    }

    public static void main(String[] args) {
        if (args.length < 5) {
            System.out.println("usage: jar Main.jar filePath sheetName tableNameCell fieldNameCell expressCell");
            return;
        }

        Main m = new Main(args[0], args[1], args[2], args[3], args[4]);
        try {
            m.init();
            m.start();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        for (DataTree dt : m.allData) {
            for (String sql : dt.toSql())
                System.out.println(sql);
        }
    }
}
