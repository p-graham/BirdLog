/*
Author:Phil Graham
ID: z1690752
Program:BirdLog
Due:4/17/2015
Purpose:Provides a birdwatcher with a record of where and when he has seen birds
This File: MainActivity contains the code responsible for the actions of the menus and listView
 */
package edu.cs.niu.graham.birdlog;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.List;

import edu.cs.niu.graham.birdlog.EditBird;
import edu.cs.niu.graham.birdlog.AddBird;

/**
 * Created by Phil Graham on 4/12/2015.
 */
public class MainActivity extends Activity {
    ListView theList;
    DbAdapter db;
    DbCursorAdapter myCursorAdapter;
    private final static int CODE_A = 1;
    private final static int CODE_B = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        theList = (ListView)findViewById(R.id.birdList);
        registerForContextMenu(theList);
        db = new DbAdapter(this);
        try
        {
            db.open();
            myCursorAdapter = new DbCursorAdapter(this, db.getAll(),0);
            theList.setAdapter(myCursorAdapter);
        }
        catch(SQLException e)
        {
            Log.d("DBERROR", e.toString());
        }
    }

    //Use the longtouch file for contextmenu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo cinfo)
    {
        getMenuInflater().inflate(R.menu.longtouch, menu);
    }

    //Actions performed when contextmenu item selected
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        //if delete
        if(item.getItemId() == R.id.delete_item)
        {
            if(db.deleteOne((int)info.id))
            {
                myCursorAdapter.changeCursor(db.getAll());
                ;           }
            return true;
        }
        //if edit
        else if(item.getItemId() == R.id.edit_item)
        {
            Intent i = new Intent(this,EditBird.class);
            Cursor c = db.getOne(info.id);
            String n,l,d;
            Long pk;
            pk = c.getLong(0);
            n = c.getString(1);
            l = c.getString(2);
            d = c.getString(3);
            i.putExtra("_id",pk);
            i.putExtra("nam",n);
            i.putExtra("loc",l);
            i.putExtra("dat",d);
            //Switch screen
            this.startActivity(i);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    //Main menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(item.getItemId() == R.id.add_item)//Add new bird
        {
            Intent i = new Intent(this,AddBird.class);
            this.startActivity(i);//switch screens
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
