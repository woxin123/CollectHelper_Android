package online.mengchen.collectionhelper.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import online.mengchen.collectionhelper.domain.entity.FileInfo

@Dao
interface FileInfoDao {

    @Query("SELECT * FROM file_info WHERE file_type = :fileType AND uid = :uid AND store_type = :storeType")
    suspend fun findByFileType(fileType: Int, uid: Long, storeType: Int): List<FileInfo>

    @Query("SELECT * FROM file_info WHERE category_id = :categoryId AND uid = :uid AND store_type = :storeType")
    suspend fun findByCategory(categoryId: Int, uid: Long, storeType: Int): FileInfo?

    @Insert
    suspend fun insert(fileInfo: FileInfo)

    @Delete
    suspend fun delete(fileInfo: FileInfo)
}