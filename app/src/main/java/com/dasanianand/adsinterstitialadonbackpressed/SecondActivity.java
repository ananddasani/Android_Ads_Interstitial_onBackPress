package com.dasanianand.adsinterstitialadonbackpressed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.mediation.MediationInterscrollerAd;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "MyAds";
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //ads will be shown and then user wil be moved to MainActivity
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd != null) {

                    mInterstitialAd.show(SecondActivity.this);
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            startActivity(new Intent(SecondActivity.this, MainActivity.class));
                        }
                    });
                } else
                    startActivity(new Intent(SecondActivity.this, MainActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        //Keep Ad Loaded
        Log.d(TAG, "onBackPressed Second Activity");

        if (mInterstitialAd != null) {

            mInterstitialAd.show(SecondActivity.this);
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();

                    mInterstitialAd = null;
                    SecondActivity.super.onBackPressed();
                }
            });

        } else
            super.onBackPressed();
    }

    //for keep ad always loaded
    @Override
    protected void onResume() {
        super.onResume();

        //load ad
        Log.d(TAG, "Loading Ad...");
        loadInterstitialAd();
    }

    //load ads without call backs included
    public void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, getString(R.string.interstitial_video), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
                Log.d(TAG, "Ad loaded :)");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                mInterstitialAd = null;

                //again reload the ad after waiting for 5 sec
                new CountDownTimer(5000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        Log.d(TAG, millisUntilFinished / 1000 + " sec");
                    }

                    @Override
                    public void onFinish() {
                        loadInterstitialAd();
                    }
                }.start();
            }
        });
    }
}
