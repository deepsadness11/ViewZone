package com.example.administrator.pathmeasurecount.example

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import com.example.administrator.pathmeasurecount.R

/**
 * Created by Administrator on 2017/11/6 0006.
 */
class CameraView : View {
    constructor(context: Context) : super(context) {
        initPaints(context)
    }


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initPaints(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initPaints(context)
    }

    lateinit var innerDrawable: Drawable
    private var degress: Float = 0f
    lateinit var camera: Camera
    lateinit var m: Matrix

    private fun initPaints(context: Context) {
        camera = Camera()
        m = Matrix()
        innerDrawable = context.resources.getDrawable(R.mipmap.ic_messages_like_selected)
        setBackgroundColor(Color.GREEN)
        val ani = ValueAnimator.ofFloat(30f)
        ani.duration = 20000
        ani.repeatMode = ValueAnimator.RESTART
        ani.repeatCount = ValueAnimator.INFINITE
        ani.addUpdateListener { animation ->
            degress = animation.animatedFraction * 90
            println("degree=$degress")
            invalidate()
        }
        ani.start()
        paint.color = Color.BLUE
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        println("CameraView onLayout")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        println("measuredWidth=$measuredWidth")
        println("measuredHeight=$measuredHeight")
        innerDrawable.setBounds(measuredWidth / 2 - innerDrawable.intrinsicWidth / 2, measuredHeight / 2 - innerDrawable.intrinsicHeight / 2, measuredWidth / 2 + innerDrawable.intrinsicWidth / 2, measuredHeight / 2 + innerDrawable.intrinsicHeight / 2)
        println("CameraView onMeasure")


        option.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.dog, option)
        option.inSampleSize = calculateInSampleSize(option, getWidth() / 2, getHeight() / 2);
        option.inJustDecodeBounds = false;
    }

    private var option = BitmapFactory.Options();

    private val paint: Paint = Paint()

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        println("CameraView onDraw")
        super.onDraw(canvas)
//        canvas.save()
//        m.reset();
//        camera.save();
//        camera.translate(10f, 50f, -180f);
//        camera.getMatrix(m);
//        camera.restore();
//        canvas.concat(m);
//
//        canvas.drawCircle(60f, 60f, 60f, paint);
        canvas.save()
        m.reset();

        camera.save();
        camera.rotateX(degress);
        camera.getMatrix(m);
        camera.restore();

        //定位中心点
        m.preTranslate(-getWidth() / 2f, -getHeight() / 2f);
        m.postTranslate(getWidth() / 2f, getHeight() / 2f);

        canvas.drawRect(0f, measuredHeight / 2f, measuredWidth * 1f, measuredHeight * 1f, paint)
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.dog, option), m, paint);
        canvas.restore()
        canvas.save()
        val c = RectF(0f, 0f, measuredWidth * 1f, measuredHeight / 2f)
        canvas.clipRect(c)
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.dog, option), 0f, 0f, paint);
        canvas.restore()
    }

    fun calculateInSampleSize(options: BitmapFactory.Options,
                              reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight;
        val width = options.outWidth;
        var inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2;
            val halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

}