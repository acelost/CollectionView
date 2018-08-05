package ru.acelost.collectionadapter.sample_rating;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.jetbrains.annotations.NotNull;

import ru.acelost.collectionadapter.R;
import ru.acelost.collectionadapter.adapter.CollectionView;
import ru.acelost.collectionadapter.measurement.Measurement;

public class RatingView extends LinearLayout implements CollectionView {

    private final Adapter mAdapter = new Adapter();

    public RatingView(Context context) {
        super(context);
        init();
    }

    public RatingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RatingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RatingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mAdapter.attachView(this);
    }

    public void setRating(int rating) {
        mAdapter.setRating(rating);
    }

    @Override
    public void addChildInLayout(@NonNull View view, int position) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(50, 50);
        }
        addViewInLayout(view, position, lp, true);
    }

    public void setRecycledViewPool(@NotNull RecycledViewPool pool) {
        mAdapter.setRecycledViewPool(pool);
    }

    private class FullHolder extends ViewHolder {
        FullHolder(ImageView view) {
            super(view);
            view.setImageResource(R.drawable.star);
        }
    }
    private class HalfHolder extends ViewHolder {
        HalfHolder(ImageView view) {
            super(view);
            view.setImageResource(R.drawable.star_half);
        }
    }
    private class EmptyHolder extends ViewHolder {
        EmptyHolder(ImageView view) {
            super(view);
            view.setImageResource(R.drawable.star_outline);
        }
    }

    private class Adapter extends CollectionView.Adapter<ViewHolder> {

        private static final int VIEW_TYPE_FULL = 0;
        private static final int VIEW_TYPE_HALF = 1;
        private static final int VIEW_TYPE_EMPTY = 2;

        private int mRating;

        public void setRating(int rating) {
            mRating = rating;
            notifyDataChanged();
        }

        @Override
        public int getItemCount() {
            return (mRating + 1) / 2;
        }

        @Override
        protected int getItemViewType(int position) {
            int half = position * 2 + 1;
            if (mRating > half) {
                return VIEW_TYPE_FULL;
            } else if (mRating < half) {
                return VIEW_TYPE_EMPTY;
            } else {
                return VIEW_TYPE_HALF;
            }
        }

        @NonNull
        @Override
        protected ViewHolder onCreateViewHolder(@NonNull CollectionView parent, int viewType) {
            Measurement.getInstance().increment("Create Holder");
            Measurement.getInstance().printCounters();
            ImageView view = new AppCompatImageView(getContext());
            switch (viewType) {
                case VIEW_TYPE_FULL:
                    return new FullHolder(view);
                case VIEW_TYPE_HALF:
                    return new HalfHolder(view);
                case VIEW_TYPE_EMPTY:
                    return new EmptyHolder(view);
            }
            throw new IllegalArgumentException("Unknown view type " + viewType + ".");
        }

        @Override
        protected void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        }
    }

}
