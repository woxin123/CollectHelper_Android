package online.mengchen.collectionhelper.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import online.mengchen.collectionhelper.common.FileType
import online.mengchen.collectionhelper.common.StoreType
import online.mengchen.collectionhelper.domain.entity.FileInfo

@Dao
interface FileInfoDao {

    @Query("SELECT * FROM file_info WHERE file_type = :fileType AND uid = :uid AND store_type = :storeType")
    suspend fun findByFileType(fileType: Int, uid: Long, storeType: Int): List<FileInfo>

    @Query("SELECT * FROM file_info WHERE category_id = :categoryId AND uid = :uid AND store_type = :storeType")
    suspend fun findByCategory(categoryId: Long, uid: Long, storeType: Int): List<FileInfo>

    @Query("SELECT * FROM file_info WHERE `key` = :key")
    suspend fun findByKey(key: String): FileInfo?

    @Query("SELECT * FROM file_info WHERE file_type = :fileType AND store_type = :storeType AND uid = :uid")
    suspend fun findByFileTypeAndStoreType(@FileType.TypeFile fileType: Int, @StoreType.TypeStore storeType: Int, uid: Long): List<FileInfo>

    @Insert
    suspend fun insert(fileInfo: FileInfo)

    @Delete
    suspend fun delete(fileInfo: FileInfo)
}