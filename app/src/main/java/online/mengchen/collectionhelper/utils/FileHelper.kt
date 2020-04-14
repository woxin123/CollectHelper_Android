package online.mengchen.collectionhelper.utils

import java.io.*
import java.nio.file.Path

object FileHelper {

    /**
     * 将 inputStream 保存到 File 中
     */
    fun saveFile(inputStream: InputStream, file: File) {
        var outputStream: OutputStream? = null
        try {
            outputStream = FileOutputStream(file)
            val bytes = ByteArray(1024)
            var len = 0
            while (inputStream.read(bytes).also { len = it } > 0) {
                outputStream.write(bytes, 0, len)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            inputStream.close()
            outputStream?.close()
        }
    }

    fun deleteFile(file: File): Boolean {
        return file.delete()
    }

    fun getFileName(fileName: String): String? {
        val lastIndex = fileName.lastIndexOf("/")
        if (lastIndex == -1) {
            return null
        }
        return fileName.substring(lastIndex + 1)
    }

    fun getFileSuffix(fileName: String): String? {
        val lastIndex = fileName.lastIndexOf(".")
        if (lastIndex == -1) {
            return null
        }
        return fileName.substring(lastIndex + 1)
    }
}