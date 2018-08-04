package ru.acelost.collectionadapter.adapter;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractListViewAdapter<T, VH extends ViewHolder> extends CollectionViewAdapter<VH> {

    private final List<T> mCollection = new ArrayList<>();

    @Override
    public int getItemCount() {
        return mCollection.size();
    }

    @NonNull
    public T getItem(int position) {
        if (position < 0 || position >= mCollection.size()) {
            throw new IndexOutOfBoundsException("Attempt to get item for position " + position + " while collection size is " + mCollection.size());
        }
        T item = mCollection.get(position);
        if (item == null) {
            throw new IllegalStateException("Item on " + position + " position is null.");
        }
        return item;
    }

    @MainThread
    public void setData(@Nullable Collection<T> collection) {
        mCollection.clear();
        if (collection != null) {
            mCollection.addAll(collection);
        }
        notifyDataSetChanged();
    }

}
