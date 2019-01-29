package rose.pangj.sga_app

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.row_view_news.view.*


class NewsViewHolder(itemView: View, var adapter: NewsListAdapter): RecyclerView.ViewHolder(itemView){

    val caption = itemView.news_caption as TextView
    val newscontent = itemView.news_content as TextView

    init {
        itemView.setOnClickListener {
            adapter.selectNewsAt(adapterPosition)
        }


    }

    fun bind(pic: News){
        caption.text = pic.newscaption
        newscontent.text = pic.newscontent
    }
}