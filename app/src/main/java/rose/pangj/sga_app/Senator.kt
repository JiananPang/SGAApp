package rose.pangj.sga_app

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ServerTimestamp

class Senator(var name: String = "",
              var district: String = "",
              var uid: String = "",
              var src: Int = R.mipmap.anonymous_profile_round): Parcelable {
    @get:Exclude
    var id = ""
    @ServerTimestamp
    var lastTouched: Timestamp? = null

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(district)
        parcel.writeString(uid)
        parcel.writeInt(src)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Senator> {
        override fun createFromParcel(parcel: Parcel): Senator {
            return Senator(parcel)
        }

        override fun newArray(size: Int): Array<Senator?> {
            return arrayOfNulls(size)
        }

        fun fromSnapshot(snapshot: QueryDocumentSnapshot): Senator {
            val senator = snapshot.toObject(Senator::class.java)
            senator.id = snapshot.id
            return senator
        }
    }
}