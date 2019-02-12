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
    var senators = ArrayList<Senator>()

    private val ref = FirebaseFirestore
        .getInstance()
        .collection(Constants.SENATOR)
    private lateinit var listenerRegistration: ListenerRegistration

    fun showAddEditDialog( uid: String, position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Add a senator")
        val view = LayoutInflater.from(context).inflate(
            R.layout.dialog_add_edit_pic, null, false
        )
        builder.setView(view)

        builder.setIcon(android.R.drawable.ic_input_add)
        if (position >= 0) {
            view.edit_caption.setText(senators[position].name)
            view.edit_url.setText(senators[position].district)
        }

        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            val caption = view.edit_caption.text.toString()
            var urlString = view.edit_url.text.toString()
            add(caption, urlString, uid, R.mipmap.tony_xi_profile_round)

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
            val senator = Senator.fromSnapshot(documentChange.document)
            when (documentChange.type) {
                DocumentChange.Type.ADDED -> {
                    Log.d(Constants.TAG, "Adding $senator")
                    senators.add(0, senator)
                    notifyItemInserted(0)
                }
                DocumentChange.Type.REMOVED -> {
                    Log.d(Constants.TAG, "Removing $senator")
//                    movieQuotes.remove(movieQuote)
//                    notifyDataSetChanged()
                    for ((k, mq) in senators.withIndex()) {
                        if (mq.id == senator.id) {
                            senators.removeAt(k)
                            notifyItemRemoved(k)
                            break
                        }
                    }
                }
                DocumentChange.Type.MODIFIED -> {
                    Log.d(Constants.TAG, "Modifying $senator")
                    for ((k, mq) in senators.withIndex()) {
                        if (mq.id == senator.id) {
                            senators[k] = senator
                            notifyItemChanged(k)
                            break
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SenatorViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.senator_row_view, p0, false)
        return SenatorViewHolder(view, this)
    }
    override fun onBindViewHolder(holder: SenatorViewHolder, position: Int) {
        holder.bind(senators[position])
    }
    override fun getItemCount() = senators.size

    fun add(cap: String, con: String, uid: String, src: Int){
        ref.add(Senator(cap, con, uid, src))
    }

    fun selectSenatorAt(pos: Int){
        listener?.onSenatorSelected(senators[pos])
    }

}