package com.vad.qrscanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.vad.qrscanner.domain.FileUtils;
import com.vad.qrscanner.domain.QRTools;
import com.vad.qrscanner.fragments.ResultFragment;
import com.vad.qrscanner.fragments.menu.MenuFragment;
import com.vad.qrscanner.navigation.CustomAction;
import com.vad.qrscanner.navigation.HasCustomAction;
import com.vad.qrscanner.navigation.HasCustomActions;
import com.vad.qrscanner.navigation.HasCustomTitle;
import com.vad.qrscanner.navigation.Navigator;
import com.yandex.mobile.ads.banner.BannerAdSize;
import com.yandex.mobile.ads.banner.BannerAdView;
import com.yandex.mobile.ads.common.AdError;
import com.yandex.mobile.ads.common.AdRequest;
import com.yandex.mobile.ads.common.AdRequestConfiguration;
import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.common.ImpressionData;
import com.yandex.mobile.ads.interstitial.InterstitialAd;
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener;
import com.yandex.mobile.ads.interstitial.InterstitialAdLoadListener;
import com.yandex.mobile.ads.interstitial.InterstitialAdLoader;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements Navigator {

    private Toolbar toolbar;
    private BannerAdView mBanner;
    @Nullable
    private InterstitialAd mInterstitialAd = null;
    @Nullable
    private InterstitialAdLoader mInterstitialAdLoader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        mBanner = (BannerAdView) findViewById(R.id.adView);
        //demo-yandex
        mBanner.setAdUnitId("R-M-2167912-1");
        mBanner.setAdSize(getAdSize());
        AdRequest adRequest = new AdRequest.Builder().build();
        mBanner.loadAd(adRequest);

        mInterstitialAdLoader = new InterstitialAdLoader(this);
        mInterstitialAdLoader.setAdLoadListener(new InterstitialAdLoadListener() {
            @Override
            public void onAdLoaded(@NonNull final InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
                // The ad was loaded successfully. Now you can show loaded ad.
            }

            @Override
            public void onAdFailedToLoad(@NonNull final AdRequestError adRequestError) {
                // Ad failed to load with AdRequestError.
                // Attempting to load a new ad from the onAdFailedToLoad() method is strongly discouraged.
            }
        });
        loadInterstitialAd();

        Common common = Common.getInstance();
        common.mGetContent = mGetContent;

        getSupportFragmentManager().registerFragmentLifecycleCallbacks(fragmentListener, false);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_replacer, new MenuFragment()).commit();

    }

    private BannerAdSize getAdSize() {
        final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        // Calculate the width of the ad, taking into account the padding in the ad container.
        int adWidthPixels = mBanner.getWidth();
        if (adWidthPixels == 0) {
            // If the ad hasn't been laid out, default to the full screen width
            adWidthPixels = displayMetrics.widthPixels;
        }
        final int adWidth = Math.round(adWidthPixels / displayMetrics.density);

        return BannerAdSize.stickySize(this, adWidth);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        updateOnUI();
        return true;
    }

    private final FragmentManager.FragmentLifecycleCallbacks fragmentListener = new FragmentManager.FragmentLifecycleCallbacks() {
        @Override
        public void onFragmentViewCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull View v, @Nullable Bundle savedInstanceState) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState);
            updateOnUI();
        }

        @Override
        public void onFragmentViewDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
            super.onFragmentViewDestroyed(fm, f);
            updateOnUI();
        }
    };


    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.frame_replacer);
    }

    private void updateOnUI() {
        Fragment fragment = getCurrentFragment();

        if (fragment instanceof HasCustomTitle) {
            toolbar.setTitle(((HasCustomTitle) fragment).getTitle());
        }
        toolbar.getMenu().clear();
        if (fragment instanceof HasCustomAction) {
            createCustomToolbarAction(((HasCustomAction)fragment).setCustomAction(this));
        } else if (fragment instanceof HasCustomActions) {
            List<CustomAction> customActionList = ((HasCustomActions)fragment).setCustomAction(this);
            for (CustomAction a: customActionList) {
                createCustomToolbarAction(a);
            }
        }

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(getSupportFragmentManager().getBackStackEntryCount() > 0);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @SuppressLint("ResourceType")
    private void createCustomToolbarAction(CustomAction customActionFragment) {
        Drawable iconDrawable = DrawableCompat.wrap(Objects.requireNonNull(ContextCompat.getDrawable(this, customActionFragment.getIcon())));
        iconDrawable.setTint(Color.WHITE);
        MenuItem menuItem = toolbar.getMenu().add("");
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuItem.setIcon(iconDrawable);
        menuItem.setOnMenuItemClickListener(menuItem1 -> {
            customActionFragment.getAction().run();
            return true;
        });
    }

    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    String s = FileUtils.getPath(uri, this);
                    startResult(s);
                }
            });

    private void startResult(String content) {
        Bundle args = new Bundle();
        args.putString("temp_image", content);
        args.putString("content", QRTools.decodeQRImage(content));
        Fragment fragmentResult = new ResultFragment();
        fragmentResult.setArguments(args);
        getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.frame_replacer, fragmentResult).commit();
    }

    @Override
    public void startFragment(@NonNull Fragment fragment) {

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .add(R.id.frame_replacer, fragment)
                .commit();
    }

    @Override
    public void hide() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void loadInterstitialAd() {
        if (mInterstitialAdLoader != null ) {
            final AdRequestConfiguration adRequestConfiguration =
                    new AdRequestConfiguration.Builder("R-M-2167912-3").build();
            mInterstitialAdLoader.loadAd(adRequestConfiguration);
        }
    }

    public void showAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd.setAdEventListener(new InterstitialAdEventListener() {
                @Override
                public void onAdShown() {
                    // Called when ad is shown.
                }

                @Override
                public void onAdFailedToShow(@NonNull final AdError adError) {
                    // Called when an InterstitialAd failed to show.
                }

                @Override
                public void onAdDismissed() {
                    // Called when ad is dismissed.
                    // Clean resources after Ad dismissed
                    if (mInterstitialAd != null) {
                        destroyInterstitialAd();
                    }

                    // Now you can preload the next interstitial ad.
                    loadInterstitialAd();
                }

                @Override
                public void onAdClicked() {
                    // Called when a click is recorded for an ad.
                }

                @Override
                public void onAdImpression(@Nullable final ImpressionData impressionData) {
                    // Called when an impression is recorded for an ad.
                }
            });
            mInterstitialAd.show(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mInterstitialAdLoader != null) {
            mInterstitialAdLoader.setAdLoadListener(null);
            mInterstitialAdLoader = null;
        }
        destroyInterstitialAd();
    }

    private void destroyInterstitialAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd.setAdEventListener(null);
            mInterstitialAd = null;
        }
    }

}