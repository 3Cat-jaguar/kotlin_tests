package com.nyanyajaguar.homework_dice_2

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SensorEventListener {

    var ax = 0.0f
    var ay = 0.0f
    var az = 0.0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var sens_manager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        var sens_accel = sens_manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        sens_manager.registerListener(this,sens_accel, SensorManager.SENSOR_DELAY_GAME)

    }


    override fun onAccuracyChanged(p0: Sensor, p1:Int) {

    }

    override fun onSensorChanged(p0 : SensorEvent) {
        if(p0.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            ax = p0.values[0]
            ay = p0.values[1]
            az = p0.values[2]
            textView2.text = "ax = " + ax
            textView3.text = "ay = " + ay
            textView4.text = "az = " + az

            roll_dice()
        }

    }

    fun roll_dice() {
        if (ax > 30 || ax < -30 || ay > 30 || ay < -30 || az > 30 || az < -30) {
            var n1 = (Math.random() * 6) + 1
            var n2 = n1.toInt()

            var n3 = (Math.random() * 6) + 1
            var n4 = n3.toInt()

            dice1.set_number(n2)
            dice2.set_number(n4)

            textView.text = "" + (n2 + n4)
        }
    }



    fun roll(view : View) {
        var n1 = (Math.random() * 6) + 1
        var n2 = n1.toInt()

        var n3 = (Math.random() * 6) + 1
        var n4 = n3.toInt()

        dice1.set_number(n2)
        dice2.set_number(n4)

        textView.text = "" + (n2+n4)
    }

}
