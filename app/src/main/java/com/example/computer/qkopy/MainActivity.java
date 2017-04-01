package com.example.computer.qkopy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.digits.sdk.android.Digits;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "wWESpiJBQ9W5TWFROPm4y3yCG";
    private static final String TWITTER_SECRET = "eYBK5bAG15GCI7o8zJImudxKG3HBRFHSD9yd7KZmcqlAp8GviB";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());
        setContentView(R.layout.activity_main);
    }
}
