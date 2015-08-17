/*
Author:Phil Graham
ID: z1690752
Program:BirdLog
Due:4/17/2015
Purpose:Provides a birdwatcher with a record of where and when he has seen birds
This File: AddBird allows the user to add a new bird sighting to the database
 */
package edu.cs.niu.graham.birdlog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.Date;

/**
 * Created by z1690752 on 4/15/2015.
 */
public class AddBird extends Activity {
    DbAdapter db;
    Button btnSave;
    EditText etName, etLoc;
    TextView tvDate;
    private String nameIn, locIn, nameOut, locOut;
    @Override
    protected void onCreate(Bundle savedInstanceStates)
    {
        //Hook up the elements and variables
        super.onCreate(savedInstanceStates);
        db = new DbAdapter(this);
        setContentView(R.layout.add_entry);
        Intent sender = getIntent();
        Date now = new Date();
        final String date = now.toString();
        etName = (EditText)findViewById(R.id.editTextName);
        etLoc = (EditText)findViewById(R.id.editTextLoc);
        tvDate = (TextView)findViewById(R.id.textViewDateAdd);
        tvDate.setText(date);
        btnSave = (Button)findViewById(R.id.buttonSave);

        //When save button is pressed
        btnSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //insert the new bird
                nameOut = etName.getText().toString();
                locOut = etLoc.getText().toString();
                insert(nameOut,locOut,date);
                //Switch to the main screen
                Intent i = new Intent(AddBird.this,MainActivity.class);
                AddBird.this.startActivity(i);
            }
        });
    }

    //Method for insertion of a new bird into the database
    private void insert(String n, String l,String d) {
        try
        {
            db.open();

        }
        catch(Throwable e)
        {
            Log.d("DBERROR 1", e.toString());
        }
        try
        {
            db.insertOne(n, l,d);
            db.close();
        }
        catch(Throwable e)
        {
            Log.d("DBERROR 2", e.toString());
        }
    }
}
