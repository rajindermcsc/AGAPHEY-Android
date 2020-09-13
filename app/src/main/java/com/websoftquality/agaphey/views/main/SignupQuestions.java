package com.websoftquality.agaphey.views.main;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.websoftquality.agaphey.R;
import com.websoftquality.agaphey.configs.AppController;
import com.websoftquality.agaphey.configs.SessionManager;
import com.websoftquality.agaphey.datamodels.main.JsonResponse;
import com.websoftquality.agaphey.interfaces.ApiService;
import com.websoftquality.agaphey.interfaces.ServiceListener;
import com.websoftquality.agaphey.utils.Apierror_handle;
import com.websoftquality.agaphey.utils.CommonMethods;
import com.websoftquality.agaphey.utils.Loading;
import com.websoftquality.agaphey.utils.RequestCallback;
import com.websoftquality.agaphey.views.customize.CustomDialog;
import com.websoftquality.agaphey.views.signup.SignUpActivity;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import static com.websoftquality.agaphey.utils.Enums.REQ_EMAIL_SIGNUP;
import static com.websoftquality.agaphey.utils.Enums.REQ_GET_LOGIN_SLIDER;

public class SignupQuestions extends AppCompatActivity implements View.OnClickListener, ServiceListener {
    @Inject
    SessionManager sessionManager;
    @Inject
    CommonMethods commonMethods;
    @Inject
    ApiService apiService;
    @Inject
    CustomDialog customDialog;
    @Inject
    Gson gson;
    Intent intent;
    String fullname,username,email,password;
    TextView tv_signup;
    EditText edt_ministry_school,edt_rpg,edt_lifestyle;
    AlertDialog dialog;
    JSONObject jsonObject;
    Loading loading;
    String apiurl;
    Apierror_handle apierror_handle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_questions);
        AppController.getAppComponent().inject(this);
        loading=new Loading(this);
        apierror_handle=new Apierror_handle(this);

        dialog = commonMethods.getAlertDialog(this);
        intent=getIntent();
        fullname=intent.getStringExtra("fullname");
        username=intent.getStringExtra("username");
        email=intent.getStringExtra("email");
        password=intent.getStringExtra("password");
        edt_ministry_school=findViewById(R.id.edt_ministry_school);
        edt_rpg=findViewById(R.id.edt_rpg);
        edt_lifestyle=findViewById(R.id.edt_lifestyle);
        tv_signup=findViewById(R.id.tv_signup);
        tv_signup.setOnClickListener(this);
    }

    private void getEmailSignup() {
        commonMethods.showProgressDialog(SignupQuestions.this, customDialog);
        Log.e("TAG", "onCreate: "+fullname);
        Log.e("TAG", "onCreate: "+username);
        Log.e("TAG", "onCreate: "+email);
        Log.e("TAG", "onCreate: "+password);
        apiService.signupemail(fullname,username,email,password,edt_ministry_school.getText().toString(),
                edt_rpg.getText().toString(),edt_lifestyle.getText().toString()).enqueue(new RequestCallback(1, this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_signup:
                validation();
                break;
        }
    }

    private void validation() {
        if (edt_ministry_school.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Please enter the answer to continue signup...", Toast.LENGTH_SHORT).show();
        }
        else if (edt_rpg.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Please enter the answer to continue signup...", Toast.LENGTH_SHORT).show();
        }else if (edt_rpg.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Please enter the answer to continue signup...", Toast.LENGTH_SHORT).show();
        }
        else {
            getEmailSignup(getResources().getString(R.string.base_url).concat("signupnew"));
//            Intent intent=new Intent(SignupQuestions.this,);
//            startActivity(intent);
        }
    }

    private void getEmailSignup(String apiurl) {
        loading.showDialog();
        String tag_string_req = "req_login";
        Log.e("TAG", "attendance_webservice: "+apiurl);
        StringRequest strReq = new StringRequest(Request.Method.POST, apiurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try{
                    loading.hideDialog();
                    final JSONObject jsonObject = new JSONObject(response);
                    Log.e("TAG", "onResponse: "+jsonObject);
                    if (jsonObject.getString("status_code").equals("1"))
                    {
                        Intent intent=new Intent(SignupQuestions.this,PaymentPlans.class);
                        sessionManager.setToken(jsonObject.getString("access_token"));
                        sessionManager.setUserName(jsonObject.getString("user_name"));
                        sessionManager.setUserId(jsonObject.getInt("user_id"));
                        sessionManager.setStatus(jsonObject.getString("status"));
                        startActivity(intent);
                    }
                    else
                    {
                        loading.hideDialog();

                    }
                }
                catch(Exception e)
                {
                    Log.e("TAG", "onResponse: "+e.getMessage());
                    loading.hideDialog();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e("TAG", "onResponse: "+error.getMessage());
                loading.hideDialog();

                try
                {
                    apierror_handle.get_error(error);
                }catch (Exception e)
                {
                    Log.e("TAG", "onErrorResponse: " + e.getMessage());
                }
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("full_name", fullname);
                params.put("user_name", username);
                params.put("password", password);
                params.put("email", email);
                params.put("ministry_school", edt_ministry_school.getText().toString());
                params.put("rgp", edt_rpg.getText().toString());
                params.put("lifestyle", edt_lifestyle.getText().toString());
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }



    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        try {
            Log.e("TAG", "onSuccess: "+jsonResp.getRequestCode());
            Log.e("TAG", "onSuccess: "+jsonResp.getStrResponse());
//            Log.e("TAG", "onSuccess: "+jsonResp.getRequestData());
            jsonObject = new JSONObject(jsonResp.getStrResponse());

            Log.e("TAG", "onSuccess: "+jsonObject);
            if (jsonObject.getString("status_code").equalsIgnoreCase("1") && jsonObject.getString("status_message").equalsIgnoreCase("success")) {

                Intent intent=new Intent(SignupQuestions.this,PaymentPlans.class);
                sessionManager.setToken(jsonObject.getString("access_token"));
                sessionManager.setUserId(jsonObject.getInt("user_id"));
                sessionManager.setStatus(jsonObject.getString("status"));
                startActivity(intent);
                finish();
            }
            else {
            Toast.makeText(this, jsonResp.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            Log.e("TAG", "onSuccessexc: "+e.getMessage());
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) commonMethods.showMessage(SignupQuestions.this, dialog, data);
    }
}