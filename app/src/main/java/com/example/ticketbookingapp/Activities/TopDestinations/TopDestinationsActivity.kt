package com.example.ticketbookingapp.Activities.TopDestinations

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.ticketbookingapp.Activities.Splash.StatusTopBarColor
import com.example.ticketbookingapp.Domain.UserModel

class TopDestinationsActivity : AppCompatActivity() {
    private val TAG = "TopDestinationsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Lấy UserModel từ Intent
        val user = intent.getSerializableExtra("user") as? UserModel
        if (user == null) {
            Log.e(TAG, "Không tìm thấy dữ liệu người dùng trong Intent")
            Toast.makeText(this, "Lỗi: Không tìm thấy dữ liệu người dùng", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        Log.d(TAG, "Nhận được người dùng: ${user.username}")

        setContent {
            StatusTopBarColor()
            TopDestinationsScreen(user = user)
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, TopDestinationsActivity::class.java)
        }
    }
}