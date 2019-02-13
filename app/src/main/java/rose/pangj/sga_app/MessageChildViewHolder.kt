package rose.pangj.sga_app

import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder
import kotlinx.android.synthetic.main.message_list_child.view.*

class MessageChildViewHolder(itemView: View, var adapter: MessageExpandableAdapter) : ChildViewHolder(itemView) {
    val replyText = itemView.reply_edit_text as EditText
    val sendButton = itemView.sendReply as ImageButton

//    init {
//        sendButton.setOnClickListener{
//            adapter.sendReply(replyText.text.toString())
//        }
//    }

    fun bind(message: SenateMessage) {
        replyText.setText(message.mChildrenList[0].text)
    }
}