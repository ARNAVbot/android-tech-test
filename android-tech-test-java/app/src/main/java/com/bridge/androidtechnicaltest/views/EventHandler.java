package com.bridge.androidtechnicaltest.views;

import android.view.View;

public interface EventHandler<T> {
    void onViewClicked(View view, T item, int position);

    void loadMore();
}