package anindya.fb;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import anindya.fb.datamodel.Datum;

/**
 * Created by anind on 4/13/2017.
 */

public class FavoritesList {

    @SerializedName("favoriteList")
    @Expose
    static HashMap<String, Favorite> favoriteList = new HashMap<>();

    static SharedPreferences mPref;
    static Gson gson;
    static SharedPreferences.Editor mEditor;
    static int done = 0;

    public static final String PREF = "naya_preferences";
    public static final String PREF_FAVORITE = "favorites_data";

    public static void initList(Context context) {
        if(done == 0) {
            mPref = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
            mEditor = mPref.edit();
            gson = new Gson();
            String json = mPref.getString(PREF_FAVORITE, "");
            if (json.equals(""))
                favoriteList = new HashMap<>();
            else
                favoriteList = gson.fromJson(json, new TypeToken<HashMap<String,Favorite>>(){}.getType());
            done = 1;
        }
    }

    public static void add(Datum data, String type) {
        favoriteList.put(data.getId(), new Favorite(data, type));
        commit();
    }

    public static void remove(Datum data) {
        favoriteList.remove(data.getId());
        commit();
    }

    public static void commit() {
        String json = gson.toJson(favoriteList);
        mEditor.putString(PREF_FAVORITE, json);
        mEditor.commit();
    }

    public static boolean contains(Datum data) {
        return favoriteList.containsKey(data.getId());
    }

    public static boolean contains(String id) {
        return favoriteList.containsKey(id);
    }

    public static List<Datum> getFavorites(String type, Context context) {
        initList(context);
        ArrayList<Datum> data = new ArrayList<>();
        for(String key: favoriteList.keySet()) {
            if(favoriteList.get(key).type.equals(type))
                data.add(favoriteList.get(key).data);
        }
        return data;
    }
}

class Favorite {
    Datum data;
    String type;

    Favorite(Datum data, String type) {
        this.data = data;
        this.type = type;
    }
}