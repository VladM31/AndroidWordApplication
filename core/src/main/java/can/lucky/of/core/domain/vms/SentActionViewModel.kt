package can.lucky.of.core.domain.vms

interface SentActionViewModel<A> {
    fun sent(action: A)
}