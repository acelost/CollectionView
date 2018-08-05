package ru.acelost.collectionadapter.adapter;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Базовая реализация {@link CollectionView.Adapter} для вью, в которых коллекция представляется ввиде списка элементов.
 *
 * @param <T>   - тип элементов списка данных
 * @param <VH>  - тип вью-холдеров дочерних элементов
 */
public abstract class AbstractListViewAdapter<T, VH extends CollectionView.ViewHolder> extends CollectionView.Adapter<VH> {

    /**
     * Коллекция данных. Предполагается, что нет нулевых элементов.
     */
    private final List<T> mCollection = new ArrayList<>();

    @Override
    public int getItemCount() {
        return mCollection.size();
    }

    /**
     * Получить элемент на указанной позиции в коллекции.
     *
     * @param position - позиция элемента в коллекции
     * @return элемент списка
     */
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

    /**
     * Задать данные для отображения ввиде коллекции.
     *
     * @param data - данные
     */
    @MainThread
    public void setData(@Nullable Collection<T> data) {
        mCollection.clear();
        if (data != null) {
            mCollection.addAll(data);
        }
        notifyDataChanged();
    }

}
