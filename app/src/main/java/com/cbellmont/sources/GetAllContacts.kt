package com.cbellmont.sources

import User
import android.util.Log
import com.cbellmont.workshopui.MainActivityViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class GetAllContacts {
    companion object {
        private const val URL = "https://randomuser.me/api/?results=10"

        fun send(viewModel: MainActivityViewModel) {
            val client = OkHttpClient()
            val request = Request.Builder().url(URL).build()
            val call = client.newCall(request)
            viewModel.downloadStarted()
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    Log.e(GetAllContacts::class.simpleName, call.toString())
                    viewModel.downloadCancelled()
                }

                override fun onResponse(call: Call, response: Response) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val bodyInString = response.body?.string()
                        bodyInString?.let { it ->
                            Log.w(GetAllContacts::class.simpleName, it)
                            try {
                                val jsonObject = JSONObject(it)

                                val results = jsonObject.optJSONArray("results")
                                results?.let {
                                    Log.d(GetAllContacts::class.simpleName, results.toString())
                                    val gson = Gson()
                                    val itemType = object : TypeToken<List<User>>() {}.type
                                    val list = gson.fromJson<List<User>>(results.toString(), itemType)
                                    viewModel.downloadFinished(list)
                                }
                            } catch (e : Exception) {
                                Log.e(GetAllContacts::class.simpleName, "La página web no ha respondido bien. Reintentamos la descarga")
                                e.printStackTrace()
                                viewModel.downloadData()
                            }
                        }
                    }
                }
            })
        }
    }
}