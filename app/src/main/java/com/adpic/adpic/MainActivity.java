package com.adpic.adpic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.github.jorgecastilloprz.listeners.FABProgressListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Media;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.MediaService;
import com.twitter.sdk.android.core.services.StatusesService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.fabric.sdk.android.Fabric;
import retrofit.mime.TypedFile;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        HomeFragment.OnFragmentInteractionListener,
        ChallengeFragment.OnFragmentInteractionListener,
        ChallengeFragmentBase.OnFragmentInteractionListener,
        NearbyChallengeFragment.OnFragmentInteractionListener,
        MakeNewPostFragment.OnFragmentInteractionListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        FABProgressListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "iOwwNXFbIIlTFsrCMvMySlqWj";
    private static final String TWITTER_SECRET = "LIWtkr3ia7SDbxNXnujGROkbEP65TKR1UW7k1Y05N3iRS5hbLQ";


    Fragment activeFragment;
    String TAG = "MainActivity";
    GoogleApiClient mGoogleApiClient;
    NavigationView navigationView;
    View bottomNavBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(com.adpic.adpic.R.layout.activity_main);

        Utils.init();

        Toolbar toolbar = (Toolbar) findViewById(com.adpic.adpic.R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(com.adpic.adpic.R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, com.adpic.adpic.R.string.navigation_drawer_open, com.adpic.adpic.R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(com.adpic.adpic.R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


        //set default to home
        navigationView.setCheckedItem(com.adpic.adpic.R.id.nav_home);
        HomeFragment fragment = new HomeFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(com.adpic.adpic.R.id.container, fragment);
        fragmentTransaction.commit();
        activeFragment = fragment;

        FABProgressCircle fabProgressCircle = (FABProgressCircle) findViewById(R.id.fabProgressCircle);
        fabProgressCircle.attachListener(this);

        bottomNavBar = findViewById(R.id.bottom_nav_bar);
        BtmNavBarOnClickListener bottomNavBarClickListener = new BtmNavBarOnClickListener();
        ImageButton bnbHome = (ImageButton) bottomNavBar.findViewById(R.id.btm_nav_bar_home);
        bnbHome.setOnClickListener(bottomNavBarClickListener);
        ImageButton bnbChallenge = (ImageButton) bottomNavBar.findViewById(R.id.btm_nav_bar_challenge);
        bnbChallenge.setOnClickListener(bottomNavBarClickListener);
        ImageButton bnbSnap = (ImageButton) bottomNavBar.findViewById(R.id.btm_nav_bar_snap);
        bnbSnap.setOnClickListener(bottomNavBarClickListener);
        ImageButton bnbProfile = (ImageButton) bottomNavBar.findViewById(R.id.btm_nav_bar_profile);
        bnbProfile.setOnClickListener(bottomNavBarClickListener);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(com.adpic.adpic.R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.adpic.adpic.R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == com.adpic.adpic.R.id.action_settings) {
                Intent pickIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickIntent, com.adpic.adpic.Constants.SELECT_PIC_REQUEST_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateBottomNavigationBar() {
        LinearLayout navBarLayout = (LinearLayout) bottomNavBar.findViewById(R.id.linearLayout);
        for (int i = 0; i < navBarLayout.getChildCount(); i++) {
            LinearLayout buttonLayout = (LinearLayout) navBarLayout.getChildAt(i);
            buttonLayout.setBackground(new ColorDrawable(getResources().getColor(R.color.white)));
        }

        if (activeFragment instanceof HomeFragment) {
            navBarLayout.getChildAt(0).setBackground(new ColorDrawable(getResources().getColor(R.color.ColorPrimary)));
        } else if (activeFragment instanceof ChallengeFragment){
            navBarLayout.getChildAt(1).setBackground(new ColorDrawable(getResources().getColor(R.color.ColorPrimary)));
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();

        //Checking if the item is in checked state or not, if not make it in checked state
        if(menuItem.isChecked()) menuItem.setChecked(false);
        else menuItem.setChecked(true);

        if (id == com.adpic.adpic.R.id.nav_home) {
            getSupportActionBar().setTitle(com.adpic.adpic.R.string.app_name);
            HomeFragment fragment = new HomeFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(com.adpic.adpic.R.id.container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            activeFragment = fragment;
        } else if (id == com.adpic.adpic.R.id.nav_challenge) {
            getSupportActionBar().setTitle(com.adpic.adpic.R.string.challenge);
            ChallengeFragment fragment = new ChallengeFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(com.adpic.adpic.R.id.container,fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            activeFragment = fragment;
        } else if (id == com.adpic.adpic.R.id.nav_following) {
            getSupportActionBar().setTitle(R.string.following);
//            SettingsFragment fragment = new SettingsFragment();
//            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.container,fragment);
//            fragmentTransaction.commit();
        } else if (id == com.adpic.adpic.R.id.nav_store) {
            getSupportActionBar().setTitle(R.string.store);
            StoreFragment fragment = new StoreFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            activeFragment = fragment;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(com.adpic.adpic.R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(int fragmentId) {
        System.out.println(fragmentId);
        activeFragment = getSupportFragmentManager().findFragmentById(fragmentId);
    }

    @Override
    public void onFragmentInteraction(HomeCardData cardData) {
        HomeFragment fragment = new HomeFragment();
        getSupportActionBar().setTitle(getString(R.string.home));
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
        activeFragment = fragment;

        if (Utils.twitterSession != null) {
            postToTwitter(cardData);
        }
    }

    private void postToTwitter(HomeCardData cardData) {
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        MediaService mediaService = twitterApiClient.getMediaService();
        final StatusesService statusesService = twitterApiClient.getStatusesService();

        final String twitterPostCaption = cardData.caption + " #apiclaunch";

        File imageFile = new File(getCacheDir(), "upload_twitter.jpeg");

        try {
            Bitmap rawBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Utils.mostRecentPhoto));
            int width = rawBitmap.getWidth() / 2;
            int height = rawBitmap.getHeight() / 2;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(rawBitmap, width, height, false);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out); // '100' is quality
            byte[] byteArray = out.toByteArray();

            imageFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(imageFile);
            fos.write(byteArray);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String mimeType = getContentResolver().getType(Utils.mostRecentPhoto);
        Log.v(TAG, "Image size is " + imageFile.length());

        TypedFile media = new TypedFile("application/octet-stream", imageFile);
        mediaService.upload(media, null, null, new Callback<Media>() {
            @Override
            public void success(Result<Media> result) {
                Log.v(TAG, "Success! Media ID: " + result.data.mediaIdString);

                statusesService.update(twitterPostCaption, null, null, null, null, null, null, null, result.data.mediaIdString, new Callback<Tweet>() {
                    @Override
                    public void success(Result<Tweet> result) {
                        long id = result.data.id;
                        Log.v(TAG, "Success! Tweet ID: " + result.data.id);
                    }

                    @Override
                    public void failure(TwitterException e) {
                        Log.v(TAG, e.toString());
                    }
                });
            }

            @Override
            public void failure(TwitterException e) {
                Log.v(TAG, e.toString());
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        System.out.println(uri);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.TAKE_PIC_REQUEST_CODE ||
                requestCode == Constants.SELECT_PIC_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Uri imageUri;
                if (requestCode == Constants.SELECT_PIC_REQUEST_CODE) {
                    imageUri = data.getData();
                    Utils.mostRecentPhoto = imageUri;
                } else {
                    imageUri = Utils.mostRecentPhoto;
                }

                Log.d("Image Location", imageUri.toString());

                MakeNewPostFragment fragment = MakeNewPostFragment.newInstance(Utils.mostRecentMerchantName,
                                                        Utils.mostRecentMerchantDistance);
                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.commitAllowingStateLoss();
                activeFragment = fragment;
                getSupportActionBar().setTitle(getString(R.string.make_new_post));
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image selection
            } else {
                // Image selection failed, advise user
            }
        } else if (resultCode == Constants.LAUNCH_UBER_REQUEST_CODE) {
            System.out.println("CAME BACK FROM UBER.");
        }

        System.out.println(activeFragment.toString());
        activeFragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        Log.v(TAG, "onActivityResult");
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        activeFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.v(TAG, "onRequestPermissionsResult");
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "Google Play Services connected!");
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Utils.setLastKnownLocation(mLastLocation);
            Log.d(TAG, "Latitude: " + Utils.getLastKnownLatitude());
            Log.d(TAG, "Longitude: " + Utils.getLastKnownLongitude());
        } else {
            Log.d(TAG, "Location is null");
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Google Play Services suspended!");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Failed to connect: Google Play Services!");
    }

    @Override
    public void onFABProgressAnimationEnd(){
        FABProgressCircle fabProgressCircle = (FABProgressCircle) findViewById(R.id.fabProgressCircle);
        Snackbar.make(fabProgressCircle, "Your Uber has arrived!", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show();
        fabProgressCircle.hide();
        FloatingActionButton uberButton = (FloatingActionButton) findViewById(R.id.uber_button);
        uberButton.setVisibility(View.GONE);
    }

    class BtmNavBarOnClickListener implements View.OnClickListener {

        private void resetAllNavBarColors() {
            LinearLayout navBarLayout = (LinearLayout) bottomNavBar.findViewById(R.id.linearLayout);
            for (int i = 0; i < navBarLayout.getChildCount(); i++) {
                LinearLayout buttonLayout = (LinearLayout) navBarLayout.getChildAt(i);
                buttonLayout.setBackground(new ColorDrawable(getResources().getColor(R.color.white)));
            }
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            resetAllNavBarColors();

            if (id == R.id.btm_nav_bar_home) {
                HomeFragment fragment = new HomeFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(com.adpic.adpic.R.id.container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                activeFragment = fragment;

                updateBottomNavigationBar();
            } else if (id == R.id.btm_nav_bar_challenge) {
                ChallengeFragment fragment = new ChallengeFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(com.adpic.adpic.R.id.container,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                activeFragment = fragment;

                updateBottomNavigationBar();
            } else if (id == R.id.btm_nav_bar_snap) {
//            SettingsFragment fragment = new SettingsFragment();
//            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.container,fragment);
//            fragmentTransaction.commit();

                LinearLayout parent = (LinearLayout) v.getParent();
                parent.setBackground(new ColorDrawable(getResources().getColor(R.color.ColorPrimary)));
            } else if (id == R.id.btm_nav_bar_profile) {
                StoreFragment fragment = new StoreFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                activeFragment = fragment;

                LinearLayout parent = (LinearLayout) v.getParent();
                parent.setBackground(new ColorDrawable(getResources().getColor(R.color.ColorPrimary)));
            }
        }
    }
}
