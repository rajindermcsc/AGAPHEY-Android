package com.websoftquality.agaphey.views.signup;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.websoftquality.agaphey.R;
import com.websoftquality.agaphey.configs.AppController;
import com.websoftquality.agaphey.configs.RunTimePermission;
import com.websoftquality.agaphey.configs.SessionManager;
import com.websoftquality.agaphey.datamodels.main.JsonResponse;
import com.websoftquality.agaphey.datamodels.main.SignUpModel;
import com.websoftquality.agaphey.interfaces.ApiService;
import com.websoftquality.agaphey.interfaces.ServiceListener;
import com.websoftquality.agaphey.interfaces.SignUpActivityListener;
import com.websoftquality.agaphey.utils.CommonMethods;
import com.websoftquality.agaphey.views.customize.CustomDialog;
import com.websoftquality.agaphey.views.main.AccountKit.TwilioAccountKitActivity;
import com.websoftquality.agaphey.views.main.HomeActivity;

import java.util.HashMap;

import javax.inject.Inject;


/*****************************************************************
 Signup home page contain all signup fragment page
 ****************************************************************/
public class SignUpActivity extends AppCompatActivity implements SignUpActivityListener, ServiceListener {

    @Inject
    RunTimePermission runTimePermission;
    @Inject
    CustomDialog customDialog;
    @Inject
    SessionManager sessionManager;
    @Inject
    ApiService apiService;
    @Inject
    Gson gson;

    @Inject
    CommonMethods commonMethods;
    int min, max;
    HashMap<String, String> hashMap;
    public HashMap<String, String> signUp = new HashMap<>();

    public AlertDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        AppController.getAppComponent().inject(this);

        dialog = commonMethods.getAlertDialog(this);
        Intent intent = getIntent();
        hashMap = (HashMap<String, String>) intent.getSerializableExtra("map");
        if (hashMap != null)
            Log.v("HashMapTest", hashMap.get("auth_id"));

        min = Integer.parseInt(sessionManager.getMinAge());
        max = Integer.parseInt(sessionManager.getMaxAge());

        //changeFragment(MY_PHONE_NUMBER, null, false);
        TwilioAccountKitActivity.openTwilioAccountKitActivity(this);
    }


    @Override
    public Resources getRes() {
        return this.getResources();
    }

    @Override
    public SignUpActivity getInstance() {
        return SignUpActivity.this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //checkAllPermission(Constants.PERMISSIONS_STORAGE);
    }


    public void putHashMap(String key, String value) {
        if (signUp.containsKey(key)) signUp.remove(key);

        signUp.put(key, value);
    }


    public HashMap<String, String> getHashMap() {
        return signUp;
    }



    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            commonMethods.showMessage(this, dialog, data);
            return;
        }

        if (jsonResp.isSuccess()) {
            onSuccessLogin(jsonResp);
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) commonMethods.showMessage(this, dialog, data);
    }

    private void onSuccessLogin(JsonResponse jsonResp) {
        SignUpModel signUpModel = gson.fromJson(jsonResp.getStrResponse(), SignUpModel.class);
        if (signUpModel != null) {
            if (!TextUtils.isEmpty(signUpModel.getAccessToken())) {
                sessionManager.setToken(signUpModel.getAccessToken());
            }
            if (!TextUtils.isEmpty(signUpModel.getUserImageUrl())) {
                sessionManager.setProfileImg(signUpModel.getUserImageUrl());
            }
            sessionManager.setUserId(signUpModel.getUserId());
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

}
