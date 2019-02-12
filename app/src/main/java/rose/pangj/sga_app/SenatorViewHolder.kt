package rose.pangj.sga_app

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.row_view_events.view.*
import kotlinx.android.synthetic.main.row_view_news.view.*

class SenatorViewHolder(itemView: View, var adapter: SenatorListAdapter): RecyclerView.ViewHolder(itemView){

    val caption = itemView.event_caption as TextView
    val newscontent = itemView.event_content as TextView

    init {
        itemView.setOnClickListener {
            adapter.selectSenatorAt(adapterPosition)
        }

    }

    fun bind(pic: Event){
        caption.text = pic.newscaption
        newscontent.text = pic.newscontent
    }
}