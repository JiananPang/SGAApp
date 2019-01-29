package rose.pangj.sga_app

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.android.synthetic.main.fragment_news_detail.view.*

private const val ARG = "news"

class NewsDetailFragment: Fragment(){

    private var news:News? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let{
            news = it.getParcelable(ARG)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_news_detail, container, false)

        view.news_title_textView.text = news?.newscaption
        view.news_date_textView.text = news?.newsdate
        view.newsContentTextView.text = news?.newscontent
        return view
    }
    companion object {
        @JvmStatic
        fun newInstance(doc: News) =
            NewsDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG, doc)
                }
            }
    }
}