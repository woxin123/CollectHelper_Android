package online.mengchen.collectionhelper.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User (
    @PrimaryKey(autoGenerate = true) val uid: Long? = null,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "avatar") val avatar: String,
    @ColumnInfo(name = "phone") val phone: String
)