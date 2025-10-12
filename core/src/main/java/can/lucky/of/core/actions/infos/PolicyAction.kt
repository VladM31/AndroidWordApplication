package can.lucky.of.core.actions.infos

interface PolicyAction {

    data object End : PolicyAction

    data class Error(val message: String) : PolicyAction
}