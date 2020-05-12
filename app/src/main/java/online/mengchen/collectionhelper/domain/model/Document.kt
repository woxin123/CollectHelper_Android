package online.mengchen.collectionhelper.domain.model

import androidx.annotation.IntDef

data class DocumentInfo (
    var documentName: String,
    var key: String,
    var categoryInfo: CategoryInfo,
    var createTime: String,
    var documentUrl: String,
    @DocumentType var documentType: Int,
    var filePath: String? = null
) {
    companion object {
        const val WORD = 0
        const val EXCEL = 1
        const val PPT = 2
        const val PDF = 3
        const val OTHER = 4
    }

    @IntDef(value = [WORD, EXCEL, PPT, PDF, OTHER])
    @Retention(AnnotationRetention.SOURCE)
    annotation class DocumentType {

    }
}