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


class SuggestionBoxFragment() : Fragment(){
    var uid = ""
    lateinit var adapter: UserChatListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = UserChatListAdapter(context!!, uid )


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
            adapter.add(Message(input.toString(), "", ""))
            input.clear()

        }
        return view
    }


 }



