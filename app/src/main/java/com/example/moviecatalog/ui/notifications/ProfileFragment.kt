package com.example.moviecatalog.ui.notifications

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.moviecatalog.*
import com.example.moviecatalog.apiservice.body.UserBody
import com.example.moviecatalog.databinding.FragmentProfileBinding
import com.example.moviecatalog.login.LoginActivity
import java.text.SimpleDateFormat
import java.util.Locale


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: ProfileViewModel

    var isFemale = false
    var isMale = false
    var id = ""
    var nickname = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        val preferences = context?.let { Preferences(it) }
        val token = "Bearer " + preferences?.getToken()

        val progressDialog = context?.let { ProgressDialog(it) }

        viewModel.getProfile(token)

        viewModel.userData.observe(viewLifecycleOwner) {
            Glide.with(this).load(it.avatarLink).placeholder(R.drawable.profile_placeholder).centerCrop().into(binding.profileImg)
            binding.profileNameTxt.text = it.nickName
            binding.emailEdtProfile.setText(it.email)
            binding.avatarLinkEdtProfile.setText(it.avatarLink)
            binding.birthdayEdtProfile.setText(convertServerDateToUserTimeZone2( it.birthDate))
            binding.nameEdtProfile.setText(it.name)
            id = it.id
            nickname = it.nickName

            if (it.gender.toInt() == 0) {
                binding.maleLayout.background = ResourcesCompat.getDrawable(resources, R.drawable.border_male_fill, null)
            } else {
                binding.femaleLayout.background = ResourcesCompat.getDrawable(resources, R.drawable.border_female_fil, null)
            }
        }

        viewModel.getLoading.observe(viewLifecycleOwner) {
            if (it) {
                progressDialog?.show()
            } else {
                progressDialog?.dismiss()
            }
        }

        viewModel.updateUserData.observe(viewLifecycleOwner) {
            viewModel.getProfile(token)
            Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
        }

        binding.maleLayout.setOnClickListener {
            binding.femaleLayout.background = ResourcesCompat.getDrawable(resources, R.drawable.border_female, null)
            binding.maleLayout.background = ResourcesCompat.getDrawable(resources, R.drawable.border_male_fill, null)
            isMale = true
            isFemale = false
        }

        binding.femaleLayout.setOnClickListener {
            binding.femaleLayout.background = ResourcesCompat.getDrawable(resources, R.drawable.border_female_fil, null)
            binding.maleLayout.background = ResourcesCompat.getDrawable(resources, R.drawable.border_male, null)
            isMale = false
            isFemale = true
        }

        binding.birthdayEdtProfile.setOnClickListener {
            val calendar = Calendar.getInstance()

            context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    { view, year, month, dayOfMonth ->
                        calendar.set(Calendar.YEAR, year)
                        calendar.set(Calendar.MONTH, month)
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                        binding.birthdayEdtProfile.setText(dateFormat.format(calendar.time))

                    },
                    calendar[Calendar.YEAR],
                    calendar[Calendar.MONTH],
                    calendar[Calendar.DAY_OF_MONTH]
                ).show()
            }
        }

        binding.saveBtn2.setOnClickListener {
            val name = binding.nameEdtProfile.text.toString()
            val email = binding.emailEdtProfile.text.toString()
            val avatarLink = binding.avatarLinkEdtProfile.text.toString()
            val birth = binding.birthdayEdtProfile.text.toString()

            if (name.isBlank() || email.isBlank() || birth.isBlank()) {
                Toast.makeText(context, "Please fill the field", Toast.LENGTH_SHORT).show()
            } else {

                val body = UserBody(
                    id,
                    nickname,
                    email,
                    avatarLink,
                    name,
                    convertServerDate(birth),
                    if (isFemale) 1 else 0
                )

                viewModel.updateProfile(token, body)
            }
        }

        binding.logoutBtn.setOnClickListener {
            viewModel.logout()
            preferences?.deleteToken()
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

    }
}