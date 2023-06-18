package com.example.moviecatalog.register

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import com.example.moviecatalog.ProgressDialog
import com.example.moviecatalog.R
import com.example.moviecatalog.apiservice.body.RegisterBody
import com.example.moviecatalog.apiservice.response.AuthResponse
import com.example.moviecatalog.databinding.ActivitySignUpBinding
import com.example.moviecatalog.isValidEmail
import com.example.moviecatalog.login.LoginActivity
import java.text.SimpleDateFormat
import java.util.Locale

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding
    private val viewModel: RegisterViewModel by viewModels()

    var isFemale = false
    var isMale = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val progress = ProgressDialog(this)

        viewModel.getLoading.observe(this) {
            if (it) {
                progress.show()
            } else {
                progress.dismiss()
            }
        }

        viewModel.registerData.observe(this) {
            if (it is AuthResponse) {
                finish()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        binding.usernameEdt.addTextChangedListener {
            binding.errMsgTxt.text = checkDataValid()
        }

        binding.emailEdt.addTextChangedListener {
            binding.errMsgTxt.text = checkDataValid()
        }

        binding.nameEdt.addTextChangedListener {
            binding.errMsgTxt.text = checkDataValid()
        }

        binding.passwordEdt.addTextChangedListener {
            binding.errMsgTxt.text = checkDataValid()
        }

        binding.passwordConfEdt.addTextChangedListener {
            binding.errMsgTxt.text = checkDataValid()
        }

        binding.birthEdt.addTextChangedListener {
            binding.errMsgTxt.text = checkDataValid()
        }

        binding.maleLayout.setOnClickListener {
            binding.femaleLayout.background = ResourcesCompat.getDrawable(resources, R.drawable.border_female, theme)
            binding.maleLayout.background = ResourcesCompat.getDrawable(resources, R.drawable.border_male_fill, theme)
            isMale = true
            isFemale = false
            binding.errMsgTxt.text = checkDataValid()
        }

        binding.femaleLayout.setOnClickListener {
            binding.femaleLayout.background = ResourcesCompat.getDrawable(resources, R.drawable.border_female_fil, theme)
            binding.maleLayout.background = ResourcesCompat.getDrawable(resources, R.drawable.border_male, theme)
            isMale = false
            isFemale = true

            binding.errMsgTxt.text = checkDataValid()
        }

        binding.birthEdt.setOnClickListener {
            val calendar = Calendar.getInstance()

            DatePickerDialog(this, { view, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                binding.birthEdt.setText(dateFormat.format(calendar.time))

            }, calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH]).show()
        }

        binding.signUpBtn.setOnClickListener {

            val username = binding.usernameEdt.text.toString().trim()
            val email = binding.emailEdt.text.toString().trim()
            val name = binding.nameEdt.text.toString().trim()
            val password = binding.passwordEdt.text.toString().trim()
            val birthDate = binding.birthEdt.text.toString().trim()

            if (binding.errMsgTxt.text.toString().isBlank()) {
                val body = RegisterBody(
                    username,
                    email,
                    name,
                    password,
                    birthDate,
                    if (isFemale) 1 else 0
                )
                viewModel.register(body)
            }
        }

        binding.loginTxt.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkDataValid(): String {
        val username = binding.usernameEdt.text.toString().trim()
        val email = binding.emailEdt.text.toString().trim()
        val name = binding.nameEdt.text.toString().trim()
        val password = binding.passwordEdt.text.toString().trim()
        val passwordConf = binding.passwordConfEdt.text.toString().trim()
        val birth = binding.birthEdt.text.toString().trim()

        return if (username.isBlank() || email.isBlank() || name.isBlank() || password.isBlank() || passwordConf.isBlank() || birth.isBlank()) {
            binding.signUpBtn.setBackgroundColor(resources.getColor(android.R.color.transparent, theme))
            binding.signUpBtn.setTextColor(resources.getColor(R.color.main_color, theme))
            "Have field not filled"
        }else if (!isValidEmail(email)) {
            binding.signUpBtn.setBackgroundColor(resources.getColor(android.R.color.transparent, theme))
            binding.signUpBtn.setTextColor(resources.getColor(R.color.main_color, theme))
            "Email Format not valid"
        } else if (password.length < 6) {
            binding.signUpBtn.setBackgroundColor(resources.getColor(android.R.color.transparent, theme))
            binding.signUpBtn.setTextColor(resources.getColor(R.color.main_color, theme))
            "Password to short"
        } else if (!passwordConf.equals(password, true)) {
            binding.signUpBtn.setBackgroundColor(resources.getColor(android.R.color.transparent, theme))
            binding.signUpBtn.setTextColor(resources.getColor(R.color.main_color, theme))
            "Password not matches"
        } else if (!isFemale && !isMale) {
            binding.signUpBtn.setBackgroundColor(resources.getColor(android.R.color.transparent, theme))
            binding.signUpBtn.setTextColor(resources.getColor(R.color.main_color, theme))
            "Gender Not Selected"
        }  else {
            binding.signUpBtn.setBackgroundColor(resources.getColor(R.color.main_color, theme))
            binding.signUpBtn.setTextColor(resources.getColor(R.color.white, theme))
            ""
        }
    }
}