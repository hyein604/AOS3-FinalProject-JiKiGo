package com.protect.jikigo.ui.travel

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.protect.jikigo.R
import com.protect.jikigo.data.Storage
import com.protect.jikigo.data.model.Coupon
import com.protect.jikigo.data.repo.CouponRepo
import com.protect.jikigo.databinding.FragmentTravelSearchBinding
import com.protect.jikigo.ui.adapter.CouponAdaptor
import com.protect.jikigo.ui.adapter.TravelCouponOnClickListener
import com.protect.jikigo.ui.extensions.statusBarColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TravelSearchFragment : Fragment(), TravelCouponOnClickListener {
    private var _binding: FragmentTravelSearchBinding? = null
    private val binding get() = _binding!!

    private val maxChips = 10
    private val sharedPreference  by lazy {
        requireContext().getSharedPreferences("recent_search", Context.MODE_PRIVATE)
    }

    private lateinit var coupon : List<Coupon>
    lateinit var adapter: CouponAdaptor

    @Inject
    lateinit var couponRepo: CouponRepo

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
        setLayout()
    }

    private fun setLayout() {
        moveToTravel()
        setStatusBar()
        setupSearchListener()
        setRecyclerView()
        loadChips()
        showKeyboard()
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
            hideRecentSearch()
            addChip(query)
            filterCoupon(query)
        }
        hideKeyboard()
    }

    private fun hideRecentSearch() {
        binding.tvRecent.visibility = View.GONE
        binding.cgHistory.visibility = View.GONE
        binding.tvNoHistory.visibility = View.GONE
    }

    private fun addChip(query: String) {
        binding.tvNoHistory.visibility = View.GONE
        findChip(query)?.let { binding.cgHistory.removeView(it) }

        val chip = createChip(query)
        binding.cgHistory.addView(chip, 0)

        chip.setOnClickListener {
            hideRecentSearch()

            // 칩 클릭 시 해당 검색어로 검색
            binding.etSearch.setText(query)
            filterCoupon(query)
        }

        chip.setOnCloseIconClickListener {
            removeChip(chip, query)
        }

        // 칩이 10개 이상일 경우, 가장 오래된 칩 제거
        if (binding.cgHistory.childCount > maxChips) {
            binding.cgHistory.removeViewAt(binding.cgHistory.childCount-1)
        }

        // 칩을 내부 저장소에 저장
        saveChip(query)
    }

    private fun findChip(query: String): Chip? {
        for (i in 0 until binding.cgHistory.childCount) {
            val chip = binding.cgHistory.getChildAt(i) as? Chip
            if (chip?.text.toString() == query) {
                return chip
            }
        }
        return null
    }

    private fun createChip(text: String): Chip {

        val displayText = if (text.length > 7) {
            text.take(7) + "..."
        } else {
            text
        }

        return Chip(requireContext()).apply {
            this.text = displayText
            isCloseIconVisible = true
            setChipBackgroundColorResource(R.color.gray_5)
            setTextColor(resources.getColor(R.color.black, null))
            setChipStrokeColorResource(R.color.gray_5)
            textSize = 12f
        }
    }

    private fun removeChip(chip: Chip, query: String) {
        binding.cgHistory.removeView(chip)
        removeChipFromStorage(query)
        val remainingChips = getChipsFromStorage()
        if(remainingChips.isEmpty()){
            binding.tvNoHistory.visibility = View.VISIBLE
        }
    }

    private fun saveChip(query: String) {
        val chips = getChipsFromStorage().toMutableList()
        if (!chips.contains(query)) {
            chips.add(query)
        }
        if (chips.size > maxChips) {
            chips.removeAt(0)
        }
        sharedPreference.edit().putStringSet("chips", chips.toSet()).apply()
    }

    private fun getChipsFromStorage(): Set<String> {
        return sharedPreference.getStringSet("chips", emptySet()) ?: emptySet()
    }

    private fun removeChipFromStorage(query: String) {
        val chips = getChipsFromStorage().toMutableList()
        chips.remove(query)
        sharedPreference.edit().putStringSet("chips", chips.toSet()).apply()
    }

    private fun loadChips() {
        val chips = getChipsFromStorage()

        if (chips.isNotEmpty()){
            binding.tvNoHistory.visibility = View.GONE
        }
        else {
            binding.tvNoHistory.visibility = View.VISIBLE
        }

        // tvNoResult를 초기 상태에서 숨김
        binding.tvNoResult.visibility = View.GONE

        binding.cgHistory.removeAllViews()
        chips.forEach { chipText ->
            addChip(chipText)
        }
    }

    private fun setRecyclerView(){
        lifecycleScope.launch {
            coupon = couponRepo.getAllCoupon()
            adapter = CouponAdaptor(coupon, this@TravelSearchFragment)
            binding.rvSearchCouponList.adapter = adapter
        }
    }

    private fun filterCoupon(query: String){
        val filteredCoupons = coupon.filter {
            it.couponName.contains(query, ignoreCase = true) == true ||
                    it.couponBrand.contains(query, ignoreCase = true) == true
        }

        // 상품이 없으면 "검색 결과가 없습니다." 보여주기
        if (filteredCoupons.isEmpty()) {
            binding.rvSearchCouponList.visibility = View.GONE
            binding.tvNoResult.visibility = View.VISIBLE
            binding.tvNoResult.text = "'$query' 상품이 없어요."
        } else {
            binding.rvSearchCouponList.visibility = View.VISIBLE
            binding.tvNoResult.visibility = View.GONE
        }

        adapter.updateList(filteredCoupons)
    }
    private fun setStatusBar() {
        requireActivity().statusBarColor(R.color.white)
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