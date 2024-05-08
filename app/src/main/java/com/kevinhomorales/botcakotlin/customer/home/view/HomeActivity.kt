package com.kevinhomorales.botcakotlin.customer.home.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.kevinhomorales.botcakotlin.NetworkManager.model.ProductsModel
import com.kevinhomorales.botcakotlin.NetworkManager.response.BannersResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.CategoriesResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.Category
import com.kevinhomorales.botcakotlin.NetworkManager.response.CouponResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.ProductsResponse
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.aboutus.view.AboutUsActivity
import com.kevinhomorales.botcakotlin.customer.coupon.view.CouponActivity
import com.kevinhomorales.botcakotlin.customer.home.view.adapter.CategoryAdapter
import com.kevinhomorales.botcakotlin.customer.home.view.adapter.OnCategoryClickListener
import com.kevinhomorales.botcakotlin.customer.home.view.adapterbanner.BannerAdapter
import com.kevinhomorales.botcakotlin.customer.home.viewmodel.HomeViewModel
import com.kevinhomorales.botcakotlin.customer.login.view.LoginActivity
import com.kevinhomorales.botcakotlin.customer.products.view.ProductsActivity
import com.kevinhomorales.botcakotlin.customer.profile.view.ProfileActivity
import com.kevinhomorales.botcakotlin.databinding.ActivityHomeBinding
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.Constants
import com.kevinhomorales.botcakotlin.utils.GUEST_TOKEN
import com.kevinhomorales.botcakotlin.utils.LocationManager
import com.kevinhomorales.botcakotlin.utils.UserManager
import java.io.Serializable

class HomeActivity : MainActivity(), OnCategoryClickListener {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var bannerAdapter: BannerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
        viewModel.getCoupons(this)
        binding.swipeRefreshLayout.setOnRefreshListener {
            setUpView()
        }
    }
    private fun setUpView() {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModel.view = this
        categoryAdapter = CategoryAdapter(this, this)
        binding.categoriesId.setHasFixedSize(true)
        binding.categoriesId.layoutManager = LinearLayoutManager(this)
        binding.categoriesId.setLayoutManager(GridLayoutManager(this, 2))
        binding.categoriesId.adapter = categoryAdapter
        viewModel.getBanners(this)
        if (intent != null) {
            val loginResponse = UserManager.shared.getUser(this)
            if (!(loginResponse.me.token!!.isEmpty())) {
                val name = loginResponse.me.user.displayName.split(Constants.space).first()
                if (loginResponse.me.token == GUEST_TOKEN) {
                    title = "${getString(R.string.hi)} ${getString(R.string.hi_guest)}"
                } else {
                    title = "${getString(R.string.hi)} ${name}!"
                }
                viewModel.getCategories(this)
                return
            }
            viewModel.categoriesResponse = intent.extras!!.get(Constants.categoriesResponseKey) as CategoriesResponse
            val category = viewModel.categoriesResponse!!.categorys
            categoryAdapter.setListData(category)
            categoryAdapter.notifyDataSetChanged()
        }
    }

    fun updateTable(categoriesResponse: CategoriesResponse) {
        val categorys = categoriesResponse.categorys
        categoryAdapter.setListData(categorys)
        categoryAdapter.notifyDataSetChanged()
        binding.swipeRefreshLayout.isRefreshing = false
    }

    fun updateTableBanners(bannersResponse: BannersResponse) {
        val banners = bannersResponse.banners
        if (banners.count() > 0) {
            bannerAdapter = BannerAdapter(this)
            binding.bannerId.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.bannerId.adapter = bannerAdapter
            binding.bannerId.visibility = View.VISIBLE
            bannerAdapter.setListData(banners)
            bannerAdapter.notifyDataSetChanged()
            return
        }
        binding.bannerId.visibility = View.GONE
    }

    override fun categoryClick(category: Category) {
        val categoryID = "?categoryID=${category.categoryID}"
        val page = "&page=1&dataByPage=20"
        val model = ProductsModel(categoryID, Constants.clearString, Constants.clearString, Constants.clearString, page)
        viewModel.getProducts(this, model)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.profile_id -> {
                tapHaptic()
                if (viewModel.isGuest(this)) {
                    Alerts.twoOptions(getString(R.string.alert_title), getString(R.string.please_login), getString(R.string.accept), getString(R.string.cancel), this) { isOK ->
                        if (isOK) {
                            openLoginView()
                        }
                    }
                } else {
                    openProfileView()
                }
                true
            }
            R.id.about_us_id -> {
                tapHaptic()
                openAboutUsView()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openProfileView() {
        tapHaptic()
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    fun openProductsView(productsResponse: ProductsResponse) {
        tapHaptic()
        val intent = Intent(this, ProductsActivity::class.java)
        intent.putExtra(Constants.productsResponseKey, productsResponse as Serializable)
        hideLoading()
        startActivity(intent)
    }

    fun openCouponsView(couponResponse: CouponResponse) {
        val intent = Intent(this, CouponActivity::class.java)
        intent.putExtra(Constants.couponResponseKey, couponResponse as Serializable)
        startActivity(intent)
    }

    fun openLoginView() {
        UserManager.shared.removeUser(this)
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        hideLoading()
    }

    override fun onBackPressed() {
    }

    private fun openAboutUsView() {
        val intent = Intent(this, AboutUsActivity::class.java)
        startActivity(intent)
    }
}