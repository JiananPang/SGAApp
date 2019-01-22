package rose.pangj.sga_app

import android.content.Context
import java.io.IOException
import java.util.*
import org.apache.commons.io.IOUtils


object DocUtils {
    fun loadDocs(context: Context?): ArrayList<Doc> {
        val titles = arrayOf(
            "SGA constitution",
            "Treasury Operating Code",
            "Finance Commitee's Policy"

        )
        val resources = arrayOf(
            R.raw.constitution,
            R.raw.treasury_operating_code,
            R.raw.finance

        )
        val docs = ArrayList<Doc>()
        for (i in titles.indices) {
            val stream = context?.resources?.openRawResource(resources[i])
            var s: String? = null
            try {
                s = IOUtils.toString(stream)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            IOUtils.closeQuietly(stream) // don't forget to close your streams
            val doc = Doc(titles[i], s!!)
            docs.add(doc)
        }
        return docs
    }
}
