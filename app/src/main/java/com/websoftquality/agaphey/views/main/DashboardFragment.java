package com.websoftquality.agaphey.views.main;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.websoftquality.PagerRequest;
import com.websoftquality.agaphey.R;
import com.websoftquality.agaphey.adapters.main.RequestAdapter;
import com.websoftquality.agaphey.configs.AppController;
import com.websoftquality.agaphey.configs.Constants;
import com.websoftquality.agaphey.configs.RunTimePermission;
import com.websoftquality.agaphey.configs.SessionManager;
import com.websoftquality.agaphey.datamodels.main.JsonResponse;
import com.websoftquality.agaphey.datamodels.main.MatchProfilesModel;
import com.websoftquality.agaphey.datamodels.main.UserDetailModel;
import com.websoftquality.agaphey.iaputils.IabBroadcastReceiver;
import com.websoftquality.agaphey.iaputils.IabHelper;
import com.websoftquality.agaphey.interfaces.ActivityListener;
import com.websoftquality.agaphey.interfaces.ApiService;
import com.websoftquality.agaphey.interfaces.ServiceListener;
import com.websoftquality.agaphey.utils.CommonMethods;
import com.websoftquality.agaphey.utils.Loading;
import com.websoftquality.agaphey.utils.MyLocation;
import com.websoftquality.agaphey.utils.RequestCallback;
import com.websoftquality.agaphey.views.chat.ChatConversationActivity;
import com.websoftquality.agaphey.views.customize.CustomDialog;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.websoftquality.agaphey.utils.Enums.REQ_GET_HOME;
import static com.websoftquality.agaphey.utils.Enums.REQ_SHOW_ALL_MATCHES;
import static com.websoftquality.agaphey.utils.Enums.REQ_SWIPE_MATCH;
import static com.websoftquality.agaphey.utils.Enums.REQ_UPDATE_DEVICE_ID;
import static com.websoftquality.agaphey.utils.Enums.REQ_UPDATE_LOCATION;




public class DashboardFragment extends Fragment implements View.OnClickListener, ServiceListener, TabLayout.BaseOnTabSelectedListener, ViewPager.OnPageChangeListener {
    // Debug tag, for logging
    static final String TAG = "Boost In App Purchase";
    // SKU for our subscription (infinite Boost)

    @Inject
    ApiService apiService;
    @Inject
    CommonMethods commonMethods;
    @Inject
    CustomDialog customDialog;
    @Inject
    SessionManager sessionManager;
    @Inject
    Gson gson;

    @Inject
    RunTimePermission runTimePermission;


    IabHelper mHelper;

    IabBroadcastReceiver mBroadcastReceiver;

    private View view;
    private ActivityListener listener;
    private Resources res;
    private HomeActivity mActivity;

    private UserDetailModel userDetailModel;
    JSONObject jsonObject;
//    private TabLayout tabLayout;

    //This is our viewPager
//    private ViewPager viewPager;


    private MatchProfilesModel matchProfilesModel;
    RecyclerView rv_profiles;
    LinearLayoutManager linearLayoutManager;
    RequestAdapter requestAdapter;


    private double latitude = 0, longitude = 0;
    TextView text_active;
    AlertDialog dialog;
    Loading loading;
    /**
     * Get user current location
     */
    MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
        @Override
        public void gotLocation(Location location) {
            System.out.println("Check4");
            Log.e(TAG, "gotLocation: ");
            if (location == null) return;
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            System.out.println("Check5");
            apiService.insertLocation(sessionManager.getToken(), latitude, longitude, "update").enqueue(new RequestCallback(REQ_UPDATE_LOCATION, DashboardFragment.this));
        }
    };
    private boolean isPermissionGranted = false;

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            listener = (ActivityListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Profile must implement ActivityListener");
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        init();
        AppController.getAppComponent().inject(this);

        view = inflater.inflate(R.layout.dashboard_fragment, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {


//        tabLayout = view.findViewById(R.id.tabLayout);
        loading=new Loading(getContext());
        rv_profiles=view.findViewById(R.id.rv_profiles);
        //Adding the tabs using addTab() method
//        tabLayout.addTab(tabLayout.newTab().setText("Send Requests"));
//        tabLayout.addTab(tabLayout.newTab().setText("Accept Requests"));
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//
//        //Initializing viewPager
//        viewPager = view.findViewById(R.id.pager);
//        TabLayout.Tab tab = tabLayout.getTabAt(0); // Count Starts From 0
//        tab.select();
//
//        jsonObject=new JSONObject();
//        //Creating our pager adapter
//        PagerRequest adapter = new PagerRequest(getFragmentManager(), tabLayout.getTabCount(), jsonObject);
//
//        //Adding adapter to pager
//        viewPager.setAdapter(adapter);
//        //Adding onTabSelectedListener to swipe views
//        tabLayout.setOnTabSelectedListener(this);
//        viewPager.addOnPageChangeListener(this);
        rv_profiles=view.findViewById(R.id.rv_profiles);
        linearLayoutManager=new LinearLayoutManager(getContext());
        requestAdapter=new RequestAdapter(getContext(),jsonObject);
        rv_profiles.setLayoutManager(linearLayoutManager);
        rv_profiles.setAdapter(requestAdapter);
//        viewPager = view.findViewById(R.id.viewpager);
//        mTabLayout = view.findViewById(R.id.tab_layout);
//        text_active = view.findViewById(R.id.text_active);
//
//        dialog = commonMethods.getAlertDialog(mActivity);
//
//
//
//
//        //checkAllPermission(Constants.PERMISSIONS_STORAGE);
//        mTabLayout.setOnTabSelectedListener(this);
//        mTabLayout.addTab(mTabLayout.newTab().setText("Profiles"));
//        mTabLayout.addTab(mTabLayout.newTab().setText("Requests"));
////        viewPager.setOnPageChangeListener(this);
//        viewPager.setOffscreenPageLimit(0);
////        mTabLayout.setupWithViewPager(viewPager);
//        setupViewPager(viewPager);
        //getHomePage();
        updateDeviceId();
        checkGpsEnable();

    }

    private void swipeProfile(Integer userId, String matchType, String json) {


            apiService.swipeProfile(sessionManager.getToken(), userId, matchType, 0).enqueue(new RequestCallback(REQ_SWIPE_MATCH, this));
    }



    /**
     * Declare variable for layout views
     */
    private void init() {
        if (listener == null) return;

        res = (listener.getRes() != null) ? listener.getRes() : getActivity().getResources();
        mActivity = (listener.getInstance() != null) ? listener.getInstance() : (HomeActivity) getActivity();
    }


    /**
     * Update device id using API
     */
    private void updateDeviceId() {
        if (!sessionManager.getToken().equals("")) {
            if (sessionManager.getDeviceId().equals("")) {
                sessionManager.setDeviceId(FirebaseInstanceId.getInstance().getToken());
            }
            apiService.updateDeviceId(sessionManager.getToken(), "2", sessionManager.getDeviceId()).enqueue(new RequestCallback(REQ_UPDATE_DEVICE_ID, this));
        }
    }

    /**
     * Default on click function
     */
    @Override
    public void onClick(View v) {
        Intent intent = null;
        String msg = "", btnText = "", title = "";

        switch (v.getId()) {

            case R.id.rlt_unlike_lay:
//                if (sessionManager.getIsSawUnLike()) {
//                    String name = getUserName((int) cardStack.getTopCardItemId());
//                    msg = String.format(res.getString(R.string.alert_not_interest_msg), name);
//                    title = res.getString(R.string.alert_not_interest_title);
//                    btnText = res.getString(R.string.not_interest);
//                    showDialog(0, "", "", "click", title, msg, btnText, 0);
//                    sessionManager.setIsSawUnLike(false);
//                    sawUnLike = false;
//                } else {
//                    sawUnLike = false;
//                    cardStack.swipeTopCardLeft(SwipeDeck.ANIMATION_DURATION);
//                }
                break;

            case R.id.rlt_like_lay:
                if (!"yes".equalsIgnoreCase(sessionManager.getIsRemainingLikeLimited())) {
//                    likeSwipeCall();
                } else if (sessionManager.getRemainingLikes() > 0) {
                    sessionManager.setRemainingLikes(sessionManager.getRemainingLikes() - 1);
//                    likeSwipeCall();
                }
                break;
            default:
                break;
        }
    }


    /**
     * Get result for other activity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) return;
        if (requestCode == 100 && data != null) {
            boolean isKeepSwipe = data.getBooleanExtra("isKeepSwipe", true);
            //if (!isKeepSwipe) mActivity.getViewPager().setCurrentItem(2);
            if (!isKeepSwipe) {
                Intent intent = new Intent(mActivity, ChatConversationActivity.class);
                intent.putExtra("matchId", data.getIntExtra("matchId", 0));
                intent.putExtra("userId", data.getIntExtra("userId", 0));
                startActivity(intent);
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == 300) {
            checkAllPermission(Constants.PERMISSIONS_LOCATION);
        } else if (requestCode == 101) {
            checkGpsEnable();
        }
    }

    /**
     * Call API for get other user profiles
     */
    private void showMatchProfile() {

        if (latitude != 0 && longitude != 0) {
            Log.e(TAG, "showMatchProfile: ");
            loading.showDialog();
            apiService.showMatchingProfile(sessionManager.getToken(), latitude, longitude).enqueue(new RequestCallback(REQ_SHOW_ALL_MATCHES, this));
        }
    }

    /**
     * After API call get success response
     */
    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        loading.hideDialog();
        if (!jsonResp.isOnline()) {
//            commonMethods.showMessage(mActivity, dialog, data);
            return;
        }

        switch (jsonResp.getRequestCode()) {
            case REQ_GET_HOME:
                if (jsonResp.isSuccess()) {

                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
//                    commonMethods.showMessage(mActivity, dialog, jsonResp.getStatusMsg());
                }
                break;
            case REQ_SWIPE_MATCH:
                if (jsonResp.isSuccess()) {
                    Log.e(TAG, "onSuccess: ");
                    try {

                    }
                    catch (Exception ex) {

                        Log.e(TAG, "onSuccessexc: "+ ex.getMessage());
                    }
                    Log.e(TAG, "onSuccess: "+jsonResp);
                }
                break;
            case REQ_UPDATE_LOCATION:
                showMatchProfile();
                if (jsonResp.isSuccess()) {

                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(mActivity, dialog, jsonResp.getStatusMsg());
                }
                break;
            case REQ_SHOW_ALL_MATCHES:
                if (jsonResp.isSuccess()) {
                    try {
                        jsonObject = new JSONObject(jsonResp.getStrResponse());
                        matchProfilesModel = gson.fromJson(jsonResp.getStrResponse(), MatchProfilesModel.class);
                        if (matchProfilesModel.getUser_status().equalsIgnoreCase("active")) {
                            text_active.setVisibility(View.GONE);



                        } else {
                            text_active.setVisibility(View.GONE);
//                        text_active.setVisibility(View.VISIBLE);
//                        rl_main_data.setVisibility(View.GONE);
                        }
                        if (!TextUtils.isEmpty(matchProfilesModel.getIsOrder()) && matchProfilesModel.getIsOrder().equalsIgnoreCase("Yes")) {
                            sessionManager.setIsOrder(true);
                            sessionManager.setPlanType(matchProfilesModel.getPlanType());
                        } else {
                            sessionManager.setIsOrder(false);
                            sessionManager.setPlanType(matchProfilesModel.getPlanType());
                        }

                        Log.e(TAG, "Match Profile " + sessionManager.getRemainingBoost());

                        try {
                            if (matchProfilesModel.getUnReadCount() != null && matchProfilesModel.getUnReadCount() > 0) {
                                ((HomeActivity) getActivity()).changeChatIcon(1);
                            } else {
                                ((HomeActivity) getActivity()).changeChatIcon(0);
                            }
                        } catch (Exception e) {

                        }

                    }
                    catch (Exception e){
                        Log.e(TAG, "onSuccess: "+e.getMessage());
                    }
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(mActivity, dialog, jsonResp.getStatusMsg());

                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {

    }


    /**
     * After API call to update view
     */
    private void updateView() {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(userDetailModel.getFirstName())) {
            sb.append(userDetailModel.getFirstName());
            sessionManager.setUserName(userDetailModel.getFirstName());
        }

        if (!TextUtils.isEmpty(userDetailModel.getLastName())) {
            sb.append(" ");
            sb.append(userDetailModel.getLastName());
        }

        sb.append(", ");
        sb.append(userDetailModel.getAge());

    }

    /**
     * Check user allow all permission and ask permission to allow
     */
    private void checkAllPermission(String[] permission) {
        ArrayList<String> blockedPermission = runTimePermission.checkHasPermission(mActivity, permission);
        if (blockedPermission != null && !blockedPermission.isEmpty()) {
            boolean isBlocked = runTimePermission.isPermissionBlocked(mActivity, blockedPermission.toArray(new String[blockedPermission.size()]));
            if (isBlocked) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        //callPermissionSettings();
                        //showEnablePermissionDailog(0, getString(R.string.please_enable_permissions));
                    }
                });
            } else {

                ActivityCompat.requestPermissions(mActivity, permission, 150);
            }
        } else {
            checkGpsEnable();
        }
    }

    /**
     * Get permission result
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        System.out.println("Never Ask again : ");
        ArrayList<String> permission = runTimePermission.onRequestPermissionsResult(permissions, grantResults);
        if (permission != null && !permission.isEmpty()) {
            runTimePermission.setFirstTimePermission(true);
            String[] dsf = new String[permission.size()];
            permission.toArray(dsf);
            checkAllPermission(dsf);
        } else {
            checkGpsEnable();
        }
    }

    /**
     * If any one or more permission is deny or block show the enable permission dialog
     */
    private void showEnablePermissionDailog(final int type, String message) {
        if (!customDialog.isVisible()) {
            customDialog = new CustomDialog(message, getString(R.string.okay), new CustomDialog.btnAllowClick() {
                @Override
                public void clicked() {
                    if (type == 0)
                        callPermissionSettings();
                    else
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 101);
                }
            });
            customDialog.show(mActivity.getSupportFragmentManager(), "");
        }
    }

    /**
     * Open permission dialog
     */
    private void callPermissionSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mActivity.getApplicationContext().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 300);
    }

    /**
     * Check GPS enable or not
     */
    private void checkGpsEnable() {
        boolean isGpsEnabled = MyLocation.defaultHandler().isLocationAvailable(mActivity);
        if (!isGpsEnabled) {
            //startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 101);
            showEnablePermissionDailog(1, getString(R.string.please_enable_location));
        } else {
            isPermissionGranted = true;
            Log.e(TAG, "checkGpsEnable: ");
            MyLocation.defaultHandler().getLocation(mActivity, locationResult);
        }
    }

    /**
     * Function call the view is visible to user that time we reload the other user is swipe deck
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (sessionManager!=null&&!TextUtils.isEmpty(sessionManager.getProfileImg())) {

            // imageUtils.loadCircleImage(mActivity, civProfileImg, sessionManager.getProfileImg());
        }



        if (isVisibleToUser && view != null) {
            hideKeyboard(getContext());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        hideKeyboard(getContext());

    }


    /**
     * Show dialog while In App Purchase messages
     */
    void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        //alert("Error: " + message);
        alert("" + message);
    }

    void alert(String message) {
        android.app.AlertDialog.Builder bld = new android.app.AlertDialog.Builder(getContext());
        bld.setMessage(message);
        bld.setNeutralButton(getString(R.string.ok), null);
        Log.e(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    // We're being destroyed. It's important to dispose of the helper here!
    @Override
    public void onDestroy() {
        super.onDestroy();

        // very important:
        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) {
            mHelper.disposeWhenFinished();
            mHelper = null;
        }

        // very important:
        if (mBroadcastReceiver != null) {
            mActivity.unregisterReceiver(mBroadcastReceiver);
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
