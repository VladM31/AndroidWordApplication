package can.lucky.of.core.choosers

import android.app.Activity
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import can.lucky.of.core.domain.models.states.ChooserState
import can.lucky.of.core.utils.getFilePathFromUri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


abstract class AbstractChooser(
    protected val fragment: Fragment,
    private val prefix: String
) {

    protected val mutableChooserState = MutableStateFlow(ChooserState())
    val chooserState : StateFlow<ChooserState> = mutableChooserState
    protected var buffer : Uri? = null

    protected val worker = fragment.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        handleActivityResult(result)
    }

    protected fun handleActivityResult(result: ActivityResult) {
        mutableChooserState.value =
            chooserState.value.copy(
                file =result.getFileUri() ?: buffer,
                isSet = true,
                isSusses = result.resultCode == Activity.RESULT_OK
            )
    }

    private fun ActivityResult.getFileUri() : Uri?{
        return this.data?.data?.getFilePathFromUri(this@AbstractChooser.fragment.requireContext(), prefix)?.toUri()
    }

    abstract fun start()
}