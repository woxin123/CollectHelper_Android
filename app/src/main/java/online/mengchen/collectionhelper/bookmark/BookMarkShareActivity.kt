package online.mengchen.collectionhelper.bookmark

import android.annotation.SuppressLint
import android.app.ActionBar
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.inputmethodservice.InputMethodService
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.util.QMUIKeyboardHelper
import kotlinx.android.synthetic.main.activity_book_mark_share.*
import kotlinx.android.synthetic.main.fragment_book_mark.*
import online.mengchen.collectionhelper.R
import online.mengchen.collectionhelper.common.Constant
import online.mengchen.collectionhelper.data.network.SessionInterceptor
import online.mengchen.collectionhelper.user.LoginActivity
import online.mengchen.collectionhelper.utils.KeyBoardHelper
import java.time.LocalDateTime


class BookMarkShareActivity : AppCompatActivity() {

    companion object {
        const val TAG = "BookMarkShareActivity"
    }

    private lateinit var bookMarkShareViewModel: BookMarkShareViewModel
    private lateinit var bookMarkCategoryAdapter: BookMarkCategoryAdapter
    private val bookMarkCategoryChecked = mutableListOf<BookMarkCategory>()



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarFullTransparent()
        setContentView(R.layout.activity_book_mark_share)
        supportActionBar?.hide()
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
//        bookMarkShareViewModel.addBookMarkStatus.observe(this, Observer {
//            if (it.status != 201) {
//                Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show()
//            }
//            Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show()
//        })
//        val intent = intent!!
//        val shareText = intent.getStringExtra(Intent.EXTRA_TEXT)
//        Log.d(TAG, "shareText = $shareText")
//        bookMarkShareViewModel.addBookMark(url = shareText!!)
        initView()
        initListener()
        bookMarkShareViewModel.getBookMarkCategories()
    }

     private fun setStatusBarFullTransparent() {
        val window: Window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
         window.statusBarColor = Color.TRANSPARENT
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initView() {
        val data = mutableListOf<BookMarkCategory>()
        for (i in 1..20) {
            data.add(BookMarkCategory(i.toLong(), "java$i", LocalDateTime.now(), LocalDateTime.now()))
        }
        bookMarkCategoryAdapter = BookMarkCategoryAdapter()
        categoriesRecyclerView.adapter = bookMarkCategoryAdapter
        categoriesRecyclerView.layoutManager = GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false)
        categoriesRecyclerView.addItemDecoration(BookMarkCategoryRecyclerViewItemDecoration())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initListener() {
        // 点击上面的空间直接取消
        space.setOnClickListener {
            finish()
        }

        bookMarkShareViewModel.bookMarkCategories.observe(this, Observer {
            bookMarkCategoryAdapter.data = it.data?.toMutableList()!!
        })

        bookMarkCategoryAdapter.onItemClickListener = {
            bookMarkCategoryChecked.add(it)
        }
        commit.setOnClickListener {
            val url = intent.getStringExtra(Intent.EXTRA_TEXT)
            bookMarkShareViewModel.addBookMark(bookMarkCategoryChecked, url!!)
        }

        bookMarkShareViewModel.addBookMarkStatus.observe(this, Observer {
            if (it.status == 201) {
                Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show()
            }
            finish()
        })
        createCategory.setOnClickListener {
            createCategoryView.visibility = View.VISIBLE
            newCategoryName.requestFocus()
            QMUIKeyboardHelper.showKeyboard(newCategoryName, false)
        }
        createCategorySpace.setOnClickListener {
            if (createCategoryView.visibility == View.VISIBLE) {
                QMUIKeyboardHelper.hideKeyboard(newCategoryName)
                createCategoryView.visibility = View.GONE
            }
        }
    }


}
