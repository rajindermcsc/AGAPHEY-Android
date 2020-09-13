package com.websoftquality.agaphey.views.main;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import com.google.gson.Gson;
import com.obs.CustomTextView;
import com.websoftquality.agaphey.R;
import com.websoftquality.agaphey.adapters.main.ViewPagerAdapter;
import com.websoftquality.agaphey.configs.AppController;
import com.websoftquality.agaphey.configs.Constants;
import com.websoftquality.agaphey.configs.SessionManager;
import com.websoftquality.agaphey.datamodels.main.ImageListModel;
import com.websoftquality.agaphey.datamodels.main.JsonResponse;
import com.websoftquality.agaphey.datamodels.main.SliderModel;
import com.websoftquality.agaphey.interfaces.ApiService;
import com.websoftquality.agaphey.interfaces.ServiceListener;
import com.websoftquality.agaphey.utils.CommonMethods;
import com.websoftquality.agaphey.utils.RequestCallback;
import com.websoftquality.agaphey.views.customize.CirclePageIndicator;
import com.websoftquality.agaphey.views.customize.CustomDialog;
import com.websoftquality.agaphey.views.signup.SignUpActivity;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import static com.websoftquality.agaphey.utils.Enums.REQ_GET_LOGIN_SLIDER;

/*****************************************************************
 User Login Activity
 ****************************************************************/
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ServiceListener {


    HashMap<String, String> hashMap = new HashMap<>();
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
    private ViewPagerAdapter PagerAdapter;
    private ViewPager viewPager;
    private CirclePageIndicator pageIndicator;
    // private CustomTextView tvDotNotPostFb,tvTakePolicy,  tvLocationMsg ;
    private CustomTextView  tvLoginFb, tvLoginPhone;
    private RelativeLayout rltTutorial;
    private LinearLayout lltLoginBottom;
    private AlertDialog dialog;
    private ArrayList<ImageListModel> imageList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppController.getAppComponent().inject(this);
        setContentView(R.layout.login_activity);


        initView();
        getSliderImageList();
        initViewPagerListener();



    }

    /**
     * objectCreation method is used to create all objects.
     */
    private void initView() {
        viewPager = findViewById(R.id.viewpager);
        pageIndicator = findViewById(R.id.indicator);

        tvLoginFb = findViewById(R.id.tv_login);
        tvLoginPhone = findViewById(R.id.tv_login_phone);


       /* tvTakePolicy = (CustomTextView) findViewById(R.id.tv_take_policy);

        tvLocationMsg = (CustomTextView) findViewById(R.id.tv_location_not_shown);
        tvDotNotPostFb = (CustomTextView) findViewById(R.id.tv_do_not_fb);*/

        rltTutorial = findViewById(R.id.rlt_tutorial);
        lltLoginBottom = findViewById(R.id.llt_login_bottom);
        dialog = commonMethods.getAlertDialog(this);

        tvLoginPhone.setOnClickListener(this);
        tvLoginFb.setOnClickListener(this);

       /* tvDotNotPostFb.setTextSize(14);
        tvLocationMsg.setTextSize(14);
        tvTermsPolicy.setTextSize(14);*/


    }


    private void getSliderImageList() {
        commonMethods.showProgressDialog(LoginActivity.this, customDialog);
        apiService.getTutorialSliderImg().enqueue(new RequestCallback(REQ_GET_LOGIN_SLIDER, this));
    }

    /**
     * Method called for make circle page indicator setup.
     */
    private void initPageIndicator() {
        final float density = getResources().getDisplayMetrics().density;
        pageIndicator.setRadius(4 * density);
        pageIndicator.setPageColor(ContextCompat.getColor(this, R.color.gray_indicator));
        pageIndicator.setFillColor(ContextCompat.getColor(this, R.color.color_accent));
        pageIndicator.setStrokeColor(ContextCompat.getColor(this, R.color.gray_indicator));
        viewPager.setCurrentItem(0);
        pageIndicator.setOnClickListener(null);
        pageIndicator.setExtraSpacing(0 * density);
    }

    public void onPhoneLoginClick() {
        hashMap.put("auth_id", commonMethods.getAuthId());
        hashMap.put("auth_type", commonMethods.getAuthType());
        Intent intent = new Intent(LoginActivity.this, SignupEmailActivity.class);
        startActivity(intent);
    }




    /**
     * Method called for initiate listener which triggered get tutorial page
     * navigation.
     */
    private void initViewPagerListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        viewPager.setCurrentItem(0);
                        pageIndicator.setCurrentItem(0);
                        break;
                    case 1:
                        viewPager.setCurrentItem(1);
                        pageIndicator.setCurrentItem(1);
                        break;
                    case 2:
                        viewPager.setCurrentItem(2);
                        pageIndicator.setCurrentItem(2);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login_phone:
                onPhoneLoginClick();
                break;
            case R.id.tv_login:
                Intent intent=new Intent(LoginActivity.this,LoginEmailActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        commonMethods.hideProgressDialog();
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            commonMethods.showMessage(LoginActivity.this, dialog, data);
            return;
        }
        String statusCode = (String) commonMethods.getJsonValue(jsonResp.getStrResponse(), "status_code", String.class);
        if (jsonResp.getRequestCode() == REQ_GET_LOGIN_SLIDER && jsonResp.isSuccess()) {
            onSuccessGetSliderImg(jsonResp);
        }

        else if (statusCode.equalsIgnoreCase("2")){
            sessionManager.setIsFBUser(true);
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            intent.putExtra("map", hashMap);
            startActivity(intent);
        } else {
            commonMethods.showMessage(LoginActivity.this, dialog, jsonResp.getStatusMsg());
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) commonMethods.showMessage(LoginActivity.this, dialog, data);
    }

    private void onSuccessGetSliderImg(JsonResponse jsonResp) {
        SliderModel sliderModel = gson.fromJson(jsonResp.getStrResponse(), SliderModel.class);
        sessionManager.setMinAge(sliderModel.getMinimumAge());
        sessionManager.setMaxAge(sliderModel.getMaximumAge());


        if (sliderModel != null && sliderModel.getImageList() != null && sliderModel.getImageList().size() > 0) {
            imageList.clear();
            imageList.addAll(sliderModel.getImageList());
        }
        setViewPagerAdapter();
    }

    private void setViewPagerAdapter() {
        if (imageList.size() > 0) {
            PagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), Constants.VP_LOGIN_SLIDER, imageList.size(), imageList);
            viewPager.setAdapter(PagerAdapter);
            pageIndicator.setViewPager(viewPager);
            initPageIndicator();
        }
    }


}
