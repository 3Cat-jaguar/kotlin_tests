package com.nyanyajaguar.a08_dice

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Created by SJW on 2017. 10. 30..
 */
class dice : View {
    constructor(context : Context) : super(context) {
    }

    constructor(context : Context, attrs: AttributeSet) : super(context, attrs) {
    }

    var num = 1

    fun set_number(n:Int) {
        num=n
        invalidate()
    }

    override fun onDraw(canvas : Canvas) {
        super.onDraw(canvas)

        var p = Paint()
        p.setARGB(255, 255, 0, 0)

        var size = 0.1f * width
        var x1 = 0.2f * width
        var x2 = 0.5f * width
        var x3 = 0.8f * width

        if (num == 1) {
            canvas.drawCircle(x2, x2, size, p)
        } else if (num == 2) {
            canvas.drawCircle(x1, x1, size, p)
            canvas.drawCircle(x3, x3, size, p)
        } else if (num == 3) {
            canvas.drawCircle(x1, x1, size, p)
            canvas.drawCircle(x2, x2, size, p)
            canvas.drawCircle(x3, x3, size, p)
        } else if (num == 4) {
            canvas.drawCircle(x1, x1, size, p)
            canvas.drawCircle(x1, x3, size, p)
            canvas.drawCircle(x3, x1, size, p)
            canvas.drawCircle(x3, x3, size, p)
        } else if (num == 5) {
            canvas.drawCircle(x1, x1, size, p)
            canvas.drawCircle(x1, x3, size, p)
            canvas.drawCircle(x2, x2, size, p)
            canvas.drawCircle(x3, x1, size, p)
            canvas.drawCircle(x3, x3, size, p)
        } else if (num == 6) {
            canvas.drawCircle(x1, x1, size, p)
            canvas.drawCircle(x1, x2, size, p)
            canvas.drawCircle(x1, x3, size, p)
            canvas.drawCircle(x3, x1, size, p)
            canvas.drawCircle(x3, x2, size, p)
            canvas.drawCircle(x3, x3, size, p)
        }
    }
}