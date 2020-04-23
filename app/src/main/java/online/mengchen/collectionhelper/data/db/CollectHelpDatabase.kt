package online.mengchen.collectionhelper.data.db

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import online.mengchen.collectionhelper.common.Constant
import online.mengchen.collectionhelper.data.db.dao.*
import online.mengchen.collectionhelper.domain.entity.*
import online.mengchen.collectionhelper.utils.LocalDateTypeConverter

@Database(
    entities = [BookMark::class, BookMarkDetail::class, Category::class, User::class, AliyunConfig::class, FileInfo::class],
    version = 4
)
@TypeConverters(LocalDateTypeConverter::class)
abstract class CollectHelpDatabase : RoomDatabase() {

    abstract fun bookMarkDao(): BookMarkDao
    abstract fun categoryDao(): CategoryDao
    abstract fun bookMarkDetailDao(): BookMarkDetailDao
    abstract fun aliyunConfigDao(): AliyunConfigDao
    abstract fun fileInfoDao(): FileInfoDao

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
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CollectHelpDatabase::class.java,
                    Constant.DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            INSTANCE?.let {

                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                INSTANCE!!
            }
        }

    }
}