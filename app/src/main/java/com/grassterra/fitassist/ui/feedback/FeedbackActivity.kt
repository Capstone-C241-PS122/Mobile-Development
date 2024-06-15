package com.grassterra.fitassist.ui.feedback

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.grassterra.fitassist.databinding.ActivityFeedbackBinding
import com.grassterra.fitassist.helper.Resource
import com.grassterra.fitassist.repository.ApiRepository
import com.grassterra.fitassist.response.FeedbackResponse
import com.grassterra.fitassist.retrofit.ApiConfig
import com.grassterra.fitassist.ui.mainMenu.MainMenu
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FeedbackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedbackBinding
    private lateinit var apiRepository: ApiRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiRepository = ApiRepository(ApiConfig.getApiService())

        binding.btnBack.setOnClickListener {
            goToBack()
        }

        binding.btnSend.setOnClickListener {
            sendResult()
        }
    }

    private fun sendResult() {
        val sendMessageDescribe = binding.editTextDescription.text.toString().trim()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = apiRepository.postFeedback(sendMessageDescribe)
                handlePostFeedbackResult(result)
            } catch (e: Exception) {
                Toast.makeText(this@FeedbackActivity, "Failed to send feedback: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handlePostFeedbackResult(result: Resource<FeedbackResponse>) {
        when (result) {
            is Resource.Success -> {
                val message = result.data.message ?: "Feedback sent successfully"
                Toast.makeText(this@FeedbackActivity, message, Toast.LENGTH_SHORT).show()
                goToBack()
            }
            is Resource.Error -> {
                Toast.makeText(this@FeedbackActivity, "Error: ${result.errorMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToBack() {
        val intent = Intent(this, MainMenu::class.java)
        startActivity(intent)
        finish()
    }
}
