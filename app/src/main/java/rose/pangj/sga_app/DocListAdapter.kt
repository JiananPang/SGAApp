package rose.pangj.sga_app

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class DocListAdapter(var context: Context?, var listener: DocumentsFragment.OnDocSelectedListener?) : RecyclerView.Adapter<DocViewHolder>() {

    var docs = ArrayList<Doc>()

    init {
        docs = DocUtils.loadDocs(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_view_document, parent, false)
        return DocViewHolder(view, this)
    }

    override fun onBindViewHolder(holder: DocViewHolder, position: Int) {
        holder.bind(docs[position])
    }
    override fun getItemCount() = docs.size

    fun selectDocAt(position: Int) {
        listener?.onDocSelected(docs[position])
    }
}