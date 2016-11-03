package weibo.wangtao.weibo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import weibo.wangtao.weibo.Activity.MainActivity;
import weibo.wangtao.weibo.Fragment.PublicTimelineFragment;
import weibo.wangtao.weibo.R;
import android.util.Log;
/**
 * Created by wangtao on 2016/11/3.
 */

public class PublicTimelineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private Context context;
    private ArrayList<Status> statusList;
    private User user=new User();

    public PublicTimelineAdapter()
    {
        super();
    }
    public void set(Context context, ArrayList<Status> statusList) {

        this.context=context;
        this.statusList=statusList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.public_timeline_item, parent,
                false));
        return holder;
    }

     public static class MyViewHolder extends RecyclerView.ViewHolder
    {

        private TextView userid,date,comment_text,weibo_content;
        private ImageView user_image;
         MyViewHolder(View view)
        {
            super(view);
            userid = (TextView) view.findViewById(R.id.userid);
            date = (TextView) view.findViewById(R.id.date);
            comment_text = (TextView) view.findViewById(R.id.comment_text);
            weibo_content = (TextView) view.findViewById(R.id.weibo_content);
            user_image=(ImageView)view.findViewById(R.id.user_image);
        }

        public TextView getUserid() {
            return userid;
        }



        public ImageView getUser_image() {
            return user_image;
        }


        public TextView getWeibo_content() {
            return weibo_content;
        }


        public TextView getcomment_text() {
            return comment_text;
        }

        public TextView getDate() {
            return date;
        }

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        MyViewHolder viewHolder = (MyViewHolder)holder;
        user=statusList.get(position).user;
        Picasso
                .with(context)
                .load(user.avatar_hd)
                .fit()

                .into(viewHolder.getUser_image());
        viewHolder.getWeibo_content().setText(statusList.get(position).text);
        viewHolder.getUserid().setText(user.screen_name);

        viewHolder.getDate().setText(statusList.get(position).created_at);

    }

    @Override
    public int getItemCount()
    {

        if(statusList==null)
        {
           // Log.i("getItemCount","------------------------------");
            return 0;
        }
       // Log.i("getItemCount","------------------------------"+statusList.size());
        return statusList.size();
    }

}
