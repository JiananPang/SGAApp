package rose.pangj.sga_app

import android.os.Parcel
import android.os.Parcelable

data class Doc(var title: String, var text: String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(text)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Doc> {
        override fun createFromParcel(parcel: Parcel): Doc {
            return Doc(parcel)
        }

        override fun newArray(size: Int): Array<Doc?> {
            return arrayOfNulls(size)
        }
    }
}