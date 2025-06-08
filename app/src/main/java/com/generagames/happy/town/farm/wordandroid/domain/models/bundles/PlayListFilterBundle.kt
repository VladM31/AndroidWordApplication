package com.generagames.happy.town.farm.wordandroid.domain.models.bundles

import android.os.Parcel
import android.os.Parcelable

data class PlayListFilterBundle(
    val startCount: Long? = null,
    val endCount: Long? = null,
    val name: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(startCount)
        parcel.writeValue(endCount)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlayListFilterBundle> {
        override fun createFromParcel(parcel: Parcel): PlayListFilterBundle {
            return PlayListFilterBundle(parcel)
        }

        override fun newArray(size: Int): Array<PlayListFilterBundle?> {
            return arrayOfNulls(size)
        }
    }
}