package can.lucky.of.core.utils

fun Throwable.getRespondMessage(): String {
   if (this !is  retrofit2.HttpException) {
      return this.message.orEmpty()
   }

    val errorBody = this.response()?.errorBody()?.string()
    return errorBody.orEmpty()
}