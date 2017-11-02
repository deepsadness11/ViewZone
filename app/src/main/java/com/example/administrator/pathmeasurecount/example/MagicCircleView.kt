package com.example.administrator.pathmeasurecount.example

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

/**
 * 学习Bazier曲线的View
 * Created by Administrator on 2017/11/1 0001.
 */
class MagicCircleView : View {
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

    private fun initPaints() {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.FILL
        paint.color = Color.RED
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

        //辅助点
//        canvas.drawCircle(p0_State1.x.toFloat(), p0_State1.y.toFloat(), 10f, paint2)
//        canvas.drawCircle(p1_State1.x.toFloat(), p1_State1.y.toFloat(), 10f, paint2)
//        canvas.drawCircle(p2_State1.x.toFloat(), p2_State1.y.toFloat(), 10f, paint2)
//        canvas.drawCircle(p3.x.toFloat(), p3.y.toFloat(), 10f, paint2)
//
//        canvas.drawCircle(ctrl1.x.toFloat(), ctrl1.y.toFloat(), 8f, paint3)
//        canvas.drawCircle(ctrl2.x.toFloat(), ctrl2.y.toFloat(), 8f, paint3)
//        canvas.drawCircle(ctrl3.x.toFloat(), ctrl3.y.toFloat(), 8f, paint3)
//        canvas.drawCircle(ctrl4.x.toFloat(), ctrl4.y.toFloat(), 8f, paint3)
//        canvas.drawCircle(ctrl5.x.toFloat(), ctrl5.y.toFloat(), 8f, paint3)
//        canvas.drawCircle(ctrl6.x.toFloat(), ctrl6.y.toFloat(), 8f, paint3)
//        canvas.drawCircle(ctrl7.x.toFloat(), ctrl7.y.toFloat(), 8f, paint3)
//        canvas.drawCircle(ctrl8.x.toFloat(), ctrl8.y.toFloat(), 8f, paint3)
        //画圆
        canvas.drawPath(path, paint)
    }

    var fraction: Float = 0f
    var moveX: Int = 0

    fun startAnimator(): Unit {
        val mAnimator = ValueAnimator.ofFloat(1f, 0f)
        mAnimator.interpolator = AccelerateDecelerateInterpolator()
        mAnimator.duration = 8000
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

    var totalMoveDistance = 0;

    fun startAnimator2() {
        val targetTransLationX = 200 * 2
        //因为最后到达的位置只是原来的0.8所以将长度扩展
        totalMoveDistance = (targetTransLationX / 0.8f).toInt()
        val mAnimator = ValueAnimator.ofInt(0, totalMoveDistance)
//        val mAnimator = ValueAnimator.ofInt(0, totalMoveDistance, -totalMoveDistance)
        mAnimator.interpolator = AccelerateDecelerateInterpolator()
        mAnimator.duration = 500
        mAnimator.repeatMode = ValueAnimator.REVERSE
//        mAnimator.repeatCount = ValueAnimator.INFINITE
        mAnimator.addUpdateListener { valueAnimator ->
            var i = valueAnimator.animatedValue as Int
            moveX = i
            //偏移点
            offsetPoints()
            invalidate()
        }
        mAnimator.start()
    }

    /**
     *   初始化画圆需要的点
     *   mPath.moveTo(p0_State1.x,p0_State1.y);
     *   mPath.cubicTo(p1_State1.x, p1_State1.y, p2_State1.x, p2_State1.y, p3.x,p3.y);
     *   path.close()
     *   (4/3)*tan(pi/8) = 4*(sqrt(2)-1)/3
     */
    private fun initCirclePoints() {
        val centerPoint = Point(radius + 20, measuredHeight / 2)
        //赛贝尔曲线需要的偏移量M
//        var distance = 4 * (Math.sqrt(2.0) - 1) / 3
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

    }

    private fun offsetPoints() {
        if (p0 == null) {
            return
        }
        val centerPoint = Point(radius + 20, measuredHeight / 2)
        var distanceM = blackMagic * radius
        val count = 12
        //有四种状态，分为4个阶段
        val initPart = totalMoveDistance/12
        val finalPart = totalMoveDistance - initPart
        val startPart = initPart*2
        val endPart = totalMoveDistance - startPart
        val maxTransXBeforeMoveTogether = radius / 2

        val changeScale = 0.15f

        println("initPart=$initPart")

        if (Math.abs(moveX) in 0..initPart) {
            /*
                p1移动
                其他不动
                 */
            p0 = Point(centerPoint.x, centerPoint.y + radius)
            p1 = Point(centerPoint.x + radius + moveX, centerPoint.y)
            p2 = Point(centerPoint.x, centerPoint.y - radius)
            p3 = Point(centerPoint.x - radius, centerPoint.y)
        } else if (Math.abs(moveX) in initPart..startPart) {
            /*
                p3不动,
                p0_State1,p1_State1,p2移动
                 */
            var scaleX = Math.abs(moveX - initPart) * 1f / initPart
            scaleX=1f
            var scaleM = scaleX * changeScale
            distanceM = distanceM * (1 + scaleM)
            p1 = Point(centerPoint.x + radius + moveX, centerPoint.y)
            p0 = Point((centerPoint.x + moveX - initPart * scaleX).toInt(), centerPoint.y + radius)
            p2 = Point((centerPoint.x + moveX - initPart * scaleX).toInt(), centerPoint.y - radius)
            p3 = Point(centerPoint.x - radius, centerPoint.y)
        } else if (Math.abs(moveX) in endPart..finalPart) {
            /*
                p1不动,
                p0_State1,p3,p2移动
                 */
            var scale = changeScale - Math.abs(moveX - endPart) * 1f / initPart * changeScale
            distanceM = distanceM * (1 + scale)
//                println("p1_State1 x=${p1_State1.x * 1f - (centerPoint.x + radius)} in total =${((centerPoint.x + radius + endPart - (centerPoint.x + radius)))*1.0f / totalMoveDistance}")
            p1 = Point(centerPoint.x + radius + endPart, centerPoint.y)
            p0 = Point(centerPoint.x + moveX - initPart, centerPoint.y + radius)
            p2 = Point(centerPoint.x + moveX - initPart, centerPoint.y - radius)
            p3 = Point(centerPoint.x + moveX - initPart - radius - initPart, centerPoint.y)
        } else if (Math.abs(moveX) in finalPart..totalMoveDistance) {
            /*
                p3移动
                其他不动
                 */
            p3 = Point(centerPoint.x + moveX - initPart - radius - initPart, centerPoint.y)
//                println("final " + moveX)

        } else if (Math.abs(moveX) in startPart..endPart) {
            //先要判断出事的值，是否超过最大。操作最大,则需要计算回复
            if (initPart > maxTransXBeforeMoveTogether) {
                //需要从start到start+(initPart-maxTransXBeforeMoveTogether)

            } else {
                //一起移动
                distanceM = distanceM * (1 + changeScale)
                p1 = Point(centerPoint.x + radius + moveX, centerPoint.y)
                var min = Math.min(maxTransXBeforeMoveTogether, initPart)
                p0 = Point(p1.x - radius - min, centerPoint.y + radius)
                p2 = Point(p1.x - radius - min, centerPoint.y - radius)
//                p3 = Point(p1_State1.x - radius * 2 - startPart, centerPoint.y)
                p3 = Point(p0.x - radius - min, centerPoint.y)
            }
        }


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
    }
}