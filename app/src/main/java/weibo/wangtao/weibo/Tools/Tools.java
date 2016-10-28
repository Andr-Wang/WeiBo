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

}
