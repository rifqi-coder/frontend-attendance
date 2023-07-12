package com.mobile.livepresensiguru.views.forgotpass

import com.google.gson.annotations.SerializedName

data class ForgotPasswordRequest(

	@field:SerializedName("email")
	val email: String? = null
)
