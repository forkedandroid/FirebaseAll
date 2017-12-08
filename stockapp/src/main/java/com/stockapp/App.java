package com.stockapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.gson.Gson;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.stockapp.model.StocklistItemListResponse;
import com.stockapp.service.ApiService;
import com.utils.ConnectivityReceiverListener;
import com.utils.NetworkChangeReceiver;
import com.utils.SharePrefrences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class App extends Application {

    static String username = "test";
    static String password= "test";
    static String strApiUrl = "http://dev.markitondemand.com/MODApis/Api/";
    private static final String YOUTUBE_API_URL = "https://www.googleapis.com/youtube/v3/";
    //String strApiUrl = "http://dev.markitondemand.com/MODApis/Api/v2/Lookup/json?input=goog";


    private static App mInstance;
    private static final String TAG = App.class.getSimpleName();

    // fullscreen
    public static boolean blnFullscreenActvitity = false;

    public static String DB_NAME = "stockapp.db";
   /* public static String DB_PATH = "/data/data/" + "com.commonapp" + "/databases/";*/

    // app folder name
    public static String APP_FOLDERNAME = "stockapp";

    // share pref name
    public static String PREF_NAME = "stockapp_app";

    // class for the share pref keys and valyes get set
    public static SharePrefrences sharePrefrences;

    // for the app context
    static Context mContext;

    // for the set app fontface or type face
    static Typeface tf_Regular, tf_Bold;

    public static String OP_REGISTER = "Register";
    public static String adsAppBnrId = "ca-app-pub-4346653435295459/7368215831";

    //for the realm database encryption and decryption key
    public static String RealmEncryptionKey = "f263575e7b00a977a8e915feb9bfb2f992b2b8f22eaaaaaaa46523132131689465413132132165469487987987643545465464abbbbbccdddffff111222333";
    public static RealmConfiguration realmConfiguration;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    // application on create methode for the create and int base values
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;


        try {
            MultiDex.install(this);
            mContext = getApplicationContext();
            sharePrefrences = new SharePrefrences(App.this);

            Realm.init(this);
            realmConfiguration = getRealmConfiguration();

            createAppFolder();
            getFont_Regular();
            getFont_Bold();




        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static RealmConfiguration getRealmConfiguration()
    {
        if(realmConfiguration !=null)
        {
            return realmConfiguration;
        }
        else
        {
/*

            realmConfiguration = new RealmConfiguration.Builder()
                    .encryptionKey(App.getEncryptRawKey())
                    .build();
*/


            realmConfiguration = new RealmConfiguration.Builder()
                    .deleteRealmIfMigrationNeeded()
                    .encryptionKey(App.getEncryptRawKey())
                    .build();


            return realmConfiguration;
        }
    }

    // for the encrypt Encrypt
    public static byte[] getEncryptRawKey() {

        try {
            /*byte[] bytes64Key = App.RealmEncryptionKey.getBytes("UTF-8");
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed(bytes64Key);
            kgen.init(128, sr);
            SecretKey skey = kgen.generateKey();
            byte[] raw = skey.getEncoded();*/

            byte[] key = new BigInteger(App.RealmEncryptionKey,16).toByteArray();
            return key;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }



    public static void insertStockSymbol(Realm realm, StocklistItemListResponse gsonResponseWallpaperList) {
        try {
            App.showLog("========insertStockSymbol=====");


            realm.beginTransaction();
            StocklistItemListResponse realmDJsonDashboardModel = realm.copyToRealm(gsonResponseWallpaperList);
            realm.commitTransaction();

            getStockSymbol(realm);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                realm.commitTransaction();
            }
            catch (Exception e2)
            {e2.printStackTrace();}
        }

    }

    public static void getStockSymbol(Realm realm) {
        try {
            App.showLog("========getDataWallpaper=====");
            ArrayList<StocklistItemListResponse> arrListStocklistItemListResponse = new ArrayList<>();

            RealmResults<StocklistItemListResponse> arrDLocationModel = realm.where(StocklistItemListResponse.class).findAll();
            App.showLog("===arrDLocationModel==" + arrDLocationModel);
            List<StocklistItemListResponse> gsonResponseWallpaperList = arrDLocationModel;
            arrListStocklistItemListResponse = new ArrayList<StocklistItemListResponse>(gsonResponseWallpaperList);

            for (int k = 0; k < arrListStocklistItemListResponse.size(); k++) {
                App.showLog(k + "===arrListStocklistItemListResponse=Symbol=" + arrListStocklistItemListResponse.get(k).Symbol);
                App.showLog(k + "===arrListStocklistItemListResponse=Exchange=" + arrListStocklistItemListResponse.get(k).Exchange);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static List<StocklistItemListResponse> getCheckIsFavorite(Realm realm,String Symbol_Exchange)
    {
        try {
            RealmResults<StocklistItemListResponse> arrDLocationModel = realm.where(StocklistItemListResponse.class)
                    .beginGroup()
                    .equalTo("Symbol_Exchange", Symbol_Exchange)
                    /*.or()
                    .contains("name", "Jo")*/
                    .endGroup()

                    .findAll();

            App.showLog("===arrDLocationModel==" + arrDLocationModel);
            List<StocklistItemListResponse> gsonResponseStocklist = arrDLocationModel;
            App.showLog("=====gsonResponseWallpaperList==" + gsonResponseStocklist.size());

            return gsonResponseStocklist;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return  null;
        }
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


    private void createAppFolder() {
        try {
            String sdCardPath = Environment.getExternalStorageDirectory().toString();
            //File file2 = new File(sdCardPath + "/" + App.APP_FOLDERNAME + "");
            File file2 = new File(sdCardPath + "/" + App.APP_FOLDERNAME + "/AppLog2");
            if (!file2.exists()) {
                if (!file2.mkdirs()) {
                    System.out.println("==Create Directory " + App.APP_FOLDERNAME + "====");
                } else {
                    System.out.println("==No--1Create Directory " + App.APP_FOLDERNAME + "====");
                }
            } else {
                System.out.println("== already created---No--2Create Directory " + App.APP_FOLDERNAME + "====");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static Typeface getFont_Regular() {
        tf_Regular = Typeface.createFromAsset(mContext.getAssets(), "fonts/Montserrat-Regular.ttf");
        return tf_Regular;
    }

    public static Typeface getFont_Bold() {
        tf_Bold = Typeface.createFromAsset(mContext.getAssets(), "fonts/Montserrat-Bold.ttf");
        return tf_Bold;
    }


    public static String setLabelText(String newString, String defaultString) {
        if (newString != null) {
            return newString;
        } else {
            showLog("==setLabelText====LABEL===null===newString====set default text==");
            return defaultString;
        }
    }


    public static String setAlertText(String newString, String defaultString) {
        if (newString != null) {
            return newString;
        } else {
            showLog("==setAlertText===null===newString====set default text==");
            return defaultString;
        }
    }


    public static String getddMMMyy(String convert_date_string) {
        String final_date = "";
        String date1 = "";
        if (convert_date_string != null) {

            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM ''yy h:mm a");
                String inputDateStr = convert_date_string;
                Date date = null;
                date = inputFormat.parse(inputDateStr);
                //String outputDateStr = outputFormat.format(date);
                date1 = outputFormat.format(date);
                final_date = date1.toLowerCase();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return final_date;
    }

    public static String getHHmmddMMMyy(String convert_date_string) {
        String final_date = "";
        String date1 = "";
        if (convert_date_string != null) {

            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat outputFormat = new SimpleDateFormat("h:mm a dd-MM-yyyy");
                String inputDateStr = convert_date_string;
                Date date = null;
                date = inputFormat.parse(inputDateStr);
                //String outputDateStr = outputFormat.format(date);
                date1 = outputFormat.format(date);
                final_date = date1.toLowerCase();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return final_date;
    }

    public static String convertSecondToHHMMString(int secondtTime) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(tz);
        String time = df.format(new Date(secondtTime * 1000L));

        return time;
    }


    public static boolean isCheckReachLocation(int rangeMeter, double sDLat, double sDLon, double eDLat, double eDLon) {
        float distanceInMeters = 0;

        Location startLocation = new Location("Start");
        startLocation.setLatitude(sDLat);
        startLocation.setLongitude(sDLon);

        Location targetLocation = new Location("Ending");
        targetLocation.setLatitude(eDLat);
        targetLocation.setLongitude(eDLon);

        distanceInMeters = (targetLocation.distanceTo(startLocation));


        // distance = locationA.distanceTo(locationB);   // in meters
        //  distance = locationA.distanceTo(locationB)/1000;   // in km

        String strCalMeters = String.format("%.02f", distanceInMeters);

        App.showLog("===checkReachLocation====strCalMeters====meters====" + strCalMeters);

        if (distanceInMeters > rangeMeter) {
            return false;
        } else {
            App.showLog("====-----REACHED----=====checkReachLocation====strCalMeters====meters====" + strCalMeters);
            return true;
        }
    }


    static String strPrvCalKM = "";

    public static String getDistanceInKM(double sDLat, double sDLon, double eDLat, double eDLon) {
        float distanceInMeters = 0;

        Location startLocation = new Location("Start");
        startLocation.setLatitude(sDLat);
        startLocation.setLongitude(sDLon);

        Location targetLocation = new Location("Ending");
        targetLocation.setLatitude(eDLat);
        targetLocation.setLongitude(eDLon);

        distanceInMeters = (targetLocation.distanceTo(startLocation) / 1000);
        /*
            distanceInMeters = (targetLocation.distanceTo(startLocation));
            distanceInMeters = (distanceInMeters / 1000);
        */

        // distance = locationA.distanceTo(locationB);   // in meters
        //  distance = locationA.distanceTo(locationB)/1000;   // in km

        String strCalKM = String.format("%.02f", distanceInMeters);

        if (strPrvCalKM.equalsIgnoreCase(strCalKM)) {

        } else {
            App.showLog("======KM====" + strCalKM);
            strPrvCalKM = strCalKM;
            setJsonArrayRoute(10, eDLat, eDLon);
        }
        return strCalKM;
    }

    public static JSONArray jsonArrayRoute = new JSONArray();

    public static void setJsonArrayRoute(float meters, double eDLat, double eDLon) {
        try {
            //if (meters % 10 == 0) // 10 meter insert data
            {
                JSONObject jsonObjectRoute = new JSONObject();
                jsonObjectRoute.put("latlon", "" + eDLat + "," + eDLon);
                jsonArrayRoute.put(jsonObjectRoute);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getDistanceInKMtoMeter(float KMS) {
        float distanceInMeters = KMS;


        distanceInMeters = ((distanceInMeters) * 1000);


        // distance = locationA.distanceTo(locationB);   // in meters
        //  distance = locationA.distanceTo(locationB)/1000;   // in km

        String strCalKMtoMETER = String.format("%.02f", distanceInMeters);

        App.showLog("======METER====" + strCalKMtoMETER);

        return strCalKMtoMETER;
    }


    public static String convertMeterToKMString(float meters) {
        float distanceInMeters = 0;

        distanceInMeters = meters / 1000;
        String strCalKM = "0";
        strCalKM = String.format("%.02f", distanceInMeters);

        App.showLog("======KM====" + strCalKM);

        return strCalKM;
    }


    public static String getDistanceInMeter(double sDLat, double sDLon, double eDLat, double eDLon) {
        float distanceInMeters = 0;

        Location startLocation = new Location("Start");
        startLocation.setLatitude(sDLat);
        startLocation.setLongitude(sDLon);

        Location targetLocation = new Location("Ending");
        targetLocation.setLatitude(eDLat);
        targetLocation.setLongitude(eDLon);

        distanceInMeters = (targetLocation.distanceTo(startLocation));


        // distance = locationA.distanceTo(locationB);   // in meters
        //  distance = locationA.distanceTo(locationB)/1000;   // in km

        String strCalMeter = String.format("%.02f", distanceInMeters);

        App.showLog("======METER====" + strCalMeter);

        return strCalMeter;
    }


    public static String getCurrentDateTime() {
        String current_date = "";
        Calendar c = Calendar.getInstance();
        //System.out.println("Current time => " + c.getTime());

        SimpleDateFormat postFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        current_date = postFormater.format(c.getTime());

        return current_date;
    }


    public static void showToastLong(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }


    public static void showToastShort(Context context, String strMessage) {
        Toast.makeText(context, strMessage, Toast.LENGTH_SHORT).show();
    }


    public static void showSnackBar(View view, String strMessage) {
        Snackbar snackbar = Snackbar.make(view, strMessage, Snackbar.LENGTH_SHORT);
       /* View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.BLACK);
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);*/
        snackbar.show();
    }


    public static void showSnackBarLong(View view, String strMessage) {
        Snackbar.make(view, strMessage, Snackbar.LENGTH_LONG).show();
    }


    public static void showLog(String strMessage) {
        Log.v("==App==", "--strMessage--" + strMessage);
    }


    public static void showLogApi(String strMessage) {
        //Log.v("==App==", "--strMessage--" + strMessage);
        System.out.println("--API-MESSAGE--" + strMessage);

        //  appendLogApi("c_api", strMessage);
    }

    public static void showLogApi(String strOP, String strMessage) {
        //Log.v("==App==", "--strMessage--" + strMessage);
        System.out.println("--API-strOP--" + strOP);
        System.out.println("--API-MESSAGE--" + strMessage);

        // appendLogApi(strOP + "_c_api", strMessage);
    }

    public static void showLogApiRespose(String op, Response response) {
        //Log.w("=op==>" + op, "response==>");
        String strResponse = new Gson().toJson(response.body());
        Log.i("=op==>" + op, "response==>" + strResponse);
        // appendLogApi(op + "_r_api", strResponse);
    }


    public static void showLogResponce(String strTag, String strResponse) {
        Log.i("==App==strTag==" + strTag, "--strResponse--" + strResponse);
        //appendLogApi(strTag + "_r_api", strResponse);
    }

    public static void appendLogApi(String fileName, String text) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyMM_dd_HHmmss");
            String currentDateandTime = sdf.format(new Date());

            String sdCardPath = sdCardPath = Environment.getExternalStorageDirectory().toString();

            File logFile = new File(sdCardPath, "/" + App.APP_FOLDERNAME + "/AppLog2/" + fileName + "_" + currentDateandTime + "_lg.txt");
            if (!logFile.exists()) {
                try {
                    logFile.createNewFile();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (Exception e) {

            Log.e(TAG, e.getMessage(), e);
        }
    }


    public static void showLog(String strTag, String strMessage) {
        Log.v("==App==strTag==" + strTag, "--strMessage--" + strMessage);
    }


    public static void setTaskBarColored(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        }
    }


    public static boolean isInternetAvail(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }


    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    public static String getOnlyDigits(String s) {
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(s);
        String number = matcher.replaceAll("");
        return number;
    }


    public static String getOnlyStrings(String s) {
        Pattern pattern = Pattern.compile("[^a-z A-Z]");
        Matcher matcher = pattern.matcher(s);
        String number = matcher.replaceAll("");
        return number;
    }


    public static String getOnlyAlfaNumeric(String s) {
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher matcher = pattern.matcher(s);
        String number = matcher.replaceAll(" ");
        return number;
    }


    public void hideKeyBoard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public static void hideSoftKeyboardMy(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }



    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }


    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public static String convertTo24Hour(String Time) {
        DateFormat f1 = new SimpleDateFormat("hh:mm a"); //11:00 pm
        Date d = null;
        try {
            d = f1.parse(Time);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        DateFormat f2 = new SimpleDateFormat("HH:mm:ss");
        String x = f2.format(d); // "23:00"

        return x;
    }


    public static String convertTo12Hour(String Time) {
        DateFormat f1 = new SimpleDateFormat("HH:mm:ss"); // "23:00:00"
        Date d = null;
        try {
            d = f1.parse(Time);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        DateFormat f2 = new SimpleDateFormat("hh:mm a");
        String x = f2.format(d); //11:00 pm

        return x;
    }


    public static void expand(final View v) {
        //v.measure(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT); //WRAP_CONTENT
        v.measure(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        //? WindowManager.LayoutParams.WRAP_CONTENT //WRAP_CONTENT
                        ? WindowManager.LayoutParams.MATCH_PARENT //WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }




    public static void expandWRAP_CONTENT(final View v) {
        //v.measure(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT); //WRAP_CONTENT
        v.measure(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        //? WindowManager.LayoutParams.WRAP_CONTENT //WRAP_CONTENT
                        ? WindowManager.LayoutParams.MATCH_PARENT //WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }


    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void viewGone(final View v) {
        try{
            v.setVisibility(View.GONE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void viewShow(final View v) {
        try{
            v.setVisibility(View.VISIBLE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static RequestBody createPartFromString(String value) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), value);
    }

    private static RequestBody createPartFromFile(File file) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), file);
    }


    public static void saveBitmapSdcard(String filename, Bitmap bitmap) {
        FileOutputStream out = null;
        try {

            String directoryPath = Environment.getExternalStorageDirectory() + File.separator + App.APP_FOLDERNAME;

            File appDir = new File(directoryPath);

            if (!appDir.exists() && !appDir.isDirectory()) {
                // create empty directory
                if (appDir.mkdirs()) {
                    Log.d("===CreateDir===", "App dir created");
                } else {
                    Log.d("===CreateDir===", "Unable to create app dir!");
                }
            } else {
                //Log.d("===CreateDir===","App dir already exists");
            }


            out = new FileOutputStream(directoryPath + File.separator + filename);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // for the internet connection method
    public static void setConnectivityListener(ConnectivityReceiverListener connectivityListener2)
    {
        NetworkChangeReceiver.connectivityReceiverListener =  connectivityListener2;
    }

    // This snippet hides the system bars.
    public static void hideSystemUI(View mDecorView) {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    // This snippet shows the system bars. It does this by removing all the flags
// except for the ones that make the content appear under the system bars.
    public static void showSystemUI(View mDecorView) {
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }


    public static Context getAppContext() {
        return mContext;
    }




    //for the retrofit


//-----------------start----------------------------
    public static OkHttpClient getOkHttpClient()
    {
        return  new OkHttpClient.Builder()
                //  .addInterceptor(new BasicAuthInterceptor(username, password))
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

    }

    public static Retrofit getRetrofit()
    {
        //return  new Retrofit.Builder().baseUrl(YOUTUBE_API_URL)
        return  new Retrofit.Builder()
                .baseUrl(strApiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                // for retrofit call .client(getOkHttpClient())
                .client(createOkHttpClient()) // for java rx
                .build();
    }




    public static ApiService getApiService()
    {
        // ApiService apiService = getRetrofit().create(ApiService.class);
        return getRetrofit().create(ApiService.class);
    }



    /**
     * java rx This custom client will append the “username=demo” query after     every request.
     */
    public static OkHttpClient createOkHttpClient() {
        final OkHttpClient.Builder httpClient =
                new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {

            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                final Request original = chain.request();
                final HttpUrl originalHttpUrl = original.url();
                final HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("username", username)
                        .addQueryParameter("password", password)

                        .build();
                // Request customization: add request headers
                final Request.Builder requestBuilder = original.newBuilder()

                        .url(url);
                final Request request = requestBuilder

                        .build();
                return chain.proceed(request);
            }
        });
        return httpClient
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
    }
//----------------end-----------------------------

    public static String tagStocklistItemListResponse = "StocklistItemListResponse";
}
