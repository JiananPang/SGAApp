package rose.pangj.sga_app

import android.view.View
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder
import android.R
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import kotlinx.android.synthetic.main.message_list_parent.view.*
import kotlinx.android.synthetic.main.senator_row_view.view.*
import kotlinx.android.synthetic.main.their_message.view.*


class MessageParentViewHolder(itemView: View, var adapter: MessageExpandableAdapter): RecyclerView.ViewHolder(itemView){

    val name = itemView.senator_text_view as TextView
    val messageBody = itemView.district_text_view as TextView
    val count = itemView.item_count_text as TextView

    init {
        itemView.setOnClickListener{
            adapter.selectSenatorAt(adapterPosition)
        }
    }

    fun bind(student: Senator) {
        name.text = student.name
        messageBody.text = student.district
        count.text = student.count.toString()
        if (student.count > 0) count.visibility = View.VISIBLE else count.visibility = View.INVISIBLE
    }
}