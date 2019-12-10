package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataTree {
    public ConcurrentHashMap<String,String> dataMap = new ConcurrentHashMap<String, String>();

    class Table {
        String tableName;
        Map<String, String> fields = new HashMap<>();


        public Table(String tableName) {
            this.tableName = tableName;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public Map<String, String> getFields() {
            return fields;
        }

        public void setFields(Map<String, String> fields) {
            this.fields = fields;
        }
        public void addField(String key, String value) {
            this.fields.put(key, value);
        }

    }

    public void put(String key,String value){
        dataMap.put(key,value);
    }

    public void put(String key,int value){
        dataMap.put(key,String.valueOf(value));
    }

    public String get(String key){
        return dataMap.get(key);
    }

    public int size(){
        return dataMap.size();
    }
    public Map<String, String> getDataMap(){
        return dataMap;
    }

    public Map<String,Table> getTables(){
        Map<String, Table> tables = new HashMap<>();
        for (Map.Entry<String, String> entry : dataMap.entrySet()) {

            String[] tableAndFieldName = entry.getKey().split("\\.");
            String tableName = tableAndFieldName[0];
            String fieldName = tableAndFieldName[1];
            Table t;
            if ((t = tables.get(tableName)) == null) {
                t = new Table(tableName);
            }
            t.addField(fieldName, entry.getValue());
            tables.put(tableName, t);
        }
        return tables;
    }

    public List<String> toSql() {
        List<String> resultList = new ArrayList<>();
        for (Table t :getTables().values()) {
            String sql = "insert into " +
                    t.getTableName() +
                    " (" + t.fields.keySet() + ")" +
                    " value (" + t.fields.values() + ");";
            resultList.add(sql.replace("[", "\"").replace("]", "\"").replace(", ", "\",\""));
        }
        return resultList;
    }
}
