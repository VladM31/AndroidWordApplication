package can.lucky.of.core.domain.models.data.words

import android.os.Parcel
import android.os.Parcelable

data class UserWordDetails(
    val userWordId: String,
    val learningGrade: Long,
    val dateOfAdded: String,
    val lastReadDate: String,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().orEmpty(),
        parcel.readLong(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userWordId)
        parcel.writeLong(learningGrade)
        parcel.writeString(dateOfAdded)
        parcel.writeString(lastReadDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserWordDetails> {
        override fun createFromParcel(parcel: Parcel): UserWordDetails {
            return UserWordDetails(parcel)
        }

        override fun newArray(size: Int): Array<UserWordDetails?> {
            return arrayOfNulls(size)
        }

        fun from(userWord: UserWord) : UserWordDetails {
            return UserWordDetails(
                userWordId = userWord.id,
                learningGrade = userWord.learningGrade,
                dateOfAdded = userWord.dateOfAdded,
                lastReadDate = userWord.lastReadDate
            )
        }
    }


}