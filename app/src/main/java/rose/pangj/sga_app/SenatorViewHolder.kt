package rose.pangj.sga_app

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.senator_row_view.view.*

class SenatorViewHolder(itemView: View, var adapter: SenatorListAdapter): RecyclerView.ViewHolder(itemView){

    val name = itemView.senator_text_view as TextView
    val district = itemView.district_text_view as TextView
    val src = itemView.senator_image as ImageView

    init {
        itemView.setOnClickListener {
            adapter.selectSenatorAt(adapterPosition)
        }
    }

    fun bind(senator: Senator){
        name.text = senator.name
        district.text = senator.district
        src.setImageResource(senator.src)
    }
}