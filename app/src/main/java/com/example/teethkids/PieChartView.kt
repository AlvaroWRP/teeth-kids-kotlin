package com.example.teethkids

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class PieChartView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val goodColor = Color.GREEN
    private val badColor = Color.RED
    private var goodPercentage: Float = 0f
    private var badPercentage: Float = 0f

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectF = RectF()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (width / 2f) - 10 // Adjust the thickness of the pie chart here

        // Draw good arc
        paint.color = goodColor
        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
        canvas.drawArc(rectF, -90f, goodPercentage * 360f, true, paint)

        // Draw bad arc
        paint.color = badColor
        canvas.drawArc(rectF, -90f + (goodPercentage * 360f), badPercentage * 360f, true, paint)
    }

    fun setPercentage(goodPercentage: Float, badPercentage: Float) {
        this.goodPercentage = goodPercentage
        this.badPercentage = badPercentage
        invalidate()
    }
}
