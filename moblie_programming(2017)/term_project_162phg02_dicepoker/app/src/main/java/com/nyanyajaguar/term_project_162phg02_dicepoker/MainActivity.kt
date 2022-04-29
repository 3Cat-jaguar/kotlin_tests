package com.nyanyajaguar.term_project_162phg02_dicepoker

import android.Manifest
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import io.vrinda.kotlinpermissions.PermissionCallBack
import io.vrinda.kotlinpermissions.PermissionsActivity
import kotlinx.android.synthetic.main.activity_start.*

val SELECT =1

class MainActivity : PermissionsActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        get_permission()
    }

    fun get_permission() {
        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE), object : PermissionCallBack {
            override fun permissionGranted() {
                super.permissionGranted()
                Log.v("Call permissions" , "Granted")
            }

            override fun permissionDenied() {
                super.permissionDenied()
                Log.v("Call permissions", "Denied")
            }

        })
    }

    fun go_start(view: View) {
        var intent = Intent(this, START :: class.java)
        startActivity(intent)
    }



}
