package can.lucky.of.auth.domain.managers

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.util.UUID

internal class AuthHistoryManagerImpl(
    context: Context
) : AuthHistoryManager {

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        FILE_NAME,
        MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override val lastPhoneNumber: String?
        get() = sharedPreferences.getString(LAST_PHONE_NUMBER, null)

    override fun updateLastPhoneNumber(phoneNumber: String) {
        sharedPreferences.edit().putString(LAST_PHONE_NUMBER, phoneNumber).apply()
    }

    companion object{
        private const val LAST_PHONE_NUMBER = "last_phone_number"
        private const val FILE_NAME = "auth_history"
    }
}