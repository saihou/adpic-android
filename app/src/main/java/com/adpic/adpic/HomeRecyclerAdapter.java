package com.adpic.adpic;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by saihou on 2/19/16.
 */
public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder> {
    private ArrayList<com.adpic.adpic.HomeCardData> mDataset;
    private com.adpic.adpic.MainActivity activity;

    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        CardView cv;
        TextView username;
        TextView time;
        ImageView picture;
        ImageView heartIcon;
        GestureDetectorCompat gestureDetector;
        TextView challengeRestaurant;
        TextView challengeDistance;
        TextView caption;
        Button viewChallenge;

        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.home_card);
            username = (TextView) itemView.findViewById(R.id.username);
            time = (TextView) itemView.findViewById(R.id.time);
            picture = (ImageView) itemView.findViewById(R.id.picture);
            heartIcon = (ImageView) itemView.findViewById(R.id.heart_icon);
            challengeRestaurant = (TextView) itemView.findViewById(R.id.challenge_restaurant);
            challengeDistance = (TextView) itemView.findViewById(R.id.challenge_distance);
            caption = (TextView) itemView.findViewById(R.id.caption);
            viewChallenge = (Button) itemView.findViewById(R.id.view_challenge_btn);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public HomeRecyclerAdapter(ArrayList<com.adpic.adpic.HomeCardData> myDataset, com.adpic.adpic.MainActivity activity) {
        mDataset = myDataset;
        this.activity = activity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HomeRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_card, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        vh.gestureDetector = new GestureDetectorCompat(activity, new DoubleTapGestureListener(vh));
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final HomeCardData data = mDataset.get(position);
        holder.username.setText(data.getUsername());
        holder.time.setText(data.getTime());
        holder.challengeRestaurant.setText("Challenge: " + data.getChallengeRestaurant());
        holder.challengeDistance.setText(data.getChallengeDistance());
        holder.caption.setText(data.getCaption());

        Bitmap rawBitmap = null;
        try {
            int picture = Integer.parseInt(data.getPicture());
            rawBitmap = BitmapFactory.decodeResource(activity.getResources(), picture);
        } catch (NumberFormatException e) {
            Uri uri = Utils.mostRecentPhoto;
            try {
                rawBitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(uri));
            } catch (FileNotFoundException err) {
                e.printStackTrace();
            }
        } finally {
            if (rawBitmap != null) {
                int height = rawBitmap.getHeight();
                int width = rawBitmap.getWidth();
                int scale = 2;
                if (width > 800) {
                    height = height/scale;
                    width = width/scale;
                }
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(rawBitmap, width, height, false);
                holder.picture.setImageBitmap(scaledBitmap);
            }
        }

        holder.picture.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return holder.gestureDetector.onTouchEvent(event);
            }
        });
        holder.heartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.heartIcon.getTag().toString().equals(String.valueOf(R.drawable.ic_favorite_black_36dp))) {
                    holder.heartIcon.setImageResource(R.drawable.ic_favorite_border_black_36dp);
                    holder.heartIcon.setTag(R.drawable.ic_favorite_border_black_36dp);
                } else {
                    updateHeartButton(holder, true);
                    //holder.heartIcon.setImageResource(R.drawable.ic_favorite_black_36dp);
                    holder.heartIcon.setTag(R.drawable.ic_favorite_black_36dp);
                }
            }
        });

        holder.viewChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.mostRecentChallengeClicked = data.getChallengeRestaurant();
                Intent intent = new Intent(activity.getApplicationContext(), ChallengeDetailsActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    activity.getWindow().setExitTransition(new Slide(Gravity.LEFT));
                    activity.startActivityForResult(intent, Constants.OPEN_CHALLENGE_DETAILS_PAGE_REQUEST_CODE,
                            ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
                } else {
                    activity.startActivityForResult(intent, Constants.OPEN_CHALLENGE_DETAILS_PAGE_REQUEST_CODE);
                }
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private void updateHeartButton(final HomeRecyclerAdapter.ViewHolder holder, boolean animated) {
        if (animated) {
            if (true) {
                AnimatorSet animatorSet = new AnimatorSet();

                ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(holder.heartIcon, "rotation", 0f, 360f);
                rotationAnim.setDuration(300);
                rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

                ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(holder.heartIcon, "scaleX", 0.2f, 1f);
                bounceAnimX.setDuration(300);
                bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

                ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(holder.heartIcon, "scaleY", 0.2f, 1f);
                bounceAnimY.setDuration(300);
                bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
                bounceAnimY.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        holder.heartIcon.setImageResource(R.drawable.ic_favorite_black_36dp);
                    }
                });

                animatorSet.play(rotationAnim);
                animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);

                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        //resetLikeAnimationState(holder);
                    }
                });

                animatorSet.start();
            }
        }
    }

    class DoubleTapGestureListener extends GestureDetector.SimpleOnGestureListener {
        HomeRecyclerAdapter.ViewHolder viewHolder;
        ImageView heartIcon;

        public DoubleTapGestureListener(HomeRecyclerAdapter.ViewHolder vh) {
            super();
            this.heartIcon = vh.heartIcon;
            this.viewHolder = vh;
        }

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            updateHeartButton(viewHolder, true);
            //heartIcon.setImageResource(R.drawable.ic_favorite_black_36dp);
            heartIcon.setTag(R.drawable.ic_favorite_black_36dp);
            return true;
        }
    }
}
