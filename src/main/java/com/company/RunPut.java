package com.company;

import com.cloudwise.sdg.dic.DicInitializer;
import com.cloudwise.sdg.dic.Dictionary;
import com.cloudwise.sdg.function.BuildInFuncs;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
public class RunPut implements Runnable {
    List<DataTree> allData;
    public RunPut(List<DataTree> allData) {
        this.allData = allData;
    }

    private void putData(DataTree data) {
        //rewrite by com.company.Main
    }

    public void run() {
        DataTree data = new DataTree();
        int lastSuccessNum = 0;
        while (true) {
            putData(data);
            if (lastSuccessNum == data.size()) {
                break;
            } else {
                lastSuccessNum = data.size();
            }

        }
        this.allData.add(data);

//        synchronized (lock) {
//            try {
//                BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(resultFilePath,true),"UTF-8"));
//                for (String sql : toSql(data)) {
//                    fw.write(sql);
//                    fw.newLine();
//                }
//                fw.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    static final String addressFileName = "address.list";
    static final String communitysOfBJFileName = "community_beijng.list";
    static final String policeOfBJFileName = "police_beijing.list";
    static String[] addressArray;
    static String[] communitysOfBJArray;
    static String[] policeOfBJArray;

    public static MyCreateAddress address = new MyCreateAddress();

    static{
        try {
            DicInitializer.init();
            addressArray = readFromFileToStringArray(addressFileName);
            communitysOfBJArray = readFromFileToStringArray(communitysOfBJFileName);
            policeOfBJArray = readFromFileToStringArray(policeOfBJFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static String[] readFromFileToStringArray(String fileName) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(RunPut.class.getClassLoader().getResourceAsStream(fileName),"UTF-8"));
        Set<String> readToSet = new HashSet<String>();
        String line;
        while ((line = reader.readLine()) != null){
            readToSet.add(line);
        }
        reader.close();
        String[] readTo = new String[readToSet.size()];
        return readToSet.toArray(readTo);
    }

    private static long random(long begin, long end) {
        long rtn = begin + (long)(Math.random() * (double)(end - begin));
        return rtn != begin && rtn != end ? rtn : random(begin, end);
    }

    /*formatStr: yyyy-MM-dd
                 yyyy-MM-dd HH:mm:ss */
    public static String dateBetween(String formatStr,String start,String end) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        Date startDate = format.parse(start);
        Date endDate = format.parse(end);
        long date = random(startDate.getTime(), endDate.getTime());
        return new SimpleDateFormat(formatStr).format(new Date(date));
    }
    /*formatStr: yyyy-MM-dd
                 yyyy-MM-dd HH:mm:ss */
    public static String dateBetween(String formatStr,String start,int days) throws ParseException {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        Date startDate = format.parse(start);
        c.setTime(startDate);
        c.add(Calendar.DAY_OF_MONTH,days);

        return new SimpleDateFormat(formatStr).format(c.getTime());
    }

    public static String dic(String key){
        String[] strArray = Dictionary.getDicStr(key).replace("|||",",").split(",");
        return strArray[intRand(0,strArray.length)];
    }

    public static String communityOfBeiJing(){
        return communitysOfBJArray[intRand(0,communitysOfBJArray.length)];
    }

    public static String policeOfBeiJing(){
        return policeOfBJArray[intRand(0,policeOfBJArray.length)];
    }

    private static Map<String, String> addressMapOfBeijing(){
        return MyCreateAddress.getAddressByLonLat(115.25D, 117.30D, 39.26D, 41.03D);
    }

    private static Map<String, String> addressMapFactory(){
        ArrayList<Map<String, String>> addressMaps = new ArrayList<Map<String, String>>();

        // add beijing
        addressMaps.add(addressMapOfBeijing());

        // add hangzuo
        //......

        return addressMaps.get(intRand(0,addressMaps.size()));
    }

    public static String myAddressRand0() {
        MyCreateAddress var10000 = address;
        String randAddress = null;
        while (randAddress == null){
            Map<String, String> addressMap = addressMapFactory();
            randAddress = addressMap.get("address");
            if (randAddress != null){
                break;
            }
        }
        return randAddress;
    }

    public static String myAddressRand() {
        return addressArray[intRand(0,addressArray.length)];
    }

    public static String dateOfMarriage(String birthDayOfMan,String deathDayOfMan,String birthDayOfWoman,String deathDayOfWoman) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date  birthDayOfMan_d= format.parse(birthDayOfMan);
        Date birthDayOfWoman_d = format.parse(birthDayOfWoman);
        Date deathDayOfMan_d = format.parse(deathDayOfMan);
        Date deathDayOfWoman_d = format.parse(deathDayOfWoman);

        Calendar c = Calendar.getInstance();

        c.setTime(birthDayOfMan_d);
        c.add(Calendar.YEAR,20);
        if(deathDayOfMan_d.before(c.getTime())){
            return "";
        }

        c.setTime(birthDayOfWoman_d);
        c.add(Calendar.YEAR,20);
        if(deathDayOfWoman_d.before(c.getTime())){
            return "";
        }

        Date startTime = birthDayOfMan_d.after(birthDayOfWoman_d)?birthDayOfMan_d:birthDayOfWoman_d;
        c.setTime(startTime);
        c.add(Calendar.YEAR,20);
        startTime=c.getTime();

        Date endTime = deathDayOfMan_d.before(deathDayOfWoman_d)?deathDayOfMan_d:deathDayOfWoman_d;

        if(startTime.after(endTime)){
            return "";
        }
        long marriageData = random(c.getTimeInMillis(),endTime.getTime());
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(marriageData));

    }
    public static int intRand() {
        return BuildInFuncs.intRand();
    }
    public static int intRand(int n) {
        return BuildInFuncs.intRand(n);
    }
    public static int intRand(int s, int e) {
        return BuildInFuncs.intRand(s,e);
    }
    public static String numRand(int n){return BuildInFuncs.numRand(n);}
    public static String idCardRand(int digit) {return BuildInFuncs.idCardRand(digit);}
    public static String nameRand() {return BuildInFuncs.nameRand();}
    public static String phoneNumberRand() {return BuildInFuncs.phoneNumberRand();}
    public static String dateRand(int dateType){return BuildInFuncs.dateRand(dateType);}
}
