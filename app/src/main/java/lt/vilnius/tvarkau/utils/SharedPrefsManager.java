package lt.vilnius.tvarkau.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import lt.vilnius.tvarkau.LogApp;
import lt.vilnius.tvarkau.entity.Profile;

/**
 * Managing user's shared Preferences.
 * At the time it is only used to track if user has opted for anonymous login
 * but later on it also will help us to store actual user information.
 */
public class SharedPrefsManager {
    private static final String PREFS_NAME = "TVARKAU-VILNIU_PREFS";

    private static final String PREF_USER_PROFILE = "UserProfile";
    private static final String PREF_USER_ANONYMOUS = "UserAnonymous";

    private static SharedPrefsManager singleton;
    private static SharedPreferences sharedPreferences;


    private SharedPrefsManager(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    private static void initializeInstance(Context context) {
        if (singleton == null) {
            singleton = new SharedPrefsManager(context);
        }
    }

    public static SharedPrefsManager getInstance(Context context) {
        initializeInstance(context.getApplicationContext());
        return singleton;
    }

    public boolean isUserAnonymous() {
        return sharedPreferences.getBoolean(PREF_USER_ANONYMOUS, true);
    }

    public void changeUserAnonymityStatus(boolean status) {
        sharedPreferences.edit()
            .putBoolean(PREF_USER_ANONYMOUS, status)
            .apply();
    }

    public void saveUserDetails(Profile profile) {
        sharedPreferences.edit()
            .putString(PREF_USER_PROFILE, profile.createJsonData())
            .apply();
    }

    public boolean isUserDetailsSaved(){
        return sharedPreferences.getString(PREF_USER_PROFILE, null) != null;
    }

    @Nullable
    public Profile getUserProfile() {
        try {
            Gson gson = new Gson();
            String json = sharedPreferences.getString(PREF_USER_PROFILE, null);
            if (json != null) {
                return gson.fromJson(json, Profile.class);
            } else {
                return new Profile();
            }
        } catch (JsonSyntaxException e) {
            LogApp.logCrash(e);
            return null;
        }
    }
}
