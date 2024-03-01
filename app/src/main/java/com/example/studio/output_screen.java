package com.example.studio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class output_screen extends AppCompatActivity {

    public TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.output_screen);

        tv=findViewById(R.id.outputtv);
        Intent rc=getIntent();
        String msg=rc.getStringExtra("output");

        tv.setText(msg);


    }
}