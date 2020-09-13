package com.websoftquality.agaphey.views.main;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.websoftquality.agaphey.R;
import com.websoftquality.agaphey.adapters.payment.PaymentPlanAdapter;
import com.websoftquality.agaphey.configs.AppController;
import com.websoftquality.agaphey.configs.SessionManager;
import com.websoftquality.agaphey.datamodels.main.JsonResponse;
import com.websoftquality.agaphey.interfaces.ApiService;
import com.websoftquality.agaphey.interfaces.ServiceListener;
import com.websoftquality.agaphey.utils.CommonMethods;
import com.websoftquality.agaphey.utils.RequestCallback;
import com.websoftquality.agaphey.views.customize.CustomDialog;

import org.json.JSONObject;

import javax.inject.Inject;

public class PaymentPlans extends AppCompatActivity implements ServiceListener, View.OnClickListener, PaymentPlanAdapter.OnShareClickedListener {
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
    RecyclerView rv_payment_plans;
    LinearLayoutManager linearLayoutManager;
    PaymentPlanAdapter paymentPlanAdapter;
    AlertDialog dialog;
    JSONObject jsonObject;
    String item="0";
    TextView tv_continue;
    String amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_plans);
        AppController.getAppComponent().inject(this);
        rv_payment_plans=findViewById(R.id.rv_payment_plans);
        tv_continue=findViewById(R.id.tv_continue);
        tv_continue.setOnClickListener(this);
        dialog = commonMethods.getAlertDialog(this);
        getPaymentPlans();
    }

    private void getPaymentPlans() {
        commonMethods.showProgressDialog(PaymentPlans.this, customDialog);
        apiService.getplans().enqueue(new RequestCallback(1, this));
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        try {
            Log.e("TAG", "onSuccess: "+jsonResp.getStrResponse());
            Log.e("TAG", "onSuccess: "+jsonResp.getRequestData());
            jsonObject = new JSONObject(jsonResp.getStrResponse());

        if (jsonObject.getString("status_code").equalsIgnoreCase("1") && jsonObject.getString("status_message").equalsIgnoreCase("success")) {

                linearLayoutManager = new LinearLayoutManager(this);
                paymentPlanAdapter = new PaymentPlanAdapter(this, jsonObject);
                paymentPlanAdapter.setOnShareClickedListener(this);
                rv_payment_plans.setLayoutManager(linearLayoutManager);
                rv_payment_plans.setAdapter(paymentPlanAdapter);
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
        if (!jsonResp.isOnline()) commonMethods.showMessage(PaymentPlans.this, dialog, data);
    }


    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.tv_continue){
            if (item.equalsIgnoreCase("0") || item.isEmpty()){
                Toast.makeText(this, "Please Select a plan to continue", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent=new Intent(PaymentPlans.this,PaymentCreditCard.class);
                intent.putExtra("item",item);
                intent.putExtra("amount",amount);
                startActivity(intent);
            }
        }
    }

    @Override
    public void ShareClicked(String item,String amount) {

        Log.e("TAG", "onItemClick: "+item);
        this.item=item;
        this.amount=amount;
    }
}