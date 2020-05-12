package online.mengchen.collectionhelper.data.repository

import online.mengchen.collectionhelper.data.db.dao.TencentCOSConfigDao
import online.mengchen.collectionhelper.domain.entity.TencentCOSConfig
import online.mengchen.collectionhelper.utils.LoginUtils


class TencentCOSConfigRepository(private val tencentCOSConfigDao: TencentCOSConfigDao) {

    suspend fun getTencentCOSConfig(): TencentCOSConfig? {
        return tencentCOSConfigDao.findByUid(LoginUtils.user?.userId!!)
    }


    suspend fun insert(tencentCOSConfig: TencentCOSConfig) {
        tencentCOSConfigDao.insert(tencentCOSConfig)
    }

    suspend fun update(tencentCOSConfig: TencentCOSConfig) {
        tencentCOSConfigDao.update(tencentCOSConfig)
    }


}