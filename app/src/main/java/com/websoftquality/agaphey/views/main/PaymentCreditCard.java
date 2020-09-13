package com.websoftquality.agaphey.views.main;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class PaymentCreditCard extends AppCompatActivity implements View.OnClickListener, TextWatcher, ServiceListener {
    Intent intent;
    String amount,item,token,user_id;
    TextView tv_continue;
    EditText edt_card,edt_expiry,edt_cvv;
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
    JSONObject jsonObject;
    Loading loading;
    String apiurl;
    Apierror_handle apierror_handle;
    int previousLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_credit_card);
        loading=new Loading(this);
        apierror_handle=new Apierror_handle(this);

        AppController.getAppComponent().inject(this);
        intent=getIntent();
        amount=intent.getStringExtra("amount");
        item=intent.getStringExtra("item");
        tv_continue=findViewById(R.id.tv_continue);
        edt_card=findViewById(R.id.edt_card);
        edt_expiry=findViewById(R.id.edt_expiry);
        edt_cvv=findViewById(R.id.edt_cvv);
        tv_continue.setOnClickListener(this);
        edt_expiry.addTextChangedListener(this);
        dialog = commonMethods.getAlertDialog(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.tv_continue){
            validations();
        }
    }

    private void validations() {
        if (edt_card.getText().toString().length()<16 ){
            Toast.makeText(this, "Card Number is invalid", Toast.LENGTH_SHORT).show();
        }
        else if (edt_card.getText().toString().trim().isEmpty()){

            Toast.makeText(this, "Please Enter Card Number", Toast.LENGTH_SHORT).show();
        }else if (edt_expiry.getText().toString().trim().isEmpty()){

            Toast.makeText(this, "Please Enter Expiry Date", Toast.LENGTH_SHORT).show();
        }else if (edt_expiry.getText().toString().length()<5){

            Toast.makeText(this, "Expiry Date is invalid", Toast.LENGTH_SHORT).show();
        } else if (edt_cvv.getText().toString().trim().isEmpty()){

            Toast.makeText(this, "Please Enter CVV", Toast.LENGTH_SHORT).show();
        }else if (edt_expiry.getText().toString().length()<3){

            Toast.makeText(this, "CVV is invalid", Toast.LENGTH_SHORT).show();
        }
        else {
            savecardDetails(getResources().getString(R.string.base_url).concat("setpayment"));
        }
    }


    private void savecardDetails(String apiurl) {
        loading.showDialog();
        Log.e("TAG", "savecardDetails: "+sessionManager.getToken());
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
                        Intent intent=new Intent(PaymentCreditCard.this,ProfileActivity.class);
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
                params.put("plan_id", item);
                params.put("cvv", edt_cvv.getText().toString());
                params.put("expiry_date", edt_expiry.getText().toString());
                params.put("card_num", edt_card.getText().toString());
                params.put("token", sessionManager.getToken());
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

//    private void savecardDetails() {
//        commonMethods.showProgressDialog(PaymentCreditCard.this, customDialog);
//        apiService.setpayment(item,edt_cvv.getText().toString(),edt_expiry.getText().toString(),edt_card.getText().toString(),token,user_id).enqueue(new RequestCallback(1, this));
//    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        previousLength = edt_expiry.getText().toString().length();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int length = edt_expiry.getText().toString().trim().length();

        if (previousLength <= length && length < 3) {
            int month = Integer.parseInt(edt_expiry.getText().toString());
            if (length == 1 && month >= 2) {
                String autoFixStr = "0" + month + "/";
                edt_expiry.setText(autoFixStr);
                edt_expiry.setSelection(3);
            } else if (length == 2 && month <= 12) {
                String autoFixStr = edt_expiry.getText().toString() + "/";
                edt_expiry.setText(autoFixStr);
                edt_expiry.setSelection(3);
            } else if (length ==2 && month > 12) {
                edt_expiry.setText("1");
                edt_expiry.setSelection(1);
            }
        } else if (length == 5) {
            edt_expiry.requestFocus(); // auto move to next edittext
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {



    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        try {
            Log.e("TAG", "onSuccess: "+jsonResp);
            Log.e("TAG", "onSuccess: "+jsonResp.getStrResponse());
            Log.e("TAG", "onSuccess: "+jsonResp.getRequestData());
            jsonObject = new JSONObject(jsonResp.getStrResponse());

            if (jsonObject.getString("status_code").equalsIgnoreCase("1") && jsonObject.getString("status_message").equalsIgnoreCase("success")) {


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
        if (!jsonResp.isOnline()) commonMethods.showMessage(PaymentCreditCard.this, dialog, data);
    }

}