package com.renovavision.rxsearch.ui.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.renovavision.rxsearch.R;
import com.renovavision.rxsearch.model.Item;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ImageViewHolder extends RecyclerView.ViewHolder
        implements RequestListener<String, GlideDrawable> {

    @Bind(R.id.main_photo)
    public ImageView mainPhoto;

    protected Item mediaItem;

    protected int gridItemSize;

    public void bindData(@NonNull Item mediaItem){
        this.mediaItem = mediaItem;

        Context context = mainPhoto.getContext();

        // load gif and bitmap images
        Glide.with(context)
                .fromString()
                .load(mediaItem.link)
                .centerCrop()
                .listener(this)
                .into(mainPhoto);
    }

    public ImageViewHolder(@NonNull View itemView, int gridItemSize) {
        super(itemView);
        this.gridItemSize = gridItemSize;
        ButterKnife.bind(this, itemView);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(this.gridItemSize,
                this.gridItemSize);
        mainPhoto.setLayoutParams(params);
    }

    @Override
    public boolean onException(Exception e, String model, Target<GlideDrawable> target,
                               boolean isFirstResource) {
        return false;
    }

    @Override
    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                   boolean isFromMemoryCache, boolean isFirstResource) {
        // all is good
        return false;
    }
}
