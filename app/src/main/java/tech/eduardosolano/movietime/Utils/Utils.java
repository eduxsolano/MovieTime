package tech.eduardosolano.movietime.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tech.eduardosolano.movietime.Api.Response.Result;
import tech.eduardosolano.movietime.R;

import static tech.eduardosolano.movietime.Utils.Constants.FAVORITES;

public class Utils {

    private static Utils mUtils;
    public Retrofit mRetrofit;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ArrayList<Integer> favoritesArrayList = new ArrayList<Integer>();
    ArrayList<Result> favoriteItemsArrayList = new ArrayList<Result>();
    ArrayList<Result> itemsArrayList = new ArrayList<Result>();

    public static Utils getInstance() {
        if (mUtils == null) {
            mUtils = new Utils();
        }
        return mUtils;
    }

    public  Retrofit getRetrofit(FragmentActivity activity, View mView) {

        if (activity != null && !Utils.getInstance().isInternetAvailable(activity)) {
            if (mView != null)
                hideKeyBoard(mView.getWindowToken(), activity);
            Snackbar.make(mView, mView.getContext().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }

        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return mRetrofit;


    }

    public boolean isInternetAvailable(Context mContext) {
        if (mContext != null) {
            ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            return (cm != null) && (cm.getActiveNetworkInfo() != null);
        } else {
            return false;
        }
    }

    public void hideKeyBoard(IBinder iBinder, Context context) {
        InputMethodManager mHideKey = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mHideKey.hideSoftInputFromWindow(iBinder, 0);
    }

    public String getGenre(int id){
        String genre = "";
        switch (id) {
            case 28:
                genre="Action";
                break;
            case 12:
                genre ="Adventure";
                break;
            case 16:
                genre ="Animation";
                break;
            case 35:
                genre ="Comedy";
                break;
            case 80:
                genre ="Crime";
                break;
            case 99:
                genre ="Documentary";
                break;
            case 18:
                genre ="Drama";
                break;
            case 10751:
                genre ="Family";
                break;
            case 14:
                genre ="Fantasy";
                break;
            case 36:
                genre ="History";
                break;
            case 27:
                genre ="Horror";
                break;
            case 10402:
                genre ="Music";
                break;
            case 9648:
                genre ="Mystery";
                break;
            case 10749:
                genre ="Romance";
                break;
            case 878:
                genre ="Science Fiction";
                break;
            case 10770:
                genre ="TV Movie";
                break;
            case 53:
                genre ="Thriller";
                break;
            case 10752:
                genre ="War";
                break;
            case 37:
                genre ="Western";
                break;
            default:
                genre ="";
                break;
        }
        return genre;
    }
    public ArrayList<String> getGenreList(ArrayList<Integer> arrayList){
        ArrayList<String> genres= new ArrayList<String>();
        for (int i= 0; i<arrayList.size(); i++){
            genres.add(Utils.getInstance().getGenre(arrayList.get(i)));
        }
        return genres;
    }

    public String transformDate(String dateString){
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return new SimpleDateFormat("dd MMMM yyyy").format(calendar.getTime());
    }

    public void initSharedPrefs(Context mContext) {
        sharedPreferences = mContext.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setSharedPreferenceData(Context mContext, ArrayList<Result> object, int type, boolean isViewed){
        String json ="";
        sharedPreferences = mContext.getSharedPreferences(Constants.PREFS_DATA, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (object!= null) {
            Gson gson = new Gson();
            json = gson.toJson(object);
        }
        switch (type) {
            case 1:
                editor.putString(Constants.PREFS_MOVIE_OBJECT, json).commit();
                break;
            case 2:
                editor.putString(Constants.PREFS_FAV, json).commit();
                break;
            case 3:
                editor.putBoolean(Constants.IS_TUT_DONE,isViewed).commit();
                break;
        }
    }

    public ArrayList<Integer> getFavoriteList(){
        return  favoritesArrayList;
    }

    public ArrayList<Result> getFavoriteItemsArrayList(Context mContext) {
        ArrayList<Result> favoriteItemsArrayList = new ArrayList<Result>();
        Gson gson= new Gson();
        String jsonOutput = Utils.getInstance().getSharedPreference(mContext);
        Type listType = new TypeToken<ArrayList<Result>>(){}.getType();
        ArrayList<Result> items =   (ArrayList<Result>) gson.fromJson(jsonOutput,listType);
        ArrayList<Integer> ids= getFavoriteList();

        if (items == null)
            return new  ArrayList<Result>();

        for (int i = 0; i< items.size(); i++){
            for(int j = 0; j< ids.size();j++){
                if (Integer.valueOf(items.get(i).getId()).equals(ids.get(j)))
                    favoriteItemsArrayList.add(items.get(i));
            }
        }
        setSharedPreferenceData(mContext, favoriteItemsArrayList,2,false);
        return favoriteItemsArrayList;
    }

    public boolean isTutorialDone(Context mContext){
        sharedPreferences= mContext.getSharedPreferences(Constants.PREFS_DATA, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(Constants.IS_TUT_DONE,false);
    }

    public String getSharedPreference(Context mContext){
        sharedPreferences = mContext.getSharedPreferences(Constants.PREFS_DATA, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.PREFS_MOVIE_OBJECT,"");
    }
}

