package com.kevinhomorales.botcakotlin.customer.wishlist.viewmodel

import androidx.lifecycle.ViewModel
import com.kevinhomorales.botcakotlin.NetworkManager.response.FavoritesResponse
import com.kevinhomorales.botcakotlin.customer.wishlist.view.WishlistActivity

class WishlistViewModel: ViewModel() {
    lateinit var favoritesResponse: FavoritesResponse
    lateinit var view: WishlistActivity
}