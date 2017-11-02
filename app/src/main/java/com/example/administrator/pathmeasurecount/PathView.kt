package com.example.administrator.pathmeasurecount

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator


/**
 * Created by Administrator on 2017/11/1 0001.
 */
class PathView : View {

    constructor(context: Context) : super(context) {
        initPaints()
    }


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initPaints()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initPaints()
    }

    private lateinit var paint: Paint
    private lateinit var path: Path
    private lateinit var pathMeasure: PathMeasure
    private lateinit var mMeasure: PathMeasure
    private lateinit var destPath: Path
    private lateinit var linePath: Path
    private var scale: Float = 0f
    private var radius = 80f

    private var h: Int = 0
    private var w: Int = 0
    private var currentValue: Float = 0f
    private var pos = FloatArray(2)
    private var tan = FloatArray(2)

    private fun initPaints() {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f
        path = Path()
        path.moveTo(0f, 0f)
        destPath = Path()
        linePath = Path()

        pathMeasure = PathMeasure(path, false)
        mMeasure = PathMeasure()


        var animator = ValueAnimator.ofFloat(0f, 360f)
        animator.repeatCount = ValueAnimator.INFINITE
        animator.repeatMode = ValueAnimator.RESTART
        animator.duration = 2000
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addUpdateListener { animation ->
            scale = animation.animatedFraction
//            mEffect = DashPathEffect(floatArrayOf(length, length), scale * length)
//            mPaint.setPathEffect(mEffect)
            postInvalidate()
        }
        animator.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        h = measuredHeight
        w = measuredWidth
        println("measuredHeight=$h")
        println("measuredWidth=$w")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.color = Color.BLACK
        path.addCircle(w / 2f, h / 2f, radius, Path.Direction.CCW)
        canvas.drawPath(path, paint)
        pathMeasure.setPath(path, false)

        destPath.reset()
//        destPath.lineTo(0f, 0f);
        var length = pathMeasure.length
        var stop = length * scale
        val start = (stop - (0.5 - Math.abs(scale - 0.5)) * length).toFloat()
        pathMeasure.getSegment(start, stop, destPath, true)
        paint.color = Color.GREEN
        canvas.drawPath(destPath, paint)

        mMeasure.setPath(path, false)

        mMeasure.getPosTan(mMeasure.getLength() * scale, pos, tan);
        val degrees = (Math.atan2(tan[1].toDouble(), tan[0].toDouble()) * 180.0 / Math.PI);
//        println("degrees=$degrees")
        canvas.save();
//        linePath.reset()
        linePath.moveTo(measuredWidth / 2f, measuredHeight / 2f)
        linePath.rLineTo(pos[0] * tan[0], pos[1] * tan[1])
//        linePath.rLineTo(pos[0]+10, pos[1]+10)
//        canvas.translate(400, 400);
//        canvas.drawPath(mPath, mPaint);

        paint.color = Color.YELLOW
//        canvas.drawCircle(pos[0], pos[1], 10f, paint);
//        canvas.drawPath(linePath, paint)
        canvas.translate(measuredWidth / 2f, measuredHeight / 2f)
        canvas.rotate(degrees.toFloat())
        canvas.drawLine(0f, -radius, 300f, -radius, paint)

//        canvas.drawLine(pos[0], pos[1], pos[0], pos[1]+200*tan[1], paint);
        canvas.restore();

    }

}