package ru.acelost.collectionadapter.adapter;

import android.support.annotation.Nullable;
import android.util.SparseArray;

import java.util.ArrayList;

import ru.acelost.collectionadapter.BuildConfig;

public class RecycledViewPool {
    private static final int DEFAULT_MAX_SCRAP = 5;

    private static class ScrapData {
        final ArrayList<ViewHolder> mScrapHeap = new ArrayList<>();
        int mMaxScrap = DEFAULT_MAX_SCRAP;
    }
    private SparseArray<ScrapData> mScrap = new SparseArray<>();

    /**
     * Удалить все вью-холдеры из пула.
     */
    public void clear() {
        for (int i = 0; i < mScrap.size(); i++) {
            ScrapData data = mScrap.valueAt(i);
            data.mScrapHeap.clear();
        }
    }

    /**
     * Задать максимальное количество вью-холдеров указанного типа в пуле.
     *
     * @param viewType  - тип вью
     * @param max       - максимальное количество
     */
    public void setMaxRecycledViews(int viewType, int max) {
        ScrapData scrapData = getScrapDataForType(viewType);
        scrapData.mMaxScrap = max;
        final ArrayList<ViewHolder> scrapHeap = scrapData.mScrapHeap;
        while (scrapHeap.size() > max) {
            scrapHeap.remove(scrapHeap.size() - 1);
        }
    }

    /**
     * Получить количество вью-холдеров указанного типа в пуле.
     */
    public int getRecycledViewCount(int viewType) {
        return getScrapDataForType(viewType).mScrapHeap.size();
    }

    /**
     * Забрать вью-холдер указанного типа из пула. В случае, если
     * в пуле нет вью-холдера указанного типа, метод вернет null.
     *
     * @param viewType - тип вью
     * @return вью-холдер указанного типа или null
     */
    @Nullable
    public ViewHolder getRecycledView(int viewType) {
        final ScrapData scrapData = mScrap.get(viewType);
        if (scrapData != null && !scrapData.mScrapHeap.isEmpty()) {
            final ArrayList<ViewHolder> scrapHeap = scrapData.mScrapHeap;
            return scrapHeap.remove(scrapHeap.size() - 1);
        }
        return null;
    }

    /**
     * Добавить вью-холдер в пул для переиспользования.
     * Если пул полон для указанного типа вью, вью-холдер будет сразу удален.
     *
     * @param scrap - вью-холдер для переиспользования
     */
    public void putRecycledView(ViewHolder scrap) {
        final int viewType = scrap.getViewType();
        final ArrayList<ViewHolder> scrapHeap = getScrapDataForType(viewType).mScrapHeap;
        if (mScrap.get(viewType).mMaxScrap <= scrapHeap.size()) {
            return;
        }
        if (BuildConfig.DEBUG && scrapHeap.contains(scrap)) {
            throw new IllegalArgumentException("this scrap item already exists");
        }
        scrapHeap.add(scrap);
    }

    private ScrapData getScrapDataForType(int viewType) {
        ScrapData scrapData = mScrap.get(viewType);
        if (scrapData == null) {
            scrapData = new ScrapData();
            mScrap.put(viewType, scrapData);
        }
        return scrapData;
    }

}
