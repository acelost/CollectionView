package ru.acelost.collectionadapter.view;

import android.content.Context;
import android.support.annotation.NonNull;

import ru.acelost.collectionadapter.adapter.AbstractListViewAdapter;
import ru.acelost.collectionadapter.adapter.CollectionView;

public class ListViewAdapter extends AbstractListViewAdapter<String, ListItemHolder> {

    private static final int VIEW_TYPE_DEFAULT = 0;
    private static final int VIEW_TYPE_CENTER = 1;

    private final Context mContext;

    public ListViewAdapter(@NonNull Context context) {
        mContext = context;
    }

    @Override
    protected int getItemViewType(int position) {
        return getItem(position).charAt(0) == '!' ? VIEW_TYPE_CENTER : VIEW_TYPE_DEFAULT;
    }

    @NonNull
    @Override
    protected ListItemHolder onCreateViewHolder(@NonNull CollectionView parent, int viewType) {
        return new ListItemHolder(mContext, viewType == VIEW_TYPE_CENTER);
    }

    @Override
    protected void onBindViewHolder(@NonNull ListItemHolder holder, int position) {
        holder.setText(getItem(position));
    }

    @Override
    protected int getChildStartOffset() {
        return 1;
    }

    @Override
    protected int getChildEndOffset() {
        return 1;
    }

}
