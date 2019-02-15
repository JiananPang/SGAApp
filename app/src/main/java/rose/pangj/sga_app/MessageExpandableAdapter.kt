package rose.pangj.sga_app

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter
import com.bignerdranch.expandablerecyclerview.Model.ParentObject
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot


class MessageExpandableAdapter(var context: Context?, var listener: SenatorFragment.OnSenatorSelectedListener?,
                               var count: Int): RecyclerView.Adapter<MessageParentViewHolder>(){

    private val ref = FirebaseFirestore
        .getInstance()
        .collection(Constants.CHAT_COLLECTION)
    private lateinit var listenerRegistration: ListenerRegistration

    var msgs = ArrayList<Message>()
    var students = ArrayList<Senator>()

    fun addSnapshotListener() {
        listenerRegistration = ref

            .orderBy(News.LAST_TOUCHED_KEY)
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                    Log.w(Constants.TAG, "listen error", e)
                    return@addSnapshotListener
                }
                processSnapshotChanges(querySnapshot!!)
            }
    }

    private fun processSnapshotChanges(querySnapshot: QuerySnapshot) {
        for (documentChange in querySnapshot.documentChanges) {
            val msg = Message.fromSnapshot(documentChange.document)
            if (msg.receiveBy == "xix1" || msg.sendBy == "xix1") {
                when (documentChange.type) {
                    DocumentChange.Type.ADDED -> {
                        Log.d(Constants.TAG, "Adding $msg")
                        count+=1
                        toggleList(msg, count)
                    }
                }
            }
        }
        Log.d(Constants.TAG, "msgslist $msgs")
    }

    private fun toggleList(msg: Message, icount: Int) {
        idExists(msg)
        if (msg.receiveBy == "xix1"){
            students.add(0, Senator(msg.sendBy, msg.sendBy + ": " + msg.text, msg.sendBy, icount))
        }else{
            count-=1
            students.add(0, Senator(msg.receiveBy, msg.sendBy + ": " + msg.text, msg.receiveBy, 0))
        }
        notifyItemInserted(0)
        msgs.add(0, msg)
    }

    private fun idExists(msg: Message){
        for (i in 0 until students.size){
            if (students[i].uid == msg.sendBy || students[i].uid == msg.receiveBy){
                students.remove(students[i])
                notifyItemRemoved(i)
                break
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageParentViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.senator_row_view, parent, false)
        return MessageParentViewHolder(view, this)
    }

    override fun getItemCount(): Int {
        return students.size
    }

    override fun onBindViewHolder(holder: MessageParentViewHolder, position: Int) {
        holder.bind(students[position])
    }

    fun selectSenatorAt(pos: Int){
        listener?.onSenatorSelected(students[pos])
    }

}