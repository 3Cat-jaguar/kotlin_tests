package com.nyanyajaguar.qybg

/**
 * Created by SJW on 2018. 8. 17..
 */
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton

class CustomImageButton(context: Context, attrs: AttributeSet) : ImageButton(context, attrs) {

    private var backgroundResourceId = View.NO_ID
    private var imageResourceId = View.NO_ID

    init {
        //custom attrs load
        context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.CustomImageButton,
                0, 0).apply {

            try {
                val bgResId = getResourceId(R.styleable.CustomImageButton_backgroundResource, 0)
                if (bgResId != 0) {
                    setBackgroundResource(bgResId)
                }
            } finally {
                recycle()
            }
        }
    }

    override fun setBackgroundResource(resid: Int) {
        super.setBackgroundResource(resid)
        backgroundResourceId = resid
    }
    override fun setImageResource(resid: Int) {
        super.setImageResource(resid)
        imageResourceId = resid
    }

    fun sameCheck(checkeResId: Int) = backgroundResourceId == checkeResId
    fun sameCheck2(checkeResId: Int) = imageResourceId == checkeResId
}