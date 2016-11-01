package weibo.wangtao.weibo.Activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.User;

import java.util.ArrayList;
import java.util.HashMap;

import weibo.wangtao.weibo.Bean.AccessTokenKeeper;
import weibo.wangtao.weibo.R;
import weibo.wangtao.weibo.Tools.Constants;


public class MainActivity extends SlidingFragmentActivity {

    private Oauth2AccessToken mAccessToken;
    private SlidingMenu sm;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSlidingMenu(savedInstanceState);


    }






    private void initSlidingMenu(Bundle savedInstanceState)//侧滑菜单初始化
    {
        sm = getSlidingMenu();
        setBehindContentView(getLeftMenu());

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.left_sliding, mLeftMenu, "Left").commit();
//        }

        sm.setShadowDrawable(R.drawable.gray2); // 设置阴影图片
        sm.setShadowWidthRes(R.dimen.slidingmenu_offset2); // 设置阴影图片的宽度
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset); // 显示主界面的宽度
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN); // 设置滑动的屏幕范围，该设置为全屏区域都可以滑动
        sm.setMode(SlidingMenu.LEFT); // 设置菜单只有左菜单


    }

    private View getLeftMenu() //取得动态布局
    {
        //从主布局文件绑定的Activity调用另一个布局文件必须调用LayoutInflater
        LayoutInflater inflater = getLayoutInflater();
        //得到menu的View
        View v = inflater.inflate(R.layout.main_slidingmenu_left, null);
        //equip_list = (ListView)v.findViewById(R.id.equi_list);




        return v;
    }




    private void initUser()//加载用户信息
    {
        mAccessToken = AccessTokenKeeper.readAccessToken(this);// 从 SharedPreferences 中读取上次已保存好 AccessToken 等信息
        UsersAPI usersAPI = new UsersAPI(this, Constants.APP_KEY, mAccessToken);
        long uid = Long.parseLong(mAccessToken.getUid());
        usersAPI.show(uid, mListener);
    }

    private RequestListener mListener = new RequestListener() //回调监听
    {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                Log.i("用户资料", response);
                // 调用 User#parse 将JSON串解析成User对象
                User user = User.parse(response);
                if (user != null) {
                    Toast.makeText(MainActivity.this,
                            "获取User信息成功，用户昵称：" + user.screen_name,
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Log.e("用户资料", e.getMessage());
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            Toast.makeText(MainActivity.this, info.toString(), Toast.LENGTH_LONG).show();
        }
    };


}
