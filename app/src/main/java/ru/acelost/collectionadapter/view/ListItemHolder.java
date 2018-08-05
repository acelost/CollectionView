package ru.acelost.collectionadapter.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import ru.acelost.collectionadapter.adapter.CollectionView;

public class ListItemHolder extends CollectionView.ViewHolder {

    private final TextView mTextView;

    @NonNull
    private static View createView(@NonNull Context context, boolean center) {
        TextView view = new TextView(context);
        view.setTextSize(15);
        view.setTextColor(Color.DKGRAY);
        if (center) {
            view.setGravity(Gravity.CENTER);
        }
        return view;
    }

    public ListItemHolder(@NonNull Context context, boolean center) {
        super(createView(context, center));
        mTextView = (TextView) view;
    }

    public void setText(@NonNull String text) {
        mTextView.setText(text);
    }

}
