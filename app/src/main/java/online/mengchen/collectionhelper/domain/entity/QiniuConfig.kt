package online.mengchen.collectionhelper.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "qiniu_config")
data class QiniuConfig(
    @PrimaryKey var id: Long? = null,
    @ColumnInfo(name = "access_key") var accessKey: String,
    @ColumnInfo(name = "secret_key") var secretKey: String,
    @ColumnInfo(name = "bucket") var bucket: String,
    @ColumnInfo(name = "image_bucket") var imageBucket: String?,
    @ColumnInfo(name = "document_bucket") var documentBucket: String?,
    @ColumnInfo(name = "music_bucket") var musicBucket: String?,
    @ColumnInfo(name = "video_bucket") var videoBucket: String?,
    @ColumnInfo(name = "image_path") var imagePath: String?,
    @ColumnInfo(name = "document_path") var documentPath: String?,
    @ColumnInfo(name = "music_path") var musicPath: String?,
    @ColumnInfo(name = "video_path") var videoPath: String?,
    @ColumnInfo(name = "domain") var domain: String,
    @ColumnInfo(name = "uid") var uid: Long

)