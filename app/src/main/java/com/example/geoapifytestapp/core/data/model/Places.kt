package com.example.geoapifytestapp.core.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Description(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("shop")
    val shop: String? = null,
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("website")
    val website: String? = null,
    @SerializedName("opening_hours")
    val opening_hours: String? = null,
) : Serializable

data class Data(
    @SerializedName("raw")
    val description: Description? = null,
) : Serializable

data class Details(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("street")
    val street: String? = null,
    @SerializedName("lon")
    val lon: Double? = null,
    @SerializedName("lat")
    val lat: Double? = null,
    @SerializedName("datasource")
    val data: Data? = null,
) : Serializable

data class Place(
    @SerializedName("properties")
    val details: Details? = null,
) : Serializable

data class Places(
    @SerializedName("features")
    val places: List<Place>,
)