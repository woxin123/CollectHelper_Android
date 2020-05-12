package online.mengchen.collectionhelper.ui.document

import android.os.Bundle
import android.util.Log
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tencent.smtt.sdk.TbsReaderView
import kotlinx.android.synthetic.main.activity_display_document.*
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.utils.FileHelper
import java.io.File

class DisplayDocumentActivity : AppCompatActivity() {

    companion object {
        const val FILE_PATH = "file_path"
        const val TAG = "DisplayDocumentActivity"
    }

    private lateinit var tbsReaderView: TbsReaderView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_document)
        val filePath = intent.getStringExtra(FILE_PATH)
        if (filePath == null) {
            Toast.makeText(this, "文件为空", Toast.LENGTH_SHORT).show()
            finish()
        }
        val file = File(filePath!!)
        if (!file.exists()) {
            Toast.makeText(this, "文件不存在", Toast.LENGTH_LONG).show()
        }

        Log.d(TAG, filePath)
        val bundle = Bundle()
        bundle.putString("filePath", filePath)
        bundle.putString("tempPath", filesDir.absolutePath)
        tbsReaderView = TbsReaderView(this,
            TbsReaderView.ReaderCallback { p0, p1, p2 -> })
//        tbsReaderView.onStop()
        root.addView(tbsReaderView, RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT))
        val result = tbsReaderView.preOpen(FileHelper.getFileSuffix(filePath), false)

        if (result) {
            tbsReaderView.openFile(bundle)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tbsReaderView.onStop()
    }
}
