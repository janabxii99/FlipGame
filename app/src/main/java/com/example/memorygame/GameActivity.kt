package com.example.memorygame

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class GameActivity : AppCompatActivity() {
    private lateinit var timerTextView: TextView
    private lateinit var cardViews: Array<CardView>
    private lateinit var imageViews: Array<ImageView>

    private lateinit var countDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // Find views by their ids
        timerTextView = findViewById(R.id.timerTextView)

        cardViews = arrayOf(
            findViewById(R.id.cardview1),
            findViewById(R.id.cardview2),
            findViewById(R.id.cardview3),
            findViewById(R.id.cardview4),
            findViewById(R.id.cardview5),
            findViewById(R.id.cardview6),
            findViewById(R.id.cardview7),
            findViewById(R.id.cardview8),
            findViewById(R.id.cardview9)
        )

        imageViews = arrayOf(
            findViewById(R.id.cardImageView1),
            findViewById(R.id.cardImageView2),
            findViewById(R.id.cardImageView3),
            findViewById(R.id.cardImageView4),
            findViewById(R.id.cardImageView5),
            findViewById(R.id.cardImageView6),
            findViewById(R.id.cardImageView7),
            findViewById(R.id.cardImageView8),
            findViewById(R.id.cardImageView9)
        )

        // Start the countdown timer
        startTimer()

        // Set initial images for all cards
        imageViews.forEach { it.setImageResource(R.drawable.orangeback) }

        // Set OnClickListener for all cards
        cardViews.forEachIndexed { index, cardView ->
            cardView.setOnClickListener {
                handleCardClick(imageViews[index])
            }
        }
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(16000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Update the timerTextView with the remaining time
                timerTextView.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                // Update the timerTextView when the timer finishes
                timerTextView.text = "Game Over!"

                // Disable click listeners on all card views
                cardViews.forEach { it.setOnClickListener(null) }

                // Delay the navigation to allow the user to see "Game Over"
                Handler().postDelayed({
                    // Create an Intent to navigate back to MainActivity
                    val intent = Intent(this@GameActivity, MainActivity::class.java)
                    // Start the MainActivity
                    startActivity(intent)
                    // Finish the current activity
                    finish()
                    // Stop the timer
                    stopAllTimers()
                }, 5000) // Delay for 2 seconds
            }
        }.start()
    }

    private fun stopAllTimers() {
        // Cancel the CountDownTimer
        countDownTimer.cancel()
    }

    private fun handleCardClick(clickedImageView: ImageView) {
        // Check if the card is already matched
        if (clickedImageView.tag == R.drawable.nocircle) {
            // Card is already matched, do nothing
            return
        }

        // Check if it's the first or second card of the pair
        if (clickedImageView.tag == null) {
            // First click, set image to card_symbol_1
            clickedImageView.setImageResource(R.drawable.nocircle)
            clickedImageView.tag = R.drawable.nocircle


            Handler().postDelayed({
                clickedImageView.setImageResource(R.drawable.orangeback)
                clickedImageView.tag = null
            }, 5000)
        } else {
            // Second click, set image to orangeback
            clickedImageView.setImageResource(R.drawable.orangeback)
            clickedImageView.tag = null
        }

        // Check if all cards are flipped
        if (imageViews.all { it.tag == R.drawable.nocircle }) {
            // All cards are flipped, display "Well Done" message
            displayWinMessage()
        }
    }

    private fun displayWinMessage() {
        // Inflate the custom layout for the pop-up message
        val dialogView = LayoutInflater.from(this).inflate(R.layout.popup_message, null)

        // Create a Dialog instance
        val dialog = Dialog(this)

        // Set the custom layout to the dialog
        dialog.setContentView(dialogView)

        // Find the TextView inside the custom layout
        val messageTextView: TextView = dialogView.findViewById(R.id.messageTextView)

        // Set the message text
        messageTextView.text = "Well Done!"

        // Show the dialog
        dialog.show()

        // Delay the navigation to allow the user to see the pop-up message
        Handler().postDelayed({
            // Create an Intent to navigate back to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            // Start the MainActivity
            startActivity(intent)
            // Finish the current activity
            finish()
            // Dismiss the dialog
            dialog.dismiss()
            // Stop the timer
            stopAllTimers()
        }, 1000) // Delay for 1 second
    }
}
