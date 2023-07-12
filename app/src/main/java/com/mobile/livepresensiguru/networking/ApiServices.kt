package com.mobile.livepresensiguru.networking

object ApiServices {
    fun getLiveAttendanceServices(): LiveAttendanceApiServices{
        return RetrofitClient
            .getClient()
            .create(LiveAttendanceApiServices::class.java)
    }
}