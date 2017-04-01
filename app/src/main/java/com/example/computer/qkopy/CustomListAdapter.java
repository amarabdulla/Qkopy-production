package com.example.computer.qkopy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by computer on 2/16/2017.
 */

public class CustomListAdapter extends BaseAdapter {
    private static LayoutInflater inflater=null;
    ArrayList<String> result;
    Context context;
    ArrayList<String> imageId;
    ArrayList<String> userId;
    
    String userid_selected;
    ImageLoader imageLoader;

    public CustomListAdapter(Context mainActivity, ArrayList<String> prgmNameList, ArrayList<String> prgmImages, ArrayList<String> idList) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        context=mainActivity;
        imageId=prgmImages;
        userId=idList;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(mainActivity);
    }
    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.contacts_item, null);
        holder.tv=(TextView) rowView.findViewById(R.id.contact_name);
        holder.last_update=(TextView) rowView.findViewById(R.id.last_update);
        Typeface custom_font_pro_regular = Typeface.createFromAsset(context.getAssets(),  "fonts/proximanova-regular.otf");
        Typeface custom_font_pro_semibold = Typeface.createFromAsset(context.getAssets(),  "fonts/proximanova-semibold.otf");
        holder.img=(ImageView) rowView.findViewById(R.id.profilepic);
        holder.tv.setText(result.get(i));
        holder.tv.setTypeface(custom_font_pro_semibold);
        holder.last_update.setTypeface(custom_font_pro_regular);
        imageLoader.DisplayImage(imageId.get(i),holder.img);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                userid_selected=userId.get(i);
//                Toast.makeText(context, "You Clicked "+userid_selected, Toast.LENGTH_LONG).show();
                Intent intent= new Intent(context,UserProfilePage.class);
                intent.putExtra("user_id", userid_selected);
                context.startActivity(intent);
            }
        });
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, FullScreenImageActivity.class);
//
//                holder.img.buildDrawingCache();
//                Bitmap image= holder.img.getDrawingCache();
//
//                Bundle extras = new Bundle();
//                extras.putParcelable("imagebitmap", image);
//                intent.putExtras(extras);
//                context.startActivity(intent);
            }
        });
        return rowView;
    }

    public class Holder
    {
        TextView tv;
        TextView last_update;
        ImageView img;
    }

}
