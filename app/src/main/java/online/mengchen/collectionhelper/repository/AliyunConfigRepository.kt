package online.mengchen.collectionhelper.repository

import androidx.lifecycle.LiveData
import online.mengchen.collectionhelper.dao.AliyunConfigDao
import online.mengchen.collectionhelper.domain.entity.AliyunConfig
import online.mengchen.collectionhelper.utils.LoginUtils

class AliyunConfigRepository (
    private val aliyunConfigDao: AliyunConfigDao) {

    val aliyunConfig = aliyunConfigDao.findByUid(1)

}