package app.com.kucingapps.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.List;

import app.com.kucingapps.Model.Article;
import app.com.kucingapps.Model.Category;
import app.com.kucingapps.Model.User;
import app.com.kucingapps.Retrofit.ApiClient;
import app.com.kucingapps.Retrofit.ApiInterface;

import static android.content.Context.MODE_PRIVATE;

public class Common {
//    private static final String BASE_URL = "http://femaleinaction.com/cat/";
    private static final String BASE_URL = "http://192.168.43.249/cat/";

    public static User currentUser = null;

    public static ApiInterface getAPI()
    {
        return ApiClient.getApiClient(BASE_URL).create(ApiInterface.class);
    }

}
