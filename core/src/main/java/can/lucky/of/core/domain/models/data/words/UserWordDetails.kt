package can.lucky.of.core.domain.models.data.words

import android.os.Parcel
import android.os.Parcelable
import java.time.OffsetDateTime

data class UserWordDetails(
    val userWordId: String,
    val learningGrade: Long,
    val createdAt: OffsetDateTime,
    val lastReadDate: OffsetDateTime,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().orEmpty(),
        parcel.readLong(),
        OffsetDateTime.parse(parcel.readString().orEmpty()),
        OffsetDateTime.parse(parcel.readString().orEmpty()),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userWordId)
        parcel.writeLong(learningGrade)
        parcel.writeString(createdAt.toString())
        parcel.writeString(lastReadDate.toString())
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
                createdAt = userWord.createdAt,
                lastReadDate = userWord.lastReadDate
            )
        }
    }


}