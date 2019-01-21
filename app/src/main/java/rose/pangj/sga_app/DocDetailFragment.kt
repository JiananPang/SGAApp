package rose.pangj.sga_app

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_documents_detail.view.*

private const val ARG_DOC = "doc"

class DocDetailFragment : Fragment() {
    private var doc: Doc? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            doc = it.getParcelable(ARG_DOC)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_documents_detail, container, false)
        view.fragment_doc_detail_title.text = doc?.title
        view.fragment_doc_detail_body.text = doc?.text
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(doc: Doc) =
            DocDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_DOC, doc)
                }
            }
    }
}
