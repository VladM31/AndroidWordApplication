package can.lucky.of.core.domain.models.data.words

import android.os.Parcel
import android.os.Parcelable

data class Word(
    val id: String,
    val original: String,
    val translate: String,
    val lang: String,
    val translateLang: String,
    val cefr: String,
    val description: String? = null,
    val category: String? = null,
    val soundLink: String? = null,
    val imageLink: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(original)
        parcel.writeString(translate)
        parcel.writeString(lang)
        parcel.writeString(translateLang)
        parcel.writeString(cefr)
        parcel.writeString(description)
        parcel.writeString(category)
        parcel.writeString(soundLink)
        parcel.writeString(imageLink)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Word> {
        override fun createFromParcel(parcel: Parcel): Word {
            return Word(parcel)
        }

        override fun newArray(size: Int): Array<Word?> {
            return arrayOfNulls(size)
        }
    }
}