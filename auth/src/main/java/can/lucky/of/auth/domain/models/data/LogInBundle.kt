package can.lucky.of.auth.domain.models.data

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable

data class LogInBundle(
    val phoneNumber: String,
    val password: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(phoneNumber)
        parcel.writeString(password)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LogInBundle> {
        override fun createFromParcel(parcel: Parcel): LogInBundle {
            return LogInBundle(parcel)
        }

        override fun newArray(size: Int): Array<LogInBundle?> {
            return arrayOfNulls(size)
        }

        fun parse(bundle: Bundle?) : LogInBundle?{
            return bundle?.getParcelable("logInBundle")
        }
    }



}
