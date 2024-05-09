package com.tupleinfotech.demoapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class BarcodeBoxView : View {

    private val paint = Paint()
    private val barcodeRects = mutableListOf<RectF>()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val cornerRadius = 20f

        paint.style = Paint.Style.FILL
        paint.color = Color.GREEN
        paint.alpha = 50
        paint.strokeWidth = 5f

        barcodeRects.forEach { rect ->
            canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)
        }
    }

    fun setRects(rects: List<RectF>) {
        barcodeRects.clear()
        barcodeRects.addAll(rects)
        invalidate()
    }

}

