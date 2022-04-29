package com.nyanyajaguar.homework_sensors_162phg02

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SensorEventListener {

    var light = 0.0f
    var ax = 0.0f
    var ay = 0.0f
    var az = 0.0f
    var vx1 = 0.0f
    var vy1 = 0.0f
    var px1 = 400f
    var py1 = 600f
    var vx2 = 0.0f
    var vy2 = 0.0f
    var px2 = 400f
    var py2 = 500f



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var sens_manager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        var sens_light = sens_manager.getDefaultSensor(Sensor.TYPE_LIGHT)
        var sens_accel = sens_manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        sens_manager.registerListener(this, sens_light, SensorManager.SENSOR_DELAY_GAME)
        sens_manager.registerListener(this, sens_accel, SensorManager.SENSOR_DELAY_GAME)

    }

    override fun onAccuracyChanged(p0: Sensor, p1: Int) {

    }

    override fun onSensorChanged(p0: SensorEvent) {

        if (p0.sensor.type == Sensor.TYPE_LIGHT) {
            light = p0.values[0]
            textView.text = "light " + light
            seekBar.progress = light.toInt()
        } else if (p0.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            ax = p0.values[0]
            ay = p0.values[1]
            az = p0.values[2]
            textView1.text = "ax " + ax
            textView2.text = "ay " + ay
            textView3.text = "az " + az

            move_ball()
        }
    }

    fun move_ball() {

        var w = layout.width.toFloat()
        var h = layout.height.toFloat()
        var s = circle.width.toFloat()
        var s2 = circle2.width.toFloat()

        vx1 *= 0.99f
        vy1 *= 0.99f
        vx2 *= 0.99f
        vy2 *= 0.99f
        vx1 = vx1 - ax / 5
        vy1 = vy1 + ay / 5
        vx2 = vx2 - ax / 5
        vy2 = vy2 + ay / 5
        px1 += vx1
        py1 += vy1
        px2 += vx2
        py2 += vy2

        if (px1 < 0 && vx1 < 0) { px1 = 0f; vx1 = -vx1; }
        if (px1 + s > w && vx1 > 0) { px1 = w - s; vx1 = -vx1; }
        if (py1 < 0 && vy1 < 0) { py1 = 0f; vy1 = -vy1; }
        if (py1 + s > h && vy1 > 0) { py1 = h - s; vy1 = -vy1; }

        circle.x = px1
        circle.y = py1


        if (px2 < 0 && vx2 < 0) { px2 = 0f; vx2 = -vx2; }
        if (px2 + s2 > w && vx2 > 0) { px2 = w - s; vx2 = -vx2; }
        if (py2 < 0 && vy2 < 0) { py2 = 0f; vy2 = -vy2; }
        if (py2 + s2 > h && vy2 > 0) { py2 = h - s; vy2 = -vy2; }

        circle2.x = px2
        circle2.y = py2

    }




}

