package ru.acelost.collectionadapter.adapter;

import android.support.annotation.CallSuper;
import android.view.View;

public abstract class ViewHolder {

    public final View view;
    private int viewType;
    private boolean inStash;
    private int beforeStashVisibility;

    public ViewHolder(View view) {
        if (view == null) {
            throw new IllegalArgumentException("View may not be null.");
        }
        this.view = view;
    }

    public final int getViewType() {
        return viewType;
    }

    void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public void stash() {
        if (!inStash) {
            inStash = true;
            beforeStashVisibility = view.getVisibility();
            view.setVisibility(View.GONE);
        }
    }

    @CallSuper
    public void prepare() {
        if (inStash) {
            inStash = false;
            view.setVisibility(beforeStashVisibility);
        }
    }

    public void recycle() {
        // do nothing
    }

}
