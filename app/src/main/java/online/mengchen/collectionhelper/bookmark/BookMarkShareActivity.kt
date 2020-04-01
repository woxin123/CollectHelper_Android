package online.mengchen.collectionhelper.bookmark

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.common.Constant
import online.mengchen.collectionhelper.data.network.SessionInterceptor
import online.mengchen.collectionhelper.user.LoginActivity

class BookMarkShareActivity : AppCompatActivity() {

    companion object {
        const val TAG = "BookMarkShareActivity"
    }

    private lateinit var bookMarkShareViewModel: BookMarkShareViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_mark_share)
        bookMarkShareViewModel = ViewModelProvider(this).get(BookMarkShareViewModel::class.java)
        if (SessionInterceptor.cookieSir == null) {
            val cookie = getSharedPreferences(Constant.SP_COOKIE, Context.MODE_PRIVATE)
                .getString(Constant.COOKIE, null)
            if (cookie == null) {
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                SessionInterceptor.cookieSir = cookie
            }
        }
        val intent = intent!!
        val shareText = intent.getStringExtra(Intent.EXTRA_TEXT)
        Log.d(TAG, "shareText = $shareText")
        bookMarkShareViewModel.addBookMark(url = shareText!!).observe(this, Observer {
            if (it.status == 201) {
                Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
