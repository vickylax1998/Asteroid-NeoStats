package com.ranium.util;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ms03 on 18/9/16.
 */
public class AppController extends Application {
    static RequestQueue queue;
    static Context con;

    public static void initialize(Context context){
        if(con == null)
            con = context;

    }

    public static RequestQueue getInstance(){
        if(queue == null)
            queue = Volley.newRequestQueue(con);
        return queue;
    }

    public static boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager)con.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivity.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivity.getNetworkInfo(mNetwork);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }
        } else {
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null)
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }

            }
        }

        /*NetworkInfo[] info = cm.getAllNetworkInfo();
        if (info != null){
            for (int i = 0; i < info.length; i++){
                if (info[i].getState() == NetworkInfo.State.CONNECTED){
                    return true;
                }
            }
        }*/
        return false;
    }



    public static void showSnackBar(final Context context, View view, String message, int type){
        /*1-> Internet connection*/
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_INDEFINITE);
        if(type == 1){
//           // snackbar.setAction(context.getResources().getString(R.string.action_settings), new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent();
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.setAction(Settings.ACTION_SETTINGS);
//                    context.startActivity(intent);
//                }
//            });
        }
        snackbar.show();
    }

    public static String getDateCurrentTimeZone(long timestamp, int format) {
        DateFormat df = null;

        switch (format){
            case 1:
                df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                break;
            case 2:
                df = new SimpleDateFormat("dd-MM-yyyy");
                break;
            case 3:
                df = new SimpleDateFormat("EEE, dd MMM yyyy, hh:mm aaa");
                break;
            case 4:
                df = new SimpleDateFormat("EEE, dd MMM yyyy");
                break;
            case 5:
                df = new SimpleDateFormat("hh:mm aaa");
                break;
        }
        Date netDate = (new Date(timestamp*1000));
        return df.format(netDate);


       /* try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getTimeZone("Asia/Kolkata");
            calendar.setTimeInMillis(timestamp * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = null;
            switch (format){
                case 1:
                    sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    break;
                
                case 2:
                    sdf = new SimpleDateFormat("dd-MM-yyyy");
                    break;
            }
            
            Date currenTimeZone = (Date) calendar.getTime();
            return sdf.format(currenTimeZone);
        }catch (Exception e) {
        }*/
        /*DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date netDate = (new Date(timestamp*1000));
        return sdf.format(netDate);*/
        //return "";
    }



    public static boolean appInstalledOrNot(String uri) {
        PackageManager pm = con.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed ;
    }

    /**
     * Method to extract the user's age from the entered Date of Birth.
     *
     * @param year Integer The user's birth year.
     *
     * @return age Integer The user's age in years based on the supplied DoB.
     */
    public static int getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        //String ageS = ageInt.toString();

        return age;
    }
}
