package can.lucky.of.core.utils

import androidx.lifecycle.lifecycleScope
import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.models.states.EndetableState
import can.lucky.of.core.domain.models.states.ErrorableState
import can.lucky.of.core.ui.dialogs.showError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch


fun <S: EndetableState> StateFlow<S>.onEnd(scope: CoroutineScope, action: () -> Unit){
    scope.launch {
        this@onEnd.map { it.isEnd }
            .distinctUntilChanged()
            .drop(1)
            .filter { it }
            .shareIn(scope, SharingStarted.WhileSubscribed(), 0)
            .collectLatest {
                action()
            }
    }
}

fun <S: ErrorableState> StateFlow<S>.onError(scope: CoroutineScope, action: suspend (ErrorMessage) -> Unit) {
    scope.launch {
        this@onError.map { it.errorMessage }
            .distinctUntilChanged()
            .drop(1)
            .filterNotNull()
            .shareIn(scope, SharingStarted.WhileSubscribed(), 0)
            .collectLatest {
                action(it)
            }
    }
}