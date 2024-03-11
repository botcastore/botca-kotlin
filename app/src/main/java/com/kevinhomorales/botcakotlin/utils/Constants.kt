package com.kevinhomorales.botcakotlin.utils

class Constants {
    companion object {
        val GOOGLE_SIGN_IN = 100
        val clearString = ""
        val space = " "
        val ok = "OK"
        val cancel = "Cancel"
        val businessSampleImageURL = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8e/Shop.svg/1200px-Shop.svg.png"
        val appURL = "http://onelink.to/botcastore"
        val terms = "https://www.botca.store/terms-and-conditions/"

        val businessListKey = "BUSINESS_LIST_KEY"
        val businessKey = "BUSINESS_KEY"
        val rolesKey = "ROLES_KEY"
        val categoriesResponseKey = "CATEGORIES_RESPONSE_KEY"
        val verifyMemberResponse = "VERIFY_MEMBER_RESPONSE_KEY"
        val productsResponseKey = "PRODUCTS_RESPONSE_KEY"
        val couponResponseKey = "COUPON_RESPONSE_KEY"
        val cartAvailableResponseKey = "CART_AVAILABLE_KEY"
        val addressResponseKey = "ADDRESSES_RESPONSE_KEY"
        val cardsResponseKey = "CARDS_RESPONSE_KEY"
        val favoritesResponseKey = "FAVORITES_RESPONSE_KEY"
        val productKey = "PRODUCT_KEY"
        val invocesKey = "INVOICES_KEY"
        val invoceKey = "INVOICE_KEY"
        val usersKey = "USERS_KEY"

        // SERVER - ERROR
        val sessionExpired = "jwt expired"

        val domain = "https://dev-botca-store.onrender.com" // DEV
//        val domain = "https://botca-store.onrender.com" // PROD
        val enviroment = "${domain}/api/"
    }
}