package edu.uw.eep523.summer2021.knowyourknotes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import kotlin.properties.Delegates


const val KEY_MAIN = "KNOTE_SELECTED"
@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    
    private var mKnoteID:Int = R.drawable.figure8
    
    /*
     * Called when the activity first gets created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val user_info = findViewById<TextView>(R.id.user_info)
        user_info.text = getString(R.string.initial_info)
        Log.d("tag", "onCreate")
    }
    
    fun knoteclick(view: View){
        //Create intent
        val i = Intent(this, KnotesDetails::class.java)
        i.putExtra(KEY_MAIN, mKnoteID)
        startActivityForResult(i, PICK_KNOTE_REQUEST)    }
    
    /*
      * This method is called when the user chooses one of the knote radio buttons.
      * In this code we set which knote image is visible on the screen in the ImageView.
      */
    fun pickKnote(view: View) {
        updateKnoteImage(view)
    }
    /*
     * Called by various event handlers to update which knote image is showing
     * based on which radio button is currently checked.
     */
    private fun updateKnoteImage(view: View) {
        val figure8 = findViewById<RadioButton>(R.id.figure8)
        val butterfly = findViewById<RadioButton>(R.id.butterfly)
        val fisherman = findViewById<RadioButton>(R.id.fisherman)
        val stopper = findViewById<RadioButton>(R.id.stopper)
        
        mKnoteID = when (view) {
            figure8 -> R.drawable.figure8
            butterfly -> R.drawable.butterfly
            fisherman -> R.drawable.fisherman
            stopper -> R.drawable.stopper
            else
            -> R.drawable.figure8
        }
        
        var knote = findViewById<ImageView>(R.id.knote)
        knote.setImageResource(mKnoteID)
    }

    override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
     
        // Check which request we're responding to
        if (requestCode == PICK_KNOTE_REQUEST) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                val info_rec = data!!.getStringExtra("Something said")
                val user_info = findViewById<TextView>(R.id.user_info)
                user_info.text = info_rec
            }
        }
    }
    companion object {
        // these "request codes" are used to identify sub-activities that return results
        private const val PICK_KNOTE_REQUEST = 1234
    }
}