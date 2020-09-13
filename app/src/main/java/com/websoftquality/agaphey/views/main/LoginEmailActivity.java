package com.websoftquality.agaphey.views.main;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.websoftquality.agaphey.adapters.payment.PaymentPlanAdapter;
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

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class LoginEmailActivity extends AppCompatActivity implements View.OnClickListener, ServiceListener {

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
    AlertDialog dialog;
    TextView tv_login_phone;
    EditText edt_user_name,edt_password;
    JSONObject jsonObject;
    Loading loading;
    String apiurl;
    Apierror_handle apierror_handle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);
        AppController.getAppComponent().inject(this);
        tv_login_phone=findViewById(R.id.tv_login_phone);
        edt_user_name=findViewById(R.id.edt_user_name);
        edt_password=findViewById(R.id.edt_password);
        apierror_handle=new Apierror_handle(this);
        dialog = commonMethods.getAlertDialog(this);
        tv_login_phone.setOnClickListener(this);
        loading=new Loading(this);
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
        if (edt_user_name.getText().toString().isEmpty()){
            Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show();
        }
        else if (edt_password.getText().toString().isEmpty()){
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        }
        else {
            apiLogin(getResources().getString(R.string.base_url).concat("loginnew"));
        }
    }

//    private void apiLogin() {
////        commonMethods.showProgressDialog(LoginEmailActivity.this, customDialog);
//        apiService.getLogin(edt_user_name.getText().toString(),edt_password.getText().toString()).enqueue(new RequestCallback(1, this));
//    }


    private void apiLogin(String apiurl) {
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
                        Intent intent=new Intent(LoginEmailActivity.this,HomeActivity.class);
                        sessionManager.setToken(jsonObject.getString("access_token"));
                        sessionManager.setUserName(jsonObject.getString("user_name"));
                        sessionManager.setStatus(jsonObject.getString("status"));

                        Log.e("TAG", "onResponse: "+sessionManager.getToken());
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
                params.put("email", edt_user_name.getText().toString());
                params.put("password", edt_password.getText().toString());
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
            Log.e("TAG", "onSuccess: "+jsonResp.getStrResponse());
            Log.e("TAG", "onSuccess: "+jsonResp.getRequestData());
            jsonObject = new JSONObject(jsonResp.getStrResponse());

            if (jsonObject.getString("status_code").equalsIgnoreCase("1") && jsonObject.getString("status_message").equalsIgnoreCase("success")) {
                Log.e("TAG", "onSuccesslogin: ");
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
        if (!jsonResp.isOnline()) commonMethods.showMessage(LoginEmailActivity.this, dialog, data);
    }

}