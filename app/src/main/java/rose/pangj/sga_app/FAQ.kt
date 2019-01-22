package rose.pangj.sga_app

import android.os.Parcel
import android.os.Parcelable

data class FAQ(var title: String, var text: String) : Parcelable {
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

    companion object CREATOR : Parcelable.Creator<FAQ> {
        override fun createFromParcel(parcel: Parcel): FAQ {
            return FAQ(parcel)
        }

        override fun newArray(size: Int): Array<FAQ?> {
            return arrayOfNulls(size)
        }
    }
}