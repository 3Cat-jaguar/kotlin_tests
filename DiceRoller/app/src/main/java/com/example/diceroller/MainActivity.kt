package com.example.diceroller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    lateinit var diceImage:ImageView // 초기화는 나중에 하기로 하고 일단 변수 선언해두기
    lateinit var diceImage2:ImageView // 초기화는 나중에 하기로 하고 일단 변수 선언해두기

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        diceImage = findViewById(R.id.dice_image) // 여기에서 먼저 선언한 변수 초기화
        diceImage2 = findViewById(R.id.dice_image2) // 여기에서 먼저 선언한 변수 초기화

        //01.2
        val rollButton:Button = findViewById<Button>(R.id.roll_button)
        rollButton.setOnClickListener { rollDice() }

        //01.2 challenge
        val countButton:Button = findViewById<Button>(R.id.count_up)
        countButton.setOnClickListener { countUp() }

        val resetButton:Button = findViewById(R.id.reset)
        resetButton.setOnClickListener { reset() }
    }

    //01.2
    private fun rollDice() {
        //01.2
//        Toast
//            .makeText(this, "button clicked", Toast.LENGTH_SHORT)
//            .show()
//
//        val resultText:TextView = findViewById(R.id.result_text)
//        resultText.text = "Dice Rolled!"
//
//        val randomInt = (1..6).random()
//        resultText.text = randomInt.toString()

        //01.3
//        위에서 diceImage 변수 초기화 했으므로 이 부분이 필요없어짐
//        val diceImage:ImageView = findViewById(R.id.dice_image)
        Toast.makeText(this, "roll dice", Toast.LENGTH_SHORT).show()
        diceImage.setImageResource(getRandomDiceImage())
        diceImage2.setImageResource(getRandomDiceImage())
    }

    private fun getRandomDiceImage() : Int {
        val randomInt = (1..6).random()
        val drawableResource = when(randomInt){
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }
        return drawableResource
    }

    //01.2 challenge
    private fun countUp() {
//        //01.2 challenge
//        val resultText:TextView = findViewById(R.id.result_text)
//        val oldText = resultText.text.toString()
//        //res 에 있는 string 은 getString(위치) 를 이용하여 가져온다
//        if (oldText == getString(R.string.defaultText))
//            resultText.text = "1"
//        else {
//            var resultInt = oldText.toInt()
//            if (resultInt < 6) {
//                resultInt++
//                resultText.text = resultInt.toString()
//            }
//        }
        Toast.makeText(this, "cannot count up", Toast.LENGTH_SHORT).show()
    }

    private fun reset() {
//        //01.2 homework
//        val resultText:TextView = findViewById(R.id.result_text)
//        resultText.text = getString(R.string.defaultText)
        //01.3
        //        위에서 diceImage 변수 초기화 했으므로 이 부분이 필요없어짐
//        val diceImage:ImageView = findViewById(R.id.dice_image)
        Toast.makeText(this, "reset to 1", Toast.LENGTH_SHORT).show()
        diceImage.setImageResource(R.drawable.empty_dice)
        diceImage2.setImageResource(R.drawable.empty_dice)
    }
}