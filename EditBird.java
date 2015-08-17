/*
Author:Phil Graham
ID: z1690752
Program:BirdLog
Due:4/17/2015
Purpose:Provides a birdwatcher with a record of where and when he has seen birds
This File: EditBird contains the code for displaying and receiving the text used to update the database
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
import android.widget.Toast;

import java.sql.SQLException;

/**
 * Created by Phil Graham on 4/15/2015.
 */
public class EditBird extends Activity {
    DbAdapter db;
    Button btnSave;
    EditText etName, etLoc;
    TextView tvDate;
    private long idIn;
    private String nameIn, locIn, date, nameOut, locOut;
    @Override
    protected void onCreate(Bundle savedInstanceStates)
    {
        //set up the variables and elements
        super.onCreate(savedInstanceStates);
        setContentView(R.layout.add_entry);
        Intent sender = getIntent();
        etName = (EditText)findViewById(R.id.editTextName);
        etLoc = (EditText)findViewById(R.id.editTextLoc);
        tvDate = (TextView)findViewById(R.id.textViewDateAdd);
        db = new DbAdapter(this);
        idIn = sender.getLongExtra("_id",-1);
        nameIn = sender.getStringExtra("nam");
        locIn = sender.getStringExtra("loc");
        date = sender.getStringExtra("dat");
        String test = "" + idIn +" "+ nameIn +" "+ locIn;
        etName.setText(nameIn);
        etLoc.setText(locIn);
        tvDate.setText(date);

        //When save button is pressed...
        btnSave = (Button)findViewById(R.id.buttonSave);
        btnSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //update the bird
                nameOut = etName.getText().toString();
                locOut = etLoc.getText().toString();
                edit(idIn, nameOut, locOut);
                //Switch to main activity
                Intent i = new Intent(EditBird.this,MainActivity.class);
                EditBird.this.startActivity(i);
            }
        });
    }
    //Method for updating of a row
    private void edit(Long pk, String n, String l) {
        try
        {
            db.open();
            if(db.editOne(pk, n, l))
                Toast.makeText(this,"Edit Successful",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this,"Edit Unsuccessful",Toast.LENGTH_SHORT).show();
            db.close();
        }
        catch(SQLException e)
        {
            Log.d("DBERROR", e.toString());
        }
    }
}
