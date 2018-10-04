package ru.acelost.collectionadapter.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ru.acelost.collectionadapter.BuildConfig;

/**
 * Интерфейс вью, представляющего коллекцию элементов. Используется для вью, предполагающих отображение
 * коллекции элементов (как однотипных, так и разнотипных). Позволяет быстро и гибко реализовывать подобные
 * вью. Воркфлоу: реализовать интерфейс {@link CollectionView} на необходимом {@link ViewGroup} (так же может
 * быть реализован объектом-посредником), а так же реализовать {@link Adapter} для элементов коллекции.
 * {@link Adapter} и {@link CollectionView} должны быть связаны методом {@link Adapter#attachView(CollectionView)}.
 */
public interface CollectionView {

    /**
     * Окружение, в котором работает {@link CollectionView} и его дружественные классы.
     */
    class Environment {
        public static boolean LOGGING_ENABLED = false;
        public static boolean DEBUG = BuildConfig.DEBUG;
        static void log(Object message) {
            Log.i("CollectionViewLog", message.toString());
        }
    }

    /**
     * Значение, сигнализирующее о неуказанной позиции вью-холдера.
     */
    int NO_POSITION = -1;

    /**
     * Добавить дочернюю вью в родительскую на указанную позицию. Предполагаемая реализация - вызов
     * метода {@link android.view.ViewGroup#addViewInLayout(View, int, ViewGroup.LayoutParams, boolean)}
     * с указанием параметра preventRequestLayout = true и необходимых layout params.
     *
     * @param view      - дочерняя вью, которую необходимо добавить
     * @param position  - позиция, на которую необходимо добавить вью
     */
    void addChildInLayout(@NonNull View view, int position);

    /**
     * Удалить дочернюю вью из родительской. Предполагаемая реализация - прямой роутинг на метод
     * {@link ViewGroup#removeViewInLayout(View)}. Если интерфейс {@link CollectionView}
     * имплементирует {@link ViewGroup} - переопределять этот метод не нужно.
     *
     * @param view - дочерняя вью, которую необходимо удалить
     */
    void removeViewInLayout(@NonNull View view);

    /**
     * Удалить <code>count</code> дочерних вью из родительской, начиная с позиции <code>start</code>.
     * Предполагаемая реализация - прямой роутинг на метод {@link ViewGroup#removeViewsInLayout(int, int)}.
     * Если интерфейс {@link CollectionView} имплементируется {@link ViewGroup} - переопределять этот метод не нужно.
     *
     * @param start     - позиция, начиная с которой необходимо удалять дочерние вью
     * @param count     - количество дочерних вью, которые необходимо удалить
     */
    void removeViewsInLayout(int start, int count);

    /**
     * Получить количество дочерних вью. Предполагаемая реализация - прямой
     * роутинг на метод {@link ViewGroup#getChildCount()}. Если интерфейс {@link CollectionView}
     * имплементируется {@link ViewGroup} - переопределять этот метод не нужно.
     *
     * @return количество дочерних вью
     */
    int getChildCount();

    /**
     * Вызвать перерассчет макета. Предполагаемая реализация - прямой роутинг на метод
     * {@link View#requestLayout()}. Если интерфейс {@link CollectionView} имплементируется
     * {@link View} - переопределять этот метод не нужно.
     */
    void requestLayout();

    /**
     * Адаптер для дочерних вью внутри {@link CollectionView}. Отвечает за создание
     * дочерних вью, их жизненный цикл, привязку данных и переиспользование. Адаптер
     * управляет не обязательно всеми дочерними вью. Если реализация {@link CollectionView}
     * предполагает наличие одиночных элементов (например, заголовок в начале или счетчик в конце),
     * диапазон дочерних вью, которыми управляет адаптер, может быть задан при помощи методов
     * {@link #getChildStartOffset()} и {@link #getChildEndOffset()}.
     * Каркас логики работы адаптера заимствован из {@link android.support.v7.widget.RecyclerView}.
     *
     * @param <VH> - тип вью-холдера для дочерних вью
     */
    abstract class Adapter<VH extends ViewHolder> {

        /**
         * Количество дочерних вью, которые могут быть спрятаты в родительском вью без удаления из него по умолчанию.
         */
        private static final int DEFAULT_STASH_SIZE = 3;

        /**
         * Коллекция вью-холдеров для дочерних вью, которые сейчас находятся в родительском вью.
         * Ключ - позиция вью среди дочерних элементов коллекции (НЕ позиция внутри {@link CollectionView}).
         */
        private final SparseArray<VH> mViewHolders = new SparseArray<>();

        /**
         * Пул для переиспользования дочерних вью.
         */
        @Nullable
        private RecycledViewPool mRecycledPool;

        /**
         * Родительская вью, дочерними вью которого управляет адаптер.
         */
        @Nullable
        private CollectionView mView;

        /**
         * Создание адаптера.
         */
        public Adapter() { }

        /**
         * Создание адаптера с привязкой к экземпляру {@link CollectionView}.
         */
        public Adapter(@NonNull CollectionView view) {
            attachView(view);
        }

        /**
         * Получить количество необходимых дочерних вью. Это количество может не совпадать
         * с количеством дочерних вью в {@link CollectionView}, т.к. реализация {@link CollectionView}
         * может содержать как дочерние элементы коллекции, так и отдельные дочерние вью
         * (например, заголовок, футер или счетчик).
         *
         * @return количество дочерних вью, которые потребуется отобразить
         */
        public abstract int getItemCount();

        /**
         * Получить тип для дочерней вью на указанной позиции.
         * По умолчанию все дочерние вью имеют одинаковый тип.
         *
         * @param position - позиция дочерней вью
         * @return число, идентифицирующее тип вью
         */
        protected int getItemViewType(int position) {
            return 0;
        }

        /**
         * Получить смещение коллекции дочерних вью внутри родительской от начала {@link CollectionView}.
         * Переопределите этот метод, если перед коллекцией в родительской {@link CollectionView}
         * есть одиночные дочерние вью перед коллекцией (например, заголовок).
         *
         * @return количество дочерних вью в родительской ДО дочерних вью коллекции
         */
        protected int getChildStartOffset() {
            return 0;
        }

        /**
         * Колучить смещение коллекции дочерних вью внутри родительской от конца {@link CollectionView}.
         * Переопределите этот метод, если после коллекции в родительской {@link CollectionView}
         * есть одиночные дочерние вью после коллекции (например, счетчик "еще").
         *
         * @return количество дочерних вью в родительской ПОСЛЕ дочерних вью коллекции
         */
        protected int getChildEndOffset() {
            return 0;
        }

        // region Attach/Detach logic

        /**
         * Присоединить экземпляр {@link CollectionView} к адаптеру.
         *
         * @param view - экземпляр вью
         */
        @MainThread
        public void attachView(@NonNull CollectionView view) {
            if (mView == view) {
                return;
            }
            mView = view;
            notifyDataChanged();
        }

        /**
         * Отсоединить адаптер от {@link CollectionView}.
         */
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
                    recycleViewHolder(holder);
                }
            }
            // Удаляем все вью из родительского вью
            mViewHolders.clear();
            mView.removeViewsInLayout(getChildStartOffset(), mView.getChildCount() - getChildEndOffset());
            mView = null;
        }

        // endregion

        /**
         * Уведомить адаптер об изменении данных для перепривязки дочерних вью.
         * Данный метод выполняет досоздание/удаление/скрытие/переиспользование
         * дочерних вью коллекции и перепривязывает к ним данные, после чего
         * вызывает {@link #requestLayout()} для перестроения макета.
         */
        @CallSuper
        public void notifyDataChanged() {
            final CollectionView view = mView;
            if (view == null) {
                return;
            }
            final int count = getItemCount();
            final int childOffset = getChildStartOffset();
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
            final int end = view.getChildCount() - getChildEndOffset() - childOffset;
            if (start < end) {
                for (int i = start; i < end; ++i) {
                    final VH holder = getViewHolder(i);
                    if (holder != null) {
                        recycleViewHolder(holder);
                        mViewHolders.delete(i);
                    }
                }
                view.removeViewsInLayout(start + getChildStartOffset(), end - start);
            }
            // Привязываем коллекцию данных к вью-холдерам
            for (int i = 0; i < count; ++i) {
                final VH holder = getViewHolderForPosition(view, i, childOffset);
                if (holder == null) {
                    throw new IllegalStateException("View holder for " + i + " position is null.");
                }
                bindViewHolder(holder, i);
            }
            view.requestLayout();
        }

        // region Holder relevant methods

        /**
         * Получить вью холдер для указанной позиции в коллекции.
         *
         * @param position - позиция в коллекции
         * @return экземпляр вью-холдера или null, если вью-холдер
         * для указанной позиции не найден
         */
        public final VH getViewHolder(int position) {
            return mViewHolders.get(position);
        }

        /**
         * Получить правильный вью-холдер для указанной позиции. В случае, если вью-холдер на
         * указанной позции подходит по типу, он будет переиспользован, иначе старый вью-холлдер
         * будет отдан в пул для переиспользования, а новый будет получен из {@link #mRecycledPool},
         * либо создан методом {@link #onCreateViewHolder(CollectionView, int)}.
         *
         * @param parent        - родительская вью
         * @param position      - позиция, для которой необходимо получить вью-холдер
         * @param childOffset   - смещение дочерних вью коллекции внутри родительской вью
         * @return подготовленный к использованию экземпляр вью-холдера правильного типа
         */
        private VH getViewHolderForPosition(@NonNull CollectionView parent, int position, int childOffset) {
            // Получаем тип элемента
            final int type = getItemViewType(position);
            // Получаем вью-холдер, который на данный момент на указанной позиции
            VH holder = getViewHolder(position);
            boolean useExists = false;
            if (holder != null) {
                if (holder.getViewType() == type) {
                    // Тип вью-холдера совпадает с типом элемента
                    useExists = true;
                    prepareViewHolder(parent, holder);
                } else {
                    // Вью-холдер не подходит по типу
                    recycleViewHolder(holder);
                    parent.removeViewInLayout(holder.view);
                }
            }
            if (!useExists) {
                // Создаем новый вью-холдер и добавляем его в родительский вью
                holder = createViewHolder(parent, type);
                parent.addChildInLayout(holder.view, position + childOffset);
                prepareViewHolder(parent, holder);
                mViewHolders.put(position, holder);
            }
            return holder;
        }

        /**
         * Создать вью-холдер указанного типа. Если в {@link #mRecycledPool}
         * есть подходящий вью-холдер, он будет переиспользован. Иначе будет создан
         * новый методом {@link #onCreateViewHolder(CollectionView, int)}.
         *
         * @param parent    - родительская вью
         * @param viewType  - тип вью
         * @return экземпляр вью-холдера указанного типа
         */
        @NonNull
        private VH createViewHolder(@NonNull CollectionView parent, int viewType) {
            ViewHolder recycled = getRecycledViewPool().getRecycledView(viewType);
            if (recycled != null) {
                if (Environment.LOGGING_ENABLED) {
                    Environment.log("View holder of type " + viewType + " taken from pool " + getRecycledViewPool() + ".");
                }
                //noinspection unchecked
                return (VH) recycled;
            }
            if (Environment.LOGGING_ENABLED) {
                Environment.log("View holder of type " + viewType + " created by adapter.");
            }
            VH holder = onCreateViewHolder(parent, viewType);
            holder.setViewType(viewType);
            return holder;
        }

        /**
         * Создать новый экземпляр вью-холерда указанного типа.
         *
         * @param parent    - родительская вью
         * @param viewType  - тип вью
         * @return экземпляр вью-холдера
         */
        @NonNull
        protected abstract VH onCreateViewHolder(@NonNull CollectionView parent, int viewType);

        /**
         * Привязать данные к вью холдеру на указанной позиции.
         *
         * @param holder    - вью-холдер
         * @param position  - позиция вью-холдера в коллекции
         */
        private void bindViewHolder(@NonNull VH holder, int position) {
            onBindViewHolder(holder, position);
            holder.setAdapterPosition(position);
        }

        /**
         * Привязать данные к вью холдеру на указанной позиции.
         *
         * @param holder    - вью-холдер
         * @param position  - позиция вью-холдера в коллекции
         */
        protected abstract void onBindViewHolder(@NonNull VH holder, int position);

        /**
         * Подготовить вью-холдер к использованию.
         *
         * @param parent    - родительская вью
         * @param holder    - вью-холдер дочерней вью
         */
        private void prepareViewHolder(@NonNull CollectionView parent, @NonNull VH holder) {
            holder.prepare();
            onPrepareViewHolder(parent, holder);
        }

        /**
         * Подготовить вью-холдер к использованию.
         *
         * @param parent    - родительская вью
         * @param holder    - вью-холдер дочерней вью
         */
        protected void onPrepareViewHolder(@NonNull CollectionView parent, @NonNull VH holder) {
            // do nothing
        }

        /**
         * Освободить ресурсы, захваченные вью-холдером
         * и поместить в пул для переиспользования.
         *
         * @param holder - вью-холдер
         */
        private void recycleViewHolder(@NonNull VH holder) {
            onRecycleViewHolder(holder);
            holder.onRecycle();
            holder.setAdapterPosition(NO_POSITION);
            getRecycledViewPool().putRecycledView(holder);
        }

        /**
         * Обработать событие передачи вью-холдера на переиспользование.
         *
         * @param holder - вью-холдер для переиспользования
         */
        protected void onRecycleViewHolder(@NonNull VH holder) {
            // do nothing
        }

        /**
         * Скрыть вью, которое содержит указанный вью-холдер, без удаления из макета.
         *
         * @param holder - вью-холдер, который необходимо скрыть
         */
        private void stashViewHolder(@NonNull VH holder) {
            holder.stash();
        }

        // endregion

        // region Recycling relevant methods

        /**
         * Получить максимальное количество дочерних вью, которые
         * могут быть скрыты без удаления из макета.
         */
        protected int getStashSize() {
            return DEFAULT_STASH_SIZE;
        }

        /**
         * Получить пул для переиспользованных вью-холдеров.
         */
        @NonNull
        public RecycledViewPool getRecycledViewPool() {
            if (mRecycledPool == null) {
                mRecycledPool = new RecycledViewPool();
            }
            return mRecycledPool;
        }

        /**
         * Задать пул для переиспользованных вью-холдеров.
         */
        public void setRecycledViewPool(@Nullable RecycledViewPool pool) {
            mRecycledPool = pool;
        }

        // endregion

    }

    /**
     * Базовый класс вью-ходера для дочерней вью из коллекции.
     */
    abstract class ViewHolder {

        /**
         * Экземпляр {@link View}, которые содержит вью-холдер.
         */
        public final View view;

        /**
         * Тип вью.
         */
        private int viewType;

        /**
         * Находится ли вью в скрытом состоянии.
         */
        private boolean inStash;

        /**
         * Видимость вью до скрытия.
         */
        private int beforeStashVisibility;

        /**
         * Позиция вью-холдера в адаптере.
         */
        private int adapterPosition = NO_POSITION;

        public ViewHolder(View view) {
            if (view == null) {
                throw new IllegalArgumentException("View may not be null.");
            }
            this.view = view;
        }

        /**
         * Получить тип вью.
         */
        public final int getViewType() {
            return viewType;
        }

        /**
         * Задать тип вью.
         */
        void setViewType(int viewType) {
            this.viewType = viewType;
        }

        /**
         * Получить позицию вью-холдера в адаптере.
         */
        public final int getAdapterPosition() {
            return adapterPosition;
        }

        /**
         * Задать позицию вью-холдера в адаптере.
         * @param position - позиция
         */
        void setAdapterPosition(int position) {
            this.adapterPosition = position;
        }

        /**
         * Подготовить вью-холдер к использованию в коллекции.
         */
        @CallSuper
        public void prepare() {
            if (inStash) {
                inStash = false;
                view.setVisibility(beforeStashVisibility);
            }
        }

        /**
         * Спрятать вью-холдер и ассоциированную вью.
         */
        public void stash() {
            if (!inStash) {
                inStash = true;
                beforeStashVisibility = view.getVisibility();
                view.setVisibility(View.GONE);
            }
        }

        /**
         * Освободить ресурсы, захваченные вью-холдером,
         * перед перемещением в пул.
         */
        public void onRecycle() {
            // do nothing
        }

    }

    /**
     * Пул для переиспользованных вью-холдеров.
     */
    class RecycledViewPool {
        /**
         * Максимальное количество вью-холдеров одного типа, которые
         * могут храниться в пуле по умолчанию.
         */
        private static final int DEFAULT_MAX_SCRAP = 5;

        /**
         * Данные о хранящихся в пуле вью-холдеров конкретного типа.
         */
        private static class ScrapData {
            /**
             * Вью-холдеры для переиспользования.
             */
            final ArrayList<ViewHolder> mScrapHeap = new ArrayList<>();
            /**
             * Максимальное количество вью-холдеров в пуле.
             */
            int mMaxScrap = DEFAULT_MAX_SCRAP;
        }

        /**
         * Ассоциативный массив, где ключ - это тип вью, а значение -
         * это {@link ScrapData} для этого типа вью.
         */
        private SparseArray<ScrapData> mScrap = new SparseArray<>();

        /**
         * Удалить все вью-холдеры из пула.
         */
        public void clear() {
            for (int i = 0; i < mScrap.size(); i++) {
                RecycledViewPool.ScrapData data = mScrap.valueAt(i);
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
            final RecycledViewPool.ScrapData scrapData = mScrap.get(viewType);
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
                if (Environment.LOGGING_ENABLED) {
                    Environment.log("View holder of type " + viewType + " removed because pool " + this + " is full.");
                }
                return;
            }
            if (Environment.DEBUG && scrapHeap.contains(scrap)) {
                throw new IllegalArgumentException("this scrap item already exists");
            }
            scrapHeap.add(scrap);
        }

        /**
         * Получить {@link ScrapData} для указанного типа вью. Если
         * в {@link #mScrap} нет ScrapData для указанного типа, будет создан
         * новый экземпляр ScrapData.
         *
         * @param viewType - тип вью
         * @return экземпляр ScrapData
         */
        private RecycledViewPool.ScrapData getScrapDataForType(int viewType) {
            RecycledViewPool.ScrapData scrapData = mScrap.get(viewType);
            if (scrapData == null) {
                scrapData = new RecycledViewPool.ScrapData();
                mScrap.put(viewType, scrapData);
            }
            return scrapData;
        }

    }

}
