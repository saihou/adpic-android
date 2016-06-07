package com.adpic.adpic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ChallengeDetailsActivity extends AppCompatActivity {

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

            CardView cv = (CardView) findViewById(R.id.challenge_card);
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
                picture.setImageBitmap(scaledBitmap);
                actionBarBackground.setImageBitmap(scaledBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Get lost, what are you doing here??
        }
    }

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
}
