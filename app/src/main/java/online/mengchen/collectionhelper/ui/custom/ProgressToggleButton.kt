package online.mengchen.collectionhelper.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.widget.CompoundButton
import androidx.appcompat.widget.AppCompatToggleButton
import online.mengchen.collectionhelper.R
import kotlin.math.sqrt

class ProgressToggleButton : AppCompatToggleButton {
    interface OnCheckChangesListener {
        fun onCheckChanges(isChecked: Boolean)
    }

    private var listener: OnCheckChangesListener? = null


    private val mPaint: Paint = Paint()

    var mainColor: Int = 0

    private var circleTotalWidth: Int = 0

    private var circleCurrentWidth: Int = 0

    private var mProgress: Long = 1 // 当前进度

    // 默认最大进度
    var maxValue: Long = 100
        set(value) {
            val newValue = if (value <= 100) Long.MAX_VALUE else value
            rate = 360F.div(newValue)
            field = newValue
        }

    private var rate: Float = 0.0F

    constructor(context: Context): super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    fun init() {
        mainColor = resources.getColor(R.color.colorPrimary)
        circleTotalWidth = 5
        circleCurrentWidth = 5
        this.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
            listener?.onCheckChanges(isChecked);
            postInvalidate();
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        mPaint.color = mainColor
        mPaint.isAntiAlias = true
        mPaint.strokeWidth = 0F
        val center = width / 2 // 三角形,圆圈中央
        val sideLength = center / 5 * 4 // 三角形边长
        if (this.isChecked) {
            drawPlay(canvas, center, sideLength);
        } else {
            drawStop(canvas, center, sideLength);
        }
        // 最外围的总进度
        mPaint.strokeWidth = circleTotalWidth.toFloat()
        mPaint.style = Paint.Style.STROKE;
        val radius = center - circleTotalWidth
        canvas.drawCircle(center.toFloat(), center.toFloat(), radius.toFloat(), mPaint);
        // 最当前进度
        val oval = RectF(
            (center - radius + circleTotalWidth).toFloat(), (center
                    - radius + circleTotalWidth).toFloat(),
            (center + radius - circleTotalWidth).toFloat(), (center + radius
                    - circleTotalWidth).toFloat()
        );
        mPaint.strokeWidth = circleCurrentWidth.toFloat()
        Log.d("mengchenaa", "maxValue = $maxValue, curPorgress = $mProgress")
        canvas.drawArc(oval, -90F, mProgress.toFloat(), false, mPaint);
    }

    /**
     * 画暂停状态
     *
     * @param canvas
     * @param center
     *            三角形中心横纵坐标
     * @param sideLength
     *            三角形边长
     */
    private fun drawStop(canvas: Canvas, center: Int, sideLength: Int) {
        mPaint.style = Paint.Style.FILL_AND_STROKE
        val genSan = sqrt(3.0).toFloat()
        val path2: Path = Path()
        path2.moveTo(
            (center - sideLength / (2 * genSan)), (center - sideLength
                    / 2).toFloat()
        )
        path2.lineTo((center + 2 * sideLength / (2 * genSan)), center.toFloat())
        path2.lineTo(
            (center - sideLength / (2 * genSan)), (center + sideLength
                    / 2).toFloat()
        )
        path2.close();
        canvas.drawPath(path2, mPaint);
    }

    /**
     * 画播放状态
     *
     * @param canvas
     * @param center
     *            两条线的对称轴中心横纵坐标
     * @param sideLength
     *            线的长度
     */

    private fun drawPlay(canvas: Canvas, center: Int, sideLength: Int) {
        val genSan: Float = sqrt(3.0).toFloat();
        val linesWidth: Float = (sideLength / 5).toDouble().toFloat()
        mPaint.strokeWidth = linesWidth;
        canvas.drawLine(
            (center - sideLength / (2 * genSan)) + linesWidth / 2,
            (center - sideLength / 2).toFloat(), (center - sideLength / (2 * genSan))
                    + linesWidth / 2, (center + sideLength / 2).toFloat(), mPaint
        )
        canvas.drawLine(
            (center + sideLength / (2 * genSan)) - linesWidth / 2,
            (center - sideLength / 2).toFloat(), (center + sideLength / (2 * genSan))
                    - linesWidth / 2, (center + sideLength / 2).toFloat(), mPaint
        )
    }

    /**
     * 设置进度
     *
     * @param progress
     */

    fun setProgress(progress: Long) {
        mProgress = when {
            progress >= maxValue -> {
                maxValue
            }
            progress <= 0 -> {
                0
            }
            else -> {
                ((progress + 1) * rate).toLong()
            }
        }
        postInvalidate()
    }


    // 设置为wrap_content 时的控件高宽
    private val defaultWidth = 50


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var finalWidth = 0
        var finaLHeight = 0
        finalWidth = if (widthMode == MeasureSpec.EXACTLY) {
            widthSize;
        } else {
            paddingLeft + defaultWidth + paddingRight
        }
        finaLHeight = if (heightMode == MeasureSpec.EXACTLY) {
            heightSize;
        } else {
            paddingTop + defaultWidth + paddingBottom
        }
        setMeasuredDimension(finalWidth, finaLHeight);
    }
}