package rose.pangj.sga_app

import android.widget.TextView
import android.graphics.Color.parseColor
import android.graphics.drawable.GradientDrawable
import android.support.design.widget.CoordinatorLayout.Behavior.setTag
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter


class UserChatListAdapter(var context: Context, var uid: String) : BaseAdapter() {

    internal var messages: MutableList<Message> = ArrayList<Message>()

    fun add(message: Message) {
        this.messages.add(message)
        notifyDataSetChanged() // to render the list we need to notify
    }

    override fun getCount(): Int {
        return messages.size
    }

    override fun getItem(i: Int): Any {
        return messages[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    // This is the backbone of the class, it handles the creation of single ListView row (chat bubble)
    override fun getView(i: Int, convertView: View?, viewGroup: ViewGroup): View {
        var convertView = convertView
        val holder = MessageViewHolder()
        val messageInflater = context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val message = messages[i]

        if (message.sendBy == uid) { // this message was sent by us so let's create a basic chat bubble on the right
            convertView = messageInflater.inflate(R.layout.my_message, null)
            holder.messageBody = convertView.findViewById(R.id.message_body)
            convertView.setTag(holder)
            holder.messageBody!!.setText(message.text)
        } else { // this message was sent by someone else so let's create an advanced chat bubble on the left
            convertView = messageInflater.inflate(R.layout.their_message, null)
            holder.avatar = convertView.findViewById(R.id.avatar) as View
            holder.name = convertView.findViewById(R.id.name)
            holder.messageBody = convertView.findViewById(R.id.message_body)
            convertView.setTag(holder)

            holder.name!!.setText(message.sendBy)
            holder.messageBody!!.setText(message.text)
        }

        return convertView
    }

}

internal class MessageViewHolder {
    var avatar: View? = null
    var name: TextView? = null
    var messageBody: TextView? = null
}