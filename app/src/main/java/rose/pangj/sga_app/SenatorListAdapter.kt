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


class SenatorListAdapter(var context: Context?, var listener: SenatorFragment.OnSenatorSelectedListener?): RecyclerView.Adapter<SenatorViewHolder>() {
    var senators = ArrayList<Event>()

    private val ref = FirebaseFirestore
        .getInstance()
        .collection(Constants.SENATOR)
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
            view.edit_caption.setText(senators[position].newscaption)
            view.edit_url.setText(senators[position].newscontent)
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
                processSnapshotChanges(querySnapshot!!)
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
                    senators.add(0, pic)
                    notifyItemInserted(0)
                }
                DocumentChange.Type.REMOVED -> {
                    Log.d(Constants.TAG, "Removing $pic")
//                    movieQuotes.remove(movieQuote)
//                    notifyDataSetChanged()
                    for ((k, mq) in senators.withIndex()) {
                        if (mq.id == pic.id) {
                            senators.removeAt(k)
                            notifyItemRemoved(k)
                            break
                        }
                    }
                }
                DocumentChange.Type.MODIFIED -> {
                    Log.d(Constants.TAG, "Modifying $pic")
                    for ((k, mq) in senators.withIndex()) {
                        if (mq.id == pic.id) {
                            senators[k] = pic
                            notifyItemChanged(k)
                            break
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SenatorViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_view_events, p0, false)
        return SenatorViewHolder(view, this)
    }
    override fun onBindViewHolder(holder: SenatorViewHolder, position: Int) {

        holder.bind(senators[position])
    }
    override fun getItemCount() = senators.size


    fun add(cap: String, con: String){
        ref.add(Event(cap, con))
    }

    fun selectSenatorAt(pos: Int){
        listener?.onSenatorSelected(senators[pos])
    }

}