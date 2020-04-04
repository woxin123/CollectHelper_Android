package online.mengchen.collectionhelper.common.custom

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.WindowInsets
import android.widget.RelativeLayout


class FixStatusBarLayout : RelativeLayout {
    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int): super(context, attrs, defStyle)

    private val mInsets = IntArray(4)

    override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            mInsets[0] = insets.systemWindowInsetLeft
            mInsets[1] = insets.systemWindowInsetTop
            mInsets[2] = insets.systemWindowInsetRight
            super.onApplyWindowInsets(
                insets.replaceSystemWindowInsets(
                    0, 0, 0,
                    insets.systemWindowInsetBottom
                )
            )
        } else {
            insets
        }
    }

}