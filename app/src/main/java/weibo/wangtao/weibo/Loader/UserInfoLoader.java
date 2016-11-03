package weibo.wangtao.weibo.Loader;

import android.content.Context;

import com.sina.weibo.sdk.openapi.models.User;

import java.io.IOException;

import weibo.wangtao.weibo.Http.BaseHttp;

/**
 * Created by wangtao on 2016/11/2.
 */

public class UserInfoLoader extends DataLoader{



    public UserInfoLoader(Context context) {
        super(context);
        mContext = context;

    }

    @Override
    protected User loadData() {



        return null;
    }

    @Override
    public User loadInBackground() {
        return loadData();
    }


}
