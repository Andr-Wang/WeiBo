package weibo.wangtao.weibo.Adapter;

import android.view.Gravity;
import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.lang.Object;

import weibo.wangtao.weibo.R;


/**
 * Created by wangtao on 2016/11/11.
 */

public class GridViewAdapter extends BaseAdapter {

    private ArrayList<String> list;
    private LayoutInflater mInflater;
    private Context mContext;
    private ViewGroup.LayoutParams params;

    public GridViewAdapter(Context context, ArrayList<String> list) {

        mContext = context;
        mInflater = LayoutInflater.from(context);
        this.list=list;


    }
    public GridViewAdapter(Context context)
    {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void set_List(Object object)
    {
        ArrayList<String> list= (ArrayList<String>) object;
        this.list=list;
    }
    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ItemViewTag viewTag;


            convertView = mInflater.inflate(R.layout.grid_item, null);

            // construct an item tag
            viewTag = new ItemViewTag((ImageView)convertView.findViewById(R.id.gv_item));
            convertView.setTag(viewTag);

        Picasso
                .with(mContext)
                .load(list.get(position))
                //.thumbnail(0.1f)
                .skipMemoryCache()
                .fit()
                .placeholder(R.drawable.street)
                .error(R.drawable.street)
                .into(viewTag.image);


        return convertView;
    }

    class ItemViewTag
    {
        private ImageView image;

        /**
         * The constructor to construct a navigation view tag
         *

         */
        public ItemViewTag(ImageView image)
        {
            this.image=image;
        }
    }

}