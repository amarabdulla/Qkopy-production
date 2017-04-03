package com.example.computer.qkopy;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.entity.mime.HttpMultipartMode;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by computer on 3/21/2017.
 */

public class PostPreviewPage extends Activity implements
        GoogleApiClient.OnConnectionFailedListener,AdapterView.OnItemSelectedListener{
    Button post;
    Button my_location;
    ImageView close;
    private Spinner spinner1;
    private EditText editText;
    List<String> list;
    String result,desc,latitude,longitude,placename;
    int status_code;
    private static final String LOG_TAG ="POSTPREVEWPAGE";
    private static final String POST_URL="http://museon.net/qkopy-beta1/Auth/postfile";
    static int PLACE_PICKER_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_preview_page);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        list = new ArrayList<String>();
        list.add("Public");
        list.add("Me");
        list.add("Contacts");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(dataAdapter);
        close=(ImageView)findViewById(R.id.close);
        editText=(EditText)findViewById(R.id.editText);
        post=(Button)findViewById(R.id.post);
        my_location=(Button)findViewById(R.id.location);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostPreviewPage.this.finish();
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"Posting..",Toast.LENGTH_SHORT).show();
                new BackgroundTask(PostPreviewPage.this).execute();
//                PostPreviewPage.this.finish();
            }
        });
        my_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder= new PlacePicker.IntentBuilder();
                Intent intent;

                try {
                    if (ContextCompat.checkSelfPermission(PostPreviewPage.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(PostPreviewPage.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                PERMISSION_REQUEST_CODE);
                    }else {
                        intent=builder.build(PostPreviewPage.this);
                        startActivityForResult(intent,PLACE_PICKER_REQUEST);
                    }

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                StringBuilder stBuilder = new StringBuilder();
                 placename = String.format("%s", place.getName());
                 latitude = String.valueOf(place.getLatLng().latitude);
                 longitude = String.valueOf(place.getLatLng().longitude);
                String address = String.format("%s", place.getAddress());
                stBuilder.append("Name: ");
                stBuilder.append(placename);
                stBuilder.append("\n");
                stBuilder.append("Latitude: ");
                stBuilder.append(latitude);
                stBuilder.append("\n");
                stBuilder.append("Logitude: ");
                stBuilder.append(longitude);
                stBuilder.append("\n");
                stBuilder.append("Address: ");
                stBuilder.append(address);
//                Toast.makeText(getApplicationContext(),stBuilder.toString()+"",Toast.LENGTH_SHORT).show();
                String s= ""+" at "+placename;
                SpannableString ss1=  new SpannableString(s);
                ss1.setSpan(new RelativeSizeSpan(1f), 0,5, 0); // set size
                ss1.setSpan(new ForegroundColorSpan(Color.RED), 0, 5, 0);// set color
                editText.setText(ss1);
//                editText.setTextSize(12);
//                editText.setText("at "+address);
            }
        }
    }
    private class BackgroundTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;

        public BackgroundTask(PostPreviewPage activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Uploading, please wait.");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
                if (status_code == 200) {
                    Toast.makeText(getApplicationContext(), "successful"
                            , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PostPreviewPage.this, SimpleTabsActivity.class);
                    intent.putExtra("ThirdTab", 3);
                    startActivity(intent);
                    PostPreviewPage.this.finish();
                } else if (status_code == 206) {
                    if (result == null) {
                        Toast.makeText(getApplicationContext(), "Something went wrong.Please try again", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_SHORT).show();
                    }

                }
            }

        }

        @Override
        protected Void doInBackground(Void... params) {
           postStatus();

            return null;
        }

    }
    private void postStatus() {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(POST_URL);
        desc=editText.getText().toString();
        httppost.setHeader("Client-Service", "frontend-client");
        httppost.setHeader("Auth-Key", "naXw7ONz1zd92Y3b4dd363cyn4WpqA9o");

        System.out.println("executing request " + httppost.getRequestLine());


        MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            try {
                mpEntity.addPart("id", new StringBody("151"));
                mpEntity.addPart("post_desc", new StringBody(desc));
                mpEntity.addPart("post_loc", new StringBody(latitude+","+longitude));
                mpEntity.addPart("privacy", new StringBody("public"));
                try {

                    mpEntity.addPart("post_time", new StringBody( getCurrentTimezoneOffset()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                httppost.setEntity(mpEntity);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


        try {
            HttpResponse response = httpclient.execute(httppost);
            Log.w("Response ", "Status line : " + response.getStatusLine().toString());
            HttpEntity entity = response.getEntity();
            String test = EntityUtils.toString(entity);
            System.out.println("string" + test);
            try {
                JSONObject jsonObj = new JSONObject(test);
                status_code = jsonObj.optInt("status");
                result = jsonObj.optString("message");

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            result=response.getStatusLine().getReasonPhrase();
//            status_code=response.getStatusLine().getStatusCode();
            System.out.println("reeeee" + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getCurrentTimezoneOffset() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        TimeZone tz = TimeZone.getDefault();
        Calendar cal = GregorianCalendar.getInstance(tz);
        int offsetInMillis = tz.getOffset(cal.getTimeInMillis());

        String offset = String.format("%02d:%02d", Math.abs(offsetInMillis / 3600000), Math.abs((offsetInMillis / 60000) % 60));
        offset = formattedDate+" "+"GMT"+(offsetInMillis >= 0 ? "+" : "-") + offset;


        System.out.print("The time set is here"+offset);
        return offset;

    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PlacePicker.IntentBuilder builder= new PlacePicker.IntentBuilder();
                    Intent intent;
                    try {
                        intent=builder.build(PostPreviewPage.this);
                        startActivityForResult(intent,PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }

                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(),list.get(position) ,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
