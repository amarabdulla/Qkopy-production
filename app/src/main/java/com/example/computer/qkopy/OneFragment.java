package com.example.computer.qkopy;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class OneFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TIME_SERVER = "time-a.nist.gov";
    private static final int REQUEST_PERMISSIONS = 20;
    private static final String USERSLIST_URL = "http://museon.net/qkopy-beta1/auth/contact";
    static ArrayList<String> contactslist= new ArrayList<>();
    static ArrayList<String> profile_piclist_full= new ArrayList<>();
    static ArrayList<String> profile_piclist_selected= new ArrayList<>();
    static ArrayList<String> userIdlist= new ArrayList<>();
    static ArrayList<String> userIdlist_selected= new ArrayList<>();
    static ArrayList<String> yy= new ArrayList<>();
    private static Date time;
    ListView list;
    Boolean res=false;
    ArrayList<String> numberlist= new ArrayList<>();
    SharedPreferences pref;
    ArrayList<String> contactslist2 = new ArrayList<>();
    ArrayList<String> profilepicList2 = new ArrayList<>();
    ArrayList<String> useridList2 = new ArrayList<>();
    private String mobile,profile_pic_url;
    private String user_id="null";
    private ProgressDialog pDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    public OneFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View gv = inflater.inflate(R.layout.fragment_one, null);
        list = (ListView) gv.findViewById(R.id.listView1);
        swipeRefreshLayout = (SwipeRefreshLayout) gv.findViewById(R.id.swipe_refresh_layout);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
//        makeJSONGetRequest();
        swipeRefreshLayout.setOnRefreshListener(this);


        pref=getActivity().getSharedPreferences("MyFile",0);
        String check=pref.getString("cachingOn","default");

//        if (check.equals("true")){
//            loadArray();
//
//        }else {
//            makeJSONGetRequest();
            pref.edit().putString("cachingOn","false").commit();
//        }
//            System.out.print("");
        // Inflate the layout for this fragment
        return gv;


    }

    private Boolean storeArray(){
        res=true;
        pref=getActivity().getSharedPreferences("MyFile",0);
        SharedPreferences.Editor edt = pref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(contactslist);
        String json1 = gson.toJson(profile_piclist_selected);
        String json2 = gson.toJson(userIdlist_selected);
        edt.putString("contacts_list", json);
        edt.putString("profile_piclist_selected", json1);
        edt.putString("userIdlist_selected", json2);
        edt.commit();
        return res;
    }

    private void loadArray(){
        Boolean check;
        Gson gson = new Gson();
        pref=getActivity().getSharedPreferences("MyFile",0);
        String json = pref.getString("contacts_list", null);
        String json2 = pref.getString("profile_piclist_selected", null);
        String json3 = pref.getString("userIdlist_selected", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        contactslist2 = gson.fromJson(json, type);
        profilepicList2 = gson.fromJson(json2, type);
        useridList2 = gson.fromJson(json3, type);

        if (!contactslist2.isEmpty()) {
            list.setAdapter(new CustomListAdapter(getActivity(), contactslist2, profilepicList2,useridList2));
//            Toast.makeText(getActivity(),"Loading from cache",Toast.LENGTH_SHORT).show();
            JSONObject idsObject = new JSONObject();
            String json34 = new Gson().toJson(useridList2);
//            try {
//                idsObject.put(SignUpPage.MY_USERID+"",json34);
//                System.out.print("idss"+idsObject);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }

    }

    private void makeJSONGetRequest(){
        showpDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, USERSLIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray mainObject = new JSONArray(response.toString());
                            System.out.println(response.toString());
                            for (int i = 0; i < mainObject.length(); i++) {
                                JSONObject jsonObjectUser = mainObject.getJSONObject(i);
                                user_id=jsonObjectUser.getString("id");
                                mobile = jsonObjectUser.getString("mobile");
                                profile_pic_url=jsonObjectUser.getString("profile_pic");

                                userIdlist.add(user_id);
                                numberlist.add(mobile);
                                profile_piclist_full.add(profile_pic_url);
                            }
                            for (int i=0;i<numberlist.size();i++){
                                System.out.println(numberlist.get(i)+"numberslist");
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    for (int i=0;i<numberlist.size();i++){
                                        if (numberlist!=null) {
                                            if (findNameByNumber(numberlist.get(i)) != null) {
                                                userIdlist_selected.add(userIdlist.get(i));
                                                contactslist.add(findNameByNumber(numberlist.get(i)));
                                                profile_piclist_selected.add(profile_piclist_full.get(i));

                                                list.setBackgroundColor(Color.WHITE);
                                                if (!contactslist.isEmpty()) {
                                                    list.setAdapter(new CustomListAdapter(getActivity(), contactslist, profile_piclist_selected,userIdlist_selected));
                                                }

                                            }

                                        }

                                    }

                                    SharedPreferences.Editor edt = pref.edit();
                                    edt.putString("cachingOn", "true");
                                    edt.commit();

                                    storeArray();
                                    for (int i = 0; i < contactslist.size(); i++) {
                                        System.out.println(contactslist.get(i));
                                    }
//                                        }
                                    hidepDialog();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError error) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                 Toast.makeText(getActivity(), "Error: " + error.getMessage().toString(), Toast.LENGTH_LONG).show();
                                hidepDialog();
                            }
                        });
                    }
                }){
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

    public String findNameByNumber(String num){
        String res = null;
        try {
            ContentResolver resolver = getActivity().getContentResolver();
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(num));
            Cursor c = resolver.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);

            if (c != null) { // cursor not null means number is found contactsTable
                if (c.moveToFirst()) {   // so now find the contact Name
                    res = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                }
                c.close();
            }
        } catch (Exception ex) {
        /* Ignore */
        }
        return res;
    }

    @Override
    public void onRefresh() {
            fetchdata();
    }

    private void fetchdata(){
        // showing refresh animation before making http call
        swipeRefreshLayout.setRefreshing(false);

    }

//    private class BackgroundTask extends AsyncTask<String, String, String> {
//        ListView listView;
//        Activity mContext;
//        private ProgressDialog dialog= new ProgressDialog(getActivity());
//
//        public  BackgroundTask(Activity context, ListView list) {
//            this.listView=list;
//            this.mContext=context;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            Toast.makeText(getActivity(),"Loading..",Toast.LENGTH_LONG).show();
//            dialog.setMessage("Please wait.");
//            dialog.show();
//        }
//
//
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            StringRequest stringRequest = new StringRequest(Request.Method.GET, USERSLIST_URL,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            try {
//                                JSONArray mainObject = new JSONArray(response.toString());
//                                System.out.println(response.toString());
//                                for (int i = 0; i < mainObject.length(); i++) {
//                                    JSONObject jsonObjectUser = mainObject.getJSONObject(i);
//                                    user_id=jsonObjectUser.getString("id");
//                                    mobile = jsonObjectUser.getString("mobile");
//                                    profile_pic_url=jsonObjectUser.getString("profile_pic");
//
//                                    userIdlist.add(user_id);
//                                    numberlist.add(mobile);
//                                    profile_piclist_full.add(profile_pic_url);
//                                }
//                                for (int i=0;i<numberlist.size();i++){
//                                    System.out.println(numberlist.get(i));
//                                }
//                                getActivity().runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//
//                                        for (int i=0;i<numberlist.size();i++){
//                                            if (numberlist!=null) {
//                                                if (findNameByNumber(numberlist.get(i)) != null) {
//                                                    userIdlist_selected.add(userIdlist.get(i));
//                                                    contactslist.add(findNameByNumber(numberlist.get(i)));
//                                                    profile_piclist_selected.add(profile_piclist_full.get(i));
//
//                                                    list.setBackgroundColor(Color.WHITE);
//                                                    if (!contactslist.isEmpty()) {
//                                                        list.setAdapter(new CustomListAdapter(getActivity(), contactslist, profile_piclist_selected,userIdlist_selected));
//                                                    }
//
//                                                }
//
//                                            }
//
//                                        }
//                                        for (int i = 0; i < contactslist.size(); i++) {
//                                            System.out.println(contactslist.get(i));
//                                        }
////                                        }
//                                    }
//                                });
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(final VolleyError error) {
//                            getActivity().runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(getActivity(),"error"+error.toString(),Toast.LENGTH_LONG).show();
//                                }
//                            });
//                        }
//                    }){
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    Map<String, String> headers = new HashMap<>();
//                    headers.put("Client-Service", "frontend-client");
//                    headers.put("Auth-Key", "naXw7ONz1zd92Y3b4dd363cyn4WpqA9o");
//                    return headers;
//                }
//            };
//
//            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//            requestQueue.add(stringRequest);
//
//            return null;
//        }
//        @Override
//        protected void onPostExecute(String result) {
//            if (dialog.isShowing()) {
//                dialog.dismiss();
//
//
//            }
//        }
//    }
}
