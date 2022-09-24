# Android_Ads_Interstitial_onBackPress
Showing Full Screen Ad on back to main activity

This topic is a part of [My Complete Andorid Course](https://github.com/ananddasani/Android_Apps)

# Code

#### MainActivity.java
```
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
```

#### SecondActivity.java
```
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
```

# App Highlight

![Ads Interstitial onBackPress App2](https://user-images.githubusercontent.com/74413402/192094331-87398de8-277d-43e0-b5d7-110fd501a5d5.png)
![Ads Interstitial onBackPress App3](https://user-images.githubusercontent.com/74413402/192094336-385d8ca3-5965-49c5-bf49-099d99afbc34.png)
![Ads Interstitial onBackPress App4](https://user-images.githubusercontent.com/74413402/192094338-e2ff13d4-9d81-4bf7-ad53-7a9d999c43bb.png)
![Ads Interstitial onBackPress App5](https://user-images.githubusercontent.com/74413402/192094340-e2c22f62-a881-4f4a-9107-996d263dd5c4.png)
![Ads Interstitial onBackPress Code](https://user-images.githubusercontent.com/74413402/192094345-88e2b3e5-e001-422b-8996-06b99f426289.png)
![Ads Interstitial onBackPress App1](https://user-images.githubusercontent.com/74413402/192094346-f9ddd8ac-eec2-4528-812a-742e5ec4fa18.png)

