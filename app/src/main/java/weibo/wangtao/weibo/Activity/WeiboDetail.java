package weibo.wangtao.weibo.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;


import weibo.wangtao.weibo.Bean.AccessTokenKeeper;
import weibo.wangtao.weibo.R;

import static weibo.wangtao.weibo.Tools.Constants.APP_KEY;

public class WeiboDetail extends AppCompatActivity {

    private Status status;
    private Oauth2AccessToken mAccessToken;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weibo_detail);
        Intent intent = getIntent();
        if(intent.getStringExtra("id")!=null)
        init_Weibo(intent.getStringExtra("id"));
        Log.i("WeiboDetail------",intent.getStringExtra("id"));
    }

    private void init_Weibo(String id) {
        mAccessToken = AccessTokenKeeper.readAccessToken(this);// 从 SharedPreferences 中读取上次已保存好 AccessToken 等信息
        StatusesAPI statusesAPI = new StatusesAPI(this, APP_KEY, mAccessToken);
        String d="4041022613375622";
        statusesAPI.show(Long.valueOf(id), weibo_show_Listener);
    }

    private  RequestListener weibo_show_Listener = new RequestListener() //回调监听
    {
        @Override
        public void onComplete(String response) {

            if (!TextUtils.isEmpty(response))
            {
                Log.i("微博",response);
                status=status.parse(response);

            }
        }
        @Override
        public void onWeiboException(WeiboException e) {
            Log.e("用户资料", e.getMessage());
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            Toast.makeText(WeiboDetail.this, info.toString(), Toast.LENGTH_LONG).show();
        }
    };
}