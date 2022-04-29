package com.nyanyajaguar.a06_piano

import android.media.AudioManager
import android.media.SoundPool
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    var sp = SoundPool(5, AudioManager.STREAM_MUSIC, 0)

    var note = IntArray(8)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        note[0] = sp.load(this, R.raw.note00, 1)
        note[1] = sp.load(this, R.raw.note02, 1)
        note[2] = sp.load(this, R.raw.note04, 1)
        note[3] = sp.load(this, R.raw.note05, 1)
        note[4] = sp.load(this, R.raw.note07, 1)
        note[5] = sp.load(this, R.raw.note09, 1)
        note[6] = sp.load(this, R.raw.note11, 1)
        note[7] = sp.load(this, R.raw.note12, 1)

    }

    fun key0(view: View) {
        sp.play(note[0], 1.0f, 1.0f, 0, 0, 1.0f)
    }

    fun key1(view: View) {
        sp.play(note[1], 1.0f, 1.0f, 0, 0, 1.0f)
    }

    fun key2(view: View) {
        sp.play(note[2], 1.0f, 1.0f, 0, 0, 1.0f)
    }

    fun key3(view: View) {
        sp.play(note[3], 1.0f, 1.0f, 0, 0, 1.0f)
    }

    fun key4(view: View) {
        sp.play(note[4], 1.0f, 1.0f, 0, 0, 1.0f)
    }

    fun key5(view: View) {
        sp.play(note[5], 1.0f, 1.0f, 0, 0, 1.0f)
    }

    fun key6(view: View) {
        sp.play(note[6], 1.0f, 1.0f, 0, 0, 1.0f)
    }

    fun key7(view: View) {
        sp.play(note[7], 1.0f, 1.0f, 0, 0, 1.0f)
    }

}