package can.lucky.of.core.domain.vms

interface MviViewModel<S,A> : StateViewModel<S>, SentActionViewModel<A> {
}