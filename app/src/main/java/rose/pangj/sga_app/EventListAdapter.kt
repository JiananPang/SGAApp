package rose.pangj.sga_app

import android.app.AlertDialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.dialog_add_edit_pic.view.*


class EventListAdapter(var context: Context?, var listener: EventFragment.OnEventSelectedListener?): RecyclerView.Adapter<EventViewHolder>() {
    var events = ArrayList<Event>()

    private val ref = FirebaseFirestore
        .getInstance()
        .collection(Constants.EVENT)
    private lateinit var listenerRegistration: ListenerRegistration

    fun showAddEditDialog(position: Int = -1) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Add a quote")
        val view = LayoutInflater.from(context).inflate(
            R.layout.dialog_add_edit_pic, null, false
        )
        builder.setView(view)

        builder.setIcon(android.R.drawable.ic_input_add)
        if (position >= 0) {
            view.edit_caption.setText(events[position].newscaption)
            view.edit_url.setText(events[position].newscontent)
        }

        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            val caption = view.edit_caption.text.toString()
            var urlString = view.edit_url.text.toString()
            add(caption, urlString)

        }
        builder.setNegativeButton(android.R.string.cancel, null)
        builder.show()
    }

    fun addSnapshotListener() {
        listenerRegistration = ref

            .orderBy(News.LAST_TOUCHED_KEY)
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                    Log.w(Constants.TAG, "listen error", e)
                    return@addSnapshotListener
                }
//                populateLocalQuotes(querySnapshot!!)
                processSnapshotChanges(querySnapshot!!)
            }
    }

    fun removeSnapshotListener() {
        Log.d(Constants.TAG, "Removing listener")
        listenerRegistration.remove()
        //movieQuotes.clear()
    }

    fun populateLocalQuotes(querySnapshot: QuerySnapshot) {
        // First attempt: just get them all.
        Log.d(Constants.TAG, "Populating")
        events.clear()
        for (document in querySnapshot.documents) {
            // This is a very convenient helper method.
            Log.d(Constants.TAG, "document: $document")
            events.add(Event.fromSnapshot(document))
        }
        notifyDataSetChanged()
        if (events.isNotEmpty()) {
            Log.d(Constants.TAG, "ID of first: " + events[0].id)
        }
    }

    private fun processSnapshotChanges(querySnapshot: QuerySnapshot) {
        // Snapshots has documents and documentChanges which are flagged by type,
        // so we can handle C,U,D differently.
        for (documentChange in querySnapshot.documentChanges) {
            val pic = Event.fromSnapshot(documentChange.document)
            when (documentChange.type) {
                DocumentChange.Type.ADDED -> {
                    Log.d(Constants.TAG, "Adding $pic")
                    events.add(0, pic)
                    notifyItemInserted(0)
                }
                DocumentChange.Type.REMOVED -> {
                    Log.d(Constants.TAG, "Removing $pic")
//                    movieQuotes.remove(movieQuote)
//                    notifyDataSetChanged()
                    for ((k, mq) in events.withIndex()) {
                        if (mq.id == pic.id) {
                            events.removeAt(k)
                            notifyItemRemoved(k)
                            break
                        }
                    }
                }
                DocumentChange.Type.MODIFIED -> {
                    Log.d(Constants.TAG, "Modifying $pic")
                    for ((k, mq) in events.withIndex()) {
                        if (mq.id == pic.id) {
                            events[k] = pic
                            notifyItemChanged(k)
                            break
                        }
                    }
                }
            }
        }
    }



    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): EventViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_view_news, p0, false)
        return EventViewHolder(view, this)
    }
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {

        holder.bind(events[position])
    }
    override fun getItemCount() = events.size


    fun add(cap: String, con: String){
        ref.add(News(cap, con))
    }

    fun selectEventAt(pos: Int){
        listener?.onEventSelected(events[pos])
    }




}