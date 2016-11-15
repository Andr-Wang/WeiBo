package weibo.wangtao.weibo.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import weibo.wangtao.weibo.Activity.MainActivity;
import weibo.wangtao.weibo.Activity.UserIndex;
import weibo.wangtao.weibo.Fragment.PublicTimelineFragment;
import weibo.wangtao.weibo.R;
import weibo.wangtao.weibo.Tools.CircleTransform;
import weibo.wangtao.weibo.Tools.GlideCircleTransform;

import android.util.Log;
import android.view.ViewGroup.LayoutParams;

import static weibo.wangtao.weibo.Tools.Tools.time_Change;

/**
 * Created by wangtao on 2016/11/3.
 */

public class PublicTimelineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private final Context context;
    private ArrayList<Status> statusList;
    private User user = new User();
    private static final int VIEW_PROG = 1;//progressbar flag

    private OnLoadMoreListener onLoadMoreListener;
    private boolean loading=false;
    private int lastVisibleItem, totalItemCount,firstVisibleItem;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    private GridViewAdapter adapter;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public  interface OnRecyclerViewItemClickListener {
        void onWeiBoClick(View view , String id);
        void onUserClick(View view , String id);
    }

    public PublicTimelineAdapter(Context context, RecyclerView recyclerView){
        super();
        this.context = context;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    firstVisibleItem=linearLayoutManager.findFirstVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + 1)) {
                        // end has been reached
                        // do something
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                    }


                }
            });

        }
    }


    public void add_Statues(ArrayList<Status> statusList,int Total_Number,int page_Count) {
        try {
            Log.e("add_Statues info---",String.valueOf(Total_Number)+"    "+String.valueOf(page_Count)+"   "+String.valueOf(this.statusList.size()+"   "+String.valueOf(statusList.size())));

        } catch (NullPointerException e)
        {

        }
        int count=0;
        if(this.statusList!=null) count=this.statusList.size();
        if(this.statusList==null && page_Count==1)// fresh
        {
            this.statusList = statusList;
            notifyDataSetChanged();
            if(Total_Number>50)
            {
                this.statusList.add(null);
                Log.e("fresh",String.valueOf(this.statusList.size()));
            }
        }else if(page_Count>1 && count<Total_Number)//add
        {
            Log.e("add_Statues",String.valueOf(page_Count));

            if(this.statusList.get(this.statusList.size()-1)==null)
            {
                this.statusList.remove(this.statusList.size()-1);

                for(int i=0;i<statusList.size();i++)
                {
                    if(statusList.get(i)==null)   Log.e("add_Statues=Null",String.valueOf(i));
                    this.statusList.add(statusList.get(i));
                }
                notifyDataSetChanged();
                if (statusList.size()==50) this.statusList.add(null);
            }else
            {
                Log.e("remove_Null","last one is none-null!!!!!!!");
            }
            Log.e("add",String.valueOf(this.statusList.size()));
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        if (viewType == VIEW_PROG) {
            v=LayoutInflater.from(
                    context).inflate(R.layout.progressbar_item, parent, false);
            return new ProgressViewHolder(v);
        } else {
            v=LayoutInflater.from(
                    context).inflate(R.layout.public_timeline_item, parent,
                    false);


            return new ItemHolder(v);
        }


    }

    public void clear_List()
    {
        if(statusList!=null)
        {
            this.statusList=null;

        }

    }

    public  class ItemHolder extends RecyclerView.ViewHolder {
        private TextView userid, date, weibo_content,weibo_image_count;
        private ImageView user_image;
        private TextView reposts_btn, comments_btn, attitudes_btn;
        private GridView gridView;
        private View weibo_detail;


        ItemHolder(View view) {
            super(view);
            userid = (TextView) view.findViewById(R.id.userid);
            date = (TextView) view.findViewById(R.id.date);
           // comment_text = (TextView) view.findViewById(R.id.comment_text);
            weibo_content = (TextView) view.findViewById(R.id.weibo_content);
            user_image = (ImageView) view.findViewById(R.id.user_image);

            reposts_btn = (TextView) view.findViewById(R.id.reposts_txv);
            comments_btn = (TextView) view.findViewById(R.id.comments_txv);
            attitudes_btn = (TextView) view.findViewById(R.id.attitudes_txv);
            weibo_image_count=(TextView)view.findViewById(R.id.weibo_image_count);
            gridView=(GridView)view.findViewById(R.id.weiboimage_gv);
            weibo_detail=view.findViewById(R.id.weibo_detail);

        }
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public TextView textView;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
            textView = (TextView) itemView.findViewById(R.id.textView5);
        }
    }



    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemHolder && !loading) {
            final ItemHolder viewHolder = (ItemHolder) holder;

            try
            {
                user = statusList.get(position).user;
                Glide
                        .with(context)
                        .load(user.avatar_hd)
                        .error(R.drawable.street)
                        .transform(new GlideCircleTransform(context))
                        .skipMemoryCache(true)
                        .into(viewHolder.user_image);


                if(statusList.get(position).pic_urls!=null)
                {
                    if(!statusList.get(position).pic_urls.isEmpty())
                    {

                        adapter=new GridViewAdapter(context);
                        viewHolder.gridView.setTag(statusList.get(position).pic_urls);
                        adapter.set_List(viewHolder.gridView.getTag());
                        viewHolder.gridView.setAdapter(adapter);

                        //viewHolder.weibo_image_count.setText(statusList.get(position).pic_urls.size());
                    }

                }

                viewHolder.weibo_content.setText(statusList.get(position).text);
                viewHolder.userid.setText(user.screen_name);
                viewHolder.date.setText(time_Change(statusList.get(position).created_at));

                viewHolder.reposts_btn.setText(String.valueOf(statusList.get(position).reposts_count));
                viewHolder.comments_btn.setText(String.valueOf(statusList.get(position).comments_count));
                viewHolder.attitudes_btn.setText(String.valueOf(statusList.get(position).attitudes_count));

                viewHolder.itemView.setTag(String.valueOf(position));
                viewHolder.weibo_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            //使用getTag方法获取数据
                            mOnItemClickListener.onWeiBoClick(v,statusList.get(viewHolder.getAdapterPosition()).id);
                        }
                    }
                });
                viewHolder.user_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            //使用getTag方法获取数据

                            mOnItemClickListener.onUserClick(v,statusList.get(viewHolder.getAdapterPosition()).user.id);
                        }
                    }
                });


            } catch (NullPointerException e)
            {

            }

        } else if (holder instanceof ProgressViewHolder) {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }

    }



    @Override
    public int getItemCount() {

        if (statusList == null) {
            // Log.i("getItemCount","------------------------------");
            return 0;
        }
        // Log.i("getItemCount","------------------------------"+statusList.size());
        return statusList.size();
    }

    public void setLoading() {
        loading = true;
    }

    public void setLoaded() {
        loading = false;
    }








    @Override
    public int getItemViewType(int position) {
        if (statusList.get(position) == null )  return VIEW_PROG;
          return 0;
    }
}
