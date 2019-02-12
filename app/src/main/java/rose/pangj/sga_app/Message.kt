package rose.pangj.sga_app

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ServerTimestamp


class Message(
    val text: String = "", // message body
    var receiveBy: String = "",
    var sendBy: String = "" // data of the user that sent this message
     // is this message sent by us?
):Parcelable {
    @get:Exclude
    var id = ""
    @ServerTimestamp
    var lastTouched: Timestamp? = null

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
        id = parcel.readString()
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(text)
        parcel.writeString(receiveBy)
        parcel.writeString(sendBy)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Message> {
        override fun createFromParcel(parcel: Parcel): Message {
            return Message(parcel)
        }

        override fun newArray(size: Int): Array<Message?> {
            return arrayOfNulls(size)
        }

        fun fromSnapshot(snapshot: QueryDocumentSnapshot): Message {
            val message = snapshot.toObject(Message::class.java)
            message.id = snapshot.id
            return message
        }
    }


}

