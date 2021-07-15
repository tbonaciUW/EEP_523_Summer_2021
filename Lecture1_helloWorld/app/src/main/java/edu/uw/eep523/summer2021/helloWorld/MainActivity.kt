package edu.uw.eep523.summer2021.helloWorld

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonClickMe = findViewById<Button>(R.id.button2);
        buttonClickMe.setOnClickListener {
            buttonClicked(it)
        }
    }

    fun buttonClicked(view: View) {
        Toast.makeText(this@MainActivity, "Button clicked!", Toast.LENGTH_SHORT).show();
    }
}