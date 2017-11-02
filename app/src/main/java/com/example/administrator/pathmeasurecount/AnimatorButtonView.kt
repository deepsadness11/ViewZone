package com.example.administrator.pathmeasurecount

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator

/**
 * 自定义旋转样式的选择按钮
 * Created by Administrator on 2017/11/1 0001.
 */
class AnimatorButtonView : View {
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
    lateinit var mPaintLine: Paint
    lateinit var bgPaint: Paint

    //画辅助线的
    lateinit var mTextPaint: TextPaint
    lateinit var mPaintHelp0: Paint
    lateinit var mPaintHelp1: Paint
    lateinit var mPaintCtrlHelp: Paint


    var mRadius = 100f
    lateinit var mPath: Path
    lateinit var mCtrlPath0: Path
    lateinit var mCtrlPath1: Path
    lateinit var mCtrlPath2: Path
    lateinit var mCtrlPath3: Path

    private fun initPaints() {
        //开启硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        setBackgroundColor(startColor)

        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.style = Paint.Style.FILL
        mPaint.color = startColor
        mPaint.setShadowLayer(5f, -2f, 3f, startColor)

        bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        bgPaint.style = Paint.Style.FILL
        bgPaint.color = Color.WHITE


        mPaintLine = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaintLine.style = Paint.Style.STROKE
        mPaintLine.strokeJoin = Paint.Join.ROUND
        mPaintLine.strokeCap = Paint.Cap.ROUND
        mPaintLine.strokeWidth = 10f
        mPaintLine.color = Color.WHITE


        mPaintHelp0 = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaintHelp0.style = Paint.Style.FILL

        mPaintHelp1 = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaintHelp1.style = Paint.Style.FILL
        mPaintHelp1.color = Color.BLUE

        mPaintCtrlHelp = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaintCtrlHelp.style = Paint.Style.STROKE

        mTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint.textSize = 15f
        mTextPaint.color = Color.BLACK

        mPath = Path()
        mCtrlPath0 = Path()
        mCtrlPath1 = Path()
        mCtrlPath2 = Path()
        mCtrlPath3 = Path()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        initM()
    }

    var centerX = measuredWidth / 2f
    var centerY = measuredHeight / 2f

    private var movePointX: Float = 0f
    private var movePointY: Float = 0f

    //需要绘制的4个点
    val p0_State1: DrawPoint = DrawPoint("P0_State1")
    val p1_State1: DrawPoint = DrawPoint("P1_State1")
    val p2_State1: DrawPoint = DrawPoint("P2_State1")
    val p3_State1: DrawPoint = DrawPoint("P3_State1")

    val p0_State2: DrawPoint = DrawPoint("PO_State2")
    val p1_State2: DrawPoint = DrawPoint("P1_State2")
    val p2_State2: DrawPoint = DrawPoint("P2_State2")
    val p3_State2: DrawPoint = DrawPoint("P3_State2")

    var p0_move: DrawPoint = DrawPoint("PO_Move")
    var p1_move: DrawPoint = DrawPoint("P1_Move")
    var p2_move: DrawPoint = DrawPoint("P2_Move")
    var p3_move: DrawPoint = DrawPoint("P3_Move")

    val ctrl0: DrawPoint = DrawPoint("Ctrl0")
    val ctrl1: DrawPoint = DrawPoint("Ctrl1")
    val ctrl2: DrawPoint = DrawPoint("Ctrl2")
    val ctrl3: DrawPoint = DrawPoint("Ctrl3")

    val canvasMove: DrawPoint = DrawPoint("Canvas_Move")

    private var pos = FloatArray(2)
    private var tan = FloatArray(2)

    var open: Boolean = false
    var valueAnimator: ValueAnimator? = null
    var targetLength: Float = 0f

    private fun initM() {
        //按钮距离两边的间距
        val paddingDistance = 20f
        centerX = measuredWidth / 2f - mRadius
        centerY = measuredHeight / 2f

        val scale = 0.5
        val scale2 = scale + 0.1
        val angleDegree = 45.0
        val toRadians = Math.toRadians(angleDegree)
        val cos = Math.cos(toRadians)
        val sin = Math.sin(toRadians)
        val offsetX = mRadius * scale * cos
        val offsetY = mRadius * scale * sin

        //state2
        p0_State2.x = centerX - mRadius / 5 * 3
        p0_State2.y = centerY

        p1_State2.x = (centerX-offsetX/2f).toFloat()
        p1_State2.y = (centerY + mRadius * scale2 * sin).toFloat()

        p2_State2.x = (centerX-offsetX/2f).toFloat()
        p2_State2.y = (centerY + mRadius * scale2 * sin).toFloat()

        p3_State2.x = (centerX + mRadius * scale2 * cos).toFloat()
        p3_State2.y = (centerY - mRadius * scale2 * cos).toFloat()

        //state1
        p0_State1.x = (centerX + offsetX).toFloat()

        p0_State1.y = (centerY + offsetY).toFloat()

        p1_State1.x = (centerX - offsetX).toFloat()
        p1_State1.y = (centerY - offsetY).toFloat()

        p2_State1.x = (centerX + offsetX).toFloat()
        p2_State1.y = (centerY - offsetX).toFloat()

        p3_State1.x = (centerX - offsetX).toFloat()
        p3_State1.y = (centerY + offsetX).toFloat()

        ctrl0.x = centerX + mRadius / 2
        ctrl0.y = centerY - mRadius / 2

        ctrl1.x = centerX - mRadius / 4 * 5
        ctrl1.y = centerY + mRadius / 4

        ctrl2.x = centerX - mRadius
        ctrl2.y = centerY - mRadius / 2

        ctrl3.x = centerX + mRadius / 2 * 2
        ctrl3.y = centerY + mRadius

        p0_move = p0_State1.copy("PO_Move")
        p1_move = p1_State1.copy("P1_Move")
        p2_move = p2_State1.copy("P2_Move")
        p3_move = p3_State1.copy("P3_Move")

        mCtrlPath0.moveTo(p0_State1.x, p0_State1.y)
        mCtrlPath0.quadTo(ctrl0.x, ctrl0.y, p0_State2.x, p0_State2.y)

        mCtrlPath1.moveTo(p1_State1.x, p1_State1.y)
        mCtrlPath1.quadTo(ctrl1.x, ctrl1.y, p1_State2.x, p1_State2.y)

        mCtrlPath2.moveTo(p2_State1.x, p2_State1.y)
        mCtrlPath2.quadTo(ctrl2.x, ctrl2.y, p2_State2.x, p2_State2.y)
//
        mCtrlPath3.moveTo(p3_State1.x, p3_State1.y)
        mCtrlPath3.quadTo(ctrl3.x, ctrl3.y, p3_State2.x, p3_State2.y)

        //整体view的是走直线
        targetLength = mRadius * 2
        mPath.moveTo(0f, 0f)
        mPath.lineTo(targetLength, 0f)

        drawRecf0 = RectF(centerX - mRadius - paddingDistance, measuredHeight / 2 - mRadius - paddingDistance, centerX + mRadius + paddingDistance, measuredHeight / 2 + mRadius + paddingDistance)
        clipRecf0 = RectF(centerX - mRadius - paddingDistance, measuredHeight / 2 - mRadius - paddingDistance, centerX, measuredHeight / 2 + mRadius + paddingDistance)
        drawRecf1 = RectF(centerX + mRadius - paddingDistance, measuredHeight / 2 - mRadius - paddingDistance, centerX + mRadius * 3 + paddingDistance, measuredHeight / 2 + mRadius + paddingDistance)
        clipRecf1 = RectF(centerX + mRadius * 2, measuredHeight / 2 - mRadius - paddingDistance, centerX + mRadius * 3 + paddingDistance, measuredHeight / 2 + mRadius + paddingDistance)
        drawRecfMain = RectF(centerX, measuredHeight / 2 - mRadius - paddingDistance, centerX + mRadius * 2, measuredHeight / 2 + mRadius + paddingDistance)
    }

    lateinit var drawRecf0: RectF
    lateinit var clipRecf0: RectF
    lateinit var clipRecf1: RectF
    lateinit var drawRecf1: RectF
    lateinit var drawRecfMain: RectF

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //画左边半圆
        canvas.save()
        canvas.clipRect(clipRecf0)
        canvas.drawArc(drawRecf0, 0f, 360f, false, bgPaint)
        canvas.restore()
        //中间矩形
        canvas.drawRect(drawRecfMain, bgPaint)
        //右边半圆
        canvas.save()
        canvas.clipRect(clipRecf1)
        canvas.drawArc(drawRecf1, 0f, 360f, false, bgPaint)
        canvas.restore()
        //画按钮本体
        canvas.save()
        canvas.translate(canvasMove.x, canvasMove.y)
        canvas.drawCircle(centerX, centerY, mRadius, mPaint)
        //绘制实际的线条
        canvas.drawLine(p0_move.x, p0_move.y, p1_move.x, p1_move.y, mPaintLine)
        canvas.drawLine(p2_move.x, p2_move.y, p3_move.x, p3_move.y, mPaintLine)
        canvas.restore()

//        drawHelpPointsAndLines(canvas, p0_State2)
//        drawHelpPointsAndLines(canvas, p1_State2)
//        drawHelpPointsAndLines(canvas, p2_State2)
//        drawHelpPointsAndLines(canvas, p3_State2)
    }

    private fun drawMovePoints(canvas: Canvas, p: DrawPoint) {
        canvas.drawCircle(p.x, p.y, 10f, mPaintHelp0)
        canvas.drawText(p.name, p.x, p.y, mTextPaint)
    }

    private fun drawHelpPointsAndLines(canvas: Canvas, p: DrawPoint) {
        canvas.drawCircle(p.x, p.y, 10f, mPaintHelp0)
        canvas.drawText(p.name, p.x, p.y, mTextPaint)
    }

    fun startAnimator0() {
        var animatedFraction = 0f
        if (valueAnimator != null) {
            animatedFraction = valueAnimator!!.animatedFraction
            valueAnimator!!.cancel()
        }
        var totalLength = targetLength;
        if (animatedFraction != 0f || animatedFraction != 1f) {
            totalLength = targetLength * (animatedFraction)
        }
        val pathMeasure = PathMeasure()
        if (open) {
            open = false

            valueAnimator = ValueAnimator.ofFloat(0f, totalLength)
            valueAnimator!!.addUpdateListener { animation ->
                val animatedFraction = 1 - animation.animatedFraction
                setMovePoint(pathMeasure, animatedFraction, mPath, canvasMove)
                setMovePoint(pathMeasure, animatedFraction, mCtrlPath0, p0_move)
                setMovePoint(pathMeasure, animatedFraction, mCtrlPath1, p1_move)
                setMovePoint(pathMeasure, animatedFraction, mCtrlPath2, p2_move)
                setMovePoint(pathMeasure, animatedFraction, mCtrlPath3, p3_move)
                changePaintColor(animatedFraction, mPaint)
                invalidate()
            }
        } else {
            open = true
            valueAnimator = ValueAnimator.ofFloat(totalLength, 0f)
            valueAnimator!!.addUpdateListener { animation ->
                val animatedFraction = animation.animatedFraction
                setMovePoint(pathMeasure, animatedFraction, mPath, canvasMove)
                setMovePoint(pathMeasure, animatedFraction, mCtrlPath0, p0_move)
                setMovePoint(pathMeasure, animatedFraction, mCtrlPath1, p1_move)
                setMovePoint(pathMeasure, animatedFraction, mCtrlPath2, p2_move)
                setMovePoint(pathMeasure, animatedFraction, mCtrlPath3, p3_move)
                changePaintColor(animatedFraction, mPaint)
                invalidate()
            }
        }
//        Toast.makeText(context, "Open =$open", Toast.LENGTH_SHORT).show()
        valueAnimator!!.interpolator= AccelerateInterpolator()
//        valueAnimator!!.interpolator= BounceInterpolator()
        valueAnimator!!.setDuration(400)
        valueAnimator!!.start()
    }

    var startColor: Int = Color.parseColor("#F3e5d3")
    var endColor: Int = Color.parseColor("#7e86fa")

    private fun changePaintColor(animatedFraction: Float, mPaint: Paint) {
        val r0 = 243
        val g0 = 229
        val b0 = 211
        val h0 = 34f
        val s0 = 13f / 100f
        val b10 = 95f / 100f

        val r1 = 126
        val g1 = 134
        val b1 = 250
        val h1 = 236f
        val s1 = 50f / 100f
        val b11 = 98f / 100f

        val hsv0 = FloatArray(3)
        hsv0[0] = h0 + (h1 - h0) * animatedFraction
        hsv0[1] = s0 + (s1 - s0) * animatedFraction
        hsv0[2] = b10 + (b11 - b10) * animatedFraction

        val rgb0 = IntArray(3)
        rgb0[0] = (r0 + (r1 - r0) * animatedFraction).toInt()
        rgb0[1] = (g0 + (g1 - g0) * animatedFraction).toInt()
        rgb0[2] = (b0 + (b1 - b0) * animatedFraction).toInt()

        var hsvToColor = Color.HSVToColor(hsv0)

        var rgbColor = Color.rgb(rgb0[0], rgb0[1], rgb0[2])
        mPaint.color = rgbColor
        mPaint.setShadowLayer(5f, -2f, 3f, rgbColor)
    }

    private fun setMovePoint(pathMeasure: PathMeasure, animatedFraction: Float, ctrlPath: Path, p_move: DrawPoint) {
        pathMeasure.setPath(ctrlPath, false)
        pathMeasure.getPosTan(pathMeasure.length * animatedFraction, pos, tan)
        p_move.x = pos[0]
        p_move.y = pos[1]
    }
}