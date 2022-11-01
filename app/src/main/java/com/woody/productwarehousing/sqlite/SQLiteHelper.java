package com.woody.productwarehousing.sqlite;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.util.List;

public class SQLiteHelper {
    //資料庫版本號
    private static final String DATA_BASE_NAME = "PWDB.db3";
    private static final String DB_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
    private static SQLiteDatabase database;

    public static SQLiteDatabase getDataBase() {
        if (database == null || !database.isOpen()) {
            database = SQLiteDatabase.openOrCreateDatabase(new File(DB_PATH, DATA_BASE_NAME), null);
        }
        return database;
    }

    public static Cursor rawQuery(SQLiteDatabase database, String sqlCommand) {
        return database.rawQuery(sqlCommand, null);
    }

    public static void execSQL(SQLiteDatabase database, String sqlCommand) {
        database.execSQL(sqlCommand);
    }

    public static void bulkInsert(SQLiteDatabase database, List<String> sqlCommandList) {
        try {
            database.beginTransaction();
            for (String command : sqlCommandList) {
                execSQL(database, command);
            }
            database.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (database != null && database.inTransaction()) {
                database.endTransaction();
            }
        }
    }
}