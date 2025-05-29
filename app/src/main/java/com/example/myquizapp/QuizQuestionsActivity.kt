package com.example.myquizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer

class QuizQuestionsActivity : AppCompatActivity()//,OnClickListener{
{

    private var mCurrentPosition : Int = 1
    private var mQuestionList : ArrayList<Question>? = null
    private var mSelectedOptionPosition : Int = 0
    private var mUserName : String? = null
    private var mCorrectAnswers : Int = 0


    private var progressBar: ProgressBar? = null
    private var tvProgress : TextView? = null
    private var tvQuestion: TextView? = null
    private var ivImage : ImageView? = null

    private var tvOptionOne : TextView? = null
    private var tvOptionTwo : TextView? = null
    private var tvOptionThree : TextView? = null
    private var tvOptionFour : TextView? = null
    private var btnSubmit : Button? = null
    private var selectedOptionIndex: Int? = null
    private val quizViewModel: QuizViewModel by viewModels()
    private lateinit var optionViews: List<TextView> // Tüm seçenekleri bir listede tutalım


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)

        mUserName = intent.getStringExtra(Constants.USER_NAME)

        progressBar = findViewById(R.id.progressbar)
        tvProgress = findViewById(R.id.tv_progress)
        tvQuestion = findViewById(R.id.tv_question)
        ivImage = findViewById(R.id.iv_image)
        tvOptionOne = findViewById(R.id.tv_option_one)
        tvOptionTwo = findViewById(R.id.tv_option_two)
        tvOptionThree = findViewById(R.id.tv_option_three)
        tvOptionFour = findViewById(R.id.tv_option_four)
        btnSubmit = findViewById(R.id.btnSubmit)


        optionViews = listOf(
            findViewById(R.id.tv_option_one),
            findViewById(R.id.tv_option_two),
            findViewById(R.id.tv_option_three),
            findViewById(R.id.tv_option_four)
        )
        optionViews.forEachIndexed { index, textView ->
            textView.setOnClickListener {
                handleOptionClick(index)
            }
        }

        btnSubmit?.setOnClickListener {
            checkAnswer()
        }

        observeViewModel()
    }


    private fun observeViewModel() {
        quizViewModel.currentQuestion.observe(this, Observer { question ->
            question?.let {
                tvQuestion?.text = it.question
                tvOptionOne?.text = it.optionOne
                tvOptionTwo?.text = it.optionTwo
                tvOptionThree?.text = it.optionThree
                tvOptionFour?.text = it.optionFour
                ivImage?.setImageResource(it.image)
                defaultOptionsView()
            } ?: run {
                // Quiz bitti
                tvQuestion?.text = "Quiz Tamamlandı!" + "\n Final Skorunuz: \n ${quizViewModel.score.value}/${quizViewModel.questions.value?.size}"
                tvOptionOne?.visibility = View.GONE
                tvOptionTwo?.visibility = View.GONE
                tvOptionThree?.visibility = View.GONE
                tvOptionFour?.visibility = View.GONE
                btnSubmit?.visibility = View.GONE
                ivImage?.visibility = View.GONE
            }
        })
        quizViewModel.score.observe(this, Observer { score -> findViewById<TextView>(R.id.tv_progress).text = "Skor: $score" })
        quizViewModel.progress.observe(this,Observer{ progress -> findViewById<ProgressBar>(R.id.progressbar).progress = progress})
    }


    private fun defaultOptionsView(){
        val options = ArrayList<TextView>()

        tvOptionOne?.let{
            options.add(0,it)
        }
        tvOptionTwo?.let{
            options.add(1,it)
        }
        tvOptionThree?.let{
            options.add(2,it)
        }
        tvOptionFour?.let{
            options.add(3,it)
        }

        for (i in options){
            i.setTextColor(Color.parseColor("#7A8089"))
            i.typeface = Typeface.DEFAULT
            i.background = ContextCompat.getDrawable(this, R.drawable.default_option_border_bg)
        }
    }


    private fun handleOptionClick(clickedIndex: Int) {
        // Tüm seçenekleri varsayılan haline getir
        resetOptions()

        // Tıklanan seçeneği vurgula
        optionViews[clickedIndex].background = ContextCompat.getDrawable(this, R.drawable.selected_option_border_bg)

        // ViewModel'a seçimi bildir
        quizViewModel.selectOption(clickedIndex)
        btnSubmit?.isEnabled = true
    }

    private fun resetOptions() {
        optionViews.forEach { textView ->
            textView.background = ContextCompat.getDrawable(this, R.drawable.default_option_border_bg)
        }
    }

    private fun checkAnswer() {
        quizViewModel.selectedOption.value?.let { selectedIndex ->
            val isCorrect = quizViewModel.checkAnswer()
            highlightAnswer(isCorrect, selectedIndex)

            btnSubmit?.isEnabled = false

            optionViews[0].postDelayed({
                if (quizViewModel.isQuizFinished()) {
                    quizViewModel.moveToNextQuestion()
                } else {
                    quizViewModel.moveToNextQuestion()
                }
            }, 1500)
        }
    }

    private fun highlightAnswer(isCorrect: Boolean, selectedIndex: Int) {
        if (isCorrect) {
            optionViews[selectedIndex].background = ContextCompat.getDrawable(this, R.drawable.correct_option_border_bg)
        } else {
            optionViews[selectedIndex].background = ContextCompat.getDrawable(this, R.drawable.wrong_option_border_bg)
            quizViewModel.currentQuestion.value?.correctAnswer?.let { correctIndex ->
                optionViews[correctIndex].background = ContextCompat.getDrawable(this, R.drawable.correct_option_border_bg)
            }
        }
    }

}



