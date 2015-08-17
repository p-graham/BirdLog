/*
Author:Phil Graham
ID: z1690752
Program:BirdLog
Due:4/17/2015
Purpose:Provides a birdwatcher with a record of where and when he has seen birds
This File:DbCursorAdapter contains the code responsible for displaying the databases contents in the main ListView
 */
package edu.cs.niu.graham.birdlog;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.sql.SQLException;

/**
 * Created by Phil Graham on 4/12/2015.
 */
public class DbCursorAdapter extends CursorAdapter
{
    DbAdapter db;
    LayoutInflater cursorInflater;
    public DbCursorAdapter(Context ctx, Cursor curse,int flags)
    {
        super(ctx,curse,flags);
        cursorInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        //populates the elements
        return cursorInflater.inflate(R.layout.single,parent,false);
    }
    @Override
    public void bindView(View view, Context ctx, Cursor cursor)
    {
        //creates the view for individual rows
        TextView tvName, tvLoc,tvDate;
        int id;
        Cursor tempCursor;
        tvName = (TextView)view.findViewById(R.id.textViewName);
        tvLoc = (TextView)view.findViewById(R.id.textViewLoc);
        tvDate = (TextView)view.findViewById(R.id.textViewDate);
        db = new DbAdapter(ctx);
        try
        {
            db.open();
            tempCursor = db.getOne(cursor.getLong(0));
            id = tempCursor.getInt(0);
            tvName.setText(tempCursor.getString(1));
            tvLoc.setText(tempCursor.getString(2));
            tvDate.setText(tempCursor.getString(3));
            db.close();
        }
        catch(SQLException e)
        {
            Log.e("DBERROR", e.toString());
        }
    }
}
