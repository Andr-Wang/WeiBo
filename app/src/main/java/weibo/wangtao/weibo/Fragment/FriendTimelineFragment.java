package weibo.wangtao.weibo.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.StatusList;

import weibo.wangtao.weibo.Activity.UserIndex;
import weibo.wangtao.weibo.Activity.WeiboDetail;
import weibo.wangtao.weibo.Adapter.OnLoadMoreListener;
import weibo.wangtao.weibo.Adapter.PublicTimelineAdapter;
import weibo.wangtao.weibo.Bean.AccessTokenKeeper;
import weibo.wangtao.weibo.R;
import android.util.Log;
import static weibo.wangtao.weibo.Tools.Constants.APP_KEY;


public class FriendTimelineFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private RecyclerView mRecyclerView;
    private Oauth2AccessToken mAccessToken;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PublicTimelineAdapter mAdapter;
    private static int page_count=1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.public_timeline, container,false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);



        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false){
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state)//add extra space in bottom of recycleview
            {
                return 6000;
            }
        };
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter=new PublicTimelineAdapter(getActivity(),mRecyclerView);
        mAdapter.setOnItemClickListener(new PublicTimelineAdapter.OnRecyclerViewItemClickListener(){
            @Override
            public void onWeiBoClick(View view , String id){
                // Log.i("onItemClick------");
                Intent intent=new Intent();
                intent.setClass(getActivity(), WeiboDetail.class);
                intent.putExtra("id",id );
                startActivity(intent);
            }

            @Override
            public void onUserClick(View view, String id) {
                Intent intent=new Intent();
                intent.setClass(getActivity(), UserIndex.class);
                intent.putExtra("id",id );
                startActivity(intent);
            }
        });
        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                addPublic_Timeline();
            }
        });
        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                addPublic_Timeline();
            }
        });



        mRecyclerView.setAdapter(mAdapter);


        mRecyclerView.setAdapter(mAdapter);


        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refresh);

        swipeRefreshLayout.setColorSchemeResources(
                R.color.swipe_color_1,
                R.color.swipe_color_2,
                R.color.swipe_color_3,
                R.color.swipe_color_4);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {//自动刷新

            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);


                freshPublic_Timeline();
            }
        });

        return view;
    }
    private void  addPublic_Timeline()
    {
        Log.e("addPublic_Timeline","addPublic_Timeline");
        mAccessToken = AccessTokenKeeper.readAccessToken(getActivity());// 从 SharedPreferences 中读取上次已保存好 AccessToken 等信息
        StatusesAPI statusesAPI=new StatusesAPI(getActivity(),APP_KEY,mAccessToken);
        mAdapter.setLoading();//flag set loading
        // statusesAPI.friendsTimeline(0,0,50,page_count,false,0,false,public_Time_Line_listener);
        statusesAPI.bilateralTimeline(0,0,50,page_count,false,0,false,bilateralTimeline_listener);
        page_count++;

        //statusesAPI.friendsTimeline(0,0,50,1,false,0,false,public_Time_Line_listener);
    }


    private void freshPublic_Timeline()
    {
        Log.e("freshPublic_Timeline","freshPublic_Timeline");
        mAccessToken = AccessTokenKeeper.readAccessToken(getActivity());// 从 SharedPreferences 中读取上次已保存好 AccessToken 等信息
        StatusesAPI statusesAPI=new StatusesAPI(getActivity(),APP_KEY,mAccessToken);
        page_count=1;

        mAdapter.setLoading();//flag set loading
        mAdapter.clear_List();
        statusesAPI.bilateralTimeline(0,0,50,1,false,0,false,bilateralTimeline_listener);
        //statusesAPI.friendsTimeline(0,0,50,1,false,0,false,public_Time_Line_listener);



    }
    private  RequestListener bilateralTimeline_listener = new RequestListener() //回调监听
    {
        @Override
        public void onComplete(String response) {

            if (!TextUtils.isEmpty(response))
            {

                Log.i("微博",response);
                StatusList statusList=new StatusList();
                statusList= statusList.parse(response);
                if(statusList.statusList!=null && statusList.total_number!=0)
                {
                    Log.i("StatusList",String.valueOf(statusList.statusList.size()));
                    Log.i("StatusList_total_number",String.valueOf(statusList.total_number));

                    mAdapter.add_Statues(statusList.statusList,statusList.total_number,page_count);

                    swipeRefreshLayout.setRefreshing(false);
                    mAdapter.setLoaded();
                }

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

        freshPublic_Timeline();
    }
}
