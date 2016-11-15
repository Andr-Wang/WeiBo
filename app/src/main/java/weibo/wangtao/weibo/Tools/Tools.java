package weibo.wangtao.weibo.Tools;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.DialogPreference;
import android.provider.Settings;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.util.Log;
/**
 * Created by wangtao on 2016/10/27.
 */

public class Tools {

    public static void checkNetwork(final Context context)
    {
        if(!isNetworkAvaliable(context))
        {
            TextView msg=new TextView(context);
            msg.setText("There is not avaliable network now");
            new AlertDialog.Builder(context)
                    .setTitle("network state info").setView(msg).setPositiveButton("yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                }
            }).create().show();
        }
    }
    public static boolean isNetworkAvaliable(Context context)
    {
        ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager==null)
        {
            return false;
        }
        else
        {
            NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
            if(networkInfo==null) return false;
            if(networkInfo.isConnected())
            {
                return true;
            }
            else
            {
                return false;
            }
        }

    }
    public static String time_Change(String time)//Sun Nov 13 14:56:38 +0800 2016
    {
        String[] str = "Sun Nov 13 14:56:38 +0800 2016".split(" ");
       // String year=str[5];
        String month=month_Change(str[1]);
        String day=str[2];

        return day+"-"+month;
    }
    private static String month_Change(String month)
    {
        switch (month)
        {
            case "Jan":
                return "1";
            case "Feb":
                return "2";
            case "Apr":
                return "3";
            case "Mat":
                return "4";
            case "May":
                return "5";
            case "Jun":
                return "6";
            case "Jul":
                return "7";
            case "Agu":
                return "8";
            case "Sep":
                return "9";
            case "Nov":
                return "11";
            case "Dec":
                return "12";
            default:
                return null;
        }
    }

}
