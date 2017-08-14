package md.mazharul.islam.jihan.mobilerapidprpromotion.ServerInfo;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Arifur on 7/5/2016.
 */
public class MySharePrefference {
    public static final String TOKEN = "authorization";
    public static final String USER_ID = "username";
    public static final String PASSWORD = "password";
    public static final String GRAND_TYPE = "grant_type=password";

    Context context;
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    public MySharePrefference(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("MAIN_PREFF", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static void saveData(String key, String data) {
        editor.putString(key, data);
        editor.commit();
    }

    public static String getData(String key) {
        String data = sharedPreferences.getString(key, "Android");
        return data;
    }
}
