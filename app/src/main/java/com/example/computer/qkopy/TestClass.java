package com.example.computer.qkopy;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by computer on 4/1/2017.
 */

public class TestClass extends Activity {

    LatLng latLng = new LatLng(35.0116363, 135.7680294);
    GoogleMap mMap ;
    ImageLoader imageLoader;
    String latEiffelTower = "48.858235";
    String lngEiffelTower = "2.294571";
    String url = "http://maps.google.com/maps/api/staticmap?center=" + latEiffelTower + "," + lngEiffelTower + "&zoom=15&size=400x250&sensor=false";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        imageLoader = new ImageLoader(TestClass.this);
        System.out.println(url);
        final ImageView mapPreview = (ImageView) findViewById(R.id.imageView);
        imageLoader.DisplayImage(url,mapPreview);
    }
}
