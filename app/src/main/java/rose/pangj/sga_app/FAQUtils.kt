package rose.pangj.sga_app

import android.content.Context
import org.apache.commons.io.IOUtils
import java.io.IOException
import java.util.ArrayList

object FAQUtils {
    fun loadFAQs(context: Context?): ArrayList<FAQ> {
        val titles = arrayOf(
            "What does SGA do?",
            "How do I get involved?",
            "When/where are the meetings of the Senate?",
            "Who is my senator?",
            "What sorts of clubs does SGA sponsor?",
            "How can I get funding from SGA?"

        )
        val resources = arrayOf(
            R.raw.faq1,
            R.raw.faq2,
            R.raw.faq3,
            R.raw.faq4,
            R.raw.faq5,
            R.raw.faq6

        )
        val faqs = ArrayList<FAQ>()
        for (i in titles.indices) {
            val stream = context?.resources?.openRawResource(resources[i])
            var s: String? = null
            try {
                s = IOUtils.toString(stream)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            IOUtils.closeQuietly(stream) // don't forget to close your streams
            val faq = FAQ(titles[i], s!!)
            faqs.add(faq)
        }
        return faqs
    }
}
