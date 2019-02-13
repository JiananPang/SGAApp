package rose.pangj.sga_app

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.row_view_events.view.*
import kotlinx.android.synthetic.main.row_view_news.view.*

class EventViewHolder(itemView: View, var adapter: EventListAdapter): RecyclerView.ViewHolder(itemView){

    val caption = itemView.event_caption as TextView
    val newscontent = itemView.event_content as TextView
    val date = itemView.event_date as TextView

    init {
        itemView.setOnClickListener {
            adapter.selectEventAt(adapterPosition)
        }

    }

    fun bind(e: Event){
        caption.text = e.newscaption
        newscontent.text = e.newscontent
        date.text = e.date
    }
}