package online.mengchen.collectionhelper.data.repository

import online.mengchen.collectionhelper.common.StoreType.TypeStore
import online.mengchen.collectionhelper.data.db.dao.FileInfoDao
import online.mengchen.collectionhelper.domain.entity.FileInfo
import online.mengchen.collectionhelper.utils.LoginUtils

class FileInfoRepository(private val fileInfoDao: FileInfoDao) {

    suspend fun insert(fileInfo: FileInfo) {
        fileInfoDao.insert(fileInfo)
    }

    suspend fun getFileInfoByCategoryId(categoryId: Long, @TypeStore storeType: Int): List<FileInfo> {
        return fileInfoDao.findByCategory(categoryId, LoginUtils.user?.userId!!, storeType)
    }

    suspend fun getFileInfoByKey(key: String): FileInfo? {
        return fileInfoDao.findByKey(key)
    }

}