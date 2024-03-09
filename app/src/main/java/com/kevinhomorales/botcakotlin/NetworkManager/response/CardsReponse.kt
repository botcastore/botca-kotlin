package com.kevinhomorales.botcakotlin.NetworkManager.response

import java.io.Serializable
import com.google.gson.annotations.SerializedName
import com.kevinhomorales.botcakotlin.utils.Constants

data class CardsReponse (
    val cards: MutableList<Card>,

    @SerializedName("has_more")
    val hasMore: Boolean
): Serializable

data class Card (
    val id: String,

    @SerializedName("object")
    val objectd: String,

    @SerializedName("address_city")
    val addressCity: Any? = null,

    @SerializedName("address_country")
    val addressCountry: Any? = null,

    @SerializedName("address_line1")
    val addressLine1: Any? = null,

    @SerializedName("address_line1_check")
    val addressLine1Check: Any? = null,

    @SerializedName("address_line2")
    val addressLine2: Any? = null,

    @SerializedName("address_state")
    val addressState: Any? = null,

    @SerializedName("address_zip")
    val addressZip: Any? = null,

    @SerializedName("address_zip_check")
    val addressZipCheck: Any? = null,

    val brand: String,
    val country: String,
    val customer: String,

    @SerializedName("cvc_check")
    val cvcCheck: String,

    @SerializedName("dynamic_last4")
    val dynamicLast4: Any? = null,

    @SerializedName("exp_month")
    val expMonth: Long,

    @SerializedName("exp_year")
    val expYear: Long,

    val fingerprint: String,
    val funding: String,
    val last4: String,
    val metadata: Metadatum,
    val name: String,

    @SerializedName("tokenization_method")
    val tokenizationMethod: Any? = null,

    val wallet: Any? = null
): Serializable

data class Metadatum (
    val ok: String = Constants.clearString
): Serializable