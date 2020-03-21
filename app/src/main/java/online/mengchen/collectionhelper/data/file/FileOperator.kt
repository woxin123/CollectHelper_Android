package online.mengchen.collectionhelper.data.file

import java.io.File

interface FileOperator {
    fun upload(key: String, file: File): Boolean
    fun delete(id: String)
    fun getFile(id: String)

}