package rose.pangj.sga_app

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_event_detail.view.*

private const val ARG = "event"

class EventDetailFragment: Fragment(){

    private var event: Event? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let{
            event = it.getParcelable(ARG)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_event_detail, container, false)

        view.event_title_textView.text = event?.newscaption
        view.event_content_textView.text = event?.newscontent
        (activity as MainActivity).fab.hide()
        return view
    }
    companion object {
        @JvmStatic
        fun newInstance(event: Event) =
            EventDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG, event)
                }
            }
    }
}