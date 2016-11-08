package weibo.wangtao.weibo.Activity;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.media.Image;
import android.os.Bundle;


import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Privacy;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.sina.weibo.sdk.openapi.models.User;
import com.squareup.picasso.Picasso;

import weibo.wangtao.weibo.Bean.AccessTokenKeeper;
import weibo.wangtao.weibo.Fragment.FriendTimelineFragment;
import weibo.wangtao.weibo.Fragment.LeftMenuFragment;
import weibo.wangtao.weibo.Fragment.PublicTimelineFragment;
import weibo.wangtao.weibo.Loader.UserInfoLoader;
import weibo.wangtao.weibo.R;
import weibo.wangtao.weibo.Tools.CircleTransform;
import weibo.wangtao.weibo.Tools.Constants;





public class MainActivity extends SlidingFragmentActivity implements LeftMenuFragment.fragment_ChangeClickListener {


    private SlidingMenu sm;

    private FragmentManager fm;

    private PublicTimelineFragment publicTimelineFragment=new PublicTimelineFragment();
    private FriendTimelineFragment friendTimelineFragment=new FriendTimelineFragment();
    private LeftMenuFragment leftMenuFragment;

    private LeftMenuFragment mLeftMenu = new LeftMenuFragment();/** 左边菜单 */

   // private PublicTimelineFragment publicTimelineFragment;
    private TextView fragment_Title;
    private ImageView goto_Left;


    @Override
    public void fragment_Change(String frgmnt)
    {
        if(publicTimelineFragment==null)
        {
            publicTimelineFragment=new PublicTimelineFragment();
        }
        if(friendTimelineFragment==null)
        {
            friendTimelineFragment=new FriendTimelineFragment();
        }
        FragmentTransaction transaction;
        transaction=fm.beginTransaction();
        if(frgmnt=="pubilc")
        {

            fragment_Title.setText("公共微博");


            transaction.hide(friendTimelineFragment);
            transaction.show(publicTimelineFragment);
            transaction.commit();
        }
        if(frgmnt=="friend")
        {
            fragment_Title.setText("朋友微博");
            transaction.hide(publicTimelineFragment);
            transaction.show(friendTimelineFragment);
            transaction.commit();
        }
        Log.i("fragment_Change",frgmnt);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init_View();

        initSlidingMenu(savedInstanceState);

        setDefaultFragment();

    }

    private void init_View()
    {
        fragment_Title=(TextView)findViewById(R.id.fragment_title) ;
        goto_Left=(ImageView)findViewById(R.id.goto_left);
        goto_Left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sm.showMenu();
            }
        });
    }
    private void setDefaultFragment()
    {
        fm = getFragmentManager();
        FragmentTransaction transaction;
        transaction=fm.beginTransaction();
        transaction.add(R.id.main_content,publicTimelineFragment);
        transaction.add(R.id.main_content,friendTimelineFragment);
        transaction.hide(friendTimelineFragment);
        fragment_Title.setText("公共微博");
        transaction.show(publicTimelineFragment).commit();
    }

    private void initSlidingMenu(Bundle savedInstanceState)//侧滑菜单初始化
    {
        sm = getSlidingMenu();
        setBehindContentView(R.layout.layout_leftmenu_content);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.left_sliding, mLeftMenu, "Left").commit();
        }

        sm.setShadowDrawable(R.drawable.gray2); // 设置阴影图片
        sm.setShadowWidthRes(R.dimen.slidingmenu_offset2); // 设置阴影图片的宽度
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset); // 显示主界面的宽度
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN); // 设置滑动的屏幕范围，该设置为全屏区域都可以滑动
        sm.setMode(SlidingMenu.LEFT); // 设置菜单只有左菜单

    }



    @Override
    protected void onSaveInstanceState(Bundle outState)//fragment随MyActivity一起销毁,incase of null
    {

    }
}
