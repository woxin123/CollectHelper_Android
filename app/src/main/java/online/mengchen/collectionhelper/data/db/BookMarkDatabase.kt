package online.mengchen.collectionhelper.data.db

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.CoroutineScope
import online.mengchen.collectionhelper.dao.BookMarkDao
import online.mengchen.collectionhelper.domain.entity.BookMark
import online.mengchen.collectionhelper.utils.LocalDateTypeConverter

@Database(entities = [BookMark::class], version = 1)
@TypeConverters(LocalDateTypeConverter::class)
abstract class BookMarkDatabase : RoomDatabase() {

    abstract fun bookMarkDao(): BookMarkDao

    companion object {

        private var INSTANCE: BookMarkDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): BookMarkDatabase {
            val tempDatabase = INSTANCE
            if (tempDatabase != null) {
                return tempDatabase
            }
            return synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    BookMarkDatabase::class.java,
                    "book_mark")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                INSTANCE!!
            }
        }

    }
}