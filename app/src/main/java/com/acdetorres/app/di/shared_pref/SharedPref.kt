package com.acdetorres.app.di.shared_pref

import android.content.Context
import android.content.SharedPreferences
import com.acdetorres.app.dashboard.fragments.data.SelectedTrackDetails
import com.acdetorres.app.dashboard.repository.api_response.GetSearchTermResponse
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import org.json.JSONObject

//Shared pref that can be injected anywhere
class SharedPref (context : Context) {

    val sharedPref : SharedPreferences = context.getSharedPreferences("shared_pref", Context.MODE_PRIVATE)

    val editor = sharedPref.edit()

    //Gets the required details and store it as json String in shared pref
    fun storeSelectedTrackDetails(details : GetSearchTermResponse.Result) {
        val obj = JSONObject()
        obj.put("trackName", if (details.trackName.isNullOrEmpty()) "" else details.trackName)
        obj.put("price", details.trackPrice.toString())
        obj.put("previewUrl", if (details.previewUrl.isNullOrEmpty()) "" else details.previewUrl)
        obj.put("trackUrl", details.trackViewUrl)
        obj.put("wrapperType", details.wrapperType)
        obj.put("genre", details.primaryGenreName)
        obj.put("description", details.longDescription)


        editor.putString("lastScreenViewed", obj.toString())
        editor.apply()
    }

    //Gets the last viewed track, the json String is parsed to SelectedTrackDetails data class
    fun getLastSelectedTrack() : SelectedTrackDetails? {
        val objStr = sharedPref.getString("lastScreenViewed", null) ?: return null

        return try {
            Gson().fromJson(objStr, SelectedTrackDetails::class.java)
        } catch (e : JsonSyntaxException) {
            null
        }
    }

    //Clears all the shared pref storage
    fun clearLastSelectedTrack() {
        editor.clear()
        editor.apply()
    }





}