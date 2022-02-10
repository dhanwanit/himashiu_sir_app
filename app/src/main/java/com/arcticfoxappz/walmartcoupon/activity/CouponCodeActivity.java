package com.arcticfoxappz.walmartcoupon.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.arcticfoxappz.walmartcoupon.R;

import java.util.Random;

public class CouponCodeActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView descTV,CcodeTV,copyCodeTV,wedTV;
    private ImageView rateImg,back_arrow;
    private ClipboardManager clipboard;
    private String couponCode;
    private LinearLayout banner_container;
    Button btn;
    private InterstitialAd mInterstitialAd;
    private AdView adView;
    private AdLoader adLoader;
    private boolean adLoaded = false;
    TemplateView template;
    private RewardedVideoAd AdMobrewardedVideoAd;
    private String AdId="ca-app-pub-3940256099942544/5224354917";
    private String status;
    TextView peopletxt, liketxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // loadRewardedVideoAd();
        blinkTextView();
        SetupUI();
        //NativeAds();
        String peopleused="People Used";
        String likes="Likes";
        Random r = new Random();
        int result = r.nextInt(500 -50 ) + 50 ;
        int result2 = r.nextInt(5000 -2500 ) + 50 ;
        peopletxt.setText(String.valueOf(result2 + " " + peopleused));
        liketxt.setText(String.valueOf(result+ " " + likes));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("show_ads");
        DatabaseReference myRef1 = database.getReference("interstitial_ads");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                status = dataSnapshot.getValue(String.class);
                if ( status != null && status.equalsIgnoreCase("yes")){
                   Admob();
                    // NativeAds();
                    // loadRewardedVideoAd(); // Log.e("sanjfirebase123", ""+status);  }else {
                    // Log.e("sanjfirebase1234", ""+status);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("firebase", "Failed to read value.", error.toException());
            }
        });

        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String status = snapshot.getValue(String.class);
                if ( status != null && status.equalsIgnoreCase("yes")){
                    InterstitialAds();
                    // Log.e("sanjfirebase123", ""+status);
                }else {
                    // Log.e("sanjfirebase1234", ""+status);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("firebase", "Failed to read value.", error.toException());

            }
        });

    }

    private void InterstitialAds(){
        // MobileAds.initialize(this, "ca-app-pub-4761500786576152~8215465788");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.ads_interstitial));
        AdRequest request = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(request);
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        });
    }

    private void Admob(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(( adRequest));

       // MobileAds.initialize(this, "ca-app-pub-4761500786576152~8215465788");
//        interstitialAd = new InterstitialAd(this);
//        interstitialAd.setAdUnitId(getString(R.string.ads_interstitial));
//        AdRequest request = new AdRequest.Builder().build();
//        interstitialAd.loadAd(request);
//        interstitialAd.setAdListener(new AdListener() {
//            public void onAdLoaded() {
//                if (interstitialAd.isLoaded()) {
//                    interstitialAd.show();
//                }
//            }
//        });
    }

    private void NativeAds() {
        adLoader = new AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110").forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {

            private ColorDrawable background;@Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {

                NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(String.valueOf(background)).build();

               // template = findViewById(R.id.nativeTemplateView);
                template.setStyles(styles);
                template.setNativeAd(unifiedNativeAd);
                adLoaded = true;
                // Showing a simple Toast message to user when Native an ad is Loaded and ready to show
                //   Toast.makeText(CouponCodeActivity.this, "Native Ad is loaded ,now you can show the native ad  ", Toast.LENGTH_LONG).show();
            }

        }).build();

        AdRequest adRequest = new AdRequest.Builder().build();
        adLoader.loadAd(adRequest);
        // Toast.makeText(CouponCodeActivity.this, "Native Ad is loading ", Toast.LENGTH_LONG).show();
        if (adLoaded) {
            template.setVisibility(View.VISIBLE);
            // Showing a simple Toast message to user when an Native ad is shown to the user
            //   Toast.makeText(CouponCodeActivity.this, "Native Ad  is loaded and Now showing ad  ", Toast.LENGTH_LONG).show();

        }
    }


    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
    private void SetupUI(){

       /* AudienceNetworkAds.initialize(this);
        adView = new AdView(this,"160487525200986_160490891867316", AdSize.BANNER_HEIGHT_50);
        banner_container = (LinearLayout)findViewById(R.id.banner_container);
        banner_container.addView(adView);*/

        descTV = (TextView)findViewById(R.id.descTV);
        CcodeTV = (TextView)findViewById(R.id.CcodeTV);
        peopletxt = (TextView)findViewById(R.id.peopletxt);
        liketxt = (TextView)findViewById(R.id.liketxt);
        copyCodeTV = (TextView)findViewById(R.id.copyCodeTV);
        back_arrow = (ImageView) findViewById(R.id.back_arrow);
        btn = (Button) findViewById(R.id.btn);
        copyCodeTV.setOnClickListener(this);
        back_arrow.setOnClickListener(this);
        btn.setOnClickListener(this);

        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        Intent i= getIntent();
        couponCode = i.getStringExtra("code");
        CcodeTV.setText(couponCode);
        descTV.setText(""+i.getStringExtra("desc"));
        String url = (""+i.getStringExtra("url"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.copyCodeTV:
                ClipData clip = ClipData.newPlainText("simple text",couponCode);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, "Code Copied", Toast.LENGTH_SHORT).show();
                break;

            case R.id.back_arrow:
                Intent intent = new Intent(this,HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;

            case R.id.btn:
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.uae.jobsindubai"); // missing 'http://' will cause crashed
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
               // showRewardedVideoAd();
                break;
        }
    }

    public void loadRewardedVideoAd()
    {
        // initializing RewardedVideoAd Object
        // RewardedVideoAd  Constructor Takes Context as its
        // Argument
        AdMobrewardedVideoAd
                = MobileAds.getRewardedVideoAdInstance(this);

        // Rewarded Video Ad Listener
        AdMobrewardedVideoAd.setRewardedVideoAdListener(
                new RewardedVideoAdListener() {
                    @Override
                    public void onRewardedVideoAdLoaded()
                    {

                    }

                    @Override
                    public void onRewardedVideoAdOpened()
                    {

                    }

                    @Override
                    public void onRewardedVideoStarted()
                    {

                    }

                    @Override
                    public void onRewardedVideoAdClosed()
                    {
                        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.uae.jobsindubai"); // missing 'http://' will cause crashed
                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(i);
                    }

                    @Override
                    public void onRewarded(com.google.android.gms.ads.reward.RewardItem rewardItem) {
                    }

                    @Override
                    public void
                    onRewardedVideoAdLeftApplication()
                    {
                    }

                    @Override
                    public void onRewardedVideoAdFailedToLoad(
                            int i)
                    {

                    }

                    @Override
                    public void onRewardedVideoCompleted()
                    {

                    }
                });

        // Loading Rewarded Video Ad
        AdMobrewardedVideoAd.loadAd(
                AdId, new AdRequest.Builder().build());
    }

    public void showRewardedVideoAd()
    {
        // Checking If Ad is Loaded or Not
        if (AdMobrewardedVideoAd.isLoaded()) {
            // showing Video Ad
            AdMobrewardedVideoAd.show();
        }
        else {

            // Loading Rewarded Video Ad
            AdMobrewardedVideoAd.loadAd(
                    AdId, new AdRequest.Builder().build());
        }
    }
    private void blinkTextView() {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int timeToBlink = 500;
                try{Thread.sleep(timeToBlink);}catch (Exception e) {}
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        copyCodeTV = findViewById(R.id.copyCodeTV);
                        if(copyCodeTV.getVisibility() == View.VISIBLE) {
                            copyCodeTV.setVisibility(View.INVISIBLE);
                        } else {
                            copyCodeTV.setVisibility(View.VISIBLE);
                        }
                        blinkTextView();
                    }
                });
            }
        }).start();
    }
}

