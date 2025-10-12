package can.lucky.of.core.domain.models.data.words

import android.os.Parcel
import android.os.Parcelable
import can.lucky.of.core.domain.models.enums.CEFR
import can.lucky.of.core.domain.models.enums.Language

data class Word(
    val id: String,
    val original: String,
    val translate: String,
    val lang: Language,
    val translateLang: Language,
    val cefr: CEFR,
    val description: String? = null,
    val category: String? = null,
    val soundLink: String? = null,
    val imageLink: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        Language.valueOf(parcel.readString().orEmpty()),
        Language.valueOf(parcel.readString().orEmpty()),
        CEFR.valueOf(parcel.readString().orEmpty()),
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
        parcel.writeString(lang.name)
        parcel.writeString(translateLang.name)
        parcel.writeString(cefr.name)
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