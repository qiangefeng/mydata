package com.company;

import java.io.*;
import java.lang.reflect.Method;
import java.util.Properties;

public class MyConfig {

    public  Integer maxCount;
    public  Double minLongitude;
    public  Double maxLongitude;
    public  Double minLatitude;
    public  Double maxLatitude;
    public  String xlsxFile;
    public  String sheetName;
    public  String tableNameColumn;
    public  String fieldNameColumn;
    public  String expressColumn;
    private final static String configFile = "mydata.properties";
    private static MyConfig myConfig;

    public Integer getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

    public Double getMinLongitude() {
        return minLongitude;
    }

    public void setMinLongitude(Double minLongitude) {
        this.minLongitude = minLongitude;
    }

    public Double getMaxLongitude() {
        return maxLongitude;
    }

    public void setMaxLongitude(Double maxLongitude) {
        this.maxLongitude = maxLongitude;
    }

    public Double getMinLatitude() {
        return minLatitude;
    }

    public void setMinLatitude(Double minLatitude) {
        this.minLatitude = minLatitude;
    }

    public Double getMaxLatitude() {
        return maxLatitude;
    }

    public void setMaxLatitude(Double maxLatitude) {
        this.maxLatitude = maxLatitude;
    }

    public String getXlsxFile() {
        return xlsxFile;
    }

    public void setXlsxFile(String xlsxFile) {
        this.xlsxFile = xlsxFile;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getTableNameColumn() {
        return tableNameColumn;
    }

    public void setTableNameColumn(String tableNameColumn) {
        this.tableNameColumn = tableNameColumn;
    }

    public String getFieldNameColumn() {
        return fieldNameColumn;
    }

    public void setFieldNameColumn(String fieldNameColumn) {
        this.fieldNameColumn = fieldNameColumn;
    }

    public String getExpressColumn() {
        return expressColumn;
    }

    public void setExpressColumn(String expressColumn) {
        this.expressColumn = expressColumn;
    }

    public static MyConfig getMyConfig() {
        if (myConfig == null){
            try {
                InputStream in = new BufferedInputStream(MyConfig.class.getClassLoader().getResourceAsStream(configFile));
                Properties properties = new Properties();
                properties.load(in);
                myConfig = (MyConfig)properties2Object(properties,new MyConfig());
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }

        return myConfig;
    }

    private static Object properties2Object(final Properties p, final Object object) {
        Method[] methods = object.getClass().getMethods();
        for (Method method : methods) {
            String mn = method.getName();
            if (mn.startsWith("set")) {
                try {
                    String tmp = mn.substring(4);
                    String first = mn.substring(3, 4);

                    String key = first.toLowerCase() + tmp;
                    String property = p.getProperty(key);
                    if (property != null) {
                        Class<?>[] pt = method.getParameterTypes();
                        if (pt != null && pt.length > 0) {
                            String cn = pt[0].getSimpleName();
                            Object arg = null;
                            if (cn.equals("int") || cn.equals("Integer")) {
                                arg = Integer.parseInt(property);
                            } else if (cn.equals("long") || cn.equals("Long")) {
                                arg = Long.parseLong(property);
                            } else if (cn.equals("double") || cn.equals("Double")) {
                                arg = Double.parseDouble(property);
                            } else if (cn.equals("boolean") || cn.equals("Boolean")) {
                                arg = Boolean.parseBoolean(property);
                            } else if (cn.equals("float") || cn.equals("Float")) {
                                arg = Float.parseFloat(property);
                            } else if (cn.equals("String")) {
                                arg = property;
                            } else {
                                continue;
                            }
                            method.invoke(object, arg);
                        }
                    }
                } catch (Throwable ignored) {
                }
            }
        }
        return object;
    }
}
