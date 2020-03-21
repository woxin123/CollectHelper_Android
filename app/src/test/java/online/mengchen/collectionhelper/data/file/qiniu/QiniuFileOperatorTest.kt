package online.mengchen.collectionhelper.data.file.qiniu

import org.junit.Before
import org.junit.Test
import java.io.File

class QiniuFileOperatorTest {

    companion object {
        const val BUCKET_NAME = "mctest001"
        const val FILE_DIR = "/Users/bytedance/Documents/CollectionHelper/android/CollectionHelper/app/src/main/res/mipmap-hdpi/ic_launcher_round.png"
        const val KEY = "test.jpg"
    }

    private lateinit var token: String
    private lateinit var qiniuFileOPerator: QiniuFileOperator

    @Before
    fun setUp() {
        token = QiniuConfig.getToken(BUCKET_NAME)
        qiniuFileOPerator = QiniuFileOperator(token)
    }

    @Test
    fun testFileUpload() {
        qiniuFileOPerator.upload(KEY, File(FILE_DIR))
    }

}