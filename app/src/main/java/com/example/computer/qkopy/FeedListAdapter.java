package com.example.computer.qkopy;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;


import static android.content.Context.DOWNLOAD_SERVICE;


public class FeedListAdapter extends BaseAdapter {
    //	private Activity activity;
    private static LayoutInflater inflater = null;
    //	private List<FeedItem> feedItems;
    ArrayList<String> post_id_list;
    ArrayList<String> post_url_list;
    ArrayList<String> post_time_list;
    ArrayList<String> post_thumb_list = new ArrayList<>();
    //	ImageLoader imageLoaderVolley;
    Context context;
    private long lastDownload = -1L;
    private DownloadManager mgr = null;
    ImageLoader imageLoader;
    VideoView videoView;

    public FeedListAdapter(Context activity, ArrayList<String> url_list, ArrayList<String> time_list, ArrayList<String> id_list, ArrayList<String> thumb_list) {
        context = activity;
        this.post_time_list = time_list;
        this.post_url_list = url_list;
        this.post_id_list = id_list;
        this.post_thumb_list=thumb_list;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = CustomVolleyRequestQueue.getInstance(this.context).getImageLoader();
        mgr = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);

    }

    @Override
    public int getCount() {
        return post_url_list.size();
    }

    @Override
    public Object getItem(int location) {
        return post_url_list.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        View rowView;
        rowView = inflater.inflate(R.layout.feed_test, null);


//		if (inflater == null)
//			inflater = (LayoutInflater) activity
//					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		if (convertView == null)
//			convertView = inflater.inflate(R.layout.feed_item, null);

        if (imageLoader == null)
            imageLoader = CustomVolleyRequestQueue.getInstance(this.context).getImageLoader();

        TextView name = (TextView) rowView.findViewById(R.id.name);
//        ImageView download = (ImageView) rowView.findViewById(R.id.download);
        TextView timestamp = (TextView) rowView
                .findViewById(R.id.timestamp);
//        JCVideoPlayerStandard jcVideoPlayerStandard = (JCVideoPlayerStandard) rowView.findViewById(R.id.videoplayer);
//		videoView=(VideoView)rowView.findViewById(R.id.videoview);
        TextView statusMsg = (TextView) rowView
                .findViewById(R.id.txtStatusMsg);
//        TextView url = (TextView) rowView.findViewById(R.id.txtUrl);
        NetworkImageView profilePic = (NetworkImageView) rowView
                .findViewById(R.id.profilePic);
//        FeedImageView feedImageView = (FeedImageView) rowView
//                .findViewById(R.id.feedImage1);



        name.setText(post_id_list.get(position));

        // Converting timestamp into x ago format
//		CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
//				Long.parseLong(post_time_list.get(position)),
//				System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
//		timestamp.setText(timeAgo);


        // Checking for null feed url
//		if (item.getUrl() != null) {
//			url.setText(Html.fromHtml("<a href=\"" + item.getUrl() + "\">"
//					+ item.getUrl() + "</a> "));
//
//			// Making url clickable
//			url.setMovementMethod(LinkMovementMethod.getInstance());
//			url.setVisibility(View.VISIBLE);
//		} else {
//			// url is null, remove from the view
//			url.setVisibility(View.GONE);
//		}

        // user profile pic
        profilePic.setImageUrl("https://missingtricks.net/wp-content/uploads/2015/11/Awesome-Profile-Pictures-HD-For-Whatsapp-1024x818-300x240.jpg", imageLoader);


        return rowView;
    }


}
