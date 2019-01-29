package rose.pangj.sga_app

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.app_bar_main.*

class NewsFragment : Fragment() {
    lateinit var adapter: NewsListAdapter
    private var listener: OnNewsSelectedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = NewsListAdapter(context, listener)
        adapter.addSnapshotListener()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val recyclerView = inflater.inflate(R.layout.fragment_news_list, container, false) as RecyclerView

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        return recyclerView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnNewsSelectedListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnDocSelectedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
    interface OnNewsSelectedListener {
        fun onNewsSelected(pic: News)

    }


}


