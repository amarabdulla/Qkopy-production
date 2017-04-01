package com.example.computer.qkopy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class ThreeFragment extends Fragment {
    private static final String MY_PODCAST_URL = "http://museon.net/qkopy-beta1/auth/profile/151";
    String imagePath,filePath;
//    FloatingActionMenu materialDesignFAM;
    com.github.clans.fab.FloatingActionButton materialDesignFAM;
//    com.github.clans.fab.FloatingActionButton floatingActionButton1, floatingActionButton2;
    Bitmap bitmap;
    RelativeLayout viewC,viewA;
    int column_index;
    SharedPreferences pref;
    Boolean res=false;
    private ListView feedListView;
    private  int id;
    private CircleImageView imageView,imageView2;
    private ProgressDialog pDialog;
    private String name,mobile,profile_pic;
    private String post_time_from_server,post_url_from_server,post_url_thumb,privacy,post_desc,post_loc;
    private ArrayList<String> post_time_server_list,post_url_server_list, post_url_loc_list,ids_list,desc_list,privacy_list;
    private TextView number_text,name_text,name_text2,number_text2;
    private ImageLoader imageLoader;
    private String user_id;
    private ImageView empty_bg;
    private TextView empty_msg_one,empty_msg_two;
    private FloatingActionButton upload_btn;
    AnimationDrawable animationDrawable;
    public ThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View gv = inflater.inflate(R.layout.test_me_fragment, null);
//        materialDesignFAM = (FloatingActionMenu) gv.findViewById(R.id.fab);
         materialDesignFAM = (com.github.clans.fab.FloatingActionButton) gv.findViewById(R.id.fab);
        viewC=(RelativeLayout)gv.findViewById(R.id.viewC);
        viewA=(RelativeLayout)gv.findViewById(R.id.viewA);
//        LayoutTransition lt = new LayoutTransition();
//        lt.setDuration(50);
//        lt.setStartDelay (LayoutTransition.APPEARING, 0 );
//        lt.setStartDelay (LayoutTransition.DISAPPEARING, 0 );
//        lt.setStartDelay (LayoutTransition.CHANGE_APPEARING, 0 );
//        lt.setStartDelay (LayoutTransition.CHANGE_DISAPPEARING, 0 );

        animationDrawable = (AnimationDrawable) viewA.getBackground();
        animationDrawable.setEnterFadeDuration(3000);
        animationDrawable.setExitFadeDuration(1500);
        if (animationDrawable != null && !animationDrawable.isRunning()) {
            animationDrawable.start();
        }
//        floatingActionButton1 = (com.github.clans.fab.FloatingActionButton) gv.findViewById(R.id.floating_facebook);
//        floatingActionButton2 = (com.github.clans.fab.FloatingActionButton) gv.findViewById(R.id.floating_twitter);
        feedListView= (ListView) gv.findViewById(R.id.list);
        empty_bg=(ImageView) gv.findViewById(R.id.empty_bg);
        empty_msg_one=(TextView)gv.findViewById(R.id.empty_msg_one);
        empty_msg_two=(TextView)gv.findViewById(R.id.empty_msg_two);
        post_time_server_list=new ArrayList<String>();
        post_url_server_list=new ArrayList<String>();
        ids_list= new ArrayList<String>();
        desc_list= new ArrayList<String>();
        post_url_loc_list = new ArrayList<String>();
        privacy_list=new ArrayList<String>();
        number_text=(TextView)gv.findViewById(R.id.number);
        number_text2=(TextView)gv.findViewById(R.id.number2);
        name_text2=(TextView)gv.findViewById(R.id.user_name2);
        name_text=(TextView)gv.findViewById(R.id.user_name);
        imageView=(CircleImageView) gv.findViewById(R.id.navigation_drawer_user_account_picture_profile);
        imageView2=(CircleImageView) gv.findViewById(R.id.navigation_drawer_user_account_picture_profile2);
        imageLoader= new ImageLoader(getActivity());
//        upload_btn=(FloatingActionButton)gv.findViewById(R.id.fab);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

//        makeJsonRequestMeGet();
        pref=getActivity().getSharedPreferences("MyFile",0);
        String check=pref.getString("name","");

//        if (check.equals("")){
//            makeJsonRequestGetPodcast();
//        }else {
//            loadStringFromPref();
//        }

        makeJsonRequestGetPodcast();
//        Context context = getActivity().getApplicationContext();
//        SharedPreferences prefs = context.getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        materialDesignFAM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(),PostPreviewPage.class);
                startActivity(intent);
            }
        });
//        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                photoPickerIntent.setType("image/*");
//                startActivityForResult(photoPickerIntent, 1);
//
//            }
//        });
//        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                //TODO something when floating action menu second item clicked
//                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                photoPickerIntent.setType("video/*");
//                startActivityForResult(photoPickerIntent, 1);
//
//            }
//        });
//        upload_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                photoPickerIntent.setType("*/*");
//                startActivityForResult(photoPickerIntent, 1);
//
//            }
//        });
//         id = prefs.getInt("my_user_id", 0);
//        if (restoredText != null) {
//              id = prefs.getString("my_user_id", "No name defined");//"No name defined" is the default value.
//        }
        return gv;
    }

//    private void makeJsonRequestMeGet() {
//        showpDialog();
//        System.out.println("is is "+id);
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, MY_PODCAST_URL +"/" +"SignUpPage.MY_USERID"  ,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject mainObject = new JSONObject(response.toString());
//                            System.out.println(response.toString());
//                            name = mainObject.getString("name");
//                            mobile = mainObject.getString("mobile");
//                            profile_pic = mainObject.getString("profile_pic");
//
//
//                            getActivity().runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    name_text.setText(name);
//                                    number_text.setText(mobile);
//                                    imageLoaderVolley.DisplayImage(profile_pic, imageView);
//                                }
//                            });
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        hidepDialog();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(final VolleyError error) {
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
////                                Toast.makeText(getActivity(), "Error: " + error.getMessage().toString(), Toast.LENGTH_LONG).show();
//                                hidepDialog();
//                            }
//                        });
//                    }
//                }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("Client-Service", "frontend-client");
//                headers.put("Auth-Key", "naXw7ONz1zd92Y3b4dd363cyn4WpqA9o");
//                return headers;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//        requestQueue.add(stringRequest);
//
//    }
    private void makeJsonRequestGetPodcast() {
        showpDialog();
        System.out.println("id is "+id);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, MY_PODCAST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject= new JSONObject(response.toString());
                            name=jsonObject.getString("name");

                            mobile=jsonObject.getString("mobile");
                            profile_pic=jsonObject.getString("profile_pic");

//                            Storing string to shared pref
//                            storeStringToPref();

                            name_text.setText(name);
                            number_text.setText(mobile);
                            imageLoader.DisplayImage(profile_pic, imageView);
                            name_text2.setText(name);
                            number_text2.setText(mobile);
                            imageLoader.DisplayImage(profile_pic, imageView2);
                            JSONArray jsonArray = jsonObject.getJSONArray("his_posts");
                            if (jsonArray.length()==0){
//                                System.out.println("dddddddddfffffffssssss"+id);
                                empty_bg.setVisibility(View.VISIBLE);
                                empty_msg_one.setVisibility(View.VISIBLE);
                                empty_msg_two.setVisibility(View.VISIBLE);
                            }else {

                                System.out.println(response.toString());
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObjectUser = jsonArray.getJSONObject(i);
                                    post_time_from_server = jsonObjectUser.getString("post_time");
                                    post_url_from_server = jsonObjectUser.getString("post_url");
//                                    post_url_thumb = jsonObjectUser.getString("post_thumb_url");
                                    privacy = jsonObjectUser.getString("privacy");
                                    user_id = jsonObjectUser.getString("id");
                                    post_loc= jsonObjectUser.getString("post_loc");
                                    post_desc= jsonObjectUser.getString("post_desc");
                                    System.out.println("desc111"+post_desc);
                                    ids_list.add(user_id);
                                    post_time_server_list.add(post_time_from_server);
                                    post_url_loc_list.add(post_loc);
                                    desc_list.add(post_desc);
                                    privacy_list.add(privacy);
                                }

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (int j=0;j<desc_list.size();j++){
                                            System.out.println("desc"+desc_list.get(j));
                                        }
                                        if (!privacy_list.isEmpty()) {
                                            feedListView.setAdapter(new MeFeedListAdapter(getActivity(), desc_list, post_time_server_list, ids_list, post_url_loc_list,profile_pic,name));
                                        }
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        feedListView.setOnTouchListener(new View.OnTouchListener() {
                            float height;
                            String tag="TAG";
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                int action = event.getAction();
                                float height = event.getY();
                                if(action == MotionEvent.ACTION_DOWN){
                                    this.height = height;
                                }else if(action == MotionEvent.ACTION_UP){
                                    if(this.height < height){
                                        Log.v(tag, "Scrolled up");
                                        viewA.setVisibility(View.VISIBLE);
                                        viewC.setVisibility(View.GONE);
//                                        viewA.animate()
//                                                .alpha(0f)
//                                                .setDuration(300)
//                                                .setListener(new AnimatorListenerAdapter() {
//                                                    @Override
//                                                    public void onAnimationEnd(Animator animation) {
//                                                        viewC.setVisibility(View.GONE);
//                                                        viewC.clearAnimation();
//                                                    }
//                                                });

//                                        viewC.animate()
//                                                .translationY(viewA.getHeight())
//                                                .alpha(0.0f)
//                                                .setDuration(300)
//                                                .setListener(new AnimatorListenerAdapter() {
//                                                    @Override
//                                                    public void onAnimationEnd(Animator animation) {
//                                                        super.onAnimationEnd(animation);
//                                                        viewC.clearAnimation();
//                                                        viewC.setVisibility(View.GONE);
//
//                                                    }
//                                                });
                                    }else if(this.height > height){
                                        Log.v(tag, "Scrolled down");
                                        viewA.setVisibility(View.GONE);
                                         viewC.setVisibility(View.VISIBLE);


                                    }
                                }
                                return false;
                            }
                        });
//                        feedListView.setOnScrollListener(new AbsListView.OnScrollListener() {
//                            float height;
//                            @Override
//                            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//                            }
//
//                            @Override
//                            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
////                                viewA.setVisibility(View.GONE);
////                                viewC.setVisibility(View.VISIBLE);
//
//
//
//                            }
//                        });
                        for (int i = 0; i < post_url_server_list.size(); i++) {
                            System.out.println("feed"+post_url_server_list.get(i));
                        }

                        hidepDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError error) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Toast.makeText(getActivity(), "Error: " + error.getMessage().toString(), Toast.LENGTH_LONG).show();
                                hidepDialog();
                            }
                        });
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Client-Service", "frontend-client");
                headers.put("Auth-Key", "naXw7ONz1zd92Y3b4dd363cyn4WpqA9o");
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }
//    private Boolean storeStringToPref(){
//        res=true;
//        pref=getActivity().getSharedPreferences("MyFile",0);
//        SharedPreferences.Editor mEdit1 = pref.edit();
//        mEdit1.putString("name",name);
//        mEdit1.putString("mobile",mobile);
//        mEdit1.putString("profile_pic",profile_pic);
//        mEdit1.commit();
//        return res;
//    }
//    private void loadStringFromPref(){
//        pref=getActivity().getSharedPreferences("MyFile",0);
//        String name_pref=pref.getString("name","name");
//        String mobile_pref=pref.getString("mobile","mobile");
//        String profile_pic_pref=pref.getString("profile_pic","profile_pic");
//
//        name_text.setText(name_pref);
//        number_text.setText(mobile_pref);
//        imageLoaderVolley.DisplayImage(profile_pic_pref, imageView);
//    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1)
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();


                filePath = getPath(selectedImage);
//                filePath = data.getData().getPath();
//                String file_extn = filePath.substring(filePath.lastIndexOf(".") + 1);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.fromFile(new File(filePath)));
//                    image.setImageBitmap(bitmap);

//                    Bitmap bitmap2 = BitmapFactory.decodeFile(filePath);
//                    Intent intent= new Intent(getActivity(),PreviewImageAndUploadActivity.class);
//                    intent.putExtra("image", filePath);
//                     startActivity(intent);

                } catch (IOException e) {
                    e.printStackTrace();
                }
//                if (file_extn.equals("img") || file_extn.equals("jpg") || file_extn.equals("jpeg") || file_extn.equals("gif") || file_extn.equals("png")) {
//                    //FINE
////                    new BackgroundTask(SignUpPage.this).execute();
//                } else {
//                    //NOT IN REQUIRED FORMAT
//                    Toast.makeText(getActivity(), "Format not supported", Toast.LENGTH_SHORT).show();
//                }
            }
    }


    public String getPath(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        column_index = cursor
                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        imagePath = cursor.getString(column_index);
        return cursor.getString(column_index);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
