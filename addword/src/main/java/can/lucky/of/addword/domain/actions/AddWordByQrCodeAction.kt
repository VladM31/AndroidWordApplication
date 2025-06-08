package can.lucky.of.addword.domain.actions

interface AddWordByQrCodeAction {
    data class ErrorMessage(val message: String) : AddWordByQrCodeAction
    data class FetchWord(val shareId: String) : AddWordByQrCodeAction
    data object Accept : AddWordByQrCodeAction
    data object Init : AddWordByQrCodeAction
}