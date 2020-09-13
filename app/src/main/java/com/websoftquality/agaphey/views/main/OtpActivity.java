package com.websoftquality.agaphey.views.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.websoftquality.agaphey.R;

public class OtpActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv_proceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        tv_proceed=findViewById(R.id.tv_proceed);
        tv_proceed.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_proceed:
                Intent intent=new Intent(OtpActivity.this,ResetPasswordActivity.class);
                startActivity(intent);
                break;
        }
    }
}