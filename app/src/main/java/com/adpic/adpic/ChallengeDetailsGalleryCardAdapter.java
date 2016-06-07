package com.adpic.adpic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by saihou on 6/7/16.
 */
public class ChallengeDetailsGalleryCardAdapter extends RecyclerView.Adapter<ChallengeDetailsGalleryCardAdapter.ViewHolder> {
        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView caption;
            public ImageView image;

            public ViewHolder(View itemView) {
                super(itemView);
                caption = (TextView) itemView.findViewById(R.id.caption);
                image = (ImageView) itemView.findViewById(R.id.image);
            }
        }
    private ArrayList<ChallengeCardData> data;
    public ChallengeDetailsGalleryCardAdapter(ArrayList<ChallengeCardData> data) {
        this.data = data;
    }

    @Override
    public ChallengeDetailsGalleryCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View uberCard = inflater.inflate(R.layout.horizontal_gallery_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(uberCard);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ChallengeDetailsGalleryCardAdapter.ViewHolder viewHolder, int position) {
        ChallengeCardData cardData = data.get(position);
        TextView textView = viewHolder.caption;
        textView.setText(cardData.getCaption());
        textView.setTextColor(Color.WHITE);
        ImageView cardImage = viewHolder.image;
        try {
            int picture = Integer.parseInt(cardData.getPicture());
            Bitmap bitmap = BitmapFactory.decodeResource(textView.getContext().getResources(),picture);
            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            int scale = 2;
            if (width > 800) {
                height = height/scale;
                width = width/scale;
            }
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
            cardImage.setImageBitmap(scaledBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        bind(viewHolder);
    }

    private void bind(final ChallengeDetailsGalleryCardAdapter.ViewHolder viewHolder) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = viewHolder.getAdapterPosition();
                ChallengeDetailsGalleryCardAdapter.this.notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount(){
        return data.size();
    }
}
