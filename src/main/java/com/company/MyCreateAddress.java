package com.company;

import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MyCreateAddress {
    private static final String[] KEYLIST = new String[]{"73b2384003933715b5f82f6ad596c9e1", "0624584145a7300d20658ed0136c0574", "6dccb792030125506839084d3d043ccf", "e1232d327633b1eb8c30e3e2dd6e9bd7", "da176d721803946e07a1836f52655c17", "54469f1110f68e41c19f62ba404483ef", "95d253f1924a063eb9acc0cf3dc1a6ef", "67ab0cc68238e2283161d9424ac1a55f", "d87aa8f6ed20be6e40030e25dbdf1786", "89f2b4f79ce1750628f6676ffe8000771"};
    private static int COUNT = 0;
    private static final String OUTPUT = "JSON";
    private static final String GET_ADDRESS_URL = "https://restapi.amap.com/v3/geocode/regeo";

    public static Map<String, String> getAddressByLonLat(double MinLon, double MaxLon, double MinLat, double MaxLat) {
        Map<String, String> lonLatMap = randomLonLat(MinLon, MaxLon, MinLat, MaxLat);
        Map<String, String> addressMap = new HashMap();
        String location = (String)lonLatMap.get("lon") + "," + (String)lonLatMap.get("lat");

        String response;
        boolean keyStatus;
        do {
            String url = "https://restapi.amap.com/v3/geocode/regeo ?output=JSON&location=" + location + "&key=" + KEYLIST[COUNT] + "&extensions=base";
            response = getResponse(url);
            keyStatus = checkKey(response);
        } while(!keyStatus);

        JSONObject responseJson = JSONObject.fromObject(response);
        String regeocode = responseJson.getString("regeocode");
        JSONObject regeocodeJson = JSONObject.fromObject(regeocode);
        String address = regeocodeJson.getString("formatted_address");
        String addressComponent = regeocodeJson.getString("addressComponent");
        JSONObject componentJson = JSONObject.fromObject(addressComponent);
        String province = componentJson.getString("province");
        String city = componentJson.getString("city");
        String district = componentJson.getString("district");
        String township = componentJson.getString("township");
        if (address.equals("[]")) {
            address = "";
        }

        if (province.equals("[]")) {
            province = "";
        }

        if (city.equals("[]")) {
            city = "";
        }

        if (district.equals("[]")) {
            district = "";
        }

        if (township.equals("[]")) {
            township = "";
        }

        addressMap.put("address", address);
        addressMap.put("province", province);
        addressMap.put("city", city);
        addressMap.put("district", district);
        addressMap.put("township", township);
        JSONObject streetJson = JSONObject.fromObject(componentJson.getString("streetNumber"));
        String streetNumber = "";
        if (!streetJson.getString("street").equals("[]")) {
            streetNumber = streetJson.getString("street") + streetJson.getString("number");
        }

        addressMap.put("streetNumber", streetNumber);
        return addressMap;
    }

    public static boolean checkKey(String response) {
        boolean keyStatus = true;
        JSONObject responseJson = JSONObject.fromObject(response);
        String status = responseJson.getString("infocode");
        if (status.equals("10000")) {
            keyStatus = true;
        } else {
            keyStatus = false;
            COUNT = (COUNT + 1) % 10;
        }

        return keyStatus;
    }

    public static String getResponse(String serverUrl) {
        StringBuffer result = new StringBuffer();

        try {
            URL url = new URL(serverUrl);
            URLConnection conn = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            String line;
            while((line = in.readLine()) != null) {
                result.append(line);
            }

            in.close();
        } catch (MalformedURLException var6) {
            var6.printStackTrace();
        } catch (IOException var7) {
            var7.printStackTrace();
        }

        return result.toString();
    }

    public static Map<String, String> randomLonLat(double MinLon, double MaxLon, double MinLat, double MaxLat) {
        Map<String, String> lonLatMap = new HashMap();
        new Random();
        BigDecimal db = new BigDecimal(Math.random() * (MaxLon - MinLon) + MinLon);
        String lon = db.setScale(6, 4).toString();
        lonLatMap.put("lon", lon);
        db = new BigDecimal(Math.random() * (MaxLat - MinLat) + MinLat);
        String lat = db.setScale(6, 4).toString();
        lonLatMap.put("lat", lat);
        return lonLatMap;
    }
}
