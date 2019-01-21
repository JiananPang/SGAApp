package rose.pangj.sga_app

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.row_view_document.view.*

class DocViewHolder(itemView: View, var adapter: DocListAdapter) : RecyclerView.ViewHolder(itemView) {
    private val titleTextView = itemView.document_title_text_view as TextView

    init {
        itemView.setOnClickListener {
            adapter.selectDocAt(adapterPosition)
        }
    }

    fun bind(doc: Doc) {
        titleTextView.text = doc.title
    }
}
