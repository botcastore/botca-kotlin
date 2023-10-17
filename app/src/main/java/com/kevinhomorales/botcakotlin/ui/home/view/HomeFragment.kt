package com.kevinhomorales.botcakotlin.ui.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.kevinhomorales.botcakotlin.databinding.FragmentHomeBinding
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.ui.home.services.response.CategoriesResponse
import com.kevinhomorales.botcakotlin.ui.home.services.response.Category
import com.kevinhomorales.botcakotlin.ui.home.view.adapter.CategoryAdapter
import com.kevinhomorales.botcakotlin.ui.home.view.adapter.OnCategoryClickListener
import com.kevinhomorales.botcakotlin.ui.home.viewmodel.HomeViewModel
import com.kevinhomorales.botcakotlin.utils.Constants
import com.kevinhomorales.botcakotlin.utils.UserManager


class HomeFragment : Fragment(), OnCategoryClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return setUpView(inflater, container)
    }

    private fun setUpView(inflater: LayoutInflater, container: ViewGroup?): View {
        val mainActivity = requireActivity() as MainActivity
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModel.view = this
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        categoryAdapter = CategoryAdapter(requireActivity(), this)
        binding.categoriesId.setLayoutManager(GridLayoutManager(requireContext(), 2))
        binding.categoriesId.layoutManager = LinearLayoutManager(requireActivity())
        binding.categoriesId.adapter = categoryAdapter
        if (requireActivity().intent != null) {
            val loginResponse = UserManager.shared.getUser(mainActivity)
            if (!(loginResponse.me.token.isEmpty())) {
                viewModel.getCategories(mainActivity)
                return binding.root
            }
            viewModel.categoriesResponse = requireActivity().intent.extras!!.get(Constants.categoriesResponseKey) as CategoriesResponse
            val category = viewModel.categoriesResponse.categorys
            categoryAdapter.setListData(category)
            categoryAdapter.notifyDataSetChanged()
        }
        return binding.root
    }

    fun updateTable(categoriesResponse: CategoriesResponse) {
        val category = categoriesResponse.categorys
        categoryAdapter.setListData(category)
        categoryAdapter.notifyDataSetChanged()
    }

    override fun categoryClick(category: Category) {

    }
}