package com.example.moviecatalog.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.example.moviecatalog.HomeActivity
import com.example.moviecatalog.Preferences
import com.example.moviecatalog.ProgressDialog
import com.example.moviecatalog.R
import com.example.moviecatalog.apiservice.body.LoginBody
import com.example.moviecatalog.apiservice.response.AuthResponse
import com.example.moviecatalog.databinding.ActivityLoginBinding
import com.example.moviecatalog.register.SignUpActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val progressDialog = ProgressDialog(this)

        val preferences = Preferences(this)

        viewModel.getLoading.observe(this) {
            if (it) {
                progressDialog.show()
            } else {
                progressDialog.dismiss()
            }
        }

        viewModel.loginData.observe(this) { data->
            if (data is AuthResponse) {
                preferences.saveToken(data.token)
                finish()
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, data.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        binding.usernameEdt.addTextChangedListener {
            binding.errorMsgTxt.text = checkFieldValid()
        }

        binding.passwordEdt.addTextChangedListener {
            binding.errorMsgTxt.text = checkFieldValid()
        }

        binding.signInBtn.setOnClickListener {

            val username = binding.usernameEdt.text.toString().trim()
            val password = binding.passwordEdt.text.toString().trim()

            if (binding.errorMsgTxt.text.toString().isBlank()) {
                val body = LoginBody(username, password)
                viewModel.login(body)
            }
        }

        binding.signUpTxt.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkFieldValid(): String {
        val username = binding.usernameEdt.text.toString().trim()
        val password = binding.passwordEdt.text.toString().trim()
        return if (username.isBlank() || password.isBlank()) {
            binding.signInBtn.setBackgroundColor(resources.getColor(android.R.color.transparent, theme))
            binding.signInBtn.setTextColor(resources.getColor(R.color.main_color, theme))
            "Empty Field"
        } else if (password.length < 6) {
            binding.signInBtn.setBackgroundColor(resources.getColor(android.R.color.transparent, theme))
            binding.signInBtn.setTextColor(resources.getColor(R.color.main_color, theme))
            "Password to short"
        } else {
            binding.signInBtn.setBackgroundColor(resources.getColor(R.color.main_color, theme))
            binding.signInBtn.setTextColor(resources.getColor(R.color.white, theme))
            ""
        }
    }
}