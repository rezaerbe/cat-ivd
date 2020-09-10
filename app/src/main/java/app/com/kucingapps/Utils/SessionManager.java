package app.com.kucingapps.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import app.com.kucingapps.DashboardActivity;
import app.com.kucingapps.FrontActivity;
import app.com.kucingapps.HomeActivity;
import app.com.kucingapps.LoginActivity;
import app.com.kucingapps.MainActivity;

public class SessionManager {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String NAME = "NAME";
    public static final String EMAIL = "EMAIL";
    public static final String ID = "ID";
    public static final String PICTURE = "PICTURE";

    public SessionManager(Context context)
    {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String name, String email, String id, String picture)
    {
        editor.putBoolean(LOGIN, true);
        editor.putString(NAME, name);
        editor.putString(EMAIL, email);
        editor.putString(ID, id);
        editor.putString(PICTURE, picture);
        editor.apply();
    }

    public boolean isLoggin()
    {
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin()
    {
        if (!this.isLoggin())
        {
            // Todo : Create Login Activity
            Intent i = new Intent(context, HomeActivity.class);
            context.startActivity(i);
            ((FrontActivity) context).finish();
        }
    }

    public HashMap<String, String> getUserDetail()
    {
        HashMap<String, String> user = new HashMap<>();
        user.put(NAME, sharedPreferences.getString(NAME, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        user.put(ID, sharedPreferences.getString(ID, null));
        user.put(PICTURE, sharedPreferences.getString(PICTURE, null));

        return user;
    }

    public void logout()
    {
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, HomeActivity.class);
        context.startActivity(i);
        ((FrontActivity) context).finish();
    }

}
