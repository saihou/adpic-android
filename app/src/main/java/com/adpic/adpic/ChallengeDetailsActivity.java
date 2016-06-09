package com.adpic.adpic;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cocosw.bottomsheet.BottomSheet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FlipInRightYAnimator;

public class ChallengeDetailsActivity extends AppCompatActivity {

    private ArrayList<ChallengeCardData> galleryData = new ArrayList<ChallengeCardData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Utils.mostRecentChallengeClicked != null) {
            ChallengeCardData data = Utils.lookupChallenge();

            getSupportActionBar().setTitle(data.getMerchantName());
            ImageView actionBarBackground = (ImageView) findViewById(R.id.action_bar_image);
            NestedScrollView nestedScrollView = (NestedScrollView) findViewById(R.id.nested_scroll_view);
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snapPicture();
                }
            });

            RelativeLayout cv = (RelativeLayout) findViewById(R.id.challenge_card);
            TextView merchantName = (TextView) findViewById(R.id.merchant_name);
            TextView challengeDuration = (TextView) findViewById(R.id.challenge_duration);
            ImageView picture = (ImageView) findViewById(R.id.picture);
            TextView challengeDistance = (TextView) findViewById(R.id.challenge_distance);
            TextView challengeParticipants = (TextView) findViewById(R.id.challenge_participants);
            TextView  caption = (TextView) findViewById(R.id.caption);
            Button joinChallenge = (Button) findViewById(R.id.challenge_join);
            Button viewChallengeDetails = (Button) findViewById(R.id.challenge_details);
            Button uberRequest = (Button) findViewById(R.id.challenge_uber);

            merchantName.setText(data.getMerchantName());
            challengeDuration.setText(data.getTime());
            challengeDistance.setText(data.getChallengeDistance());
//            challengeParticipants.setText(data.getParticipants());
            caption.setText(data.getCaption());

            try {
                int pictureInt = Integer.parseInt(data.getPicture());
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), pictureInt);
                int height = bitmap.getHeight();
                int width = bitmap.getWidth();
                int scale = 2;
                if (width > 800) {
                    height = height / scale;
                    width = width / scale;
                }
                Bitmap scaledBitmap = Utils.getCroppedBitmapRoundRect(getApplicationContext(), Bitmap.createScaledBitmap(bitmap, width, height, false));
//                picture.setImageBitmap(scaledBitmap);
                actionBarBackground.setImageBitmap(scaledBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

            RecyclerView gallery = (RecyclerView) findViewById(R.id.gallery_recycler);
            createPlaceholderDataForGallery();
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            gallery.setLayoutManager(layoutManager);
            ChallengeDetailsGalleryCardAdapter adapter = new ChallengeDetailsGalleryCardAdapter(galleryData);
            AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
            final ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
            scaleAdapter.setFirstOnly(false);
            scaleAdapter.setInterpolator(new OvershootInterpolator());
            gallery.setItemAnimator(new FlipInRightYAnimator());
            gallery.getItemAnimator().setChangeDuration(3000);
            gallery.setAdapter(scaleAdapter);
            gallery.setFocusableInTouchMode(false);

        } else {
            // Get lost, what are you doing here??
        }
    }
    public void createPlaceholderDataForGallery(){
        galleryData.add(new ChallengeCardData("Chocolate Origin", "30 mins left", "Chocolate Origin", "0.4 mi", "Chocolate Origin",String.valueOf(R.drawable.challenge_chocolate_origin)));
        galleryData.add(new ChallengeCardData("The Black Horse", "15 mins left", "The Black Horse", "0.9 mi", "The Black Horse", String.valueOf(R.drawable.challenge_bar)));
        galleryData.add(new ChallengeCardData("Love With Burgers", "27 mins left", "Love With Burgers", "5.4 mi", "Love With Burgers",String.valueOf(R.drawable.challenge_burger)));
        galleryData.add(new ChallengeCardData("Diablo's Wings", "12 days left", "Diablo's Wings", "0.4 mi", "Diablo's Wings",String.valueOf(R.drawable.challenge_wings)));
        galleryData.add(new ChallengeCardData("Sichuan Hotpot", "30 days left", "Sichuan Hotpot", "5.4 mi", "Sichuan Hotpot",String.valueOf(R.drawable.challenge_hotpot)));
        galleryData.add(new ChallengeCardData("Chocolate Origin", "30 mins left", "Chocolate Origin", "0.4 mi", "Chocolate Origin",String.valueOf(R.drawable.challenge_chocolate_origin)));
        galleryData.add(new ChallengeCardData("The Black Horse", "15 mins left", "The Black Horse", "0.9 mi", "The Black Horse", String.valueOf(R.drawable.challenge_bar)));
        galleryData.add(new ChallengeCardData("Love With Burgers", "27 mins left", "Love With Burgers", "5.4 mi", "Love With Burgers",String.valueOf(R.drawable.challenge_burger)));
        galleryData.add(new ChallengeCardData("Diablo's Wings", "12 days left", "Diablo's Wings", "0.4 mi", "Diablo's Wings",String.valueOf(R.drawable.challenge_wings)));
        galleryData.add(new ChallengeCardData("Sichuan Hotpot", "30 days left", "Sichuan Hotpot", "5.4 mi", "Sichuan Hotpot",String.valueOf(R.drawable.challenge_hotpot)));
        galleryData.add(new ChallengeCardData("Chocolate Origin", "30 mins left", "Chocolate Origin", "0.4 mi", "Chocolate Origin",String.valueOf(R.drawable.challenge_chocolate_origin)));
        galleryData.add(new ChallengeCardData("The Black Horse", "15 mins left", "The Black Horse", "0.9 mi", "The Black Horse", String.valueOf(R.drawable.challenge_bar)));
        galleryData.add(new ChallengeCardData("Love With Burgers", "27 mins left", "Love With Burgers", "5.4 mi", "Love With Burgers",String.valueOf(R.drawable.challenge_burger)));
        galleryData.add(new ChallengeCardData("Diablo's Wings", "12 days left", "Diablo's Wings", "0.4 mi", "Diablo's Wings",String.valueOf(R.drawable.challenge_wings)));
        galleryData.add(new ChallengeCardData("Sichuan Hotpot", "30 days left", "Sichuan Hotpot", "5.4 mi", "Sichuan Hotpot",String.valueOf(R.drawable.challenge_hotpot)));
        galleryData.add(new ChallengeCardData("Chocolate Origin", "30 mins left", "Chocolate Origin", "0.4 mi", "Chocolate Origin",String.valueOf(R.drawable.challenge_chocolate_origin)));
        galleryData.add(new ChallengeCardData("The Black Horse", "15 mins left", "The Black Horse", "0.9 mi", "The Black Horse", String.valueOf(R.drawable.challenge_bar)));
        galleryData.add(new ChallengeCardData("Love With Burgers", "27 mins left", "Love With Burgers", "5.4 mi", "Love With Burgers",String.valueOf(R.drawable.challenge_burger)));
        galleryData.add(new ChallengeCardData("Diablo's Wings", "12 days left", "Diablo's Wings", "0.4 mi", "Diablo's Wings",String.valueOf(R.drawable.challenge_wings)));
        galleryData.add(new ChallengeCardData("Sichuan Hotpot", "30 days left", "Sichuan Hotpot", "5.4 mi", "Sichuan Hotpot",String.valueOf(R.drawable.challenge_hotpot)));
    };
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.TAKE_PIC_REQUEST_CODE) {
                data = new Intent();
                data.putExtra("source", "camera");
            } else if (requestCode == Constants.SELECT_PIC_REQUEST_CODE) {
                data.putExtra("source", "gallery");
            }
            setResult(RESULT_OK, data);
            finish();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void snapPicture() {
        new BottomSheet.Builder(ChallengeDetailsActivity.this, R.style.BottomSheet_StyleDialog)
                .title("Choose how you want to upload a picture")
                .grid() // <-- important part
                .sheet(R.menu.home_join_bottom_sheet)
                .listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Utils.mostRecentMerchantName = "HEYHEY";
                        Utils.mostRecentMerchantDistance = "WHAT'S UP";

                        if (which == R.id.choose_gallery) {
                            Intent pickIntent = new Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickIntent, Constants.SELECT_PIC_REQUEST_CODE);
                        } else {
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                            File imagesFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), getString(R.string.app_name));
                            if (!imagesFolder.exists()) {
                                imagesFolder.mkdirs();
                            }
                            File image = new File(imagesFolder, "IMG_ADPIC_" + timeStamp + ".png");
                            Uri uriSavedImage = Uri.fromFile(image);
                            Utils.mostRecentPhoto = uriSavedImage;

                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                            cameraIntent.putExtra("TEST", "ING");
                            startActivityForResult(cameraIntent, Constants.TAKE_PIC_REQUEST_CODE);
                        }
                    }
                }).show();
    }
}
