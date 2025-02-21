package com.protect.jikigo.ui.travel

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.protect.jikigo.R
import com.protect.jikigo.data.Storage
import com.protect.jikigo.data.model.Coupon
import com.protect.jikigo.data.repo.CouponRepo
import com.protect.jikigo.databinding.FragmentTravelCouponBinding
import com.protect.jikigo.ui.adapter.CouponAdaptor
import com.protect.jikigo.ui.adapter.TravelCouponOnClickListener
import com.protect.jikigo.ui.extensions.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TravelCouponFragment : Fragment(), TravelCouponOnClickListener {
    private var _binding: FragmentTravelCouponBinding? = null
    private val binding get() = _binding!!
    lateinit var adapter: CouponAdaptor
    private var categoryIndex: Int = 1
    private var coupon: List<Coupon> = listOf()
    private var selectedBrand: String? = null

    private var isFabVisible = false

    @Inject
    lateinit var couponRepo : CouponRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryIndex = arguments?.getInt("categoryIndex") ?: 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTravelCouponBinding.inflate(inflater, container, false)

        binding.apply {
            val sortContainer = sortContainer
            val tvSort = tvSort

            sortContainer.setOnClickListener {
                val popupMenu = PopupMenu(requireContext(), it)
                popupMenu.menuInflater.inflate(R.menu.menu_sort_options, popupMenu.menu)

                popupMenu.setOnMenuItemClickListener { item ->
                    tvSort.text = item.title
                    applyFiltersAndSorting(item.title.toString())
                    true
                }
                popupMenu.show()
            }
            setFabScrollBehavior()
        }

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
        setRecyclerView()
        setFabClickListener()
    }

    private fun setFabScrollBehavior() {
        binding.nestedScrollView2.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            if (scrollY > 1000 && !isFabVisible) {
                binding.fabTravelCoupon.visibility = View.VISIBLE
                isFabVisible = true
            }
            else if (scrollY < 100 && isFabVisible) {
                binding.fabTravelCoupon.visibility = View.INVISIBLE
                isFabVisible = false
            }
        }
    }

    private fun setFabClickListener() {
        binding.fabTravelCoupon.setOnClickListener {
            binding.nestedScrollView2.scrollTo(0, 0)
            binding.fabTravelCoupon.visibility = View.INVISIBLE
            isFabVisible = false
        }
    }

    private fun setRecyclerView() {
        // coupon = filterCouponByCategory(categoryIndex)
        // selectedBrand = "전체보기"
        lifecycleScope.launch {
            val category = when (categoryIndex){
                1 -> "숙박"
                2 -> "레저/티켓"
                3 -> "공연/전시"
                4 -> "여행용품"
                else -> "숙박"
            }

            selectedBrand = "전체보기"

            coupon = couponRepo.getCouponByCategory(category)

            setChip()

            applyFiltersAndSorting(binding.tvSort.text.toString())
        }

    }

    private fun setChip() {
        val brandSet = coupon.map { it.couponBrand }.toSet().sorted()
        val cgCouponBrand = binding.cgCouponBrand
        cgCouponBrand.removeAllViews()

        val allChip = createChip("전체보기", true)
        allChip.setOnClickListener {
            if (selectedBrand != "전체보기") {
                selectedBrand = "전체보기"
                applyFiltersAndSorting(binding.tvSort.text.toString())
            }
        }
        cgCouponBrand.addView(allChip)

        brandSet.forEach { brand ->
            val chip = createChip(brand, false)
            chip.setOnClickListener {
                if (selectedBrand != brand) {
                    selectedBrand = brand
                    applyFiltersAndSorting(binding.tvSort.text.toString())
                } else {
                    chip.isChecked = true
                }
            }
            cgCouponBrand.addView(chip)
        }

        applyFiltersAndSorting(binding.tvSort.text.toString())
    }

    private fun createChip(text: String, isDefaultChecked: Boolean): Chip {
        return Chip(requireContext()).apply {
            this.text = text
            this.isCheckable = true
            this.isChecked = isDefaultChecked

            setOnCheckedChangeListener { _, isChecked ->
                typeface = if (isChecked) Typeface.defaultFromStyle(Typeface.BOLD)
                else Typeface.defaultFromStyle(Typeface.NORMAL)
            }

            setChipBackgroundColorResource(R.color.selector_chip_background_color)
            setChipStrokeColorResource(R.color.selector_chip_stroke_color)
            setTextColor(ContextCompat.getColorStateList(context, R.color.selector_chip_text_color))
            setTextSize(12f)
        }
    }

    private fun applyFiltersAndSorting(sortOption: String) {
        lifecycleScope.launch {
            var filteredCoupon : List<Coupon>

            // Firestore에서 데이터를 필터링하여 가져오기
            filteredCoupon = if (selectedBrand == "전체보기") {
                couponRepo.getCouponByCategoryAndSort(category = getCategoryFromIndex(categoryIndex), sortOption = sortOption)
            } else {
                couponRepo.getCouponByBrandAndCategoryAndSort(selectedBrand!!, getCategoryFromIndex(categoryIndex), sortOption)
            }

            // 어댑터에 필터링된 데이터 적용
            adapter = CouponAdaptor(filteredCoupon, this@TravelCouponFragment)
            binding.rvCouponList.adapter = adapter

            val totalCount = filteredCoupon.size.toString()
            binding.tvCouponCount.text = SpannableString("총 ${totalCount}개의 상품이 있습니다.").apply {
                setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.black)),
                    2, 2 + totalCount.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
    }

    private fun getCategoryFromIndex(index: Int): String {
        return when (index) {
            1 -> "숙박"
            2 -> "레저/티켓"
            3 -> "공연/전시"
            4 -> "여행용품"
            else -> "숙박"
        }
    }

    override fun onClickListener(item: Coupon) {
        requireContext().toast(item.couponName)
        val action = TravelFragmentDirections.actionNavigationTravelToTravelCouponDetail()
        findNavController().navigate(action)
    }
}