package com.kevinhomorales.botcakotlin.ui.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kevinhomorales.botcakotlin.databinding.FragmentHomeBinding
import com.kevinhomorales.botcakotlin.ui.home.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
//    private lateinit var categoriesListAdapter: CategoriesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return setUpView(inflater, container)
    }

    private fun setUpView(inflater: LayoutInflater, container: ViewGroup?): View {
        viewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        binding = FragmentHomeBinding.inflate(inflater, container, false)
//        categoriesListAdapter = CategoriesListAdapter(requireActivity(), this)
//        binding.categoriesId.layoutManager = LinearLayoutManager(requireActivity())
//        binding.categoriesId.adapter = categoriesListAdapter
        return binding.root
    }

}