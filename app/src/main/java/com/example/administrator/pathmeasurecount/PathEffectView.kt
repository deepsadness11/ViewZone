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
class PathEffectView : View {
    constructor(context: Context) : super(context) {
        initPaints()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initPaints()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initPaints()
    }

    lateinit var paint: Paint
    lateinit var paint2: Paint
    lateinit var paint3: Paint
    lateinit var paint4: Paint
    lateinit var path: Path
    lateinit var path2: Path

    lateinit var p0: Point
    lateinit var p1: Point
    lateinit var p2: Point
    lateinit var p3: Point

    lateinit var ctrl1: Point
    lateinit var ctrl2: Point
    lateinit var ctrl3: Point
    lateinit var ctrl4: Point
    lateinit var ctrl5: Point
    lateinit var ctrl6: Point
    lateinit var ctrl7: Point
    lateinit var ctrl8: Point

    lateinit var mPathMeasure: PathMeasure
    lateinit var mEffect: DashPathEffect
    lateinit var mEffect2: DashPathEffect


    val radius = 100
    private val blackMagic = 0.551915024494f
    var pathLength = 0f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        println("measuredHeight=$measuredHeight")
        println("measuredWidth=$measuredWidth")

        initCirclePoints()
    }

    /**
     *   初始化画圆需要的点
     *   mPath.moveTo(p0_State1.x,p0_State1.y);
     *   mPath.cubicTo(p1_State1.x, p1_State1.y, p2_State1.x, p2_State1.y, p3.x,p3.y);
     *   path.close()
     *   (4/3)*tan(pi/8) = 4*(sqrt(2)-1)/3
     */
    private fun initCirclePoints() {
        val centerPoint = Point(measuredWidth / 2, measuredHeight / 2)
        //赛贝尔曲线需要的偏移量M
        var distance = 4 * (Math.sqrt(2.0) - 1) / 3
        val distanceM = blackMagic * radius

        p0 = Point(centerPoint.x, centerPoint.y + radius)
        p1 = Point(centerPoint.x + radius, centerPoint.y)
        p2 = Point(centerPoint.x, centerPoint.y - radius)
        p3 = Point(centerPoint.x - radius, centerPoint.y)


        ctrl1 = Point((p0.x + distanceM).toInt(), p0.y)
        ctrl2 = Point(p1.x, (p1.y + distanceM).toInt())

        ctrl3 = Point(p1.x, (p1.y - distanceM).toInt())
        ctrl4 = Point((p2.x + distanceM).toInt(), p2.y)

        ctrl5 = Point((p2.x - distanceM).toInt(), p2.y)
        ctrl6 = Point(p3.x, (p3.y - distanceM).toInt())

        ctrl7 = Point(p3.x, (p3.y + distanceM).toInt())
        ctrl8 = Point((p0.x - distanceM).toInt(), p0.y)

        path = Path()
        path.moveTo(p0.x * 1.0f, p0.y * 1.0f)
//        path.lineTo(p0_State1.x * 1.0f, p0_State1.y * 1.0f)
        path.cubicTo(ctrl1.x * 1.0f, ctrl1.y * 1.0f, ctrl2.x * 1.0f, ctrl2.y * 1.0f, p1.x * 1.0f, p1.y * 1.0f)
        path.cubicTo(ctrl3.x * 1.0f, ctrl3.y * 1.0f, ctrl4.x * 1.0f, ctrl4.y * 1.0f, p2.x * 1.0f, p2.y * 1.0f)
        path.cubicTo(ctrl5.x * 1.0f, ctrl5.y * 1.0f, ctrl6.x * 1.0f, ctrl6.y * 1.0f, p3.x * 1.0f, p3.y * 1.0f)
        path.cubicTo(ctrl7.x * 1.0f, ctrl7.y * 1.0f, ctrl8.x * 1.0f, ctrl8.y * 1.0f, p0.x * 1.0f, p0.y * 1.0f)

        mPathMeasure = PathMeasure(path, false)
        pathLength = mPathMeasure.getLength()

        //辅助线
        path2 = Path()
        path2.moveTo(0f, measuredHeight / 2f)
        path2.lineTo(measuredWidth.toFloat(), measuredHeight / 2f)
        mEffect2 = DashPathEffect(floatArrayOf(measuredWidth / 2f, measuredWidth / 4f), 0f)
        paint4.setPathEffect(mEffect2)
    }

    private fun initPaints() {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f

        paint2 = Paint(Paint.ANTI_ALIAS_FLAG)
        paint2.style = Paint.Style.FILL
        paint2.color = Color.RED

        paint3 = Paint(Paint.ANTI_ALIAS_FLAG)
        paint3.style = Paint.Style.FILL
        paint3.color = Color.BLUE

        paint4 = Paint(Paint.ANTI_ALIAS_FLAG)
        paint4.style = Paint.Style.STROKE
        paint4.color = Color.YELLOW
        paint4.strokeWidth = 4f

        path = Path()

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //画辅助线
//        canvas.drawLine(0f, measuredHeight / 2f, measuredWidth.toFloat(), measuredHeight / 2f, paint4)
        canvas.drawLine(measuredWidth / 2f, 0f, measuredWidth / 2f, measuredHeight.toFloat(), paint4)
        canvas.drawPath(path2, paint4)

        //辅助点
        canvas.drawCircle(p0.x.toFloat(), p0.y.toFloat(), 10f, paint2)
        canvas.drawCircle(p1.x.toFloat(), p1.y.toFloat(), 10f, paint2)
        canvas.drawCircle(p2.x.toFloat(), p2.y.toFloat(), 10f, paint2)
        canvas.drawCircle(p3.x.toFloat(), p3.y.toFloat(), 10f, paint2)

        canvas.drawCircle(ctrl1.x.toFloat(), ctrl1.y.toFloat(), 8f, paint3)
        canvas.drawCircle(ctrl2.x.toFloat(), ctrl2.y.toFloat(), 8f, paint3)
        canvas.drawCircle(ctrl3.x.toFloat(), ctrl3.y.toFloat(), 8f, paint3)
        canvas.drawCircle(ctrl4.x.toFloat(), ctrl4.y.toFloat(), 8f, paint3)
        canvas.drawCircle(ctrl5.x.toFloat(), ctrl5.y.toFloat(), 8f, paint3)
        canvas.drawCircle(ctrl6.x.toFloat(), ctrl6.y.toFloat(), 8f, paint3)
        canvas.drawCircle(ctrl7.x.toFloat(), ctrl7.y.toFloat(), 8f, paint3)
        canvas.drawCircle(ctrl8.x.toFloat(), ctrl8.y.toFloat(), 8f, paint3)
        //画圆
        canvas.drawPath(path, paint)
    }

    var fraction: Float = 0f

    fun startAnimator(): Unit {
        val mAnimator = ValueAnimator.ofFloat(1f, 0f)
        mAnimator.interpolator = AccelerateDecelerateInterpolator()
        mAnimator.duration = 2000
        mAnimator.repeatMode = ValueAnimator.RESTART
        mAnimator.repeatCount = ValueAnimator.INFINITE
        mAnimator.addUpdateListener(ValueAnimator.AnimatorUpdateListener { valueAnimator ->
            val fraction = valueAnimator.animatedValue as Float
            mEffect = DashPathEffect(floatArrayOf(pathLength, pathLength / 4 * 3, pathLength / 2, pathLength / 4, pathLength / 5), fraction * pathLength)
            paint.setPathEffect(mEffect)
            invalidate()
        })
        mAnimator.start()
    }

}