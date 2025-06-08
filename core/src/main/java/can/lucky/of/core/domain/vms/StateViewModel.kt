package can.lucky.of.core.domain.vms

import kotlinx.coroutines.flow.StateFlow

interface StateViewModel<S> {
    val state: StateFlow<S>
}