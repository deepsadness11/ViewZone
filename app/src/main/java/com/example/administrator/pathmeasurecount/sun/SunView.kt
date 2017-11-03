package com.example.administrator.pathmeasurecount.sun

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View


/**
 * Created by Administrator on 2017/11/2 0002.
 */
class SunView : View {
    constructor(context: Context) : super(context) {
        initPaints()
    }


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initPaints()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initPaints()
    }

    lateinit var mPaint: Paint
    lateinit var mPaintShadow: Paint
    lateinit var mPaintCloudShadow: Paint
    lateinit var mPaintCloud: Paint
    lateinit var mPaintSunShine: Paint
    lateinit var mPaintSunShine2: Paint
    lateinit var mPaintBG: Paint
    lateinit var mPaintLine: Paint
    lateinit var mPathLineBig: Path
    lateinit var mPathLineSmall: Path
    lateinit var mPathLineMin: Path

    private var mPathLineBigDest: Path = Path()
    private var mPathLineSmallDest: Path = Path()
    private var mPathLineMinDest: Path = Path()
    lateinit var lg: LinearGradient
    private fun initPaints() {
//        setLayerType(LAYER_TYPE_SOFTWARE, null)

        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.color = colorYellow

        mPaintShadow = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaintShadow.color = Color.parseColor("#Bac3c3")

        mPaintCloud = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaintCloud.color = Color.parseColor("#f5f3e9")

        mPaintSunShine = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaintSunShine.color = colorYellow

        mPaintSunShine2 = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaintSunShine2.color = colorYellow

        mPaintCloudShadow = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaintCloudShadow.color = Color.parseColor("#ba9630")

//        mPaint.setShadowLayer(5f, 5f, 3f, Color.GRAY)

        mPaintBG = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaintBG.color = colorBG

        mPaintLine = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaintLine.color = colorYellow
        mPaintLine.strokeJoin = Paint.Join.ROUND
        mPaintLine.strokeCap = Paint.Cap.ROUND
        mPaintLine.strokeWidth = 5f
        mPaintLine.style = Paint.Style.STROKE

        mPathLineBig = Path()
        mPathLineSmall = Path()
        mPathLineMin = Path()
    }

    var cX = measuredWidth / 2f
    var cY = measuredHeight / 2f

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        cX = measuredWidth / 2f
        cY = measuredHeight / 2f - mRadius


        mPathLineBig.moveTo(cX, cY - mRadius)
        mPathLineBig.addCircle(cX, cY, mRadius - 20f, Path.Direction.CCW)

        mPathLineMin.moveTo(cX, cY - mRadius)
        mPathLineMin.addCircle(cX, cY, mRadius - mRadius / 2, Path.Direction.CCW)

        mPathLineSmall.moveTo(cX, cY - mRadius)
        mPathLineSmall.addCircle(cX, cY, mRadius - mRadius / 4f, Path.Direction.CW)

        rectSunShine.set((cX - mRadius) * recScale, (cY - mRadius) * recScale, (cX + mRadius) * recScale, (cY + mRadius) * recScale)

        var lg = LinearGradient(rectSunShine.left, rectSunShine.top, rectSunShine.right, rectSunShine.bottom,
                intArrayOf(Color.parseColor("#ffe38e"), Color.parseColor("#b28200")),
                floatArrayOf(0f, 1f), Shader.TileMode.CLAMP)
        lg.getLocalMatrix(matrix1)
        mPaintSunShine.setShader(lg)

        val lg2 = LinearGradient(cX, cY - mRadius, cX, cY + mRadius,
                intArrayOf(Color.parseColor("#f7b600"), Color.parseColor("#ae8200")),
                floatArrayOf(0f, 1f), Shader.TileMode.CLAMP)
        lg2.setLocalMatrix(matrix2)
        mPaintSunShine2.setShader(lg2)


        mOvalRect.set(cX - mRadius - 20f, cY + mRadius / 2 * 5, cX + mRadius + 20f, cY + mRadius / 2 * 5 + 10)
        cloudRectClip = RectF(cX - mRadius / 4, cY - mRadius / 5 - mRadius / 4, cX + mRadius / 3 * 4, cX - mRadius / 8 * 5)


        var matrix = Matrix()
        val lg3 = LinearGradient(cloudRectClip.left, cloudRectClip.top, cloudRectClip.right / 2 + cloudRectClip.centerX() / 2, cloudRectClip.bottom,
                intArrayOf(Color.parseColor("#fcfbf3"), Color.parseColor("#dfdbcb")),
                floatArrayOf(0f, 1f), Shader.TileMode.CLAMP)
        var localMatrix = lg3.getLocalMatrix(matrix)
        println(matrix.toString() + "localMatrix=${localMatrix}")


        mPaintCloud.setShader(lg3)
        mPaintCloud.setMaskFilter(BlurMaskFilter(10f, BlurMaskFilter.Blur.OUTER))

        var d = cloudRectClip.bottom + mRadius * Math.abs(Math.sin(sweepAngle))
        println("cloud bottom =${d}")
        println("Math.sin(sweepAngle) =${Math.sin(sweepAngle)}")
        println("rectSunShine.bottom =${rectSunShine.bottom}")
        println("rectSunShine.bottom =${rectSunShine}")
        mCloudShadow.set((cX - mRadius) * 1f, (cY - mRadius) * 1f, (cX + mRadius) * 1f, (cY + mRadius) * 1f)

//        mCloudShadow.set(rectSunShine.left, rectSunShine.top, rectSunShine.right, rectSunShine.bottom)

    }

    var mRadius = 100f
    var mRMoveBig = 0f
    var mRMoveSmall = 10f
    var recScale = 0f

    var colorYellow = Color.parseColor("#fbcb42")
    var colorBG = Color.parseColor("#eeeeee")

    var rectSunShine: RectF = RectF()
    var mOvalRect: RectF = RectF()
    var mCloudShadow: RectF = RectF()
    var cloudRectClip = RectF()
    var cloudShadowPath = Path()

    var sweepAngle = 50.0

    private var windDistance: Float = 0f

    val matrix1 = Matrix()
    val matrix2 = Matrix()

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //太阳的边角
        canvas.save()
        canvas.translate(0f, windDistance)
        canvas.scale(recScale, recScale, cX, cY)
        canvas.rotate(0f + sunRotateAngel, cX, cY)

        matrix1.setRotate(0f - sunRotateAngel, cX, cY)
        mPaintSunShine.shader.setLocalMatrix(matrix1)

        canvas.drawRect(rectSunShine, mPaintSunShine)
        canvas.restore()

        canvas.save()
        canvas.translate(0f, windDistance)
        canvas.scale(recScale, recScale, cX, cY)
        canvas.rotate(-45f + sunRotateAngel, cX, cY)
        matrix2.setRotate(-sunRotateAngel, cX, cY)
        mPaintSunShine2.shader.setLocalMatrix(matrix2)
        canvas.drawRect(rectSunShine, mPaintSunShine2)
        canvas.restore()

        //太阳本体
        canvas.translate(0f, windDistance)
        canvas.drawCircle(cX, cY, mRMoveBig, mPaint)
        canvas.drawCircle(cX, cY, mRMoveSmall, mPaintBG)

        //state2 需要的线图
        canvas.save()
        canvas.rotate(270f, cX, cY)
        mPaintLine.strokeWidth = 20f
        canvas.drawPath(mPathLineBigDest, mPaintLine)
        mPaintLine.strokeWidth = 10f
        canvas.drawPath(mPathLineSmallDest, mPaintLine)
        mPaintLine.strokeWidth = 15f
        canvas.drawPath(mPathLineMinDest, mPaintLine)
        canvas.restore()

        //画阴影
        canvas.save()
        canvas.translate(0f, windDistance)
        canvas.scale(recScale, 1f, mOvalRect.centerX(), mOvalRect.centerY())
        canvas.drawOval(mOvalRect, mPaintShadow)
        canvas.restore()

        //画云朵下面的阴影
        canvas.save()
        canvas.translate(0f, windDistance)
        mPaintCloudShadow.alpha = (recScaleShadow * 255).toInt()
//        canvas.scale(recScaleShadow, recScaleShadow, mCloudShadow.centerX(), mCloudShadow.centerY())
        cloudShadowPath.moveTo(cloudRectClip.left, cloudRectClip.bottom)
        cloudShadowPath.lineTo(cX + mRadius, cloudRectClip.bottom)
        cloudShadowPath.addArc(mCloudShadow, 20f, sweepAngle.toFloat())
        cloudShadowPath.lineTo(cloudRectClip.left, cloudRectClip.bottom)
//        cloudShadowPath.close()
//        canvas.drawArc(mCloudShadow, 20f, sweepAngle.toFloat(), false, mPaintShadow)
//        canvas.drawArc(mCloudShadow, 0f, 360f, true, mPaintShadow)
//        canvas.drawRect(mCloudShadow, mPaintLine)
        canvas.drawPath(cloudShadowPath, mPaintCloudShadow)
        canvas.restore()

        //画云朵
        canvas.save()
        canvas.translate(0f, mRadius / 10f)
        canvas.scale(recScale, recScale, cloudRectClip.centerX(), cloudRectClip.bottom)
//        canvas.drawRect(cloudRectClip, mPaintLine)
        canvas.clipRect(cloudRectClip)
//        canvas.scale(recScale, 1f, mOvalRect.centerX(), mOvalRect.centerY())
        canvas.drawCircle(cX, cY + mRadius / 5, mRadius / 4 * 1f, mPaintCloud)
        canvas.drawCircle(cX + mRadius / 5, cY - mRadius / 5, mRadius / 4 * 1f, mPaintCloud)

        canvas.drawCircle(cX + mRadius / 5 * 3, cY, mRadius / 3 * 1f, mPaintCloud)
        canvas.drawCircle(cX + mRadius / 8 * 7, cY + mRadius / 3, mRadius / 3 * 1f, mPaintCloud)

        canvas.drawCircle(cX + mRadius / 2, cY + mRadius / 5, mRadius / 8 * 3 * 1f, mPaintCloud)
        canvas.drawCircle(cX + mRadius / 6, cY + mRadius / 3, mRadius / 3 * 1f, mPaintCloud)
//        canvas.drawCircle(cX, cY + mRadius / 5*2, 40f, mPaintShadow)
//        canvas.drawCircle(cX, cY + mRadius / 5*2, 30f, mPaintShadow)
        canvas.restore()


//        canvas.drawRect(mOvalRect, mPaint)
//        canvas.drawLine(mCloudShadow.left, 0f, mCloudShadow.left, measuredHeight * 1f, mPaintLine)
//        canvas.drawLine(mCloudShadow.right, 0f, mCloudShadow.right, measuredHeight * 1f, mPaintLine)
//        canvas.drawLine(0f, mCloudShadow.bottom, measuredHeight * 1f, mCloudShadow.bottom, mPaintLine)
//        canvas.drawLine(0f, mCloudShadow.top, measuredHeight * 1f, mCloudShadow.top, mPaintLine)

    }

    private var recScaleShadow: Float = 0f

    private var sunRotateAngel: Float = 0f

    fun startAnimator0() {
        mRMoveBig = 0f
        mRMoveSmall = 10f

        val pm = PathMeasure()

        var state1 = mRadius / 2 * 3
        var state2 = mRadius * 2
        var state3 = mRadius / 2 * 5
        var state4 = mRadius / 16 * 41
        var state5 = Float.MAX_VALUE
        var animator = ValueAnimator.ofFloat(10f, state1, state2, state3, state4)
        animator.addUpdateListener { animation ->
            //state 1 circle disappear
            var value = animation.animatedValue as Float
            if (value < mRadius) {
                mRMoveBig = value
            } else if (value <= state1) {
//                mRMoveBig = 0f
                mRMoveSmall = Math.max((value - mRadius) * 2, 10f)
                //state 2 line rotation
            } else if (value < state2) {
//                mRMoveSmall = 0f
                //使用pathMeasure方法来做，无法确定起始点
                val section = value - state1
                val scaleStop = section / (state2 - state1)
                getSegmentDestPath(pm, scaleStop, mPathLineBig, mPathLineBigDest, 0.3f)
                getSegmentDestPath(pm, scaleStop, mPathLineSmall, mPathLineSmallDest, 0.3f)
                getSegmentDestPath(pm, scaleStop, mPathLineMin, mPathLineMinDest, 0.1f)
                mRMoveSmall = 0f
                mRMoveBig = 0f
            } else if (value < state3) {
                mPathLineBigDest.reset()
                mPathLineSmallDest.reset()
                mPathLineMinDest.reset()
                mPathLineBigDest.lineTo(0f, 0f);
                mPathLineSmallDest.lineTo(0f, 0f);
                mPathLineMinDest.lineTo(0f, 0f);

                //state3 sun appear
                mRMoveBig = Math.max(mRadius / 10f, (value - state2) * 2)

                var localFactor = (value - state2) / (state3 - state2)
                //阳光的动画。
                if (localFactor > 0.6) {
                    recScale = (localFactor - 0.6f) / 0.4f
//                    rectSunShine.set((cX - mRadius) * 1, (cY - mRadius) * 1, (cX + mRadius) * 1, (cY + mRadius) * 1)
                    //同时需要进行阴影的动画
//                    mOvalRect.set(cX - mRadius, cY + mRadius / 2 * 5, cX + mRadius, cY + mRadius / 2 * 5 + 10)
                }
                println("recScale=$recScale")
                println("localFactor=$localFactor")
            } else if (value < state4) {
                recScale = 1f
                mRMoveBig = mRadius
                var localFactor = (value - state3) / (state4 - state3)
                var scaleRecf = 0.94f
                rectSunShine.set((cX - mRadius * scaleRecf) * 1, (cY - mRadius * scaleRecf) * 1, (cX + mRadius * scaleRecf) * 1, (cY + mRadius * scaleRecf) * 1)
                if (localFactor < 0.8) {
                    recScaleShadow = localFactor
                } else {
                    recScaleShadow = 1f
                }
            }
            println("value=$value mRadius*2=$state2")
            invalidate()
        }
        animator.duration = 2000
//        animator.repeatMode = ValueAnimator.RESTART
//        animator.repeatCount = ValueAnimator.INFINITE
        animator.start()

        postDelayed(Runnable {
            var animator = ValueAnimator.ofFloat(0f, 360f)
            animator.addUpdateListener { animation ->
                var animatedFraction = animation.animatedFraction
                sunRotateAngel = animatedFraction * 360
                windDistance = Math.sin(sunRotateAngel.toDouble()).toFloat()
                println("sunRotateAngel=$sunRotateAngel")
                invalidate()
            }
            animator.duration = 80000
            animator.repeatCount = ValueAnimator.INFINITE
            animator.repeatMode = ValueAnimator.RESTART
            animator.start()
        }, state4.toLong())
    }

    private fun getSegmentDestPath(pm: PathMeasure, scaleStop: Float, mPathLineBig: Path, mPathLineBigDest: Path, startScale: Float) {
        mPathLineBigDest.reset()
        mPathLineBigDest.lineTo(0f, 0f);
        pm.setPath(mPathLineBig, false)
        val length = pm.length
        var stop = length * scaleStop
        val start = Math.max(length * startScale, (stop - (0.5 - Math.abs(scaleStop - 0.5)) * length).toFloat())
        println("start=$start")
        pm.getSegment(start, stop, mPathLineBigDest, true)
    }
}