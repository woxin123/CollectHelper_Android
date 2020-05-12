package online.mengchen.collectionhelper.data.repository

import online.mengchen.collectionhelper.data.db.dao.QiNiuYunConfigDao
import online.mengchen.collectionhelper.domain.entity.QiniuConfig
import online.mengchen.collectionhelper.utils.LoginUtils

class QiNiuYunConfigRepository(private val qiNiuYunConfigDao: QiNiuYunConfigDao) {

    suspend fun insert(qiNiuYunConfig: QiniuConfig) {
        qiNiuYunConfigDao.insert(qiNiuYunConfig)
    }

    suspend fun update(qiNiuYunConfig: QiniuConfig) {
        qiNiuYunConfigDao.update(qiNiuYunConfig)
    }

    suspend fun getByUid(): QiniuConfig? {
        return qiNiuYunConfigDao.findByUid(LoginUtils.user?.userId!!)
    }

}