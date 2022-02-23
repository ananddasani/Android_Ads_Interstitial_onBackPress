package com.dasanianand.adsinterstitialadonbackpressed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class MainActivity extends AppCompatActivity {

    String TAG = "MyAds";
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show the ad if it is loaded
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(MainActivity.this);
                } else {
                    Toast.makeText(MainActivity.this, "Sending To Next Screen Without Ads :(", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, SecondActivity.class));
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "OnResume");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.d(TAG, "Loading Ad...");
                loadInterstitialAd();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }


    private void loadInterstitialAd() {
        //Make a ad request
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(MainActivity.this, getString(R.string.interstitial_video), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                mInterstitialAd = interstitialAd;
                Log.d(TAG, "Ad Loaded First Activity");


                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        Log.d(TAG, "Ad failed to load");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        Log.d(TAG, "Ad was shown");
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        Log.d(TAG, "The ad was dismissed");
                        //Main code goes here
                        startActivity(new Intent(MainActivity.this, SecondActivity.class));
                    }
                });

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd = null;

                //re load the ad after some time
                new CountDownTimer(5000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        Log.d(TAG, millisUntilFinished / 1000 + " sec");
                    }

                    @Override
                    public void onFinish() {
                        //reload the ad after wait of 5 sec
                        loadInterstitialAd();
                    }
                }.start();
            }
        });
    }
}
