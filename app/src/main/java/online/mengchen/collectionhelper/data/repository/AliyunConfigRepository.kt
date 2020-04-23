package online.mengchen.collectionhelper.data.repository

import online.mengchen.collectionhelper.data.db.dao.AliyunConfigDao
import online.mengchen.collectionhelper.domain.entity.AliyunConfig
import online.mengchen.collectionhelper.utils.LoginUtils

class AliyunConfigRepository (
    private val aliyunConfigDao: AliyunConfigDao) {

    val aliyunConfig = aliyunConfigDao.findByUid(LoginUtils.user?.userId!!)

    suspend fun insert(aliyunConfig: AliyunConfig) {
        aliyunConfigDao.insert(aliyunConfig)
    }

    suspend fun update(aliyunConfig: AliyunConfig) {
        aliyunConfigDao.update(aliyunConfig)
    }
}