package weibo.wangtao.weibo.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import java.text.SimpleDateFormat;

import weibo.wangtao.weibo.Bean.AccessTokenKeeper;
import weibo.wangtao.weibo.R;

import static weibo.wangtao.weibo.Tools.Constants.APP_KEY;
import static weibo.wangtao.weibo.Tools.Constants.REDIRECT_URL;
import static weibo.wangtao.weibo.Tools.Constants.SCOPE;
import android.util.Log;
public class Login_Activity extends AppCompatActivity  implements WeiboAuthListener {
    private AuthInfo mAuthInfo;

    /**
     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
     */
    private Oauth2AccessToken mAccessToken;

    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    private SsoHandler mSsoHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);


        Log.i("Login", "--------------");
        initView();

        mAccessToken = AccessTokenKeeper.readAccessToken(this);// 从 SharedPreferences 中读取上次已保存好 AccessToken 等信息
        if (mAccessToken.isSessionValid()) //token合法
        {
            Log.i("Login", "--------------" + mAccessToken.getToken());
            updateTokenView(true);

        } else//token 不合法
        {
            Log.i("Login", "--------------" + "Login 不合法");
            mSsoHandler.authorize(this);//打开授权页面（all in one）
        }
    }





    private void initView() {


        mAuthInfo = new AuthInfo(this, APP_KEY, REDIRECT_URL, SCOPE); // 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
        mSsoHandler = new SsoHandler(this, mAuthInfo);

    }


    /**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults

        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

    }

    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     *    该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */

    @Override
    public void onComplete(Bundle values) {
        // 从 Bundle 中解析 Token
        mAccessToken = Oauth2AccessToken.parseAccessToken(values);
        //从这里获取用户输入的 电话号码信息
        String  phoneNum =  mAccessToken.getPhoneNum();
        if (mAccessToken.isSessionValid()) {
            // 显示 Token
            updateTokenView(false);

            // 保存 Token 到 SharedPreferences
            AccessTokenKeeper.writeAccessToken(Login_Activity.this, mAccessToken);
            Toast.makeText(Login_Activity.this,
                    "success", Toast.LENGTH_SHORT).show();
        } else {
            // 以下几种情况，您会收到 Code：
            // 1. 当您未在平台上注册的应用程序的包名与签名时；
            // 2. 当您注册的应用程序包名与签名不正确时；
            // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
            String code = values.getString("code");
            String message = "failed";
            if (!TextUtils.isEmpty(code)) {
                message = message + "\nObtained the code: " + code;
            }
            Toast.makeText(Login_Activity.this, message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCancel() {
        Toast.makeText(Login_Activity.this,
                "取消", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onWeiboException(WeiboException e) {
        Toast.makeText(Login_Activity.this,
                "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
    }


    /**
     * 显示当前 Token 信息。
     *
     * @param hasExisted 配置文件中是否已存在 token 信息并且合法
     */
    private void updateTokenView(boolean hasExisted) {
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                new java.util.Date(mAccessToken.getExpiresTime()));
        String format = getString(R.string.weibosdk_demo_token_to_string_format_1);

        Toast.makeText(this,String.format(format, mAccessToken.getToken(), date),Toast.LENGTH_LONG);

        String message = String.format(format, mAccessToken.getToken(), date);
        if (hasExisted) {
            message = getString(R.string.weibosdk_demo_token_has_existed) + "\n" + message;
        }

        Toast.makeText(this,message,Toast.LENGTH_LONG);


    }
}
