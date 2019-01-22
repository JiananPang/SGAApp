package rose.pangj.sga_app

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.row_view_document.view.*
import kotlinx.android.synthetic.main.row_view_faqs.view.*

class FAQViewHolder(itemView: View, var adapter: FAQAdapter) : RecyclerView.ViewHolder(itemView) {
    private val titleTextView = itemView.faqs_title_text_view as TextView
    private val contentTextView = itemView.faqs_content_text_view as TextView

    fun bind(faq: FAQ) {
        titleTextView.text = faq.title
        contentTextView.text = faq.text

    }
}