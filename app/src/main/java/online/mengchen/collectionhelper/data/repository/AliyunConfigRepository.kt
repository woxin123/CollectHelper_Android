package online.mengchen.collectionhelper.data.repository

import online.mengchen.collectionhelper.data.db.dao.AliyunConfigDao

class AliyunConfigRepository (
    private val aliyunConfigDao: AliyunConfigDao) {

    val aliyunConfig = aliyunConfigDao.findByUid(1)

}