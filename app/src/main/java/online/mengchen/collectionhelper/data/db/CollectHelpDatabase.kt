package online.mengchen.collectionhelper.data.db

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.CoroutineScope
import online.mengchen.collectionhelper.common.Constant
import online.mengchen.collectionhelper.dao.BookMarkDao
import online.mengchen.collectionhelper.dao.BookMarkDetailDao
import online.mengchen.collectionhelper.dao.CategoryDao
import online.mengchen.collectionhelper.domain.entity.BookMark
import online.mengchen.collectionhelper.domain.entity.BookMarkDetail
import online.mengchen.collectionhelper.domain.entity.Category
import online.mengchen.collectionhelper.domain.entity.User
import online.mengchen.collectionhelper.utils.LocalDateTypeConverter

@Database(entities = [BookMark::class, BookMarkDetail::class, Category::class, User::class], version = 1)
@TypeConverters(LocalDateTypeConverter::class)
abstract class CollectHelpDatabase : RoomDatabase() {

    abstract fun bookMarkDao(): BookMarkDao
    abstract fun categoryDao(): CategoryDao
    abstract fun bookMarkDetailDao(): BookMarkDetailDao

    companion object {

        private var INSTANCE: CollectHelpDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): CollectHelpDatabase {
            val tempDatabase = INSTANCE
            if (tempDatabase != null) {
                return tempDatabase
            }
            return synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    CollectHelpDatabase::class.java,
                    Constant.DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                INSTANCE!!
            }
        }

    }
}