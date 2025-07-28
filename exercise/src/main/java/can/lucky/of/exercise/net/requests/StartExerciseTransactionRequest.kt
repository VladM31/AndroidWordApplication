package can.lucky.of.exercise.net.requests

import java.time.LocalDateTime


data class StartExerciseTransactionRequest(
    val transactionId : String,
    val exercises : List<Int>,
    val words : List<WordRequest>,
    val createdAt : Long,
    val wordCount: WordCountRequest,
    val learningPlan: LearningPlan?
){

    data class WordCountRequest(
        val addedToLearning: Int,
        val repetitions: Int,
    )

    data class LearningPlan(
        val wordsPerDay: Int,
        val nativeLang: String,
        val learningLang: String,
        val cefr: String,
        val dateOfCreation: String
    )


    data class WordRequest(

        val wordId: String,
        val userWordId: String,
        val grade: Int,
        val dateOfAdded: String,
        val lastReadDate: String,
        val original: String,
        val translate: String,
        val lang: String,
        val translateLang: String,
        val cefr: String,
        val hasDescription: Boolean,
        val category: String?,
        val hasSound: Boolean,
        val hasImage: Boolean
    )
}


