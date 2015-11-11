package com.yeah.android.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yeah.android.model.user.Message;

import java.util.ArrayList;
import java.util.List;

public class DBUtil extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String SYNC_DATA_DB_NAME = "yeah.db";
    private static final String MESSAGE_TABLE_NAME = "message";

    private SQLiteDatabase mDatabase;

    private static DBUtil dbUtil;

    public static DBUtil getInstance(Context context) {

        if(dbUtil == null) {
            synchronized (DBUtil.class) {
                if(dbUtil == null) {
                    dbUtil = new DBUtil(context);
                }
            }
        }
        return dbUtil;
    }

    public DBUtil(Context context) {
        super(context, SYNC_DATA_DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTableFavoriteAction(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        mDatabase = db;
    }

    public static final class MessageColumnKey {
        private MessageColumnKey(){}

        public static final String ID = "id";
        public static final String TITLE = "title";
        public static final String CONTENT = "content";
    }

    private void createTableFavoriteAction(SQLiteDatabase db) {
        String createFavoriteActionTable = "CREATE TABLE " + MESSAGE_TABLE_NAME + " ("
                + MessageColumnKey.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MessageColumnKey.TITLE + " TEXT, "
                + MessageColumnKey.CONTENT + " TEXT"
                + "); ";
        db.execSQL(createFavoriteActionTable);
    }

    public List<Message> getMessageList() {
        mDatabase = dbUtil.getWritableDatabase();
        Cursor cursor = mDatabase.query(MESSAGE_TABLE_NAME, new String[] {
                        MessageColumnKey.ID, MessageColumnKey.TITLE,
                        MessageColumnKey.CONTENT }, null,
                null, null, null, null);


        List<Message> messageList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Message message = new Message();
            message.setTitle(cursor.getString(cursor.getColumnIndex(MessageColumnKey.TITLE)));
            message.setContent(cursor.getString(cursor.getColumnIndex(MessageColumnKey.CONTENT)));

            messageList.add(0, message);
        }
        cursor.close();
        mDatabase.close();

        return messageList;
    }

    public void storeMessage(Message message) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MessageColumnKey.TITLE, message.getTitle());
        contentValues.put(MessageColumnKey.CONTENT, message.getContent());

        mDatabase = dbUtil.getWritableDatabase();
        mDatabase.insert(MESSAGE_TABLE_NAME, null, contentValues);
        mDatabase.close();
    }
}