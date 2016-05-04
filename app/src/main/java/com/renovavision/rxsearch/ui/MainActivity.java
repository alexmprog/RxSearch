package com.renovavision.rxsearch.ui;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.renovavision.rxsearch.R;
import com.renovavision.rxsearch.api.GoogleSearchApi;
import com.renovavision.rxsearch.model.Item;
import com.renovavision.rxsearch.model.SearchResponse;
import com.renovavision.rxsearch.ui.adapter.SearchAdapter;
import com.renovavision.rxsearch.ui.widget.RectangleItemDecoration;
import com.renovavision.rxsearch.ui.widget.RecyclerItemClickListener;
import com.renovavision.rxsearch.ui.widget.TintedProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements EditText.OnEditorActionListener,
        RecyclerItemClickListener.OnItemClickListener {

    private static final int SEARCH_RESULTS_COUNT = 9;
    private static final int GRID_SIZE = 3;

    @Bind(R.id.progress_indicator)
    TintedProgressBar progressBar;

    @Bind(R.id.empty_view)
    TextView emptyView;

    @Bind(R.id.search_text)
    EditText searchEditText;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    protected GridLayoutManager gridLayoutManager;

    protected SearchAdapter searchAdapter;

    private AtomicBoolean isLoading = new AtomicBoolean(false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_search);
        ButterKnife.bind(this);

        int paddingSize = getResources().getDimensionPixelSize(R.dimen.item_space);
        recyclerView.setPadding(paddingSize, paddingSize, paddingSize, paddingSize);
        recyclerView.addItemDecoration(new RectangleItemDecoration(paddingSize));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // calculate size for item
        int imageSize = calculateGridItemSize(this);

        gridLayoutManager = new GridLayoutManager(this, GRID_SIZE);

        // The number of Columns
        recyclerView.setLayoutManager(gridLayoutManager);

        // Set value for
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, this));

        searchAdapter = new SearchAdapter(new ArrayList<Item>(), imageSize);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return searchAdapter.isFooter(position) ? gridLayoutManager.getSpanCount() : 1;
            }
        });

        recyclerView.setAdapter(searchAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        searchAdapter.notifyFooterChanged(isLoading.get());

        searchEditText.setOnEditorActionListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        searchEditText.setOnEditorActionListener(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            isLoading.set(true);

            searchAdapter.notifyFooterChanged(true);

            resetSearch();

            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            GoogleSearchApi.getInstance().getImages(v.getText().toString(), 1, SEARCH_RESULTS_COUNT)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(onFirstResultsReceived(), onErrorReceived());
        }
        return false;
    }

    @Override
    public void onItemClick(View view, int position) {
        int itemsCount = searchAdapter.getItemCount();
        if (position == itemsCount - 1) {
            isLoading.set(true);

            searchAdapter.notifyFooterChanged(true);

            GoogleSearchApi.getInstance().getImages(searchEditText.getText().toString(), itemsCount, SEARCH_RESULTS_COUNT)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(onFirstResultsReceived(), onErrorReceived());
        } else {
           // do nothing
        }
    }

    @OnClick(R.id.button_cancel)
    public void onClearClick(View view) {
        clearSearch();
        resetSearch();
    }

    protected int calculateGridItemSize(@NonNull Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        Point p = new Point();
        defaultDisplay.getSize(p);
        int itemMargin = getResources().getDimensionPixelSize(R.dimen.item_space);
        return (p.x - 4 * itemMargin) / GRID_SIZE;
    }

    @NonNull
    private Action1<SearchResponse> onFirstResultsReceived() {
        return new Action1<SearchResponse>() {
            @Override
            public void call(SearchResponse response) {
                if (!isFinishing()) {
                    return;
                }

                List<Item> items = response.items;
                searchAdapter.insertMediaItems(items);

                isLoading.set(false);

                searchAdapter.notifyFooterChanged(false);

                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(!searchAdapter.isEmpty() ? View.VISIBLE : View.GONE);
                emptyView.setVisibility(!searchAdapter.isEmpty() ? View.GONE : View.VISIBLE);
            }
        };
    }

    @NonNull
    private Action1<Throwable> onErrorReceived() {
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable error) {
                Log.e(error.getMessage(), "Google search failed");

                isLoading.set(false);

                searchAdapter.notifyFooterChanged(false);

                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(!searchAdapter.isEmpty() ? View.VISIBLE : View.GONE);
                emptyView.setVisibility(!searchAdapter.isEmpty() ? View.GONE : View.VISIBLE);
            }
        };
    }

    private void clearSearch() {
        searchEditText.setText("");
    }

    private void resetSearch() {
        searchAdapter.clear();
        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }
}
