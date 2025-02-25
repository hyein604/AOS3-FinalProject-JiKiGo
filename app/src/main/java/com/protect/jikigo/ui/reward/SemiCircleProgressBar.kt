package com.protect.jikigo.ui.reward

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class SemiCircleProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var progress = 0
    private val maxProgress = 100
    private val strokeWidth = 30f  // 두께 조정

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.LTGRAY
        style = Paint.Style.STROKE
        this.strokeWidth = this@SemiCircleProgressBar.strokeWidth
        strokeCap = Paint.Cap.ROUND
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#7A8FFF")
        style = Paint.Style.STROKE
        this.strokeWidth = this@SemiCircleProgressBar.strokeWidth
        strokeCap = Paint.Cap.ROUND
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = MeasureSpec.getSize(widthMeasureSpec)  // 주어진 가로 크기
        val desiredHeight = (desiredWidth / 2).toInt()  // 반원의 높이로 설정
        setMeasuredDimension(desiredWidth, desiredHeight)  // 최종 크기 설정
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width.toFloat()
        val height = height.toFloat()
        val size = width * 0.9f  // 크기를 너무 크게 만들지 않음

        val left = (width - size) / 2
        val top = strokeWidth / 2  // 선 두께를 고려한 위치 조정
        val right = left + size
        val bottom = top + size

        val rectF = RectF(left, top, right, bottom)

        // 배경 아크
        canvas.drawArc(rectF, 180f, 180f, false, backgroundPaint)

        // 진행률 아크
        val sweepAngle = (progress / maxProgress.toFloat()) * 180f
        canvas.drawArc(rectF, 180f, sweepAngle, false, progressPaint)
    }

    fun setProgress(value: Int) {
        progress = value.coerceIn(0, maxProgress)
        invalidate()
    }
}
