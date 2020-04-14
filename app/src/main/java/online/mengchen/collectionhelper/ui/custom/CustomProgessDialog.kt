package online.mengchen.collectionhelper.ui.custom

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.widget.TextView
import com.qmuiteam.qmui.widget.QMUIProgressBar
import online.mengchen.collectionhelper.R

class CustomProgressDialog(context: Context, theme: Int, string: String) : Dialog(context, theme) {

    var messageTv: TextView
    var progressBar: QMUIProgressBar

    constructor(context: Context) : this(context, R.style.CustomProgressDialog, "")

    constructor(context: Context, string: String) : this(context, R.style.CustomProgressDialog, string)

    init {
        setCanceledOnTouchOutside(false)
        setContentView(R.layout.layout_custom_progress_dialog)
        messageTv = findViewById(R.id.tv_message)
        progressBar = findViewById(R.id.progressBar1)
        progressBar.setQMUIProgressBarTextGenerator { qmuiProgressBar: QMUIProgressBar, value: Int, maxValue: Int ->
            value.times(100.0).div(maxValue).toInt().toString() + "%"
        }
        messageTv.text = string
        window?.attributes?.gravity = Gravity.CENTER;
        window?.attributes?.dimAmount = 0f
    }

    fun setProgress(progress: Int) {
        progressBar.progress = progress
    }

}