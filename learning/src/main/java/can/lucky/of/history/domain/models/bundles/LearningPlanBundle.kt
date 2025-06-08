package can.lucky.of.history.domain.models.bundles

import android.os.Parcel
import android.os.Parcelable
import can.lucky.of.core.domain.models.enums.CEFR
import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.history.domain.models.enums.PlanFragmentType

data class LearningPlanBundle(
    val type: PlanFragmentType = PlanFragmentType.CREATE,
    val wordsPerDay: Int = 0,
    val nativeLang: Language = Language.UNDEFINED,
    val learningLang: Language = Language.UNDEFINED,
    val cefr: CEFR = CEFR.A1
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()?.let { PlanFragmentType.valueOf(it) } ?: PlanFragmentType.UNDEFINED,
        parcel.readInt(),
        parcel.readString()?.let { Language.valueOf(it) } ?: Language.UNDEFINED,
        parcel.readString()?.let { Language.valueOf(it) } ?: Language.UNDEFINED,
        parcel.readString()?.let { CEFR.valueOf(it) } ?: CEFR.A1
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type.name)
        parcel.writeInt(wordsPerDay)
        parcel.writeString(nativeLang.name)
        parcel.writeString(learningLang.name)
        parcel.writeString(cefr.name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LearningPlanBundle> {
        override fun createFromParcel(parcel: Parcel): LearningPlanBundle {
            return LearningPlanBundle(parcel)
        }

        override fun newArray(size: Int): Array<LearningPlanBundle?> {
            return arrayOfNulls(size)
        }
    }
}