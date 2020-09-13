package com.websoftquality.agaphey.dependencies.component;
/**
 * @package com.trioangle.igniter
 * @subpackage dependencies.component
 * @category AppComponent
 * @author Trioangle Product Team
 * @version 1.0
 **/

import javax.inject.Singleton;

import dagger.Component;
import com.websoftquality.agaphey.adapters.chat.ChatConversationListAdapter;
import com.websoftquality.agaphey.adapters.chat.MessageUserListAdapter;
import com.websoftquality.agaphey.adapters.chat.NewMatchesListAdapter;
import com.websoftquality.agaphey.adapters.chat.UnmatchReasonListAdapter;
import com.websoftquality.agaphey.adapters.chat.UserListAdapter;
import com.websoftquality.agaphey.adapters.main.ProfileSliderAdapter;
import com.websoftquality.agaphey.adapters.matches.MatchesSwipeAdapter;
import com.websoftquality.agaphey.adapters.profile.EditProfileImageListAdapter;
import com.websoftquality.agaphey.adapters.profile.EnlargeSliderAdapter;
import com.websoftquality.agaphey.adapters.profile.IgniterSliderAdapter;
import com.websoftquality.agaphey.adapters.profile.LocationListAdapter;
import com.websoftquality.agaphey.backgroundtask.ImageCompressAsyncTask;
import com.websoftquality.agaphey.configs.RunTimePermission;
import com.websoftquality.agaphey.configs.SessionManager;
import com.websoftquality.agaphey.dependencies.module.AppContainerModule;
import com.websoftquality.agaphey.dependencies.module.ApplicationModule;
import com.websoftquality.agaphey.dependencies.module.NetworkModule;
import com.websoftquality.agaphey.layoutmanager.SwipeableTouchHelperCallback;
import com.websoftquality.agaphey.likedusers.LikedUserAdapter;
import com.websoftquality.agaphey.likedusers.LikedUsersActivity;
import com.websoftquality.agaphey.pushnotification.MyFirebaseInstanceIDService;
import com.websoftquality.agaphey.pushnotification.MyFirebaseMessagingService;
import com.websoftquality.agaphey.pushnotification.NotificationUtils;
import com.websoftquality.agaphey.swipedeck.Utility.SwipeListener;
import com.websoftquality.agaphey.utils.CommonMethods;
import com.websoftquality.agaphey.utils.DateTimeUtility;
import com.websoftquality.agaphey.utils.ImageUtils;
import com.websoftquality.agaphey.utils.RequestCallback;
import com.websoftquality.agaphey.utils.WebServiceUtils;
import com.websoftquality.agaphey.views.chat.ChatConversationActivity;
import com.websoftquality.agaphey.views.chat.ChatFragment;
import com.websoftquality.agaphey.views.chat.CreateGroupActivity;
import com.websoftquality.agaphey.views.chat.MatchUsersActivity;
import com.websoftquality.agaphey.views.main.BoostDialogActivity;
import com.websoftquality.agaphey.views.main.DashboardFragment;
import com.websoftquality.agaphey.views.main.HomeActivity;
import com.websoftquality.agaphey.views.main.IgniterGoldActivity;
import com.websoftquality.agaphey.views.main.IgniterPageFragment;
import com.websoftquality.agaphey.views.main.IgniterPlusDialogActivity;
import com.websoftquality.agaphey.views.main.IgniterPlusSliderFragment;
import com.websoftquality.agaphey.views.main.LoginActivity;
import com.websoftquality.agaphey.views.main.LoginEmailActivity;
import com.websoftquality.agaphey.views.main.PaymentCreditCard;
import com.websoftquality.agaphey.views.main.PaymentPlans;
import com.websoftquality.agaphey.views.main.ProfileActivity;
import com.websoftquality.agaphey.views.main.SignupEmailActivity;
import com.websoftquality.agaphey.views.main.SignupQuestions;
import com.websoftquality.agaphey.views.main.SplashActivity;
import com.websoftquality.agaphey.views.main.TutorialFragment;
import com.websoftquality.agaphey.views.main.UserNameActivity;
import com.websoftquality.agaphey.views.main.VerificationActivity;
import com.websoftquality.agaphey.views.main.AccountKit.FacebookAccountKitActivity;
import com.websoftquality.agaphey.views.main.AccountKit.TwilioAccountKitActivity;
import com.websoftquality.agaphey.views.profile.AddLocationActivity;
import com.websoftquality.agaphey.views.profile.EditProfileActivity;
import com.websoftquality.agaphey.views.profile.EnlargeProfileActivity;
import com.websoftquality.agaphey.views.profile.GetIgniterPlusActivity;
import com.websoftquality.agaphey.views.profile.ProfileFragment;
import com.websoftquality.agaphey.views.profile.SettingsActivity;
import com.websoftquality.agaphey.views.signup.BirthdayFragment;
import com.websoftquality.agaphey.views.signup.EmailFragment;
import com.websoftquality.agaphey.views.signup.GenderFragment;
import com.websoftquality.agaphey.views.signup.OneTimePwdFragment;
import com.websoftquality.agaphey.views.signup.PasswordFragment;
import com.websoftquality.agaphey.views.signup.PhoneNumberFragment;
import com.websoftquality.agaphey.views.signup.ProfilePickFragment;
import com.websoftquality.agaphey.views.signup.SignUpActivity;

/*****************************************************************
 App Component
 ****************************************************************/
@Singleton
@Component(modules = {NetworkModule.class, ApplicationModule.class, AppContainerModule.class})
public interface AppComponent {
    // ACTIVITY

    void inject(SplashActivity splashActivity);

    void inject(HomeActivity homeActivity);

    void inject(SettingsActivity settingsActivity);

    void inject(EditProfileActivity editProfileActivity);

    void inject(GetIgniterPlusActivity getIgniterPlusActivity);

    void inject(SignUpActivity signUpActivity);

    void inject(EnlargeProfileActivity enlargeProfileActivity);

    void inject(MatchUsersActivity matchUsersActivity);

    void inject(ChatConversationActivity chatConversationActivity);

    void inject(CreateGroupActivity createGroupActivity);

    void inject(LoginActivity loginActivity);

    void inject(VerificationActivity verificationActivity);

    void inject(UserNameActivity userNameActivity);

    void inject(AddLocationActivity addLocationActivity);

    void inject(FacebookAccountKitActivity facebookAccountKitActivity);

    void inject(TwilioAccountKitActivity facebookAccountKitActivity1);

    void inject(IgniterGoldActivity igniterGoldActivity);

    void inject(LikedUsersActivity likedUsersActivity);


    // Fragments
    void inject(ProfileFragment profileFragment);

    void inject(IgniterPageFragment igniterPageFragment);

    void inject(ChatFragment chatFragment);

    void inject(ProfilePickFragment profilePickFragment);

    void inject(EmailFragment emailFragment);

    void inject(PasswordFragment passwordFragment);

    void inject(BirthdayFragment birthdayFragment);

    void inject(TutorialFragment tutorialFragment);

    void inject(PhoneNumberFragment phoneNumberFragment);

    void inject(OneTimePwdFragment oneTimePwdFragment);

    void inject(GenderFragment genderFragment);

    void inject(IgniterPlusDialogActivity igniterPlusDialogActivity);

    void inject(BoostDialogActivity boostDialogActivity);

    void inject(IgniterPlusSliderFragment igniterPlusSliderFragment);

    // Utilities
    void inject(RunTimePermission runTimePermission);

    void inject(SessionManager sessionManager);

    void inject(ImageUtils imageUtils);

    void inject(CommonMethods commonMethods);

    void inject(ProfileSliderAdapter profileSliderAdapter);

    void inject(RequestCallback requestCallback);

    void inject(DateTimeUtility dateTimeUtility);

    void inject(WebServiceUtils webServiceUtils);

    // Adapters
    void inject(IgniterSliderAdapter igniterSliderAdapter);

    void inject(NewMatchesListAdapter newMatchesListAdapter);

    void inject(MessageUserListAdapter messageUserListAdapter);

    void inject(EnlargeSliderAdapter enlargeSliderAdapter);

    void inject(EditProfileImageListAdapter editProfileImageListAdapter);

    void inject(ChatConversationListAdapter chatConversationListAdapter);

    void inject(UnmatchReasonListAdapter unmatchReasonListAdapter);

    void inject(UserListAdapter chatUserListAdapter);

    void inject(MatchesSwipeAdapter matchesSwipeAdapter);

    void inject(SwipeListener swipeListener);

    void inject(LocationListAdapter locationListAdapter);

    void inject(LikedUserAdapter likedUserAdapter);

    void inject(MyFirebaseMessagingService myFirebaseMessagingService);

    void inject(MyFirebaseInstanceIDService myFirebaseInstanceIDService);


    // AsyncTask
    void inject(ImageCompressAsyncTask imageCompressAsyncTask);

    void inject(NotificationUtils notificationUtils);

    void inject(SwipeableTouchHelperCallback swipeableTouchHelperCallback);


    void inject(SignupQuestions signupQuestions);

    void inject(SignupEmailActivity signupEmailActivity);

    void inject(PaymentPlans paymentPlans);

    void inject(PaymentCreditCard paymentCreditCard);

    void inject(ProfileActivity profileActivity);

    void inject(LoginEmailActivity loginEmailActivity);

    void inject(DashboardFragment dashboardFragment);
}
