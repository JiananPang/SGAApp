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

class NewsListAdapter(var context: Context?, var listener: NewsFragment.OnNewsSelectedListener?): RecyclerView.Adapter<NewsViewHolder>() {
    var newss = ArrayList<News>()

    private val ref = FirebaseFirestore
        .getInstance()
        .collection(Constants.NEWS)
    private lateinit var listenerRegistration: ListenerRegistration

    fun showAddEditDialog(position: Int = -1) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Add a News")
        val view = LayoutInflater.from(context).inflate(
            R.layout.dialog_add_edit_pic, null, false
        )
        builder.setView(view)

        builder.setIcon(android.R.drawable.ic_input_add)
        if (position >= 0) {
            view.edit_caption.setText(newss[position].newscaption)
            view.edit_url.setText(newss[position].newscontent)
            view.edit_date.setText(newss[position].newsdate)
        }

        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            val caption = view.edit_caption.text.toString()
            var urlString = view.edit_url.text.toString()
            val date = view.edit_date.text.toString()
            add(caption, urlString, date)

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
        newss.clear()
        for (document in querySnapshot.documents) {
            // This is a very convenient helper method.
            Log.d(Constants.TAG, "document: $document")
            newss.add(News.fromSnapshot(document))
        }
        notifyDataSetChanged()
        if (newss.isNotEmpty()) {
            Log.d(Constants.TAG, "ID of first: " + newss[0].id)
        }
    }

    private fun processSnapshotChanges(querySnapshot: QuerySnapshot) {
        // Snapshots has documents and documentChanges which are flagged by type,
        // so we can handle C,U,D differently.
        for (documentChange in querySnapshot.documentChanges) {
            val pic = News.fromSnapshot(documentChange.document)
            when (documentChange.type) {
                DocumentChange.Type.ADDED -> {
                    Log.d(Constants.TAG, "Adding $pic")
                    newss.add(0, pic)
                    notifyItemInserted(0)
                }
                DocumentChange.Type.REMOVED -> {
                    Log.d(Constants.TAG, "Removing $pic")
//                    movieQuotes.remove(movieQuote)
//                    notifyDataSetChanged()
                    for ((k, mq) in newss.withIndex()) {
                        if (mq.id == pic.id) {
                            newss.removeAt(k)
                            notifyItemRemoved(k)
                            break
                        }
                    }
                }
                DocumentChange.Type.MODIFIED -> {
                    Log.d(Constants.TAG, "Modifying $pic")
                    for ((k, mq) in newss.withIndex()) {
                        if (mq.id == pic.id) {
                            newss[k] = pic
                            notifyItemChanged(k)
                            break
                        }
                    }
                }
            }
        }
    }



    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NewsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_view_news, p0, false)
        return NewsViewHolder(view, this)
    }
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {

        holder.bind(newss[position])
    }
    override fun getItemCount() = newss.size


    fun add(cap: String, con: String, date: String){
        ref.add(News(cap,date, con))
    }

    fun selectNewsAt(pos: Int){
        listener?.onNewsSelected(newss[pos])
    }




}
