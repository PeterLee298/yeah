package com.yeah.android.utils;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by litingchang on 15-11-11.
 */
public class SQLUtil {
    private static final String AND = " AND ";;
    private static final String CMD_AND = "(%s) AND (%s)";
    private static final String CMD_CREATE_TABLE = "CREATE TABLE %s (%s);";
    private static final String CMD_OR = "(%s) OR (%s)";
    private static final String CMD_SELECT = "%s=?";
    private static final String OR = " OR ";

    public static String and(String param1, String param2) {
        return fun(CMD_AND, param1, param2);
    }

    public static String or(String param1, String param2) {
        return fun(CMD_OR, param1, param2);
    }

    /**
     * 创建表
     * @param sQLiteDatabase
     * @param tableName
     * @param fields
     */
    public static void createTable(SQLiteDatabase sQLiteDatabase,
                                   String tableName, String fields) {
        String format = CMD_CREATE_TABLE;
        Object[] params = new Object[2];
        params[0] = tableName;
        params[1] = fields;
        String formatted = String.format(format, params);
        sQLiteDatabase.execSQL(formatted);
    }

    private static String fun(String format, String param1, String param2) {
        String formatted;
        if ((param1 != null) && (param2 != null)) {
            Object[] localObject = new Object[2];
            localObject[0] = param1;
            localObject[1] = param2;
            formatted = String.format(format, localObject);
        } else if (param1 != null) {
            formatted = param1;
        } else {
            formatted = param2;
        }

        return formatted;
    }

    public static String getSelection(String field) {
        Object[] params = new Object[1];
        params[0] = field;
        return String.format(CMD_SELECT, params);
    }

    public static String getSelectionAnd(String[] fields) {
        return getSelection(AND, fields);
    }

    public static String getSelectionOr(String[] fields) {
        return getSelection(OR, fields);
    }

    private static String getSelection(String operation,
                                       String[] fields) {

        int length;
        if (fields != null) {
            length = fields.length;
        } else {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();

        Object[] args = new Object[1];
        args[0] = fields[0];
        String str1 = String.format(CMD_SELECT, args);
        stringBuilder.append(str1);

        for (int n = 1; n < length; n++) {
            stringBuilder.append(operation);
            Object[] tmpargs = new Object[1];
            tmpargs[0] = fields[n];
            str1 = String.format(CMD_SELECT, tmpargs);
            stringBuilder.append(str1);
        }

        return stringBuilder.toString();
    }
}