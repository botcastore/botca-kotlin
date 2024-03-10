package com.kevinhomorales.botcakotlin.customer.profile.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.kevinhomorales.botcakotlin.NetworkManager.model.AddressModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.CardsModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.FavoritesModel
import com.kevinhomorales.botcakotlin.NetworkManager.response.AddressResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.CardsReponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.FavoritesResponse
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.address.view.AddressActivity
import com.kevinhomorales.botcakotlin.customer.home.view.HomeActivity
import com.kevinhomorales.botcakotlin.customer.login.view.LoginActivity
import com.kevinhomorales.botcakotlin.customer.login.viewmodel.LoginViewModel
import com.kevinhomorales.botcakotlin.customer.payments.cards.view.CardsActivity
import com.kevinhomorales.botcakotlin.customer.profile.info.view.InfoActivity
import com.kevinhomorales.botcakotlin.customer.profile.viewmodel.ProfileViewModel
import com.kevinhomorales.botcakotlin.customer.wishlist.view.WishlistActivity
import com.kevinhomorales.botcakotlin.databinding.ActivityProfileBinding
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.Constants
import com.kevinhomorales.botcakotlin.utils.UserManager
import java.io.Serializable

class ProfileActivity : MainActivity() {

    lateinit var binding: ActivityProfileBinding
    lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        title = getString(R.string.profile_title)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        viewModel.view = this
        val user = UserManager.shared.getUser(this)
        Glide.with(this)
            .load(user.me.user.avatarURL)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade(2))
            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
            .placeholder(R.drawable.category_hint)
            .into(binding.profileImageId)
        binding.nameId.text = user.me.user.displayName
        binding.emailId.text = user.me.user.email
        binding.phoneId.text = user.me.user.phoneNumber
        binding.providerId.text = user.me.user.provider
        binding.birthdayId.text = user.me.user.birthday
        binding.versionId.text = viewModel.getVersion(this)
        setUpActions()
    }

    private fun setUpActions() {
        binding.addressesId.setOnClickListener {
            tapHaptic()
            viewModel.getAddresses(this)
        }
        binding.cardsId.setOnClickListener {
            tapHaptic()
            viewModel.getCards(this)
        }
        binding.ordersId.setOnClickListener {
            tapHaptic()
        }
        binding.signOutId.setOnClickListener {
            tapHaptic()
            signOut()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite_id -> {
                tapHaptic()
                openFavoritesView()
                true
            }
            R.id.info_id -> {
                tapHaptic()
                openInfoView()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openFavoritesView() {
        tapHaptic()
        viewModel.getFavorites(this)
    }

    private fun signOut() {
        Alerts.twoOptions(getString(R.string.alert_title), getString(R.string.sign_out_ask), getString(R.string.ok), getString(R.string.cancel), this) { isOK ->
            if (isOK) {
                UserManager.shared.removeUser(this)
                openLoginView()
            }
        }
    }

    private fun openLoginView() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun openAddressView(addressResponse: AddressResponse) {
        val intent = Intent(this, AddressActivity::class.java)
        intent.putExtra(Constants.addressResponseKey, addressResponse as Serializable)
        hideLoading()
        startActivity(intent)
    }

    fun openCardsView(cardsReponse: CardsReponse) {
        val intent = Intent(this, CardsActivity::class.java)
        intent.putExtra(Constants.cardsResponseKey, cardsReponse as Serializable)
        hideLoading()
        startActivity(intent)
    }

    fun openFavoritesView(favoritesResponse: FavoritesResponse) {
        val intent = Intent(this, WishlistActivity::class.java)
        intent.putExtra(Constants.favoritesResponseKey, favoritesResponse as Serializable)
        hideLoading()
        startActivity(intent)
    }

    private fun openInfoView() {
        tapHaptic()
        val intent = Intent(this, InfoActivity::class.java)
        startActivity(intent)
    }
}