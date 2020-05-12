package online.mengchen.collectionhelper.data.db

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import online.mengchen.collectionhelper.common.Constant
import online.mengchen.collectionhelper.data.db.dao.*
import online.mengchen.collectionhelper.domain.entity.*
import online.mengchen.collectionhelper.utils.LocalDateTypeConverter

@Database(
    entities = [BookMark::class, BookMarkDetail::class, Category::class, User::class, AliyunConfig::class,
        FileInfo::class, QiniuConfig::class, TencentCOSConfig::class],
    version = 9
)
@TypeConverters(LocalDateTypeConverter::class)
abstract class CollectHelpDatabase : RoomDatabase() {

    abstract fun bookMarkDao(): BookMarkDao
    abstract fun categoryDao(): CategoryDao
    abstract fun bookMarkDetailDao(): BookMarkDetailDao
    abstract fun aliyunConfigDao(): AliyunConfigDao
    abstract fun fileInfoDao(): FileInfoDao
    abstract fun qiNiuYunConfigDao(): QiNiuYunConfigDao
    abstract fun tencentCOSConfigDao(): TencentCOSConfigDao

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
                    .addMigrations(MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8, MIGRATION_8_9)
                    .addCallback(object : Callback() {
                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            INSTANCE?.let {
                                scope.launch {
//                                    it.bookMarkDetailDao().deleteAll()
//                                    it.bookMarkDao().deleteAll()
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                INSTANCE!!
            }
        }

        private val MIGRATION_5_6 = object : Migration(4, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("DROP TABLE IF EXISTS qiniu_config;")
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS qiniu_config("
                            + "    id INTEGER PRIMARY KEY,\n" +
                            "    access_key TEXT NOT NULL,\n" +
                            "    secret_key TEXT NOT NULL,\n" +
                            "    bucket TEXT NOT NULL,\n" +
                            "    image_bucket TEXT,\n" +
                            "    document_bucket TEXT,\n" +
                            "    music_bucket TEXT,\n" +
                            "    video_bucket TEXT,\n" +
                            "    image_path TEXT,\n" +
                            "    document_path TEXT,\n" +
                            "    music_path TEXT,\n" +
                            "    video_path TEXT,\n" +
                            "    domain TEXT NOT NULL,\n" +
                            "    uid INTEGER NOT NULL\n" +
                            ");"
                )

            }
        }

        private val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS tencent_cos_config (\n" +
                            "    id INTEGER PRIMARY KEY,\n" +
                            "    secret_id TEXT NOT NULL,\n" +
                            "    secret_key TEXT NOT NULL,\n" +
                            "    bucket TEXT NOT NULL,\n" +
                            "    region TEXT NOT NULL,\n" +
                            "    uid INTEGER NOT NULL" +
                            ");"
                )
            }
        }

        private val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "alter table category\n" +
                            "\tadd store_type INTEGER default 0 not null;"
                )
            }
        }

        private val MIGRATION_8_9 = object : Migration(8, 9) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """create table bookmark_dg_tmp
                        (
	                        id INTEGER
		                    primary key autoincrement,
	                        url TEXT not null,
	                        create_time TEXT not null,
	                        detail_id INTEGER,
	                        category_id INTEGER not null,
	                        uid INTEGER not null
                        );"""
                )
                database.execSQL("insert into bookmark_dg_tmp(id, url, create_time, detail_id, category_id, uid) select id, url, create_time, detail_id, category_id, uid from bookmark;")
                database.execSQL("drop table bookmark;")
                database.execSQL("alter table bookmark_dg_tmp rename to bookmark;")
            }
        }

    }
}

