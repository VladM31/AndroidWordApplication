package can.lucky.of.addword.domain.actions

interface RecognizeWordTasksAction {

    data object Reload : RecognizeWordTasksAction

    data class OpenRecognizeResult(
        val recognizeId: String
    ) : RecognizeWordTasksAction
}