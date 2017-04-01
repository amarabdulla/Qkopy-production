package com.example.computer.qkopy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by computer on 2/21/2017.
 */

public class UserProfilePage extends Activity {
    private static final String PROFILEVIEW_URL = "http://museon.net/qkopy-beta1/auth/profile";
    private String name,mobile,profile_pic;
    private ImageLoader imageLoader;
    private ImageView imageView;
    private TextView number_text,name_text;
    private  String id;
    static Boolean ab= false;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_page);
        number_text=(TextView)findViewById(R.id.number);
        name_text=(TextView)findViewById(R.id.user_name);
        imageLoader= new ImageLoader(this);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        imageView=(ImageView)findViewById(R.id.navigation_drawer_user_account_picture_profile);
        Intent intent = getIntent();
        id = intent.getExtras().getString("user_id");
        SharedPreferences pref = getSharedPreferences("MyFile",0);
        Gson gson = new Gson();
        String json = pref.getString("contacts_list", null);
        String json2 = pref.getString("profile_piclist_selected", null);
        String json3 = pref.getString("userIdlist_selected", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<String> arrayList = gson.fromJson(json, type);
        ArrayList<String> arrayList2 = gson.fromJson(json2, type);
        ArrayList<String> arrayList3 = gson.fromJson(json3, type);
//        String id = pref.getString("facebook_id", "empty");
//        System.out.println(id+"NOt this time baby!");
        for (int i=0; i< arrayList.size();i++){
            System.out.println(arrayList.get(i)+"NOt this time baby!");
        }

//        new BackgroundTask(UserProfilePage.this).execute();
        makeJsonRequestPost();

    }

//    @Override
//    public void onPermissionsGranted(int requestCode) {
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.refresh) {
            //Do something
            return true;
        } else if (id == R.id.settings) {
            //Do something
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void makeJsonRequestPost() {
        showpDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, PROFILEVIEW_URL + "/" + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject mainObject = new JSONObject(response.toString());
                            System.out.println(response.toString());
                            name = mainObject.getString("name");
                            mobile = mainObject.getString("mobile");
                            profile_pic = mainObject.getString("profile_pic");

                            UserProfilePage.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    name_text.setText(name);
                                    number_text.setText(mobile);
                                    imageLoader.DisplayImage(profile_pic, imageView);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        hidepDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError error) {
                        UserProfilePage.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage().toString(), Toast.LENGTH_LONG).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
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
    private class BackgroundTask extends AsyncTask<String, String, String> {
        private ProgressDialog dialog= new ProgressDialog(getApplicationContext());

        public BackgroundTask(UserProfilePage activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(),"Loading..",Toast.LENGTH_LONG).show();
            dialog.setMessage("Please wait.");
            dialog.show();
        }



        @Override
        protected String doInBackground(String... params) {
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            StringRequest stringRequest = new StringRequest(Request.Method.GET, PROFILEVIEW_URL+"/"+id,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject mainObject = new JSONObject(response.toString());
                                System.out.println(response.toString());
//                                    JSONObject jsonObjectUser = mainObject.getJSONObject(i);
                                    name=mainObject.getString("name");
                                    mobile=mainObject.getString("mobile");
                                    profile_pic=mainObject.getString("profile_pic");

                                UserProfilePage.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        name_text.setText(name);
                                        number_text.setText(mobile);
                                        imageLoader.DisplayImage(profile_pic,imageView);
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
                            UserProfilePage.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),"error"+error.toString(),Toast.LENGTH_LONG).show();
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

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);

            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            if (dialog.isShowing()) {
                dialog.dismiss();


            }
        }
    }
}
