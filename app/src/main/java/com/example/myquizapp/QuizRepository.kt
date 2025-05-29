package com.example.myquizapp

// VERI KAYNAĞIMIZ
class QuizRepository {
    fun getQuestions(): List<Question> = questions
    // VERI KAYNAĞIMIZ
    private val questions =listOf(
        Question(
            1,"What country does this flag belong to?",
            R.drawable.ic_flag_of_argentina,
            "Argentina","Australia","Canada","USA",
            0
        ),
        Question(
            2,"What country does this flag belong to?",
            R.drawable.ic_flag_of_australia,
            "Fiji","Canada","Australia","USA",
            2
        ),

        Question(
            3,"What country does this flag belong to?",
            R.drawable.ic_flag_of_brazil,
            "Brazil","Argentina","Australia","USA",
            0
        ),

        Question(
            4,"What country does this flag belong to?",
            R.drawable.ic_flag_of_denmark,
            "Brazil","Denmark","Australia","USA",
            1
        ),

        Question(
            5,"What country does this flag belong to?",
            R.drawable.ic_flag_of_fiji,
            "Fiji","Denmark","Australia","USA",
            0
        )
    )

}