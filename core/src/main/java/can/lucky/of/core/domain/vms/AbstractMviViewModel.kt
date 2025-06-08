package can.lucky.of.core.domain.vms

import androidx.lifecycle.ViewModel

abstract class AbstractMviViewModel<S, A> : ViewModel(), MviViewModel<S, A> {
}