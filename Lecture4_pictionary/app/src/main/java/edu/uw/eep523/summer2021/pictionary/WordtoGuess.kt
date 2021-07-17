package edu.uw.eep523.summer2021.pictionary

import android.util.Log
import androidx.lifecycle.ViewModel

class WordtoGuess: ViewModel() {

    override fun onCleared() {
        super.onCleared()
        Log.d("tag", "ViewModel instance about to be destroyed")
    }

    var currentIndex = 0
    private val wordBank = listOf(
        Word(R.string.w1,R.string.h1),
        Word(R.string.w2,R.string.h2),
        Word(R.string.w3,R.string.h3)
    )


    val currentWord: Int
        get() = wordBank[currentIndex].wordResId
    val currentHint: Int
        get() = wordBank[currentIndex].hintResId

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % wordBank.size
    }

}