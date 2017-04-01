package com.example.computer.qkopy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import io.fabric.sdk.android.Fabric;

/**
 * Created by computer on 3/15/2017.
 */

public class WelcomeScreen extends Activity {
    private TextView terms_and_condition,desc_one,desc_two,heading;
//    private Button get_started;
    private static final String TWITTER_KEY = "1MmnyN1sMLmrObIcNQs095bAk";
    private static final String TWITTER_SECRET = "VjzvFnzpVDtSeSa0qz2HDFQa6oOSBoechWsRvbyF3XTqed2vDm";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);
//        get_started=(Button)findViewById(R.id.start);
        terms_and_condition=(TextView)findViewById(R.id.terms_and_condition);
        desc_one=(TextView)findViewById(R.id.desc_one);
        desc_two=(TextView)findViewById(R.id.desc_two);
        heading=(TextView)findViewById(R.id.heading);
        String htmlString="By continuing you accept the <u>Terms and Condition</u>";
        terms_and_condition.setText(Html.fromHtml(htmlString));
        Typeface custom_font_pro_regular = Typeface.createFromAsset(getAssets(),  "fonts/proximanova-regular.otf");
        Typeface custom_font_pro_semibold = Typeface.createFromAsset(getAssets(),  "fonts/proximanova-semibold.otf");
        terms_and_condition.setTypeface(custom_font_pro_regular);
        heading.setTypeface(custom_font_pro_semibold);
        desc_one.setTypeface(custom_font_pro_regular);
        desc_two.setTypeface(custom_font_pro_regular);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());
        DigitsAuthButton digitsButton = (DigitsAuthButton) findViewById(R.id.start);
        digitsButton.setText("Get Started");
        digitsButton.setBackgroundColor(getResources().getColor(R.color.colorButtonSubmit));
        digitsButton.setAuthTheme(R.style.CustomDigitsTheme);
        digitsButton.setEnabled(true);
//        digitsButton.setAlpha(0.5f);
        digitsButton.setCallback(new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                // TODO: associate the session userID with your user model

//                Toast.makeText(getApplicationContext(), "Authentication successful for "
//                        + phoneNumber, Toast.LENGTH_LONG).show();
//                number = phoneNumber;

                Intent intent=new Intent(WelcomeScreen.this,SimpleTabsActivity.class);
                startActivity(intent);

//                Intent intent=new Intent(WelcomeScreen.this,PostPreviewPage.class);
//                startActivity(intent);


//                new SignUpPage.BackgroundTask(SignUpPage.this).execute();
            }

            @Override
            public void failure(DigitsException exception) {
                Log.d("Digits", "Sign in with Digits failure", exception);
            }
        });
        terms_and_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent= new Intent(WelcomeScreen.this,TermsAndCondition.class);
                startActivity(intent);
            }
        });
//        get_started.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent= new Intent(WelcomeScreen.this,AboutMe.class);
//                startActivity(intent);
//            }
//        });
    }
}
