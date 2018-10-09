package ru.acelost.collectionadapter.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import ru.acelost.collectionadapter.R
import ru.acelost.collectionadapter.ViewModelAdapter
import ru.acelost.collectionadapter.measurement.Measurement
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException

class MeasuredRecyclerView : RecyclerView {

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs){
        extractAdapter(attrs, context, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        extractAdapter(attrs, context, defStyle)
    }

    private fun extractAdapter(attrs: AttributeSet?, context: Context, defStyle: Int) {
        if (attrs != null) {
            val defStyleRes = 0
            val a = context.obtainStyledAttributes(attrs, R.styleable.MeasuredRecyclerView,
                    defStyle, defStyleRes)
            val layoutManagerName = a.getString(R.styleable.MeasuredRecyclerView_adapter)

            a.recycle()
            createAdapter(context, layoutManagerName, attrs)
        }
    }

    private fun createAdapter(context: Context, className: String?, attrs: AttributeSet) {
        var className = className
        if (className != null) {
            className = className.trim { it <= ' ' }
            if (!className.isEmpty()) {
                className = getFullClassName(context, className)
                try {
                    val classLoader: ClassLoader
                    if (isInEditMode) {
                        // Stupid layoutlib cannot handle simple class loaders.
                        classLoader = this.javaClass.classLoader
                    } else {
                        classLoader = context.classLoader
                    }
                    val layoutManagerClass = classLoader.loadClass(className).asSubclass(ViewModelAdapter::class.java)
                    val constructor: Constructor<out ViewModelAdapter>
                    constructor = try {
                        layoutManagerClass
                                .getConstructor()
                    } catch (e: NoSuchMethodException) {
                        try {
                            layoutManagerClass.getConstructor()
                        } catch (e1: NoSuchMethodException) {
                            e1.initCause(e)
                            throw IllegalStateException(attrs.positionDescription
                                    + ": Error creating LayoutManager " + className, e1)
                        }

                    }

                    constructor.isAccessible = true
                    adapter = constructor.newInstance()
                } catch (e: ClassNotFoundException) {
                    throw IllegalStateException(attrs.positionDescription
                            + ": Unable to find LayoutManager " + className, e)
                } catch (e: InvocationTargetException) {
                    throw IllegalStateException(attrs.positionDescription
                            + ": Could not instantiate the LayoutManager: " + className, e)
                } catch (e: InstantiationException) {
                    throw IllegalStateException(attrs.positionDescription
                            + ": Could not instantiate the LayoutManager: " + className, e)
                } catch (e: IllegalAccessException) {
                    throw IllegalStateException(attrs.positionDescription
                            + ": Cannot access non-public constructor " + className, e)
                } catch (e: ClassCastException) {
                    throw IllegalStateException(attrs.positionDescription
                            + ": Class is not a LayoutManager " + className, e)
                }

            }
        }
    }

    private fun getFullClassName(context: Context, className: String): String {
        if (className[0] == '.') {
            return context.packageName + className
        }
        return if (className.contains(".")) {
            className
        } else RecyclerView::class.java.getPackage().name + '.'.toString() + className
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        val start = System.currentTimeMillis()
        super.onMeasure(widthSpec, heightSpec)
        val duration = System.currentTimeMillis() - start
        Measurement.getInstance().increment("RecyclerMeasure", duration.toInt())
        Measurement.getInstance().printCounters()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val start = System.currentTimeMillis()
        super.onLayout(changed, l, t, r, b)
        val duration = System.currentTimeMillis() - start
        Measurement.getInstance().increment("RecyclerLayout", duration.toInt())
    }

}