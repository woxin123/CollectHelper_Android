package online.mengchen.collectionhelper.domain.entity

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class QiniuConfig(
    @PrimaryKey var id: Long? = null,
    @ColumnInfo(name = "access_key") val accessKey: String,
    @ColumnInfo(name = "secret_key") val secretKey: String,
    @ColumnInfo(name = "bucket") val bucket: String,
    @ColumnInfo(name = "image_bucket") val imageBucket: String,
    @ColumnInfo(name = "document_bucket") val documentBucket: String,
    @ColumnInfo(name = "music_bucket") val musicBucket: String,
    @ColumnInfo(name = "video_bucketName") val videoBucket: String,
    @ColumnInfo(name = "image_path") val imagePath: String,
    @ColumnInfo(name = "document_path") val documentPath: String,
    @ColumnInfo(name = "music_path") val musicPath: String,
    @ColumnInfo(name = "video_path") val videoPath: String
)