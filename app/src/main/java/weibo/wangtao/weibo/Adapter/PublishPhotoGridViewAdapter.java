package weibo.wangtao.weibo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import android.util.Log;
import java.util.ArrayList;

import weibo.wangtao.weibo.Activity.Weibo_Publish;
import weibo.wangtao.weibo.R;

/**
 * Created by wangtao on 2016/11/18.
 */

public class PublishPhotoGridViewAdapter extends BaseAdapter
{
    private ArrayList<String> list;
    private LayoutInflater mInflater;
    private Context mContext;
    private ViewGroup.LayoutParams params;
    private Open_Gallery open_gallery;

    public interface Open_Gallery
    {
        void onCallback_Gallery();

    }

    public void set_Open_Gallery_Listener(Open_Gallery open_gallery)
    {
        this.open_gallery=open_gallery;
    }

    public PublishPhotoGridViewAdapter(Context context) {

        mContext = context;
        mInflater = LayoutInflater.from(context);

    }


    public void set_List(ArrayList<String> list)
    {
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

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        convertView = mInflater.inflate(R.layout.publish_griditem_addpic, null);

        holder = new ViewHolder();

        /**得到各个控件的对象*/
        holder.image = (ImageView) convertView.findViewById(R.id.publish_image);
        if(list.get(position)=="+")//"+"
        {
            Picasso
                    .with(mContext)
                    .load(R.drawable.plus2)
                    //.thumbnail(0.1f)

                    .resize(200,200)
                    .into(holder.image);
        }else
        {

        }
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position)=="+")//"+"
                {
                    open_gallery.onCallback_Gallery();
                }else//delete
                {

                }
            }
        });
        return convertView;
    }

    public final class ViewHolder
    {
        private ImageView image;
    }
}
