package com.renovavision.rxsearch.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.renovavision.rxsearch.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FooterViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.load_more_button)
    TextView loadMoreButton;

    @Bind(R.id.progress_indicator)
    ProgressBar progressBar;

    public FooterViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setLoading(boolean isLoading) {
        loadMoreButton.setVisibility(isLoading ? View.INVISIBLE : View.VISIBLE);
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
    }
}
