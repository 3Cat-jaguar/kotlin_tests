package com.nyanyajaguar.a11_photo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import com.squareup.picasso.Picasso
import io.vrinda.kotlinpermissions.PermissionCallBack
import io.vrinda.kotlinpermissions.PermissionsActivity

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : PermissionsActivity() {

    val CAMERA = 1
    val SELECT = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        get_permission()

    }


    fun get_permission() {

        requestPermissions(arrayOf(Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE), object : PermissionCallBack {
            override fun permissionGranted() {
                super.permissionGranted()
                Log.v("Call permissions","Granted")
            }

            override fun permissionDenied() {
                super.permissionDenied()
                Log.v("Call permissions","Denied")
            }
        })

    }

    fun take_photo(view: View) {
        var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent,CAMERA)
    }

    fun select_photo(view:View) {
        var intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,SELECT)
    }

    fun get_content(view:View) {
        var intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent,SELECT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

        if (requestCode == CAMERA && resultCode == RESULT_OK) {
            Picasso.with(this).load(data.toURI()).into(imageView)
        }

        if (requestCode == SELECT && resultCode == RESULT_OK) {
            Picasso.with(this).load(data.data).into(imageView)
        }
    }

}
