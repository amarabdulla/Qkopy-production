package com.example.computer.qkopy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TwoFragment extends Fragment {
    private static final String
            MY_PODCAST_URL = "http://museon.net/qkopy-beta1/auth/show_post/137";
    private ProgressDialog pDialog;
    private ImageView empty_bg;
    private TextView empty_msg_one,empty_msg_two;
    private String post_time_from_server,post_url_from_server,post_url_thumb,privacy;
    private ArrayList<String> post_time_server_list,post_url_server_list,ids_list,post_url_thumb_list;
    private String user_id;
    private ListView feedListView;
    private ImageView imageView;
    private TextView empty_textView;
    com.github.clans.fab.FloatingActionButton materialDesignFAM;

    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View gv = inflater.inflate(R.layout.fragment_two, null);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        materialDesignFAM = (com.github.clans.fab.FloatingActionButton) gv.findViewById(R.id.fab);
        empty_textView=(TextView)gv.findViewById(R.id.empty_msg);
        imageView=(ImageView)gv.findViewById(R.id.empty_img);
        feedListView= (ListView) gv.findViewById(R.id.list);
        post_time_server_list=new ArrayList<String>();
        post_url_server_list=new ArrayList<String>();
        materialDesignFAM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(),PostPreviewPage.class);
                startActivity(intent);
            }
        });
        ids_list= new ArrayList<String>();
        post_url_thumb_list= new ArrayList<String>();
        empty_msg_one=(TextView)gv.findViewById(R.id.empty_msg_one);
        empty_msg_two=(TextView)gv.findViewById(R.id.empty_msg_two);
        empty_bg=(ImageView) gv.findViewById(R.id.empty_bg);
        imageView.setVisibility(View.GONE);
        empty_textView.setVisibility(View.GONE);
//        new ProgressBack().execute();
        makeJsonRequestGetPodcast();
        // Inflate the layout for this fragment
        return gv;
    }

    private void makeJsonRequestGetPodcast() {
        showpDialog();
//        System.out.println("is is "+id);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, MY_PODCAST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response.toString());
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
                                    post_url_thumb = jsonObjectUser.getString("post_thumb_url");
                                    privacy = jsonObjectUser.getString("privacy");
                                    user_id = jsonObjectUser.getString("id");

                                    ids_list.add(user_id);
                                    post_time_server_list.add(post_time_from_server);
                                    post_url_thumb_list.add(post_url_thumb);
                                    post_url_server_list.add(post_url_from_server);
                                }

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!post_url_server_list.isEmpty()) {
                                            feedListView.setAdapter(new FeedListAdapter(getActivity(), post_url_server_list, post_time_server_list, ids_list,post_url_thumb_list));
                                        }
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}
