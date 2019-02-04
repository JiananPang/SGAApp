package rose.pangj.sga_app

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp

data class Event(var newscaption: String = "", var newscontent: String = ""): Parcelable {
    @get:Exclude
    var id = ""
    @ServerTimestamp
    var lastTouched: Timestamp? = null

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!
    )
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(newscaption)
        parcel.writeString(newscontent)
    }

    companion object CREATOR : Parcelable.Creator<Event> {
        const val LAST_TOUCHED_KEY = "lastTouched"

        override fun createFromParcel(parcel: Parcel): Event {
            return Event(parcel)
        }

        override fun newArray(size: Int): Array<Event?> {
            return arrayOfNulls(size)
        }

        fun fromSnapshot(snapshot: DocumentSnapshot): Event {
            val event = snapshot.toObject(Event::class.java)!!
            event.id = snapshot.id
            return event
        }
    }
}