package rose.pangj.sga_app

import android.widget.TextView
import android.graphics.Color.parseColor
import android.graphics.drawable.GradientDrawable
import android.support.design.widget.CoordinatorLayout.Behavior.setTag
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.google.firebase.firestore.*


class UserChatListAdapter(var context: Context, var suid: String, var muid: String) : BaseAdapter() {

    internal var messages: MutableList<Message> = ArrayList<Message>()
    private val messageRef = FirebaseFirestore.getInstance().collection(Constants.CHAT_COLLECTION)
    private lateinit var listenerRegistration: ListenerRegistration

    fun addSnapshotListener() {
        listenerRegistration = messageRef
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                } else {
                    processSnapshotChanges(querySnapshot!!)
                }
            }
    }

    private fun processSnapshotChanges(querySnapshot: QuerySnapshot) {
        // Snapshots has documents and documentChanges which are flagged by type,
        // so we can handle C,U,D differently.
        for (documentChange in querySnapshot.documentChanges) {
            val message = Message.fromSnapshot(documentChange.document)
            when (documentChange.type) {
                DocumentChange.Type.ADDED -> {
                    messages.add(message)
                    notifyDataSetChanged()
                }

            }
        }
    }

    fun add(message: Message) {
        messageRef.add(message)
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

        if (message.sendBy == muid) { // this message was sent by us so let's create a basic chat bubble on the right
            convertView = messageInflater.inflate(R.layout.my_message, null)
            holder.messageBody = convertView.findViewById(R.id.message_body)
            convertView.setTag(holder)
            holder.messageBody!!.setText(message.text)
        } else { // this message was sent by someone else so let's create an advanced chat bubble on the left
            convertView = messageInflater.inflate(R.layout.their_message, null)
            holder.avatar = convertView.findViewById(R.id.avatar) as View
            holder.name = convertView.findViewById(R.id.name)
            holder.name!!.text = suid
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