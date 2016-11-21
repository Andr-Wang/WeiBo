package weibo.wangtao.weibo.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.User;
import com.squareup.picasso.Picasso;

import weibo.wangtao.weibo.Activity.MainActivity;
import weibo.wangtao.weibo.Activity.UserIndex;
import weibo.wangtao.weibo.Bean.AccessTokenKeeper;
import weibo.wangtao.weibo.R;
import weibo.wangtao.weibo.Tools.CircleTransform;
import weibo.wangtao.weibo.Widget.FilterImageView;

import static weibo.wangtao.weibo.Tools.Constants.APP_KEY;


/**
 * Created by wangtao on 2016/11/3.
 */

public class LeftMenuFragment extends Fragment {

    private View public_timeline,friend_timeline;
    private TextView screen_name, description;
    private ImageView userphoto, cover;
    private Oauth2AccessToken mAccessToken;
    private FragmentManager fm;

    public interface fragment_ChangeClickListener
    {
        void friend_Timeline();
        void public_Timeline();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initUser_Info()
    {
        mAccessToken = AccessTokenKeeper.readAccessToken(getActivity());
        UsersAPI usersAPI = new UsersAPI(getActivity(), APP_KEY, mAccessToken);
        long uid = Long.parseLong(mAccessToken.getUid());
        usersAPI.show(uid, user_listener);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View  view = inflater.inflate(R.layout.main_slidingmenu_left, null);
        screen_name = (TextView) view.findViewById(R.id.screen_name);
        description = (TextView) view.findViewById(R.id.description);
        userphoto = (ImageView) view.findViewById(R.id.userphoto);
        userphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(getActivity(), UserIndex.class);
                intent.putExtra("id",mAccessToken.getUid());
                startActivity(intent);
            }
        });
        cover = (ImageView) view.findViewById(R.id.cover);
        public_timeline=view.findViewById(R.id.public_timeline);
        friend_timeline=view.findViewById(R.id.friend_timeline);
        initUser_Info();
        initListener();
        return view;
    }



    private RequestListener user_listener = new RequestListener() //回调监听
    {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                Log.i("用户资料", response);
                User user = User.parse(response); // 调用 User#parse 将JSON串解析成User对象
                screen_name.setText(user.screen_name);
                description.setText(user.description);
                Picasso
                        .with(getActivity())
                        .load(user.avatar_large)
                        .transform(new CircleTransform(getActivity()))
                        .fit()
                        .centerCrop()
                        .config(Bitmap.Config.ALPHA_8)
                        .skipMemoryCache()
                        .into(userphoto);
                Picasso
                        .with(getActivity())
                        .load(user.cover_image_phone)
                        .fit()
                        .centerCrop()
                        .config(Bitmap.Config.RGB_565)
                        .skipMemoryCache()
                        .into(cover);
                userphoto.bringToFront();

            }
        }
        @Override
        public void onWeiboException(WeiboException e) {
            Log.e("用户资料", e.getMessage());
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            Toast.makeText(getActivity(), info.toString(), Toast.LENGTH_LONG).show();
        }

    };

    private void initListener()
    {

        public_timeline.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.sliding_left_public_timeline_pressed);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.sliding_left_public_timeline_normal);
                    if(getActivity() instanceof fragment_ChangeClickListener)
                    {
                        ((fragment_ChangeClickListener)getActivity()).public_Timeline();

                    }
                }
                return true;
            }
        });
        friend_timeline.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.sliding_left_public_timeline_pressed);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.sliding_left_public_timeline_normal);
                    if(getActivity() instanceof fragment_ChangeClickListener)
                    {
                        ((fragment_ChangeClickListener)getActivity()).friend_Timeline();
                    }
                }
                return true;
            }
        });
    }
}