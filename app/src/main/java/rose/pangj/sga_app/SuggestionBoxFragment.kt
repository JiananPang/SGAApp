package rose.pangj.sga_app

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_suggestion_box.*
import kotlinx.android.synthetic.main.fragment_suggestion_box.view.*

private const val ARG_MUID = "MUID"
private const val ARG_SUID = "SUID"
class SuggestionBoxFragment() : Fragment(){
    var muid = ""
    var suid = ""
    lateinit var adapter: UserChatListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            suid = it.getString(ARG_SUID)
            muid = it.getString(ARG_MUID)

        }

        adapter = UserChatListAdapter(context!!, suid, muid )
        adapter.addSnapshotListener()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_suggestion_box, container, false)
        view.messages_view.adapter = adapter
        view.sendMessage.setOnClickListener{
            val input = editText.text
            adapter.add(Message(input.toString(), suid, muid))
            input.clear()

        }
        return view
    }
    companion object {
        @JvmStatic
        fun newInstance(suid: String, muid: String) =
            SuggestionBoxFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_SUID, suid)
                    putString(ARG_MUID, muid)
                }
            }
    }


 }



