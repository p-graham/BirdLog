/*
Author:Phil Graham
ID: z1690752
Program:BirdLog
Due:4/17/2015
Purpose:Provides a birdwatcher with a record of where and when he has seen birds
This File:DbAdapter contains code to control the creation and handling of the birdDB database
 */
package edu.cs.niu.graham.birdlog;

import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;
import java.util.Date;

/**
 * Created by Phil Graham on 4/12/2015.
 */
public class DbAdapter
{
    /***
     * database name - contactsSp15.db
     * table name - birdTable
     * primary key - _id (integer autoincrement, sqlite requires this)
     * name - text
     * phone - text
     * version - integer, this tells the app to recreate the database if it exists
     * and the new version is higher than the current version
     * Set all of these up as constants
     */
    static final String TABLE_NAME = "birdTable";
    static final String PK = "_id";
    static final String NAME = "name";
    static final String LOCATION = "location";
    static final String DATE = "date";
    static final String DB_NAME = "birdDB.db";
    static final String CREATE_TABLE = "create table " + TABLE_NAME +
            "(" + PK + " integer primary key autoincrement, " +
            NAME + " text, " + LOCATION + " text, " + DATE + " text)";
    static final int VERSION = 4; //start with 1, you'll have to change it
    final Context CONTEXT;
    SQLiteDatabase db; //database variable
    MySQLiteHelper dbHelper;
    public DbAdapter(Context ctx)
    {
        this.CONTEXT = ctx;
        dbHelper = new MySQLiteHelper(CONTEXT,DB_NAME,null,VERSION);
    }//end dbAdapter constructor
    public DbAdapter open() throws SQLException
    {
        db = dbHelper.getWritableDatabase();
        return this;
    }//end open
    public void close()
    {
        dbHelper.close();
    }//end close
    /****
     * you need a couple of methods (minimal) here
     * an insert method, to add a new record to the db
     * and getAll method that returns a cursor that contains
     * all the rows
     * and a getOne method that returns a single row
     */
    public void insertOne(String n, String l, String d)    {
        ContentValues cv = new ContentValues(); //a way to create key/value pairs for inserting and updating
        cv.put(NAME, n);
        cv.put(LOCATION, l);
        cv.put(DATE, d);
        db.insert(TABLE_NAME,null,cv);
    }//end insertOne
    public Cursor getAll()
    {
        /***
         * this uses the query method to retrieve rows, in alpha order by name
         */
        return db.query(TABLE_NAME, new String[] {PK,NAME,LOCATION,DATE},null,null,null,null,NAME);
    }//end getAll
    public Cursor getOne(long rowId)
    {
        /**
         * this is used when you choose something based on the location in the db
         */
        Cursor c = db.query(true,TABLE_NAME,new String[]{PK,NAME,LOCATION,DATE},PK + " = " +
                rowId,null,null,null,null,null);
        if (c != null) //means there was a row or rows found
        {
            c.moveToFirst();
        }
        return c;
    }
    public boolean deleteOne(long rowId)
    {
        //returns true for success / false for failure
        String where = PK + "=" + rowId;
        return db.delete(TABLE_NAME,where,null) > 0;
    }//end deleteOne
    public boolean editOne(long rowId, String n, String l)
    {
        //returns true for success / false for failure
        String where = PK + "=" + rowId;
        ContentValues cv = new ContentValues();
        cv.put(NAME,n);
        cv.put(LOCATION, l);
        return db.update(TABLE_NAME, cv, PK + "=?", new String[]{rowId+""})>0;
    }//end editOne
    private static class MySQLiteHelper extends SQLiteOpenHelper
    {
        private MySQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
        {
            super(context, name, factory, version);

        }
        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(CREATE_TABLE);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            db.execSQL("drop table if exists " + TABLE_NAME);
            onCreate(db);
        }
    }//end SQLite open helper inner class
}//end dbAdapter class
