package online.mengchen.collectionhelper.repository

import online.mengchen.collectionhelper.dao.FileInfoDao
import online.mengchen.collectionhelper.domain.entity.FileInfo

class FileInfoRepository(private val fileInfoDao: FileInfoDao) {

    suspend fun insert(fileInfo: FileInfo) {
        fileInfoDao.insert(fileInfo)
    }

}