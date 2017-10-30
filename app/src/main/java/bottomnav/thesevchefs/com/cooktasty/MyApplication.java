package bottomnav.thesevchefs.com.cooktasty;

import android.app.Application;

/**
 * Created by Allets on 20/10/2017.
 */

public class MyApplication extends Application {
    private static String authToken = null;
    private static String mEmail = null;

    public static String getAuthToken() {
        return authToken;
    }

    public static void setAuthToken(String input) {
        authToken = input;
    }

    public static String getEmail() {
        return mEmail;
    }

    public static void setEmail(String input) {
        String email = input.split("@")[0];
        mEmail = email;
    }

}
