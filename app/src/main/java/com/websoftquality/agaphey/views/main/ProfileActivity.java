package com.websoftquality.agaphey.views.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.gson.Gson;
import com.obs.CustomEditText;
import com.obs.CustomTextView;
import com.obs.image_cropping.CropImage;
import com.obs.image_cropping.ImageMinimumSizeCalculator;
import com.websoftquality.agaphey.BuildConfig;
import com.websoftquality.agaphey.R;
import com.websoftquality.agaphey.adapters.profile.EditProfileImageListAdapter;
import com.websoftquality.agaphey.backgroundtask.ImageCompressAsyncTask;
import com.websoftquality.agaphey.configs.AppController;
import com.websoftquality.agaphey.configs.Constants;
import com.websoftquality.agaphey.configs.RunTimePermission;
import com.websoftquality.agaphey.configs.SessionManager;
import com.websoftquality.agaphey.datamodels.main.EditProfileModel;
import com.websoftquality.agaphey.datamodels.main.ImageModel;
import com.websoftquality.agaphey.datamodels.main.JsonResponse;
import com.websoftquality.agaphey.interfaces.ApiService;
import com.websoftquality.agaphey.interfaces.ImageListener;
import com.websoftquality.agaphey.interfaces.ServiceListener;
import com.websoftquality.agaphey.utils.Apierror_handle;
import com.websoftquality.agaphey.utils.CommonMethods;
import com.websoftquality.agaphey.utils.ImageUtils;
import com.websoftquality.agaphey.utils.Loading;
import com.websoftquality.agaphey.utils.RequestCallback;
import com.websoftquality.agaphey.views.customize.CustomDialog;
import com.websoftquality.agaphey.views.customize.CustomRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.RequestBody;

import static com.websoftquality.agaphey.utils.Enums.REQ_GET_EDIT_PROFILE;
import static com.websoftquality.agaphey.utils.Enums.REQ_REMOVE_IMAGE;
import static com.websoftquality.agaphey.utils.Enums.REQ_UPDATE_PROFILE;
import static com.websoftquality.agaphey.utils.Enums.REQ_UPLOAD_PROFILE_IMG;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, ServiceListener, ImageListener {
    private static final String TAG = "ProfileActivity";
    ImageView updateImageView;
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
    ImageUtils imageUtils;
    @Inject
    RunTimePermission runTimePermission;
    private CustomTextView tvHeader, tvBackArrow,  tvAboutUserName, tvAboutCount,edt_dob;
    private CustomEditText edtAbout,edt_relation,edt_lifestyle,edt_characteristics,edt_deal,edt_vision,edt_sound,edt_smoke,edt_child,edt_relocate,edt_alive,
            edt_fun,edt_lord,edt_join,edt_pray,edt_additional;
    private RelativeLayout rltProfileImageOne, rltProfileImageTwo, rltProfileImageThree, rltProfileImageFour, rltProfileImageFive, rltProfileImageSix;
    private CustomRecyclerView rvEditProfileList;
    private EditProfileImageListAdapter imageListAdapter;
    private AlertDialog dialog;
    private EditProfileModel editProfileModel;
    private boolean isDelete = false;
    private ImageView ivUserImageOne, ivUserImageTwo, ivUserImageThree, ivUserImageFour, ivUserImageFive, ivUserImageSix;
    private ImageView tvAddIconOne, tvAddIconTwo, tvAddIconThree, tvAddIconFour, tvAddIconFive, tvAddIconSix;
    private ImageView tvCloseIconOne, tvCloseIconTwo, tvCloseIconThree, tvCloseIconFour, tvCloseIconFive, tvCloseIconSix,tv_apply;
    String dob;
    private File imageFile = null;
    private Uri imageUri;
    private String imagePath = "";

    // private InstagramHelper instagramHelper;
    private int clickPos = 1;
    private String userName = "";
    private String img;
    private String img_id;
    private ArrayList<String> image_id;

    Loading loading;
    String apiurl;
    Apierror_handle apierror_handle;
    JSONObject jsonObject;
    DatePickerDialog picker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        AppController.getAppComponent().inject(this);
        loading=new Loading(this);
        apierror_handle=new Apierror_handle(this);
        image_id = new ArrayList<String>();
        //instagramHelper = AppController.getInstagramHelper();
        initView();
        getIntentValues();
//        initRecyclerView();
        getEditProfileDetails();
    }

    private void initView() {
        tvHeader = findViewById(R.id.tv_header_title);
        tvBackArrow = findViewById(R.id.tv_left_arrow);
        tvAboutUserName = findViewById(R.id.tv_about_username);
        tvAboutCount = findViewById(R.id.tv_about_count);
        tvAboutCount.setVisibility(View.GONE);

        edtAbout = findViewById(R.id.edt_about);
        edt_relation = findViewById(R.id.edt_relation);
        edt_lifestyle = findViewById(R.id.edt_lifestyle);
        edt_characteristics = findViewById(R.id.edt_characteristics);
        edt_deal = findViewById(R.id.edt_deal);
        edt_vision = findViewById(R.id.edt_vision);
        edt_sound = findViewById(R.id.edt_sound);
        edt_smoke = findViewById(R.id.edt_smoke);
        edt_child = findViewById(R.id.edt_child);
        edt_relocate = findViewById(R.id.edt_relocate);
        edt_alive = findViewById(R.id.edt_alive);
        edt_fun = findViewById(R.id.edt_fun);
        edt_lord = findViewById(R.id.edt_lord);
        edt_join = findViewById(R.id.edt_join);
        edt_pray = findViewById(R.id.edt_pray);
        tv_apply = findViewById(R.id.tv_apply);
        edt_additional = findViewById(R.id.edt_additional);
        edt_dob = findViewById(R.id.edt_dob);
        edt_dob.setOnClickListener(this);
        rvEditProfileList = findViewById(R.id.rv_edit_profile_list);


        dialog = commonMethods.getAlertDialog(this);

        rltProfileImageOne = findViewById(R.id.rlt_profile_image_one);
        rltProfileImageTwo = findViewById(R.id.rlt_profile_image_two);
        rltProfileImageThree = findViewById(R.id.rlt_profile_image_three);
        rltProfileImageFour = findViewById(R.id.rlt_profile_image_four);
        rltProfileImageFive = findViewById(R.id.rlt_profile_image_five);
        rltProfileImageSix = findViewById(R.id.rlt_profile_image_six);

        ivUserImageOne = rltProfileImageOne.findViewById(R.id.iv_user_image);
        ivUserImageTwo = rltProfileImageTwo.findViewById(R.id.iv_user_image);
        ivUserImageThree = rltProfileImageThree.findViewById(R.id.iv_user_image);
        ivUserImageFour = rltProfileImageFour.findViewById(R.id.iv_user_image);
        ivUserImageFive = rltProfileImageFive.findViewById(R.id.iv_user_image);
        ivUserImageSix = rltProfileImageSix.findViewById(R.id.iv_user_image);

        tvAddIconOne = rltProfileImageOne.findViewById(R.id.tv_add_icon);
        tvAddIconTwo = rltProfileImageTwo.findViewById(R.id.tv_add_icon);
        tvAddIconThree = rltProfileImageThree.findViewById(R.id.tv_add_icon);
        tvAddIconFour = rltProfileImageFour.findViewById(R.id.tv_add_icon);
        tvAddIconFive = rltProfileImageFive.findViewById(R.id.tv_add_icon);
        tvAddIconSix = rltProfileImageSix.findViewById(R.id.tv_add_icon);

        tvCloseIconOne = rltProfileImageOne.findViewById(R.id.tv_close_icon);
        tvCloseIconTwo = rltProfileImageTwo.findViewById(R.id.tv_close_icon);
        tvCloseIconThree = rltProfileImageThree.findViewById(R.id.tv_close_icon);
        tvCloseIconFour = rltProfileImageFour.findViewById(R.id.tv_close_icon);
        tvCloseIconFive = rltProfileImageFive.findViewById(R.id.tv_close_icon);
        tvCloseIconSix = rltProfileImageSix.findViewById(R.id.tv_close_icon);


        edtAbout.clearFocus();

        tvAddIconOne.setTag(1);
        tvAddIconTwo.setTag(2);
        tvAddIconThree.setTag(3);
        tvAddIconFour.setTag(4);
        tvAddIconFive.setTag(5);
        tvAddIconSix.setTag(6);

        tvCloseIconOne.setTag(1);
        tvCloseIconTwo.setTag(2);
        tvCloseIconThree.setTag(3);
        tvCloseIconFour.setTag(4);
        tvCloseIconFive.setTag(5);
        tvCloseIconSix.setTag(6);

        setImageViewCount();
        iniTextChangeListener();

        tvHeader.setTextColor(getResources().getColor(R.color.black));
        tvHeader.setText(getString(R.string.header_edit_info));

        tvBackArrow.setOnClickListener(this);


        tvAddIconOne.setOnClickListener(this);
        tvAddIconTwo.setOnClickListener(this);
        tvAddIconThree.setOnClickListener(this);
        tvAddIconFour.setOnClickListener(this);
        tvAddIconFive.setOnClickListener(this);
        tvAddIconSix.setOnClickListener(this);
        tv_apply.setOnClickListener(this);

        tvCloseIconOne.setOnClickListener(this);
        tvCloseIconTwo.setOnClickListener(this);
        tvCloseIconThree.setOnClickListener(this);
        tvCloseIconFour.setOnClickListener(this);
        tvCloseIconFive.setOnClickListener(this);
        tvCloseIconSix.setOnClickListener(this);
    }

    private void setImageViewCount() {
        String[] count = this.getResources().getStringArray(R.array.photo_count);
        CustomTextView tvImageCount1 = rltProfileImageOne.findViewById(R.id.tv_count);
        tvImageCount1.setText(count[0]);
        CustomTextView tvImageCount2 = rltProfileImageTwo.findViewById(R.id.tv_count);
        tvImageCount2.setText(count[1]);
        CustomTextView tvImageCount3 = rltProfileImageThree.findViewById(R.id.tv_count);
        tvImageCount3.setText(count[2]);
        CustomTextView tvImageCount4 = rltProfileImageFour.findViewById(R.id.tv_count);
        tvImageCount4.setText(count[3]);
        CustomTextView tvImageCount5 = rltProfileImageFive.findViewById(R.id.tv_count);
        tvImageCount5.setText(count[4]);
        CustomTextView tvImageCount6 = rltProfileImageSix.findViewById(R.id.tv_count);
        tvImageCount6.setText(count[5]);
    }

    private void iniTextChangeListener() {
        edtAbout.setCursorVisible(false);

        edtAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtAbout.setCursorVisible(true);
            }
        });

        edtAbout.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //edtAbout.setCursorVisible(false);
                tvAboutCount.setText(String.valueOf(500 - s.length()));
                tvAboutCount.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }



    private void getEditProfileDetails() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.getEditProfileDetailNew(sessionManager.getToken()).enqueue(new RequestCallback(REQ_GET_EDIT_PROFILE, this));
    }

    @SuppressLint("StringFormatInvalid")
    private void getIntentValues() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !TextUtils.isEmpty(bundle.getString("userName"))) {
            tvAboutUserName.setText(String.format(getString(R.string.about), bundle.getString("userName")));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edt_dob:
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                picker = new DatePickerDialog(ProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edt_dob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();

                break;
            case R.id.tv_left_arrow:
                updateProfile();
                break;
            case R.id.tv_apply:
                updateProfile();
                break;

            case R.id.tv_add_icon:
                clickPos = (int) v.getTag();
                //pickProfileImg(false);
                isDelete = false;
                checkAllPermission(Constants.PERMISSIONS_PHOTO, isDelete);
                break;
            case R.id.tv_close_icon:
                clickPos = (int) v.getTag();
                if (clickPos == 1 && image_id.size() == 1) {
                    isDelete = true;
                    checkAllPermission(Constants.PERMISSIONS_PHOTO, isDelete);
                    //pickProfileImg(true);
                } else {
                    commonMethods.showProgressDialog(this, customDialog);
                    apiService.remove_profile_image(Integer.valueOf(image_id.get(clickPos - 1)), sessionManager.getToken()).enqueue(new RequestCallback(REQ_REMOVE_IMAGE, this));
                }
                break;


            default:
                break;
        }
    }

    private HashMap<String, String> getParams() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", sessionManager.getToken());
        //hashMap.put("instagram_id", instagramUserName);

        return hashMap;
    }



    public void pickProfileImg(boolean isDelete) {
        this.isDelete = isDelete;
        View view = getLayoutInflater().inflate(R.layout.camera_dialog_layout, null);
        LinearLayout lltCamera = view.findViewById(R.id.llt_camera);
        LinearLayout lltLibrary = view.findViewById(R.id.llt_library);

        final Dialog bottomSheetDialog = new Dialog(this, R.style.MaterialDialogSheet);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCancelable(true);
        if (bottomSheetDialog.getWindow() == null) return;
        bottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomSheetDialog.show();

        lltCamera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imageFile = commonMethods.cameraFilePath();
                imageUri = FileProvider.getUriForFile(ProfileActivity.this, BuildConfig.APPLICATION_ID + ".provider", imageFile);

                try {
                    List<ResolveInfo> resolvedIntentActivities = ProfileActivity.this.getPackageManager().queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
                        String packageName = resolvedIntentInfo.activityInfo.packageName;
                        grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                    cameraIntent.putExtra("return-data", true);
                    cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, 1);
                commonMethods.refreshGallery(ProfileActivity.this, imageFile);
            }
        });

        lltLibrary.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                imageFile = commonMethods.getDefaultFileName(ProfileActivity.this);

                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, Constants.REQUEST_CODE_GALLERY);
            }
        });
    }

    public void loadImages() {
        switch (clickPos) {
            case 1:
                getImageFromUrl(ivUserImageOne);
                tvAddIconOne.setVisibility(View.GONE);
                tvCloseIconOne.setVisibility(View.VISIBLE);
                break;
            case 2:
                getImageFromUrl(ivUserImageTwo);
                tvAddIconTwo.setVisibility(View.GONE);
                tvCloseIconTwo.setVisibility(View.VISIBLE);
                break;
            case 3:
                getImageFromUrl(ivUserImageThree);
                tvAddIconThree.setVisibility(View.GONE);
                tvCloseIconThree.setVisibility(View.VISIBLE);
                break;
            case 4:
                getImageFromUrl(ivUserImageFour);
                tvAddIconFour.setVisibility(View.GONE);
                tvCloseIconFour.setVisibility(View.VISIBLE);
                break;
            case 5:
                getImageFromUrl(ivUserImageFive);
                tvAddIconFive.setVisibility(View.GONE);
                tvCloseIconFive.setVisibility(View.VISIBLE);
                break;
            case 6:
                getImageFromUrl(ivUserImageSix);
                tvAddIconSix.setVisibility(View.GONE);
                tvCloseIconSix.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            commonMethods.showMessage(this, dialog, data);
            return;
        }
        switch (jsonResp.getRequestCode()) {
            case REQ_GET_EDIT_PROFILE:
                if (jsonResp.isSuccess()) {
                    onSuccessGetEditProfile(jsonResp);
                } else {
                }
                break;
            case REQ_UPDATE_PROFILE:
                if (jsonResp.isSuccess()) {
                    onBackPressed();
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;
            case REQ_UPLOAD_PROFILE_IMG:
                if (jsonResp.isSuccess()) {
                    if (isDelete) {
                        commonMethods.showProgressDialog(this, customDialog);
                        apiService.remove_profile_image(Integer.valueOf(image_id.get(clickPos - 1)), sessionManager.getToken()).enqueue(new RequestCallback(REQ_REMOVE_IMAGE, this));
                    } else {
                        onSuccessGetEditProfile(jsonResp);
                    }
                } else {
                }
                break;
            case REQ_REMOVE_IMAGE:
                if (jsonResp.isSuccess()) {
                    onSuccessGetEditProfile(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;
            default:
                break;
        }
    }

    public void getImageFromUrl(ImageView imageView) {
        Glide.with(this)
                .load(img)
                .transforms(new CenterCrop(), new RoundedCorners(20))
                //.bitmapTransform(new RoundedCornersTransformation(this, 5, 1))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) commonMethods.showMessage(this, dialog, data);
    }

    private void onSuccessGetEditProfile(JsonResponse jsonResp) {
        getImageId(jsonResp);
        editProfileModel = gson.fromJson(jsonResp.getStrResponse(), EditProfileModel.class);
        if (editProfileModel != null) {
            updateView();
        }
    }

    private void updateProfile() {

        validations();
//        commonMethods.showProgressDialog(this, customDialog);
//        apiService.updateProfile(getParams()).enqueue(new RequestCallback(REQ_UPDATE_PROFILE, this));
    }

    private void validations() {

        if (edtAbout.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "About me field is mandatory", Toast.LENGTH_SHORT).show();
        }else if (edt_relation.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Relation field is mandatory", Toast.LENGTH_SHORT).show();
        }else if (edt_lifestyle.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Lifestyle field is mandatory", Toast.LENGTH_SHORT).show();
        }else if (edt_characteristics.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Characteristics field is mandatory", Toast.LENGTH_SHORT).show();
        }else if (edt_deal.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Non-negotiables and deal breakers field is mandatory", Toast.LENGTH_SHORT).show();
        }else if (edt_vision.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Vision field is mandatory", Toast.LENGTH_SHORT).show();
        }else if (edt_dob.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "DOB is mandatory", Toast.LENGTH_SHORT).show();
        }else {
            apiupdateProfile(getResources().getString(R.string.base_url).concat("update_profile_new"));
        }
    }


    private void apiupdateProfile(String apiurl) {
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
                        Intent intent=new Intent(ProfileActivity.this,HomeActivity.class);
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
                params.put("description", edtAbout.getText().toString());
                params.put("jesus_rel", edt_relation.getText().toString());
                params.put("lifestyle", edt_lifestyle.getText().toString());
                params.put("partner_char", edt_characteristics.getText().toString());
                params.put("deal_breakers", edt_deal.getText().toString());
                params.put("vision", edt_vision.getText().toString());
                params.put("true_abt", edt_sound.getText().toString());
                params.put("drink", edt_smoke.getText().toString());
                params.put("children", edt_child.getText().toString());
                params.put("relocate", edt_relocate.getText().toString());
                params.put("alive", edt_alive.getText().toString());
                params.put("fun", edt_fun.getText().toString());
                params.put("lord_speaking", edt_lord.getText().toString());
                params.put("why_join", edt_join.getText().toString());
                params.put("pray", edt_pray.getText().toString());
                params.put("additional", edt_additional.getText().toString());
                SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date;
                try {
                    date = originalFormat.parse(edt_dob.getText().toString());
                    dob=targetFormat.format(date);
                    Log.e(TAG, "getParams: "+dob);
                } catch (ParseException ex) {
                    // Handle Exception.
                }

                params.put("dob", dob);
                params.put("token", sessionManager.getToken());
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }




    private void updateView() {

        if (!TextUtils.isEmpty(editProfileModel.getIsOrder()) && editProfileModel.getIsOrder().equalsIgnoreCase("Yes")) {
            sessionManager.setIsOrder(true);
            sessionManager.setPlanType(editProfileModel.getPlanType());
        } else {
            sessionManager.setIsOrder(false);
            sessionManager.setPlanType(editProfileModel.getPlanType());
        }

        if (!TextUtils.isEmpty(editProfileModel.getAbout())) {
            edtAbout.setText(editProfileModel.getAbout());
        }
        if (!TextUtils.isEmpty(editProfileModel.getJesus_rel())) {
            edt_relation.setText(editProfileModel.getJesus_rel());
        }
        if (!TextUtils.isEmpty(editProfileModel.getLifestyle())) {
            edt_lifestyle.setText(editProfileModel.getLifestyle());
        }
        if (!TextUtils.isEmpty(editProfileModel.getPartner_char())) {
            edt_characteristics.setText(editProfileModel.getPartner_char());
        } if (!TextUtils.isEmpty(editProfileModel.getDeal_breakers())) {
            edt_deal.setText(editProfileModel.getDeal_breakers());
        } if (!TextUtils.isEmpty(editProfileModel.getVision())) {
            edt_vision.setText(editProfileModel.getVision());
        } if (!TextUtils.isEmpty(editProfileModel.getTrue_abt())) {
            edt_sound.setText(editProfileModel.getTrue_abt());
        } if (!TextUtils.isEmpty(editProfileModel.getDrink())) {
            edt_smoke.setText(editProfileModel.getDrink());
        }if (!TextUtils.isEmpty(editProfileModel.getChildren())) {
            edt_child.setText(editProfileModel.getChildren());
        }if (!TextUtils.isEmpty(editProfileModel.getRelocate())) {
            edt_relocate.setText(editProfileModel.getRelocate());
        }if (!TextUtils.isEmpty(editProfileModel.getAlive())) {
            edt_alive.setText(editProfileModel.getAlive());
        }if (!TextUtils.isEmpty(editProfileModel.getFun())) {
            edt_fun.setText(editProfileModel.getFun());
        }if (!TextUtils.isEmpty(editProfileModel.getLord_speaking())) {
            edt_lord.setText(editProfileModel.getLord_speaking());
        }if (!TextUtils.isEmpty(editProfileModel.getWhy_join())) {
            edt_join.setText(editProfileModel.getWhy_join());
        }if (!TextUtils.isEmpty(editProfileModel.getPray())) {
            edt_pray.setText(editProfileModel.getPray());
        }if (!TextUtils.isEmpty(editProfileModel.getAdditional())) {
            edt_additional.setText(editProfileModel.getAdditional());
        }if (!TextUtils.isEmpty(editProfileModel.getDob())) {
            edt_dob.setText(editProfileModel.getDob());
        }



        ArrayList<ImageModel> imageList = editProfileModel.getImageList();
        if (imageList.size() > 0) {
            if (imageList.get(0) != null) {
                imageUtils.loadImageCurve(this, ivUserImageOne, imageList.get(0).getSmallImageUrl(), imageList.get(0).getImageId());
                tvAddIconOne.setVisibility(View.GONE);
                tvCloseIconOne.setVisibility(View.VISIBLE);
            } else {
                tvAddIconOne.setVisibility(View.VISIBLE);
                tvCloseIconOne.setVisibility(View.GONE);
                ivUserImageOne.setImageDrawable(null);
                tvAddIconTwo.setVisibility(View.VISIBLE);
                tvCloseIconTwo.setVisibility(View.GONE);
                ivUserImageTwo.setImageDrawable(null);
                tvAddIconThree.setVisibility(View.VISIBLE);
                tvCloseIconThree.setVisibility(View.GONE);
                ivUserImageThree.setImageDrawable(null);
                tvAddIconFour.setVisibility(View.VISIBLE);
                tvCloseIconFour.setVisibility(View.GONE);
                ivUserImageFour.setImageDrawable(null);
                tvAddIconFive.setVisibility(View.VISIBLE);
                tvCloseIconFive.setVisibility(View.GONE);
                ivUserImageFive.setImageDrawable(null);
                tvAddIconSix.setVisibility(View.VISIBLE);
                tvCloseIconSix.setVisibility(View.GONE);
                ivUserImageSix.setImageDrawable(null);
            }
            if (imageList.size() > 1) {
                imageUtils.loadImageCurve(this, ivUserImageTwo, imageList.get(1).getSmallImageUrl(), imageList.get(1).getImageId());
                tvAddIconTwo.setVisibility(View.GONE);
                tvCloseIconTwo.setVisibility(View.VISIBLE);
            } else {
                tvAddIconTwo.setVisibility(View.VISIBLE);
                tvCloseIconTwo.setVisibility(View.GONE);
                ivUserImageTwo.setImageDrawable(null);
                tvAddIconThree.setVisibility(View.VISIBLE);
                tvCloseIconThree.setVisibility(View.GONE);
                ivUserImageThree.setImageDrawable(null);
                tvAddIconFour.setVisibility(View.VISIBLE);
                tvCloseIconFour.setVisibility(View.GONE);
                ivUserImageFour.setImageDrawable(null);
                tvAddIconFive.setVisibility(View.VISIBLE);
                tvCloseIconFive.setVisibility(View.GONE);
                ivUserImageFive.setImageDrawable(null);
                tvAddIconSix.setVisibility(View.VISIBLE);
                tvCloseIconSix.setVisibility(View.GONE);
                ivUserImageSix.setImageDrawable(null);
            }
            if (imageList.size() > 2) {
                imageUtils.loadImageCurve(this, ivUserImageThree, imageList.get(2).getSmallImageUrl(), imageList.get(2).getImageId());
                tvAddIconThree.setVisibility(View.GONE);
                tvCloseIconThree.setVisibility(View.VISIBLE);
            } else {
                tvAddIconThree.setVisibility(View.VISIBLE);
                tvCloseIconThree.setVisibility(View.GONE);
                ivUserImageThree.setImageDrawable(null);
                tvAddIconFour.setVisibility(View.VISIBLE);
                tvCloseIconFour.setVisibility(View.GONE);
                ivUserImageFour.setImageDrawable(null);
                tvAddIconFive.setVisibility(View.VISIBLE);
                tvCloseIconFive.setVisibility(View.GONE);
                ivUserImageFive.setImageDrawable(null);
                tvAddIconSix.setVisibility(View.VISIBLE);
                tvCloseIconSix.setVisibility(View.GONE);
                ivUserImageSix.setImageDrawable(null);
            }
            if (imageList.size() > 3) {
                imageUtils.loadImageCurve(this, ivUserImageFour, imageList.get(3).getSmallImageUrl(), imageList.get(3).getImageId());
                tvAddIconFour.setVisibility(View.GONE);
                tvCloseIconFour.setVisibility(View.VISIBLE);
            } else {
                tvAddIconFour.setVisibility(View.VISIBLE);
                tvCloseIconFour.setVisibility(View.GONE);
                ivUserImageFour.setImageDrawable(null);
                tvAddIconFive.setVisibility(View.VISIBLE);
                tvCloseIconFive.setVisibility(View.GONE);
                ivUserImageFive.setImageDrawable(null);
                tvAddIconSix.setVisibility(View.VISIBLE);
                tvCloseIconSix.setVisibility(View.GONE);
                ivUserImageSix.setImageDrawable(null);
            }
            if (imageList.size() > 4) {
                imageUtils.loadImageCurve(this, ivUserImageFive, imageList.get(4).getSmallImageUrl(), imageList.get(4).getImageId());
                tvAddIconFive.setVisibility(View.GONE);
                tvCloseIconFive.setVisibility(View.VISIBLE);
            } else {
                tvAddIconFive.setVisibility(View.VISIBLE);
                tvCloseIconFive.setVisibility(View.GONE);
                ivUserImageFive.setImageDrawable(null);
                tvAddIconSix.setVisibility(View.VISIBLE);
                tvCloseIconSix.setVisibility(View.GONE);
                ivUserImageSix.setImageDrawable(null);
            }
            if (imageList.size() > 5) {
                imageUtils.loadImageCurve(this, ivUserImageSix, imageList.get(5).getSmallImageUrl(), imageList.get(5).getImageId());
                tvAddIconSix.setVisibility(View.GONE);
                tvCloseIconSix.setVisibility(View.VISIBLE);
            } else {
                tvAddIconSix.setVisibility(View.VISIBLE);
                tvCloseIconSix.setVisibility(View.GONE);
                ivUserImageSix.setImageDrawable(null);
            }

        }
    }

    public void getImageId(JsonResponse jsonResp) {
        Log.e(TAG, "getImageId: "+jsonResp.getUrl());
        image_id.clear();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonResp.getStrResponse());
            JSONArray array = jsonObject.getJSONArray("image_url");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                img_id = object.getString("image_id");
                image_id.add(img_id);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                    startCropImage();
                    break;
                case Constants.REQUEST_CODE_GALLERY:
                    try {
                        InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                        FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
                        copyStream(inputStream, fileOutputStream);
                        fileOutputStream.close();
                        if (inputStream != null) inputStream.close();
                        startCropImage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    try {
                        imagePath = result.getUri().getPath();
                        if (!TextUtils.isEmpty(imagePath)) {
                            Bitmap mbitmap = BitmapFactory.decodeFile(imagePath);
                            Bitmap imageRounded = Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), mbitmap.getConfig());
                            Canvas canvas = new Canvas(imageRounded);
                            Paint mpaint = new Paint();
                            mpaint.setAntiAlias(true);
                            mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                            canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 25, 25, mpaint);// Round Image Corner 100 100 100 100
                            loadImages();
                            commonMethods.showProgressDialog(this, customDialog);
                            new ImageCompressAsyncTask(ProfileActivity.this, imagePath, this, "").execute();
                        }
                    } catch (OutOfMemoryError | Exception e) {
                        e.printStackTrace();
                    }
                    break;
               /* case InstagramHelperConstants.INSTA_LOGIN:
                    InstagramUser user = instagramHelper.getInstagramUser(this);
                    if (user != null && user.getData() != null && !TextUtils.isEmpty(user.getData().getUsername())) {
                        tvInstagram.setText(user.getData().getUsername());
                        tvConnect.setText(getString(R.string.disconnect));
                        instagramUserName = user.getData().getUsername();
                    }
                    break;*/
                default:
                    break;
            }
        }
    }


    private void copyStream(InputStream input, FileOutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    private void startCropImage() {
        if (imageFile == null) return;
        int[] minimumSquareDimen = ImageMinimumSizeCalculator.getMinSquarDimension(Uri.fromFile(imageFile), this);
        CropImage.activity(Uri.fromFile(imageFile))
                .setDefaultlyCropEnabled(true)
                .setAspectRatio(10, 10)
                .setOutputCompressQuality(100)
                .setMinCropResultSize(minimumSquareDimen[0], minimumSquareDimen[1])
                .start(this);
    }

    @Override
    public void onImageCompress(String filePath, RequestBody requestBody) {
        commonMethods.hideProgressDialog();
        if (!TextUtils.isEmpty(filePath) && requestBody != null) {
            commonMethods.showProgressDialog(this, customDialog);
            apiService.uploadProfileImg(requestBody).enqueue(new RequestCallback(REQ_UPLOAD_PROFILE_IMG, this));
        }
    }



    protected void onResume() {
        super.onResume();


    }

    private void checkAllPermission(String[] permission, boolean isDelete) {
        ArrayList<String> blockedPermission = runTimePermission.checkHasPermission(this, permission);
        if (blockedPermission != null && !blockedPermission.isEmpty()) {
            boolean isBlocked = runTimePermission.isPermissionBlocked(this, blockedPermission.toArray(new String[blockedPermission.size()]));
            if (isBlocked) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        showEnablePermissionDailog(0, getString(R.string.please_enable_permissions));
                    }
                });
            } else {
                ActivityCompat.requestPermissions(this, permission, 300);
            }
        } else {
            pickProfileImg(isDelete);
            //checkGpsEnable();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        ArrayList<String> permission = runTimePermission.onRequestPermissionsResult(permissions, grantResults);
        if (permission != null && !permission.isEmpty()) {
            runTimePermission.setFirstTimePermission(true);
            String[] dsf = new String[permission.size()];
            permission.toArray(dsf);
            checkAllPermission(dsf, isDelete);
        } else {
            pickProfileImg(isDelete);
        }
    }

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
            customDialog.show(getSupportFragmentManager(), "");
        }
    }

    private void callPermissionSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 300);
    }
}
