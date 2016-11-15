package weibo.wangtao.weibo.Activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.User;
import com.squareup.picasso.Picasso;

import weibo.wangtao.weibo.Bean.AccessTokenKeeper;
import weibo.wangtao.weibo.R;
import weibo.wangtao.weibo.Tools.CircleTransform;

import static weibo.wangtao.weibo.Tools.Constants.APP_KEY;
import android.util.Log;
public class UserIndex extends AppCompatActivity {
    private User user;
    private Oauth2AccessToken mAccessToken;
    private ImageView user_index_cover,user_index_userphoto;
    private TextView user_index_toolbar_text,user_index_description,user_index_screen_name,user_index_city,user_index_fans,user_index_sex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_index);
        init_View();
        Intent intent = getIntent();
        if(intent.getStringExtra("id")!=null)
        init_User(intent.getStringExtra("id"));
    }
    private void init_View()
    {
        user_index_toolbar_text=(TextView)findViewById(R.id.user_index_toolbar_text);
        user_index_cover=(ImageView)findViewById(R.id.user_index_cover);
        user_index_userphoto=(ImageView)findViewById(R.id.user_index_userphoto);
        user_index_description=(TextView)findViewById(R.id.user_index_description);
        user_index_screen_name=(TextView)findViewById(R.id.user_index_screen_name);
        user_index_city=(TextView)findViewById(R.id.user_index_city);
        user_index_fans=(TextView)findViewById(R.id.user_index_fans);
        user_index_sex=(TextView)findViewById(R.id.user_index_sex);
    }
    private void init_User(String id) {
        mAccessToken = AccessTokenKeeper.readAccessToken(this);// 从 SharedPreferences 中读取上次已保存好 AccessToken 等信息
        UsersAPI usersAPI = new UsersAPI(this, APP_KEY, mAccessToken);
        Log.e("UsersAPI>>>",id);
        usersAPI.show(Long.valueOf(id), user_show_Listener);
    }

    private RequestListener user_show_Listener = new RequestListener() //回调监听
    {
        @Override
        public void onComplete(String response) {

            if (!TextUtils.isEmpty(response))
            {
                user=new User();
                user= User.parse(response);
                user_index_toolbar_text.setText(user.screen_name);
                user_index_description.setText(user.description);
                user_index_screen_name.setText(user.screen_name);

                user_index_city.setText(user.location);


                user_index_fans.setText(String.valueOf(user.followers_count));

                switch (user.gender)
                {
                    case "f":
                        user_index_sex.setText("女");
                        break;
                    case "m":
                        user_index_sex.setText("男");
                        break;
                    default:
                        user_index_sex.setText("未知");
                        break;
                }


                Picasso
                        .with(UserIndex.this)
                        .load(user.avatar_large)
                        .transform(new CircleTransform(UserIndex.this))
                        .fit()
                        .centerCrop()
                        .config(Bitmap.Config.ALPHA_8)
                        .skipMemoryCache()
                        .into(user_index_userphoto);
                if(user.cover_image_phone!="")
                {
                    Picasso
                            .with(UserIndex.this)
                            .load(user.cover_image_phone)
                            .fit()
                            .centerCrop()
                            .config(Bitmap.Config.RGB_565)
                            .skipMemoryCache()
                            .into(user_index_cover);
                }
                user_index_userphoto.bringToFront();
            }
        }
        @Override
        public void onWeiboException(WeiboException e) {
            Log.e("用户资料", e.getMessage());
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            Toast.makeText(UserIndex.this, info.toString(), Toast.LENGTH_LONG).show();
        }
    };
}
