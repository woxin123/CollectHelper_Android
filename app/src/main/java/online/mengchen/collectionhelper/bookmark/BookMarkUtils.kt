package online.mengchen.collectionhelper.bookmark

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import java.net.URL

object BookMarkUtils {

    private const val FAVICON = "favicon.ico"

    val map: MutableMap<String, (Document) -> String> = mutableMapOf()


    init {
       map["blog.csdn.net"] = { doc ->
           val n = doc.selectFirst("#article_content")
           extractText(n)
       }
    }


    fun parseUrlToBookMark(urlString: String): BookMarkDetailInfo? {
        val doc = Jsoup.connect(urlString).execute().parse()
        val title = doc.title()
        val url = URL(urlString)
        println(url.host)
        val favicon = url.protocol + "://" + url.host + (if (url.port != -1) url.port else "") + "/" + FAVICON
        val summary: String = map[url.host]?.invoke(doc) ?: doc.selectFirst("h1").run {
            if (this == null) {
                ""
            } else {
                extractText(this)
            }
        }
        println(summary)
        return BookMarkDetailInfo(-1, title, summary, favicon)
    }

    private fun extractText(node: Node): String {
        if (node is TextNode) {
            return node.text()
        }
        val buffer = StringBuffer()
        for (child in node.childNodes()) {
            if (buffer.length > 200) {
                return buffer.toString()
            }
            buffer.append(extractText(child).trim())
        }
        return buffer.toString()
    }

}
