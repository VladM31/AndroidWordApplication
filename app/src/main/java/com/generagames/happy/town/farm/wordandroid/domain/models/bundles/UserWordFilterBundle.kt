package com.generagames.happy.town.farm.wordandroid.domain.models.bundles

import android.os.Parcel
import android.os.Parcelable
import can.lucky.of.core.domain.models.enums.CEFR
import can.lucky.of.core.domain.models.enums.UserWordSortBy

data class UserWordFilterBundle(
    val originalLang : String? = null,
    val translateLang : String? = null,
    val original : String? = null,
    val translate : String? = null,
    val categories : List<String>? = null,
    val cefr: CEFR? = null,
    val asc: Boolean,
    val sortBy: UserWordSortBy
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readString()?.let { CEFR.valueOf(it) },
        parcel.readByte() != 0.toByte(),
        parcel.readString()?.let { UserWordSortBy.valueOf(it) } ?: UserWordSortBy.ORIGIN
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(originalLang)
        parcel.writeString(translateLang)
        parcel.writeString(original)
        parcel.writeString(translate)
        parcel.writeStringList(categories)
        parcel.writeString(cefr?.name)
        parcel.writeByte(if (asc) 1 else 0)
        parcel.writeString(sortBy.name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserWordFilterBundle> {
        override fun createFromParcel(parcel: Parcel): UserWordFilterBundle {
            return UserWordFilterBundle(parcel)
        }

        override fun newArray(size: Int): Array<UserWordFilterBundle?> {
            return arrayOfNulls(size)
        }
    }
}