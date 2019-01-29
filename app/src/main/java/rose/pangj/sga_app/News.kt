package rose.pangj.sga_app

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp

data class News(var newscaption: String = "",var newsdate: String = "", var newscontent: String = ""): Parcelable {
    @get:Exclude
    var id = ""
    @ServerTimestamp
    var lastTouched: Timestamp? = null

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(newscaption)
        parcel.writeString(newsdate)
        parcel.writeString(newscontent)
    }

    companion object CREATOR : Parcelable.Creator<News> {
        const val LAST_TOUCHED_KEY = "lastTouched"

        override fun createFromParcel(parcel: Parcel): News {
            return News(parcel)
        }

        override fun newArray(size: Int): Array<News?> {
            return arrayOfNulls(size)
        }

        fun fromSnapshot(snapshot: DocumentSnapshot): News {
            val news = snapshot.toObject(News::class.java)!!
            news.id = snapshot.id
            return news
        }
    }
}