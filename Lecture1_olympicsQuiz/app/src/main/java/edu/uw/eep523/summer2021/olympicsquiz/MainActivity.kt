package edu.uw.eep523.summer2021.olympicsquiz

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private var mCurrentIndex = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mCurrentIndex = 0;
    }

    private val questionBank = listOf(
        Question(R.string.q1, false),
        Question(R.string.q2, false),
        Question(R.string.q3, false),
        Question(R.string.q4, true),
        Question(R.string.q5, true),
        Question(R.string.q6, true),
        Question(R.string.q7, false),
        Question(R.string.q8, true),
        Question(R.string.q9, true),
        Question(R.string.q10, false)
    )

    fun updateQuestion(view: View){
        mCurrentIndex ++

        if (mCurrentIndex >= questionBank.size) {
            mCurrentIndex = 1
        }
        val questionTextResID = questionBank[mCurrentIndex].textResId
        val questionView = findViewById<TextView>(R.id.questionView)
        questionView.setText(questionTextResID)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[mCurrentIndex].answer
        if(userAnswer == correctAnswer){
            val text = " =D  Correct!"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(applicationContext, text, duration)
            toast.setGravity(Gravity.TOP or Gravity.RIGHT, 0, 0)
            toast.show()

            //show_snackbar(text)
            //show_custom_snackbar(text)
        }
        else{
            val text = ":P not correct"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(applicationContext, text, duration)
            toast.setGravity(Gravity.TOP or Gravity.RIGHT, 0, 0)
            toast.show()
            //show_snackbar(text)
            //show_custom_snackbar(text)
        }
    }

    //Function which is executed when the user press on the True Botton
    fun check_true(view: View){
        checkAnswer(true)
    }

    //Function which is executed when the user press on the False Botton
    fun check_false(view: View){
        checkAnswer(false)
    }


    //Example function to show a snackbar to notify the user the result of the guess
    fun show_snackbar(text:String){
        val myLayout = findViewById<LinearLayout>(R.id.myLayout)
        Snackbar.make(
            myLayout,
            text,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    //Example function to show a snackbar to notify the user the result of the guess
    //This snackbar includes an action item.
    fun show_custom_snackbar(text:String){
        val myLayout = findViewById<LinearLayout>(R.id.myLayout)
        val snackbar =
            Snackbar.make(myLayout, text, Snackbar.LENGTH_LONG)
        snackbar.setAction(
            "ACTION"
        ) { Toast.makeText(applicationContext, "ACTION FROM SNACKBAR", Toast.LENGTH_SHORT).show() }

        snackbar.setActionTextColor(Color.WHITE)

        val snackbarView = snackbar.view
        val snackbarText =
            snackbarView.findViewById<View>(R.id.snackbar_text) as TextView
        snackbarText.setTextColor(Color.YELLOW)
        snackbar.show()
    }
}