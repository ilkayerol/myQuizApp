package com.example.myquizapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

class QuizViewModel : ViewModel() {
    private val repository = QuizRepository() // veri kaynağımız = QuizRepository.kt
    private val _questions = MutableLiveData<List<Question>>()
    private val _currentQuestionIndex = MutableLiveData<Int>().apply { value = 0 }
    private val _score = MutableLiveData<Int>().apply { value = 0 }
    private val _selectedOption = MutableLiveData<Int?>(null)
    private var _progress = MutableLiveData<Int>().apply { value = 0 }

    val progress : LiveData<Int> = _progress
    val questions: LiveData<List<Question>> = _questions
    val currentQuestionIndex: LiveData<Int> = _currentQuestionIndex
    val score: LiveData<Int> = _score
    val selectedOption: LiveData<Int?> = _selectedOption
    val currentQuestion: LiveData<Question?>
        get() = _currentQuestionIndex.map { index -> // .map _currentQuestionIndex değiştiğinde, otomatik olarak içindeki lambda'yı çalıştırır ve yeni bir LiveData<Question?> üretir.
            _questions.value?.getOrNull(index)
        }

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        _questions.value = repository.getQuestions()
    }

    fun selectOption(optionIndex: Int) {
        _selectedOption.value = optionIndex
    }


    fun checkAnswer(): Boolean {
        val currentQuestion = _questions.value?.get(_currentQuestionIndex.value ?: 0)
        val isCorrect = (_selectedOption.value == currentQuestion?.correctAnswer)
        _progress.value = (_progress.value ?: 0) + 1
        if (isCorrect) {
            _score.value = (_score.value ?: 0) + 1
        }
        return isCorrect
    }

    fun moveToNextQuestion() {
        _currentQuestionIndex.value = (_currentQuestionIndex.value ?: 0) + 1
        _selectedOption.value = null
    }

    fun isQuizFinished(): Boolean {
        return (_currentQuestionIndex.value ?: 0) >= (_questions.value?.size ?: 0) - 1
    }

}