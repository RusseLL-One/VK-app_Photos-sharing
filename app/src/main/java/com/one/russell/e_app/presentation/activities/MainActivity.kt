package com.one.russell.e_app.presentation.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.one.russell.e_app.R
import androidx.lifecycle.observe

import com.one.russell.e_app.presentation.misc.BottomSheet
import com.one.russell.e_app.presentation.viewmodels.MainViewModel
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Handler
import android.view.View
import android.widget.Toast
import java.io.File

private const val PHOTO_PICK_REQUEST = 10101

class MainActivity : AppCompatActivity() {


    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        checkAuth()
        initListeners()
        initObservers()
    }

    private fun checkAuth() {
        val accessToken = viewModel.getAccessToken(this)
        if (accessToken == null || !accessToken.isValid) {
            VK.login(this, listOf(VKScope.WALL, VKScope.PHOTOS))
        }
    }

    private fun initListeners() {
        btn_choose_photo.setOnClickListener {
            val pickPhoto = Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(pickPhoto, PHOTO_PICK_REQUEST)
        }
    }

    private fun initObservers() {
        viewModel.getUploadingPostLiveData().observe(this) { onPostUploaded(it) }
    }

    private fun onPostUploaded(resultCode: Int) {
        hideProgress()

        val resultMessage = if (resultCode == -1) {
            getString(R.string.send_failed)
        } else {
            getString(R.string.send_succeed)
        }

        Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show()
    }

    private fun showProgress() {
        overlay_background.visibility = View.VISIBLE
        pb_progress.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        overlay_background.visibility = View.GONE
        pb_progress.visibility = View.GONE
    }

    private fun showBottomSheet(selectedImage: File?) {
        Handler().postDelayed({
            BottomSheet(selectedImage, object : BottomSheet.OnSendClickedListener {
                override fun onSendClicked(photo: File, message: String) {
                    viewModel.publishPost(photo, message)
                    showProgress()
                }
            }).show(supportFragmentManager, "bottom_sheet_tag")

        }, 200)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PHOTO_PICK_REQUEST) {
            if (resultCode == RESULT_OK) {
                val selectedImage = viewModel.saveImage(data?.data, contentResolver)
                showBottomSheet(selectedImage)
            }
        } else if (!VK.onActivityResult(requestCode, resultCode, data, object : VKAuthCallback {
                override fun onLogin(token: VKAccessToken) {
                    viewModel.saveAccessToken(this@MainActivity, token)
                }

                override fun onLoginFailed(errorCode: Int) {
                    Toast.makeText(this@MainActivity, getString(R.string.auth_failed), Toast.LENGTH_SHORT).show()
                    finish()
                }
            })
        ) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
