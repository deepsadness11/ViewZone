package com.example.administrator.pathmeasurecount.example

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.example.administrator.pathmeasurecount.R


/**
 * JkeCommentViewCopy
 * Created by Administrator on 2017/11/3 0003.
 */
class JCommentView : View {
    constructor(context: Context) : super(context) {
        initResource(context)
    }


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initResource(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initResource(context)
    }

    @Suppress("DEPRECATION")
    private fun initResource(context: Context) {
        commentDrawable = context.resources.getDrawable(R.mipmap.ic_messages_like_unselected)

        iconPaint.apply {
            style = Paint.Style.STROKE
            color = Color.parseColor("#da8d7f")
            strokeWidth = 3f
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
        }
        linePaint.apply {
            style = Paint.Style.STROKE
            color = redColor
            strokeWidth = 5f
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
        }
        textPaint.apply {
            color = grayColor
        }
    }

    private lateinit var commentDrawable: Drawable
    private var drawableOriginalRectF = Rect()

    private val iconPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val linePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val ringPath: Path = Path()
    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    private var isComment: Boolean = false

    private var centerX: Float = 0f

    private var centerY: Float = 0f
    private val animatorDuration = 200L

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        centerX = measuredWidth / 2f
        centerY = measuredHeight / 2f
        drawableOriginalRectF.set((centerX - commentDrawable.intrinsicWidth).toInt(), (centerY - commentDrawable.intrinsicHeight / 2).toInt(), centerX.toInt(), (centerY + commentDrawable.intrinsicHeight / 2).toInt())
        commentDrawable.bounds = drawableOriginalRectF
        textPaint.textSize = drawableOriginalRectF.height().toFloat() / 8 * 5
    }

    private var drawableScale: Float = 1f
    private var circleScale: Float = 1f
    private var lineScale: Float = 0f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val topRotatePixY = drawableOriginalRectF.top.toFloat()
        val centerX = drawableOriginalRectF.centerX().toFloat()
        canvas.apply {
            //draw circle
            save()
            iconPaint.strokeWidth = 3f
            //将绘制操作保存到新的图层，因为图像合成是很昂贵的操作，将用到硬件加速，这里将图像合成的处理放到离屏缓存中进行
            drawPath(ringPath, iconPaint)
            restore()
            //draw icon
            save()
            scale(drawableScale, drawableScale, centerX, drawableOriginalRectF.centerY().toFloat())
            commentDrawable.draw(this)
            restore()

            //draw line
            val radius = 10f
            val lineStartY = topRotatePixY + radius / 4 * 3 - radius
            val lineLength = radius * 2 / 4 * lineScale
            if (lineLength == 0f) {

            } else {
                save()
                translate(0f, -2f)
                drawLine(centerX, lineStartY, centerX, lineStartY - lineLength, linePaint)
                translate(3f, 2f)
                rotate(40f, centerX, topRotatePixY + radius / 4 * 3)
                drawLine(centerX, lineStartY, centerX, lineStartY - lineLength, linePaint)
                translate(-4f, 2f)
                rotate(-80f, centerX, topRotatePixY + radius / 4 * 3)
                drawLine(centerX, lineStartY, centerX, lineStartY - lineLength, linePaint)
                translate(-0f, 2f)
                rotate(-40f, centerX, topRotatePixY + radius / 4 * 3)
                drawLine(centerX, lineStartY, centerX, lineStartY - lineLength, linePaint)
                restore()
            }

            //draw text
            //不需要动画的部分
            textPaint.alpha = 255
            val drawablePadding = 12f
            val textStartX = drawableOriginalRectF.right.toFloat() + drawablePadding
            val textStartY = drawableOriginalRectF.centerY().toFloat() + drawableOriginalRectF.height() / 4f
            if (staticNumber) {//静态数字
                save()
                drawText(currentNumber.toString(), textStartX, textStartY, textPaint)
                restore()
            } else {
//                textPaint.color = Color.RED
                save()
                val noChangeNumberPart = getNoChangeNumberPart()
//                println("noChangeNumberPart=$noChangeNumberPart")
                drawText(noChangeNumberPart, textStartX, textStartY, textPaint)
                restore()

                save()
                //需要动画的部分的从下往上滑部分
//                textPaint.color = Color.GREEN
                val alpha = transLateY * 1f / totalDistance
                textPaint.alpha = (255 * alpha).toInt()
                val changeNumberPartTranlateX = getDownToMidPartTranlateX()
                translate(changeNumberPartTranlateX, (totalDistance - transLateY).toFloat())
                var downToMidPart = ""
                if (upOrDown) {
                    downToMidPart = getDownToMidPart()
                } else {
                    downToMidPart = getChangeNumberNewPart()
//                    println("downToMidPart=$downToMidPart")
                }
                drawText(downToMidPart, textStartX, textStartY, textPaint)
                restore()

                save()
//                textPaint.color = Color.BLUE
                //需要动画的部分的从中间往上滑部分
                textPaint.alpha = 255
                textPaint.alpha = (255 * (1 - alpha)).toInt()
                translate(getMidToUpPartTranslationX(), (0 - transLateY).toFloat())
                val changeNumberOldPart = getChangeNumberOldPart()
//                println("changeNumberOldPart=$changeNumberOldPart")
                drawText(changeNumberOldPart, textStartX, textStartY, textPaint)
                restore()
            }
        }
    }

    private var commentRedColorArray = intArrayOf(228, 88, 62)
    private var commentGrayColorArray = intArrayOf(204, 204, 204)
    private val redColor = Color.rgb(commentRedColorArray[0], commentRedColorArray[1], commentRedColorArray[2])
    private val grayColor = Color.rgb(commentGrayColorArray[0], commentGrayColorArray[1], commentGrayColorArray[2])

    fun changeCommentLikeState() {
        val changeAnimator = ValueAnimator.ofFloat(0f, 1f)
        if (isComment) {
            changeAnimator.addUpdateListener { animation ->
                val animatedFraction = animation.animatedFraction
                changeDrawableColor(commentRedColorArray, commentGrayColorArray, animatedFraction)
                scaleDrawable(animatedFraction, isComment)
//                scaleRing(animatedFraction)
                scaleLine(animatedFraction, isComment)
                invalidate()
            }

        } else {
            changeAnimator.addUpdateListener { animation ->
                val animatedFraction = animation.animatedFraction
                changeDrawableColor(commentGrayColorArray, commentRedColorArray, animatedFraction)
                scaleDrawable(animatedFraction, isComment)
                scaleRing(animatedFraction)
                scaleLine(animatedFraction, isComment)
                invalidate()
            }
            startAddOne()
        }
        isComment = !isComment
        changeAnimator.duration = animatorDuration
        changeAnimator.start()
    }

    private fun scaleRing(animatedFraction: Float) {
        circleScale = when {
            animatedFraction > 0.9 -> 0f
            animatedFraction > 0.3 -> animatedFraction
            else -> 0f
        }
        ringPath.reset()
        ringPath.addCircle(drawableOriginalRectF.centerX().toFloat(), drawableOriginalRectF.centerY().toFloat(), circleScale * drawableOriginalRectF.width() / 4 * 3, Path.Direction.CW)
    }

    private fun scaleLine(animatedFraction: Float, isScaleBig: Boolean) {
        if (isScaleBig) {
            linePaint.color = redColor
            if (animatedFraction > 0.3f) {
                lineScale = animatedFraction
            }
        } else {
//            linePaint.color=grayColor
//            if (animatedFraction > 0.6f) {
            lineScale = 0f
//            } else {
//                lineScale = 1 - animatedFraction
//            }
        }
    }

    private fun scaleDrawable(animatedFraction: Float, comment: Boolean) {
        drawableScale = if (comment) {
            when {
                animatedFraction <= 0.5f -> //放大
                    1 + 0.2f * animatedFraction / 0.5f
                else -> //缩小
                    1.2f - 0.2f * (animatedFraction - 0.5f) / 0.5f
            }
        } else {
            when {
                animatedFraction <= 0.5f -> //缩小
                    1 - 0.2f * animatedFraction / 0.5f
                else -> //放大
                    0.8f + 0.2f * (animatedFraction - 0.5f) / 0.5f
            }
        }

    }

    private fun changeDrawableColor(startColorIntArray: IntArray, endColorIntArray: IntArray, animatedFraction: Float) {
        val redGradient = startColorIntArray[0] - (startColorIntArray[0] - endColorIntArray[0]) * animatedFraction
        val greenGradient = startColorIntArray[1] - (startColorIntArray[1] - endColorIntArray[1]) * animatedFraction
        val blueGradient = startColorIntArray[2] - (startColorIntArray[2] - endColorIntArray[2]) * animatedFraction
        val color = Color.rgb(redGradient.toInt(), greenGradient.toInt(), blueGradient.toInt())
        val porterDuffColorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        commentDrawable.colorFilter = porterDuffColorFilter
        textPaint.colorFilter = porterDuffColorFilter
    }

    //draw text method
    private var currentNumber = 100999
    private var distanceChange: Int = 0
    private var animateDistance: Int = 0
    private var upOrDown: Boolean = false
    private var staticNumber: Boolean = true

    private fun getMidToUpPartTranslationX(): Float {
        val midToUp = getMidToUpScrollPart()
        return textPaint.measureText(midToUp)
    }

    private fun getDownToMidPartTranlateX(): Float {
        //unchangeNumber
        val noChangeNumberPart = getNoChangeNumberPart()
        return textPaint.measureText(noChangeNumberPart)
    }

    private fun getChangeNumberOldPart(): String {

        if (upOrDown) {//++
            val temp = currentNumber - 1
            val v = temp % Math.pow(10.0, animateDistance.toDouble())
            //            v++;
            return "" + v.toInt()
        } else { //--
            val temp = currentNumber + 1
            val v = temp % Math.pow(10.0, animateDistance.toDouble())
            //            v--;
            return "" + v.toInt()
        }
    }

    private fun getChangeNumberNewPart(): String {
        val temp = currentNumber - 1
        if (upOrDown) {//++
            var v = temp % Math.pow(10.0, animateDistance.toDouble())
            v++
            var s = "" + v.toInt()
            if (s.length > 1) {
                s = s.substring(1)
            }
            return s
        } else { //--
            var v = temp % Math.pow(10.0, animateDistance.toDouble())
            v--
            return "" + v.toInt()
        }
    }

    private fun getDownToMidPart(): String {
        val temp = currentNumber - 1
        var v = temp % Math.pow(10.0, animateDistance.toDouble())
        v++
        val d = v % Math.pow(10.0, animateDistance.toDouble())
        var s = "" + v.toInt()
        if (d == 0.0 || v == d) {
            s = s
        } else
            if (s.length > 1) {
                s = s.substring(1)
            }
        return s
    }

    private fun getNoChangeNumberPart(): String {
        val v = currentNumber / Math.pow(10.0, animateDistance.toDouble()).toInt()
        val f = currentNumber % Math.pow(10.0, animateDistance.toDouble()).toInt()
        println("getNoChangeNumberPart v=$v")
        println("getNoChangeNumberPart f=$f")
        if (v == 0 || f == 0) {
            return ""
        } else {
            return "" + v
        }
    }

    private fun getMidToUpScrollPart(): String {
        val v = currentNumber / Math.pow(10.0, animateDistance.toDouble()).toInt()
        val f = currentNumber % Math.pow(10.0, animateDistance.toDouble()).toInt()
//        println("getMidToUpScrollPart v=$v")
//        println("getMidToUpScrollPart f=$f")
        if (v == 1 && f != 0) {
            var numberLength = getNumberLength(currentNumber)
            val lastIndex = numberLength - animateDistance + 1
            if (lastIndex < numberLength) {
                val substring = currentNumber.toString().substring(0, lastIndex)
//                println("target v==1 substring=$substring")
                return substring
            } else {
                return "" + v
            }
        } else if (v == 0 || f == 0) {
            return ""
        } else {
            return "" + v
        }
    }

    internal var transLateY: Int = 0

    internal var totalDistance = 100

    fun subNumbers() {
        staticNumber = false
        currentNumber--
        totalDistance = textPaint.getFontSpacing().toInt()
        val valueAnimator = ValueAnimator.ofInt(totalDistance, 0)
        valueAnimator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Int
            transLateY = animatedValue
            invalidate()
        }
        valueAnimator.duration = animatorDuration
        valueAnimator.start()
    }

    private fun startAddOne() {
        startAddOne(animatorDuration)
    }

    private fun startAddOne(duration: Long) {
        staticNumber = false
        //需要改变的数字
        val oldNumber = currentNumber
        //先得到该数是几位数
        val numberLength = getNumberLength(oldNumber)
        //得到+1需要改变的位数,因为每次都是+1,故依次判断最后一位是否是9
        val changeNumberLength = getChangeNumberLengthOnAddOne(oldNumber, numberLength)
        if (changeNumberLength >= numberLength) {   //如果大于等于原来的位数，则需要全部改变
            //全部改变，则不管
            distanceChange = 0
            animateDistance = numberLength
        } else {    //如果只是部分改变
            distanceChange = numberLength - changeNumberLength
            animateDistance = changeNumberLength
        }
        //加1，则是开启向上的动画
        upOrDown = true
        //+1
        currentNumber++
        //计算textPaint的行间距
        totalDistance = textPaint.fontSpacing.toInt()
        //根据行间距开始动画
        val valueAnimator = ValueAnimator.ofInt(0, totalDistance)
        valueAnimator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Int
            transLateY = animatedValue
            invalidate()
        }
        valueAnimator.duration = duration
        valueAnimator.start()
    }

    fun setNumberWithAnimator(number: Int) {
        val oldNumber = currentNumber
        val diff = Math.abs(number - oldNumber)
        if (diff == 0) {
            return
        }
        val interval = animatorDuration / diff
        for (i in oldNumber until number) {
            val offset = i - oldNumber
            if (number > oldNumber) {
                postDelayed({
                    startAddOne(interval)
                }, offset * interval)
            } else {
                invalidate()
            }
        }
    }

    //往上+,可能需要改变的位数
    private fun getChangeNumberLengthOnAddOne(oldNumber: Int, countSize: Int): Int {
        var count = 0
        for (j in 1 until countSize) {
            var subSum = 0
            for (i in 1 until j) {
                val multi = 9
                subSum += (Math.pow(10.0, i.toDouble()) * multi).toInt()
            }
            val pow = Math.pow(10.0, j.toDouble())
            if (oldNumber % pow - subSum == 9.0) {
                count++
            } else {
                break
            }
        }
        count++
        return count
    }

    private fun getNumberLength(number: Int) = number.toString().length

    private fun getNumberLast0count(oldNumber: Int, count: Int): Int {
        var count = count
        if (oldNumber % 10 == 0) {
            count++
            return getNumberLast0count(oldNumber / 10, count)
        } else {
            count++
            return count
        }
    }
}