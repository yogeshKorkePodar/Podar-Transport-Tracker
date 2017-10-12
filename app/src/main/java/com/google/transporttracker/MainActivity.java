package com.google.transporttracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button parent, driver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parent = (Button)findViewById(R.id.button2);
        parent.setOnClickListener(this);

        driver = (Button) findViewById(R.id.button3);
        driver.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.button2){
            //parent

            Intent intent = new Intent(this, Parent_tracking_activity.class);
            startActivity(intent);
        }
        else{
            //driver
            Intent intent = new Intent(this, TrackerActivity.class);
            startActivity(intent);
        }
    }
}
