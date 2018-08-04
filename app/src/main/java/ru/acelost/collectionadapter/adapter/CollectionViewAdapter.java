package ru.acelost.collectionadapter.adapter;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;

public abstract class CollectionViewAdapter<VH extends ViewHolder> {

    private static final int DEFAULT_STASH_SIZE = 3;

    private final SparseArray<VH> mViewHolders = new SparseArray<>();

    @Nullable
    private RecycledViewPool mRecycledPool;

    @Nullable
    private CollectionView mView;

    public abstract int getItemCount();

    protected int getItemViewType(int position) {
        return 0;
    }

    protected int getChildStartOffset() {
        return 0;
    }

    protected int getChildEndOffset() {
        return 0;
    }

    // region Attach/Detach logic

    @MainThread
    public void attachView(@NonNull CollectionView view) {
        if (mView == view) {
            return;
        }
        mView = view;
        notifyDataSetChanged();
    }

    @MainThread
    public void detachView() {
        if (mView == null) {
            return;
        }
        // Отправляем вью-холдеры на переиспользование
        int size = mViewHolders.size();
        for (int i = 0; i < size; ++i) {
            VH holder = mViewHolders.valueAt(i);
            if (holder != null) {
                recycleViewHolder(mView, holder);
            }
        }
        // Удаляем все вью из родительского вью
        mView.removeViewsInLayout(getChildStartOffset(), mView.getChildCount() - getChildEndOffset());
        mView = null;
    }

    // endregion

    public void notifyDataSetChanged() {
        final CollectionView view = mView;
        if (view == null) {
            return;
        }
        final int count = getItemCount();
        final int childOffset = getChildStartOffset();
        // Привязываем коллекцию данных к вью-холдерам
        for (int i = 0; i < count; ++i) {
            final VH holder = getViewHolderForPosition(view, i, childOffset);
            if (holder == null) {
                throw new IllegalStateException("View holder for " + i + " position is null.");
            }
            bindViewHolder(holder, i);
        }
        // Складываем в стеш лишние вью-холдеры
        final int stashSize = getStashSize();
        for (int i = 0; i < stashSize; ++i) {
            final VH holder = getViewHolder(count + i);
            if (holder != null) {
                stashViewHolder(holder);
            }
        }
        // Отправляем не поместившиеся в стеш вью-холдеры на переиспользование
        final int start = count + stashSize;
        final int end = view.getChildCount() - getChildEndOffset();
        if (start < end - 1) {
            for (int i = start; i < end; ++i) {
                final VH holder = getViewHolder(i);
                if (holder != null) {
                    recycleViewHolder(view, holder);
                    mViewHolders.delete(i);
                }
            }
            view.removeViewsInLayout(start + getChildStartOffset(), end - start + getChildStartOffset());
        }
        view.requestLayout();
    }

    // region Holder relevant methods

    public final VH getViewHolder(int position) {
        return mViewHolders.get(position);
    }

    private VH getViewHolderForPosition(@NonNull CollectionView view, int position, int childOffset) {
        // Получаем тип элемента
        final int type = getItemViewType(position);
        // Получаем вью-холдер, который на данный момент на указанной позиции
        VH holder = getViewHolder(position);
        boolean useExists = false;
        if (holder != null) {
            if (holder.getViewType() == type) {
                // Тип вью-холдера совпадает с типом элемента
                useExists = true;
                prepareViewHolder(view, holder);
            } else {
                // Вью-холдер не подходит по типу
                recycleViewHolder(view, holder);
                view.removeViewInLayout(holder.view);
            }
        }
        if (!useExists) {
            // Создаем новый вью-холдер и добавляем его в родительский вью
            holder = createViewHolder(view, type);
            view.addChildInLayout(holder.view, position + childOffset);
            mViewHolders.put(position, holder);
        }
        return holder;
    }

    @NonNull
    private VH createViewHolder(@NonNull CollectionView parent, int viewType) {
        ViewHolder recycled = getRecycledViewPool().getRecycledView(viewType);
        if (recycled != null) {
            Log.e("CollectionViewAdapter", "Get view with type " + viewType + " from pool.");
            //noinspection unchecked
            return (VH) recycled;
        }
        Log.e("CollectionViewAdapter", "Create new view with type " + viewType + ".");
        VH holder = onCreateViewHolder(parent, viewType);
        holder.setViewType(viewType);
        return holder;
    }

    @NonNull
    protected abstract VH onCreateViewHolder(@NonNull CollectionView parent, int viewType);

    private void bindViewHolder(@NonNull VH holder, int position) {
        onBindViewHolder(holder, position);
    }

    protected abstract void onBindViewHolder(@NonNull VH holder, int position);

    private void prepareViewHolder(@NonNull CollectionView view, @NonNull VH holder) {
        holder.prepare();
        onPrepareViewHolder(view, holder);
    }

    protected void onPrepareViewHolder(@NonNull CollectionView view, @NonNull VH holder) {
        // do nothing
    }

    private void recycleViewHolder(@NonNull CollectionView view, @NonNull VH holder) {
        onRecycleViewHolder(holder);
        holder.recycle();
        getRecycledViewPool().putRecycledView(holder);
    }

    protected void onRecycleViewHolder(@NonNull VH holder) {
        // do nothing
    }

    private void stashViewHolder(@NonNull VH holder) {
        holder.stash();
    }

    // endregion

    // region Recycling relevant methods

    protected int getStashSize() {
        return DEFAULT_STASH_SIZE;
    }

    @NonNull
    private RecycledViewPool getRecycledViewPool() {
        if (mRecycledPool == null) {
            mRecycledPool = new RecycledViewPool();
        }
        return mRecycledPool;
    }

    public void setRecycledViewPool(@Nullable RecycledViewPool pool) {
        mRecycledPool = pool;
    }

    // endregion

}
