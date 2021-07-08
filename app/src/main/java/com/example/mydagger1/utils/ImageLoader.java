package com.example.mydagger1.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

public final class ImageLoader {
    public void loadImageGlide(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .centerCrop()
                .into(imageView);
    }

    public void loadImagePicasso(String url, ImageView imageView) {
        Picasso.get()
                .load(url)
                .fit()
                .centerCrop()
                .into(imageView);
    }
}
