package rose.pangj.sga_app
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import rose.pangj.sga_app.*

class FAQAdapter(var context: Context?) : RecyclerView.Adapter<FAQViewHolder>() {

    var faqs = ArrayList<FAQ>()

    init {
        faqs = FAQUtils.loadFAQs(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_view_faqs, parent, false)
        return FAQViewHolder(view, this)
    }

    override fun onBindViewHolder(holder: FAQViewHolder, position: Int) {
        holder.bind(faqs[position])
    }
    override fun getItemCount() = faqs.size


}