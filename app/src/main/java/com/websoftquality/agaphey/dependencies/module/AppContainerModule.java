package com.websoftquality.agaphey.dependencies.module;
/**
 * @package com.trioangle.igniter
 * @subpackage dependencies.module
 * @category AppContainerModule
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import com.websoftquality.agaphey.configs.Constants;
import com.websoftquality.agaphey.configs.RunTimePermission;
import com.websoftquality.agaphey.configs.SessionManager;
import com.websoftquality.agaphey.datamodels.main.JsonResponse;
import com.websoftquality.agaphey.utils.CalendarDatePickerDialog;
import com.websoftquality.agaphey.utils.CalendarTimePickerDialog;
import com.websoftquality.agaphey.utils.CommonMethods;
import com.websoftquality.agaphey.utils.DateTimeUtility;
import com.websoftquality.agaphey.utils.ImageUtils;
import com.websoftquality.agaphey.utils.Validator;
import com.websoftquality.agaphey.views.customize.CustomDialog;

/*****************************************************************
 App Container Module
 ****************************************************************/
@Module(includes = com.websoftquality.agaphey.dependencies.module.ApplicationModule.class)
public class AppContainerModule {
    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return application.getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    CommonMethods providesCommonMethods() {
        return new CommonMethods();
    }

    @Provides
    @Singleton
    SessionManager providesSessionManager() {
        return new SessionManager();
    }


    @Provides
    @Singleton
    Validator providesValidator() {
        return new Validator();
    }

    @Provides
    @Singleton
    RunTimePermission providesRunTimePermission() {
        return new RunTimePermission();
    }

    @Provides
    @Singleton
    Context providesContext(Application application) {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    ArrayList<String> providesStringArrayList() {
        return new ArrayList<>();
    }

    @Provides
    @Singleton
    JsonResponse providesJsonResponse() {
        return new JsonResponse();
    }

    @Provides
    @Singleton
    DateTimeUtility providesDateTimeUtility() {
        return new DateTimeUtility();
    }

    @Provides
    @Singleton
    ImageUtils providesImageUtils() {
        return new ImageUtils();
    }

    @Provides
    @Singleton
    CalendarDatePickerDialog providesCalendarDatePickerDialog() {
        return new CalendarDatePickerDialog();
    }

    @Provides
    @Singleton
    CalendarTimePickerDialog providesCalendarTimePickerDialog() {
        return new CalendarTimePickerDialog();
    }

    @Provides
    @Singleton
    CustomDialog providesCustomDialog() {
        return new CustomDialog();
    }
}
