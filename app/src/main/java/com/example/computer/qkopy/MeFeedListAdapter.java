package com.example.computer.qkopy;

import android.app.DownloadManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;


import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by computer on 3/4/2017.
 */

public class MeFeedListAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    ArrayList<String> post_id_list;
    ArrayList<String> post_desc_list;
    ArrayList<String> post_time_list;
    ArrayList<String> post_loc_list = new ArrayList<>();
    Context context;
    private long lastDownload = -1L;
    private DownloadManager mgr = null;
    com.android.volley.toolbox.ImageLoader imageLoaderVolley;
    ImageLoader imageLoader;
    String prof_pic,name_striing;

    public MeFeedListAdapter(Context activity, ArrayList<String> desc_list, ArrayList<String> time_list, ArrayList<String> id_list, ArrayList<String> loc_list, String profile_pic, String naam) {
        context = activity;
        this.post_time_list = time_list;
        this.post_desc_list = desc_list;
        this.post_id_list = id_list;
        this.post_loc_list =loc_list;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoaderVolley = CustomVolleyRequestQueue.getInstance(this.context).getImageLoader();
        mgr = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        this.prof_pic=profile_pic;
        this.name_striing=naam;
    }
    @Override
    public int getCount() {
        return post_desc_list.size();
    }

    @Override
    public Object getItem(int position) {
        return post_desc_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView;
        rowView = inflater.inflate(R.layout.feed_test, null);
        if (imageLoaderVolley == null)
            imageLoaderVolley = CustomVolleyRequestQueue.getInstance(this.context).getImageLoader();

        TextView name = (TextView) rowView.findViewById(R.id.name);
        TextView timestamp = (TextView) rowView
                .findViewById(R.id.timestamp);
        TextView statusMsg = (TextView) rowView
                .findViewById(R.id.txtStatusMsg);
//        TextView url = (TextView) rowView.findViewById(R.id.txtUrl);
        NetworkImageView profilePic = (NetworkImageView) rowView

                .findViewById(R.id.profilePic);
        imageLoader=new ImageLoader(context);
        final ImageView mapPreview= (ImageView) rowView.findViewById(R.id.feedImage1);
        if (!post_loc_list.get(position).equals("")){
            String map="https://maps.googleapis.com/maps/api/staticmap?zoom=15&size=400x280&sensor=false&maptype=roadmap&markers=color:red|"+post_loc_list.get(position);
            Glide.with(context).load(map).into(mapPreview);
        }
        if (!post_desc_list.get(position).equals("")){
            statusMsg.setText(post_desc_list.get(position)+"");
        }

        name.setText(name_striing);
        // user profile pic
        profilePic.setImageUrl(prof_pic, imageLoaderVolley);

        return rowView;
    }

}
