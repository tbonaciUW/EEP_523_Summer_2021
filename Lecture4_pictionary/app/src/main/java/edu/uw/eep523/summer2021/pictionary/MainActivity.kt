package edu.uw.eep523.summer2021.pictionary

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders

class MainActivity : AppCompatActivity() {

    //private var drawView: DrawView ? = null
    private lateinit var checkBut: ImageButton
    private lateinit var nextBut: ImageButton
    private lateinit var wordTextView: TextView
    private lateinit var hintTextView: TextView
    private var points = 0
    private var highestScore = 0

    private val wordViewModel: WordtoGuess by lazy {
        ViewModelProviders.of(this).get(WordtoGuess::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Alternative to use the lazy initialization above
        // val provider: ViewModelProvider = ViewModelProviders.of(this)
        // val quizViewModel = provider.get(WordtoGuess::class.java)
        // Log.d("tag", "Got a QuizViewModel: $quizViewModel")

        checkBut = findViewById(R.id.check_button)
        nextBut = findViewById(R.id.next_button)
        wordTextView = findViewById(R.id.word_text)
        hintTextView = findViewById(R.id.hint_text)



        // Setup view instances
        drawView = findViewById<DrawView>(R.id.draw_view)
        drawView?.setStrokeWidth(10.0f)
        drawView?.setColor(Color.WHITE)

        // Setup classification trigger so that it classify after every stroke drew
        drawView?.setOnTouchListener { _, event ->
            // As we have interrupted DrawView's touch event,
            // we first need to pass touch events through to the instance for the drawing to show up
            drawView?.onTouchEvent(event)
            // Then if user finished a touch event, run classification
            if (event.action == MotionEvent.ACTION_UP) {
            }

            true
        }

        nextBut.setOnClickListener {
            wordViewModel.moveToNext()
            updatedWord()
        }

        checkBut.setOnClickListener{
            checkAnswer()
        }

        updatedWord()
    }



    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i("TAG", "onSaveInstanceState")
        savedInstanceState.putInt("key", points)
    }

    private fun updatedWord() {
        val questionTextResId = wordViewModel.currentWord
        val hintTextResId = wordViewModel.currentHint
        wordTextView.setText(questionTextResId)
        hintTextView.setText(hintTextResId)
        answer_text.setText("")
    }

    private fun checkAnswer() {
        val correctAnswer = getString(wordViewModel.currentWord)
        val userAnswer = answer_text.text.toString()
        var messageResId = ""
        if (correctAnswer == userAnswer) {
            points ++

            messageResId = points.toString()
            "correct"

        }
        else {
            messageResId="incorrect"
            points --
        }

        if(points>highestScore){
            val prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE)
            val prefsEditor = prefs.edit()
            prefsEditor.putInt("highScore", points)
            prefsEditor.apply()
            max_text.text = "High Score: $points      "  // display the score in a TextView


        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()

        points_text.setText(points.toString())
    }



    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState)

        // Restore state members from saved instance
        savedInstanceState?.run { points = savedInstanceState.getInt("key", 0)
            points_text.text = "POINTS: $points"
        }
    }
}
