package ru.acelost.collectionadapter.adapter;

import android.support.annotation.NonNull;
import android.view.View;

public interface CollectionView {

    void addChildInLayout(@NonNull View view, int position);

    void removeViewInLayout(@NonNull View view);

    void removeViewsInLayout(int start, int count);

    int getChildCount();

    void requestLayout();
}
