package com.kevinhomorales.botcakotlin.utils

class Constants {
    companion object {
        val GOOGLE_SIGN_IN = 100
        val clearString = ""
        val ok = "OK"
        val cancel = "Cancelar"
        val businessSampleImageURL = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8e/Shop.svg/1200px-Shop.svg.png"

        val businessListKey = "BUSINESS_LIST_KEY"
        val businessKey = "BUSINESS_KEY"
        val rolesKey = "ROLES_KEY"
        val categoriesResponseKey = "CATEGORIES_RESPONSE_KEY"
        val invocesKey = "INVOICES_KEY"
        val invoceKey = "INVOICE_KEY"
        val usersKey = "USERS_KEY"

        // SERVER - ERROR
        val sessionExpired = "jwt expired"

        val domain = "https://botca.onrender.com" // DEV
//        val domain = "https://botca.com" // PROD
        val enviroment = "${domain}/api/"
    }
}