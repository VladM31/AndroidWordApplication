package can.lucky.of.addword.net.models.responses

data class SharePlayListRespond(
    val name: String,
    val shareId: String,
    val words: List<SharePlayListWordRespond>
){

    data class SharePlayListWordRespond(
        val original: String,
        val lang: String,
        val translate: String,
        val translateLang: String
    )
}
