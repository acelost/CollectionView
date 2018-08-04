package ru.acelost.collectionadapter.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.acelost.collectionadapter.adapter.CollectionView;

public class ListView extends LinearLayout implements CollectionView {

    private TextView mHeader;
    private TextView mFooter;

    public ListView(Context context) {
        super(context);
        init();
    }

    public ListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        mHeader = new TextView(getContext());
        mHeader.setTextSize(17);
        mHeader.setTextColor(Color.BLACK);
        mHeader.setText("[Some header]");
        addView(mHeader);

        mFooter = new TextView(getContext());
        mFooter.setTextSize(15);
        mFooter.setTextColor(Color.BLACK);
        mFooter.setText("[Some footer]");
        addView(mFooter);
    }

    @Override
    public void addChildInLayout(@NonNull View view, int position) {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addViewInLayout(view, position, layoutParams, true);
    }

}
