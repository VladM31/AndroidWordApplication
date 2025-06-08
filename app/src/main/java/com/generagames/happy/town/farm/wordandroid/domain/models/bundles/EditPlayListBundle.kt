package com.generagames.happy.town.farm.wordandroid.domain.models.bundles

import android.os.Parcel
import android.os.Parcelable

data class EditPlayListBundle(
    val playListId: String,
    val name: String,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(playListId)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EditPlayListBundle> {
        override fun createFromParcel(parcel: Parcel): EditPlayListBundle {
            return EditPlayListBundle(parcel)
        }

        override fun newArray(size: Int): Array<EditPlayListBundle?> {
            return arrayOfNulls(size)
        }
    }
}
