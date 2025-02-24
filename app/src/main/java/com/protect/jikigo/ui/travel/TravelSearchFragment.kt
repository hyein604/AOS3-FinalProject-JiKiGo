package com.protect.jikigo.ui.travel

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.protect.jikigo.R
import com.protect.jikigo.data.model.Coupon
import com.protect.jikigo.databinding.FragmentTravelSearchBinding
import com.protect.jikigo.ui.adapter.CouponAdaptor
import com.protect.jikigo.ui.adapter.TravelCouponOnClickListener
import com.protect.jikigo.ui.viewModel.TravelSearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TravelSearchFragment : Fragment(), TravelCouponOnClickListener {
    private var _binding: FragmentTravelSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TravelSearchViewModel by viewModels()

    private var adapter: CouponAdaptor? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTravelSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        moveToTravel()
        setupSearchListener()
        setupRecentSearches()
        setupRecyclerView()
        showKeyboard()
    }

    private fun setupObservers() {
        viewModel.couponList.observe(viewLifecycleOwner) { coupons ->
            // 리사이클러뷰는 처음에는 안 보이게 하고, 검색 결과가 있을 때만 보이게
            binding.rvSearchCouponList.visibility = if (coupons.isNotEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        viewModel.filteredCoupons.observe(viewLifecycleOwner) { coupons ->
            if (coupons.isEmpty()) {
                binding.rvSearchCouponList.visibility = View.GONE
                binding.tvNoResult.visibility = View.VISIBLE
                binding.tvNoResult.text = "'${binding.etSearch.text}'에 해당하는 상품이 없습니다."
                binding.tvRecent.visibility = View.GONE
                binding.cgHistory.visibility = View.GONE
            } else {
                binding.rvSearchCouponList.visibility = View.VISIBLE
                binding.tvNoResult.visibility = View.GONE
                adapter?.updateList(coupons)
                binding.tvRecent.visibility = View.GONE
                binding.cgHistory.visibility = View.GONE
            }
        }

        viewModel.recentSearches.observe(viewLifecycleOwner) { searches ->
            Log.d("TravelSearchViewModel", "recentSearches updated: $searches")
            setupChips(searches)
        }
    }




    private fun setupSearchListener() {
        binding.etSearch.setOnEditorActionListener { _, _, _ ->
            handleSearch()
            true
        }

        binding.toobarSearch.setOnMenuItemClickListener {
            handleSearch()
            true
        }
    }

    private fun handleSearch() {
        val query = binding.etSearch.text.toString()
        if (query.isNotEmpty()) {
            viewModel.addRecentSearch(query)
            viewModel.filterCoupons(query)
        }
        hideKeyboard()
    }

    private fun setupRecentSearches() {
        binding.cgHistory.removeAllViews()
    }

    private fun setupChips(searches: List<String>) {
        binding.cgHistory.removeAllViews()

        if (searches.isNotEmpty()) {
            binding.tvNoHistory.visibility = View.GONE
        } else {
            binding.tvNoHistory.visibility = View.VISIBLE
        }

        searches.forEach { query ->
            val chip = createChip(query)
            binding.cgHistory.addView(chip)

            chip.setOnClickListener {
                val query = chip.text.toString()
                binding.etSearch.setText(query)

                // 커서를 텍스트 끝으로 이동
                binding.etSearch.setSelection(query.length)

                viewModel.filterCoupons(query)

                // 클릭한 검색어를 최근 검색어로 다시 추가
                viewModel.addRecentSearch(query)
            }

            chip.setOnCloseIconClickListener {
                viewModel.removeRecentSearch(query)
            }
        }
    }

    private fun createChip(text: String): Chip {
        return Chip(requireContext()).apply {
            this.text = if (text.length > 7) text.take(7) + "..." else text
            isCloseIconVisible = true
            setChipBackgroundColorResource(R.color.gray_5)
            setTextColor(resources.getColor(R.color.black, null))
            setChipStrokeColorResource(R.color.gray_5)
            textSize = 12f
        }
    }

    private fun setupRecyclerView() {
        if (adapter == null) {
            adapter = CouponAdaptor(emptyList(), this@TravelSearchFragment)
        }
        binding.rvSearchCouponList.adapter = adapter
    }

    private fun showKeyboard() {
        binding.etSearch.requestFocus()
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        imm.showSoftInput(binding.etSearch, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard() {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
    }

    private fun moveToTravel() {
        binding.toobarSearch.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onClickListener(item: Coupon) {
        val action = TravelFragmentDirections.actionNavigationTravelToTravelCouponDetail()
        findNavController().navigate(action)
    }
}

