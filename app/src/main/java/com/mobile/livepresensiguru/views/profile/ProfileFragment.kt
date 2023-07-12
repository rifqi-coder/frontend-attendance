package com.mobile.livepresensiguru.views.profile

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings.ACTION_LOCALE_SETTINGS
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.mobile.livepresensiguru.BuildConfig
import com.mobile.livepresensiguru.R
import com.mobile.livepresensiguru.databinding.FragmentProfileBinding
import com.mobile.livepresensiguru.dialog.MyDialog
import com.mobile.livepresensiguru.hawkstorage.HawkStorage
import com.mobile.livepresensiguru.model.LogoutResponse
import com.mobile.livepresensiguru.networking.ApiServices
import com.mobile.livepresensiguru.views.changepass.ChangePasswordActivity
import com.mobile.livepresensiguru.views.login.LoginActivity
import com.mobile.livepresensiguru.views.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {
    private var binding: FragmentProfileBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
        updateView()
    }

    private fun updateView() {
        val user = HawkStorage.instance(context).getUser()
        val imageUrl = BuildConfig.BASE_IMAGE_URL + user.photo
        Glide.with(requireContext()).load(imageUrl).placeholder(android.R.color.darker_gray).into(binding!!.ivProfile)
        binding?.tvNameProfile?.text = user.name
        binding?.tvEmailProfile?.text = user.email
    }

    private fun onClick() {
        binding?.btnChangePassword?.setOnClickListener {
            startActivity(Intent(context, ChangePasswordActivity::class.java))
        }

        binding?.btnChangeLanguage?.setOnClickListener {
            startActivity(Intent(ACTION_LOCALE_SETTINGS))
        }

        binding?.btnLogout?.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle(getString(R.string.logout))
                .setMessage(getString(R.string.apakah_anda_yakin))
                .setPositiveButton(getString(R.string.yes)){dialog, _ ->
                    logoutRequest(dialog)
                }
                .setNegativeButton(getString(R.string.no)){dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun logoutRequest(dialog: DialogInterface?) {
        val token = HawkStorage.instance(context).getToken()
        MyDialog.showProgressDialog(context)
        ApiServices.getLiveAttendanceServices()
            .logoutRequest("Bearer $token")
            .enqueue(object : Callback<LogoutResponse> {
                override fun onResponse(
                    call: Call<LogoutResponse>,
                    response: Response<LogoutResponse>
                ) {
                    dialog?.dismiss()
                    MyDialog.hideDialog()
                    if (response.isSuccessful){
                        HawkStorage.instance(context).deleteAll()
                        context?.startActivity(Intent(context, LoginActivity::class.java))
                        (activity as MainActivity).finishAffinity()
                    }else{
                        MyDialog.dynamicDialog(context, getString(R.string.alert), getString(R.string.something_wrong))
                    }
                }

                override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                    dialog?.dismiss()
                    MyDialog.hideDialog()
                    MyDialog.dynamicDialog(context, getString(R.string.alert), "Error: ${t.message}")
                }

            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}