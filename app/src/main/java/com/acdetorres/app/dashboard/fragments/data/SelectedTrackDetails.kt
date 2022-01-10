package com.acdetorres.app.dashboard.fragments.data

//Data class for parsed Json String of selectedtrackdetails
data class SelectedTrackDetails (
    val trackName : String,
    val price : String,
    val previewUrl : String,
    val trackUrl : String,
    val wrapperType : String,
    val genre : String,
    val description : String
    )
