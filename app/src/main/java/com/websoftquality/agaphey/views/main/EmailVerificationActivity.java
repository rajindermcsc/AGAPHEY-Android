package com.websoftquality.agaphey.views.main;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.websoftquality.agaphey.R;
import com.websoftquality.agaphey.configs.SessionManager;
import com.websoftquality.agaphey.datamodels.main.JsonResponse;
import com.websoftquality.agaphey.interfaces.ApiService;
import com.websoftquality.agaphey.interfaces.ServiceListener;
import com.websoftquality.agaphey.utils.CommonMethods;
import com.websoftquality.agaphey.utils.RequestCallback;
import com.websoftquality.agaphey.views.customize.CustomDialog;

import java.util.HashMap;

import javax.inject.Inject;

import static com.websoftquality.agaphey.utils.Enums.REQ_SEND_OTP;
import static com.websoftquality.agaphey.utils.Enums.REQ_VERIFY_NUMBER;

public class EmailVerificationActivity extends AppCompatActivity implements View.OnClickListener, ServiceListener {


    public @Inject
    SessionManager sessionManager;

    public @Inject
    CommonMethods commonMethods;

    public @Inject
    ApiService apiService;

    public @Inject
    Gson gson;

    public @Inject
    CustomDialog customDialog;
    EditText email;
    ConstraintLayout ctlPhoneNumber;
    ConstraintLayout ctlOTP;
    ProgressBar pbNumberVerification;
    ImageView imgvArrow;
    RelativeLayout rlEdittexts;
    EditText edtxOne;
    EditText edtxTwo;
    EditText edtxThree;
    EditText edtxFour;
    EditText edtxPhoneNumber;
    CardView cvNext;
    TextView tvPhoneBack;
    TextView tvResendOTP;
    TextView tvOTPback;
    boolean isPhoneNumberLayoutIsVisible = true;
    TextView mobileNumberHeading;
    TextView tvResendOTPLabel;
    TextView tvResendOTPCountdown;
    private String otp = "";
    private String receivedOTPFromServer;
    private long resendOTPWaitingSecond = 120000;
    private CountDownTimer resentCountdownTimer, backPressCounter;
    private boolean isDeletable = true;
    TextView tvOTPErrorMessage;
    public AlertDialog dialog;
    String facebookVerifiedPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);
        dialog = commonMethods.getAlertDialog(this);
        email=findViewById(R.id.email);
        mobileNumberHeading = findViewById(R.id.tv_mobile_heading);
        tvOTPErrorMessage = findViewById(R.id.tv_otp_error_field);
        tvResendOTPLabel = findViewById(R.id.tv_otp_resend_label);
        tvResendOTPCountdown = findViewById(R.id.tv_otp_resend_countdown);
        ctlOTP = findViewById(R.id.cl_otp_input);
        pbNumberVerification = findViewById(R.id.pb_number_verification);
        imgvArrow = findViewById(R.id.imgv_next);
        rlEdittexts = findViewById(R.id.rl_edittexts);
        edtxOne = findViewById(R.id.one);
        edtxTwo = findViewById(R.id.two);
        edtxThree = findViewById(R.id.three);
        edtxFour = findViewById(R.id.four);
        edtxPhoneNumber = findViewById(R.id.phone);
        tvResendOTP = findViewById(R.id.tv_resend_button);
        tvPhoneBack = findViewById(R.id.tv_back_phone_arrow);
        tvOTPback = findViewById(R.id.tv_back_otp_arrow);
        cvNext = findViewById(R.id.fab_verify);
        cvNext.setOnClickListener(this);
        tvResendOTP.setOnClickListener(this);
        tvOTPback.setOnClickListener(this);
        tvPhoneBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_resend_button:
                callSendOTPAPI();
                break;
            case R.id.tv_back_otp_arrow:
                showPhoneNumberField();
                break;
            case R.id.tv_back_phone_arrow:
                finishThisActivity();
                break;
            case R.id.fab_verify:
                startAnimationd();
                break;
        }
    }


    private void verifyOTP() {
        if (!TextUtils.isEmpty(otp)) {
            if (otp.equalsIgnoreCase(receivedOTPFromServer)) {
                callPhoneNumberValidationAPI(edtxPhoneNumber.getText().toString().trim());
            } else {
                showOTPMismatchIssue();
            }
        } else {
            commonMethods.showMessage(this, dialog, getString(R.string.otp_number_alert));
        }
    }


    private void shakeEdittexts() {
        TranslateAnimation shake = new TranslateAnimation(0, 20, 0, 0);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(3));
        rlEdittexts.startAnimation(shake);

    }

    public void showOTPMismatchIssue() {
        shakeEdittexts();
        tvOTPErrorMessage.setVisibility(View.VISIBLE);
    }



    // api call
    void callPhoneNumberValidationAPI(String twPhoneNumber) {
        this.facebookVerifiedPhoneNumber = twPhoneNumber;
        sessionManager.setPhoneNumber(twPhoneNumber);
        commonMethods.showProgressDialog(this, customDialog);
        String fbId = "";
        if (sessionManager.getIsFBUser()) {
            Intent intent = getIntent();
            HashMap<String, String> hashMap = (HashMap<String, String>) intent.getSerializableExtra("map");
            if (hashMap != null) {
                Log.v("HashMapTest", hashMap.get("auth_id"));
                fbId = hashMap.get("auth_id");
            }
        }
        apiService.verifyPhoneNumber(twPhoneNumber,  commonMethods.getAuthId(),commonMethods.getAuthType()).enqueue(new RequestCallback(REQ_VERIFY_NUMBER, this));
    }

    public void startAnimationd() {
        //startAnimation();
        if (isPhoneNumberLayoutIsVisible && edtxPhoneNumber.getText().toString().length() > 5) {
            callSendOTPAPI();
        } else if (!isPhoneNumberLayoutIsVisible) {
            verifyOTP();

        }
        /*showOTPfield();
        showOTPMismatchIssue();*/

    }


    public void finishThisActivity() {
        super.onBackPressed();
    }



    public void showPhoneNumberField() {
        cvNext.setCardBackgroundColor(getResources().getColor(R.color.light_blue_button_color));
        ctlPhoneNumber.setVisibility(View.VISIBLE);
        ctlOTP.setVisibility(View.GONE);
        isPhoneNumberLayoutIsVisible = true;
        tvResendOTP.setVisibility(View.GONE);
        tvResendOTPLabel.setVisibility(View.GONE);
        tvResendOTPCountdown.setVisibility(View.GONE);
        resentCountdownTimer.cancel();
    }


    public void showProgressBarAndHideArrow(Boolean status) {
        if (status) {
            pbNumberVerification.setVisibility(View.VISIBLE);
            imgvArrow.setVisibility(View.GONE);
        } else {
            pbNumberVerification.setVisibility(View.GONE);
            imgvArrow.setVisibility(View.VISIBLE);
        }
    }


    public void callSendOTPAPI() {
        showProgressBarAndHideArrow(true);
        apiService.numbervalidation(edtxPhoneNumber.getText().toString()).enqueue(new RequestCallback(REQ_SEND_OTP, this));
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {

    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {

    }
}