package com.goteny.melo.views.demo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.goteny.melo.utils.log.LogMelo;
import com.goteny.melo.views.demo.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        LogMelo.i(getClass().getSimpleName(), "onClick()");

    }
}
