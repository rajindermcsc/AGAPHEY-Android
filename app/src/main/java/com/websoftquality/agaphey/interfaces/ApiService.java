package com.websoftquality.agaphey.interfaces;
/**
 * @package com.trioangle.igniter
 * @subpackage interfaces
 * @category ApiService
 * @author Trioangle Product Team
 * @version 1.0
 **/

import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/*****************************************************************
 ApiService
 ****************************************************************/

public interface ApiService {

    // Login Page Slider Image
    @GET("login_slider")
    Call<ResponseBody> getTutorialSliderImg();

    // FaceBook SignUp
    @GET("social_signup")
    Call<ResponseBody> facebookSignUp(@Query("auth_id") String authId,@Query("auth_type") String authType, @Query("email_id") String fbEmail, @Query("first_name") String firstName, @Query("last_name") String lastName, @Query("dob") String fbAge, @Query("work") String fbWork, @Query("college") String fbEducation, @Query("gender") String fbGender, @Query("user_image_url") String userImgUrl, @Query("job_title") String job_title);

    // PhoneNumber Verification
    @GET("phone_number_verification")
    Call<ResponseBody> verifyPhoneNumber(@Query("phone_number") String phoneNumber,  @Query("auth_id") String authId,@Query("auth_type") String authType);

    // PhoneNumber SignUp
    @POST("signup")
    Call<ResponseBody> signUp(@Body RequestBody RequestBody);

    // Facebook Phone number signup
    @GET("fb_phone_signup")
    Call<ResponseBody> fbPhoneSignup(@QueryMap HashMap<String, String> hashMap);
    //Call<ResponseBody> fbPhoneSignup(@Query("phone_number") String phoneNumber, @Query("country_code") String countryCode,@Query("fb_id") String fbId,@Query("email_id") String fbEmail,@Query("first_name") String firstName, @Query("last_name") String lastName,@Query("dob") String fbAge,@Query("work") String fbWork,@Query("college") String fbEducation,@Query("gender") String fbGender,@Query("user_image_url") String userImgUrl,@Query("job_title") String job_title);

    // login
    @GET("login")
    Call<ResponseBody> login(@Query("phone_number") String phoneNumber, @Query("country_code") String countryCode);

    @GET("getplans")
    Call<ResponseBody> getplans();

    @FormUrlEncoded
    @POST("setpayment")
    Call<ResponseBody> setpayment(@Field("plan_id") String plan_id, @Field("cvv") String cvv, @Field("expiry_date") String expiry_date,
                                  @Field("card_num") String card_num, @Field("token") String token, @Field("user_id") String user_id);


    // PhoneNumber Verification
    @FormUrlEncoded
    @POST("signupnew")
    Call<ResponseBody> signupemail(@Field("full_name") String fullname,  @Field("user_name") String username,@Field("email") String email,
                                   @Field("password") String password,@Field("ministry_school") String ministry_school,@Field("rgp") String rgp,@Field("lifestyle") String lifestyle);


    @POST("signupnew")
    Call<ResponseBody> signup_email(@Body RequestBody RequestBody);


    @POST("loginnew")
    Call<ResponseBody> getLogin(@Query("email") String email, @Query("password") String password);


    @FormUrlEncoded
    @POST("update_profile_new")
    Call<ResponseBody> updateProfileNew(@Field("description") String description,  @Field("jesus_rel") String jesus_rel,@Field("lifestyle") String lifestyle,
                                   @Field("partner_char") String partner_char,@Field("deal_breakers") String deal_breakers,@Field("vision") String vision,
                                     @Field("true_abt") String true_abt,@Field("drink") String drink,@Field("children") String children,
                                     @Field("relocate") String relocate,@Field("alive") String alive,@Field("fun") String fun,
                                     @Field("lord_speaking") String lord_speaking,@Field("why_join") String why_join,@Field("pray") String pray,
                                     @Field("additional") String additional,@Field("dob") String dob,@Field("token") String token);

     @GET("email_verification")
    Call<ResponseBody> emailverification(@Query("email") String email);


    // Get Edit Profile details
    @GET("edit_profile")
    Call<ResponseBody> getEditProfileDetail(@Query("token") String token);

    @GET("edit_profile_new")
    Call<ResponseBody> getEditProfileDetailNew(@Query("token") String token);

    // Upload profile image
    @POST("upload_profile_image")
    Call<ResponseBody> uploadProfileImg(@Body RequestBody RequestBody);

    // Update Profile
    @GET("update_profile")
    Call<ResponseBody> updateProfile(@QueryMap HashMap<String, String> hashMap);

    // Get My Profile Detail
    @GET("own_profile_view")
    Call<ResponseBody> getMyProfileDetail(@Query("token") String token);

    // Update Device ID for Push notification
    @GET("update_device")
    Call<ResponseBody> updateDeviceId(@Query("token") String token, @Query("device_type") String device_type, @Query("device_id") String device_id);

    // Get Settings
    @GET("user_settings")
    Call<ResponseBody> getUserSettings(@Query("token") String token);

    // Update Settings
    @GET("edit_settings")
    Call<ResponseBody> updateSettings(@QueryMap HashMap<String, String> hashMap);

    // Get Plus
    @GET("plus_settings")
    Call<ResponseBody> getPlusSettings(@Query("token") String token);

    // Update Plus
    @GET("update_plus_settings")
    Call<ResponseBody> updatePlusSettings(@QueryMap HashMap<String, String> hashMap);

    // Get Other Profile View
    @GET("other_profile_view")
    Call<ResponseBody> otherProfileView(@Query("token") String token, @Query("user_id") Integer userId);

    // Home Page
    @GET("home_page")
    Call<ResponseBody> homePage(@Query("token") String token);

    // Chat
    @GET("message_conversation")
    Call<ResponseBody> chat(@Query("token") String token, @Query("match_id") String userId);

    // Swipe Profile
    @GET("swipe_profiles")
    Call<ResponseBody> swipeProfile(@Query("token") String token, @Query("user_id") Integer userId, @Query("status") String status, @Query("used_subscription_id") Integer subscriptionId);

    // Boost user Profile
    @GET("add_user_boost")
    Call<ResponseBody> boostUser(@Query("token") String token);

    // Insert Location
    @GET("insertlocation")
    Call<ResponseBody> insertLocation(@Query("token") String token, @Query("latitude") Double latitude, @Query("longitude") Double longitude, @Query("type") String type);


    // Cheange Default Location
    @GET("defaultLocaion")
    Call<ResponseBody> changeDefaultLocation(@Query("token") String token, @Query("id") int id);


    // Show all matched profile
    @GET("matching_profiles")
    Call<ResponseBody> showMatchingProfile(@Query("token") String token, @Query("latitude") Double latitude, @Query("longitude") Double longitude);

    // UserName Claim
    @GET("username_claim")
    Call<ResponseBody> claimUserName(@Query("token") String token, @Query("username") String username);

    // Igniter Plus Slider
    @GET("super_plus_slider")
    Call<ResponseBody> igniterPlusSlider(@Query("token") String token);

    // Igniter Plan Slider
    @GET("plan_slider")
    Call<ResponseBody> igniterPlanSlider(@Query("type") String token);

    // logout
    @GET("logout")
    Call<ResponseBody> logout(@Query("token") String token);

    // Get UnMatch Details
    @GET("report_details")
    Call<ResponseBody> unMatchDetails(@Query("token") String token,@Query("type") String unmatch);

    @GET("report_account")
    Call<ResponseBody> unMatchUser(@QueryMap HashMap<String, String> hashMap);


    // Matched profile list
    @GET("match_details")
    Call<ResponseBody> matchedDetails(@Query("token") String token);

    // Message Conversation
    @GET("message_conversation")
    Call<ResponseBody> messageConversation(@Query("token") String token, @Query("match_id") Integer matchId,@Query("timezone") String timezone);

    // Sent Message
    @GET("send_message")
    Call<ResponseBody> sendMessage(@Query("token") String token, @Query("match_id") Integer matchId, @Query("message") String message,@Query("timezone") String timezone);

    // Message Like
    @GET("message_like")
    Call<ResponseBody> likeMessage(@Query("token") String token, @Query("message_id") Integer messageId, @Query("user_id") Integer userId, @Query("message") String message);

    //Remove photos
    @GET("remove_profile_image")
    Call<ResponseBody> remove_profile_image(@Query("image_id") Integer imageid, @Query("token") String token);

    // After Payment (Update Order id)
    @GET("after_payment")
    Call<ResponseBody> afterPayment(@Query("payment_type") String paymentType, @Query("transaction_id") String transactionId, @Query("plan_id") Integer planId, @Query("plan_type") String planType, @Query("package_name") String packageName, @Query("product_id") String productId, @Query("purchase_token") String purchaseToken, @Query("token") String token);

    //Check user Mobile Number
    @GET("numbervalidation")
    Call<ResponseBody> numbervalidation(@Query("mobile_number") String mobile_number);

    @GET("liked_profiles")
    Call<ResponseBody> likedProfiles(@Query("page") String pageNo, @Query("token") String token);
}
