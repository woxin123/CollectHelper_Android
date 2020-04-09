package online.mengchen.collectionhelper.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDateTime

class LocalDateTypeConverter {

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toDate(destString: String): LocalDateTime {
        return LocalDateTime.parse(destString)
    }

    @TypeConverter
    fun toDateString(date: LocalDateTime): String {
        return date.toString()
    }

}