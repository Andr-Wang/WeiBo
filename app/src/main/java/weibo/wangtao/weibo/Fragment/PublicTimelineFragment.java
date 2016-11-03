package weibo.wangtao.weibo.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.sina.weibo.sdk.openapi.models.User;

import weibo.wangtao.weibo.Activity.MainActivity;
import weibo.wangtao.weibo.Adapter.PublicTimelineAdapter;
import weibo.wangtao.weibo.Bean.AccessTokenKeeper;
import weibo.wangtao.weibo.R;

import static weibo.wangtao.weibo.Tools.Constants.APP_KEY;

/**
 * Created by wangtao on 2016/11/3.
 */

public class PublicTimelineFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener
{
    private RecyclerView mRecyclerView;
    private Oauth2AccessToken mAccessToken;
    private SwipeRefreshLayout swipeRefreshLayout;

    private  PublicTimelineAdapter madapter;
    private LinearLayoutManager mLinearLayoutManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        madapter=new PublicTimelineAdapter();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.public_timeline, container,false);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(madapter);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refresh);

        swipeRefreshLayout.setColorSchemeResources(
                R.color.swipe_color_1,
                R.color.swipe_color_2,
                R.color.swipe_color_3,
                R.color.swipe_color_4);
        Log.i("PublicTimelineFragment","onCreateView完成");
        swipeRefreshLayout.setOnRefreshListener(this);


        return view;
    }
    private void initPublic_Timeline()
    {
        mAccessToken = AccessTokenKeeper.readAccessToken(getActivity());// 从 SharedPreferences 中读取上次已保存好 AccessToken 等信息
        StatusesAPI statusesAPI=new StatusesAPI(getActivity(),APP_KEY,mAccessToken);
        statusesAPI.publicTimeline(50,1,false,public_Time_Line_listener);
    }
    private RequestListener public_Time_Line_listener = new RequestListener() //回调监听
    {
        @Override
        public void onComplete(String response) {

            if (!TextUtils.isEmpty(response))
            {

                Log.i("微博",response);
                StatusList statusList=new StatusList();
                statusList= statusList.parse(response);
                Log.i("StatusList",String.valueOf(statusList.statusList.size()));
                madapter.set(getActivity(),statusList.statusList);
                mRecyclerView.setAdapter(madapter);
                madapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        }
        @Override
        public void onWeiboException(WeiboException e) {
            Log.e("用户资料", e.getMessage());
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            Toast.makeText(getActivity(), info.toString(), Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onRefresh()
    {
        initPublic_Timeline();
    }
}
