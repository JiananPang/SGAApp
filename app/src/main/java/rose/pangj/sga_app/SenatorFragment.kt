package rose.pangj.sga_app

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

private const val ARG_MY_UID = "UID"
class SenatorFragment : Fragment() {

    lateinit var adapter: SenatorListAdapter
    private var listener: SenatorFragment.OnSenatorSelectedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = SenatorListAdapter(context, listener)
        adapter.addSnapshotListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val recyclerView = inflater.inflate(R.layout.fragment_senator_list, container, false) as RecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        return recyclerView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSenatorSelectedListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnSenatorSelectedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
    interface OnSenatorSelectedListener {
        fun onSenatorSelected(senator: Senator)

    }



}

