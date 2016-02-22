package com.saihou.adpic;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by saihou on 2/19/16.
 */
public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder> {
    private ArrayList<HomeCardData> mDataset;
    private Activity activity;
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

        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.home_card);
            username = (TextView) itemView.findViewById(R.id.username);
            time = (TextView) itemView.findViewById(R.id.time);
            picture = (ImageView) itemView.findViewById(R.id.picture);
            heartIcon = (ImageView) itemView.findViewById(R.id.heart_icon);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public HomeRecyclerAdapter(ArrayList<HomeCardData> myDataset, Activity activity) {
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
        vh.gestureDetector = new GestureDetectorCompat(activity, new DoubleTapGestureListener(vh.heartIcon));
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        HomeCardData data = mDataset.get(position);
        holder.username.setText(data.getUsername());
        holder.time.setText(data.getTime());
        Uri uri;

        switch (position) {
            case 0: uri = Uri.parse("content://media/external/images/media/12671");
                break;
            case 1: uri = Uri.parse("content://media/external/images/media/12672");
                break;
            case 2: uri = Uri.parse("content://media/external/images/media/12673");
                break;
            default: uri = Uri.parse("content://media/external/images/media/12671");
                break;
        }

        try {
//            Uri uri = Uri.fromFile(new File("/content://media/external/images/media/12671"));
            Bitmap bitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(uri));
            holder.picture.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
                holder.heartIcon.setImageResource(R.drawable.ic_favorite_black_36dp);
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

    class DoubleTapGestureListener extends GestureDetector.SimpleOnGestureListener {
        ImageView heartIcon;
        public DoubleTapGestureListener(ImageView heartIcon) {
            super();
            this.heartIcon = heartIcon;
        }

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        public void setHeartIcon(ImageView heartIcon) {
            this.heartIcon = heartIcon;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            heartIcon.setImageResource(R.drawable.ic_favorite_black_36dp);
            return true;
        }

    }
}
