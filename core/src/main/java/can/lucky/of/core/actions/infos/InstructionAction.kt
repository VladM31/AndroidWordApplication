package can.lucky.of.core.actions.infos

interface InstructionAction {

    data object End : InstructionAction

    data class Error(val message: String) : InstructionAction
}