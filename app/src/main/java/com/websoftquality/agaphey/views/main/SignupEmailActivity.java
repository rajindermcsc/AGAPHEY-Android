package com.websoftquality.agaphey.views.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.websoftquality.agaphey.R;
import com.websoftquality.agaphey.configs.AppController;
import com.websoftquality.agaphey.interfaces.ApiService;

import javax.inject.Inject;

public class SignupEmailActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv_login_phone;
    public @Inject
    ApiService apiService;

    public @Inject
    Gson gson;
    EditText edt_full_name,edt_user_name,edt_email,edt_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_email);

        AppController.getAppComponent().inject(this);
        edt_full_name=findViewById(R.id.edt_full_name);
        edt_user_name=findViewById(R.id.edt_user_name);
        edt_email=findViewById(R.id.edt_email);
        edt_password=findViewById(R.id.edt_password);
        tv_login_phone=findViewById(R.id.tv_login_phone);
        tv_login_phone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_login_phone:
                validations();
                break;
        }
    }

    private void validations() {
        if (edt_full_name.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Enter Full Name", Toast.LENGTH_SHORT).show();
        }else if (edt_user_name.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Enter Username", Toast.LENGTH_SHORT).show();
        }else if (edt_email.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
        }else if (edt_password.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
        }else if (edt_password.getText().toString().length()<8){
            Toast.makeText(this, "Password should be at least 8 digits", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent=new Intent(SignupEmailActivity.this,SignupQuestions.class);
            intent.putExtra("fullname",edt_full_name.getText().toString());
            intent.putExtra("username",edt_user_name.getText().toString());
            intent.putExtra("email",edt_email.getText().toString());
            intent.putExtra("password",edt_password.getText().toString());
            startActivity(intent);
        }
    }
}