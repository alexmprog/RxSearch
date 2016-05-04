package com.renovavision.rxsearch.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.renovavision.rxsearch.R;
import com.renovavision.rxsearch.model.Item;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int FOOTER_ITEM = 100;

    protected static final int IMAGE_ITEM = 1;

    protected boolean isLoading;

    private List<Item> mediaItems;

    private int gridItemSize;

    public SearchAdapter(@NonNull List<Item> mediaItems,
                         int gridItemSize) {
        this.mediaItems = mediaItems;
        this.gridItemSize = gridItemSize;
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= mediaItems.size()) {
            return FOOTER_ITEM;
        }
        return IMAGE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case IMAGE_ITEM:
                itemView = View.inflate(parent.getContext(), R.layout.photo_item, null);
                return new ImageViewHolder(itemView, gridItemSize);
            case FOOTER_ITEM:
                itemView = View.inflate(parent.getContext(), R.layout.footer_item, null);
                return new FooterViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position >= mediaItems.size() && holder instanceof FooterViewHolder) {
            ((FooterViewHolder)holder).setLoading(isLoading);
        } else if(holder instanceof ImageViewHolder){
            Item mediaItem = mediaItems.get(position);
            ((ImageViewHolder)holder).bindData(mediaItem);
        }
    }

    @Override
    public int getItemCount() {
        return mediaItems.size() + 1;
    }

    public boolean isEmpty() {
        return getItemCount() < 2;
    }

    public boolean isFooter(int position) {
        return getItemViewType(position) == SearchAdapter.FOOTER_ITEM;
    }

    public void notifyFooterChanged(boolean loading) {
        this.isLoading = loading;
        notifyItemChanged(getItemCount()-1);
    }

    public void insertMediaItems(@NonNull List<Item> mediaItems) {
        int itemsSize = this.mediaItems.size();
        this.mediaItems.addAll(itemsSize == 0 ? 0 : itemsSize, mediaItems);
        notifyDataSetChanged();
    }

    public void clear() {
        mediaItems.clear();
        notifyDataSetChanged();
    }
}
