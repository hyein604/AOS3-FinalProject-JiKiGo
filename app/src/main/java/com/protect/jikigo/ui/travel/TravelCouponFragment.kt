package com.protect.jikigo.ui.travel

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.protect.jikigo.R
import com.protect.jikigo.data.Coupon
import com.protect.jikigo.data.Storage
import com.protect.jikigo.databinding.FragmentTravelCouponBinding
import com.protect.jikigo.ui.adapter.CouponAdaptor
import com.protect.jikigo.ui.adapter.TravelCouponOnClickListener
import com.protect.jikigo.ui.extensions.toast

class TravelCouponFragment : Fragment(), TravelCouponOnClickListener {
    private var _binding: FragmentTravelCouponBinding? = null
    private val binding get() = _binding!!
    lateinit var adapter: CouponAdaptor
    private var categoryIndex: Int = 1
    private var coupon: List<Coupon> = listOf()
    private var selectedBrand: String? = null

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
        setChip()
    }

    private fun setRecyclerView() {
        coupon = filterCouponByCategory(categoryIndex)
        selectedBrand = "전체보기"
    }

    private fun setChip() {
        val brandSet = coupon.map { it.brand }.toSet()
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

    private fun filterCouponByCategory(categoryIndex: Int): List<Coupon> {
        val category = listOf("숙박", "레저/티켓", "공연/전시", "여행용품")
        if (categoryIndex < 1 || categoryIndex > 4) return emptyList()

        val selectedCategory = category[categoryIndex - 1]
        return Storage.coupon.filter { it.category == selectedCategory }
    }

    private fun applyFiltersAndSorting(sortOption: String) {
        // 필터링 먼저 적용
        var filteredCoupon = if (selectedBrand == "전체보기") {
            coupon
        } else {
            coupon.filter { it.brand == selectedBrand }
        }

        // 정렬 옵션 적용
        filteredCoupon = when (sortOption) {
            "추천순" -> filteredCoupon.sortedByDescending { it.salesCount }
            "낮은 가격순" -> filteredCoupon.sortedBy { it.price }
            "높은 가격순" -> filteredCoupon.sortedByDescending { it.price }
            else -> filteredCoupon
        }

        // 어댑터에 필터링된 데이터 적용
        adapter = CouponAdaptor(filteredCoupon, this)
        binding.rvCouponList.adapter = adapter

        val totalCount = filteredCoupon.size.toString()
        binding.tvCouponCount.text = SpannableString("총 ${totalCount}개의 상품이 있습니다.").apply {
            setSpan(
                ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.black)),
                2, 2 + totalCount.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    override fun onClickListener(item: Coupon) {
        requireContext().toast(item.name)
        val action = TravelFragmentDirections.actionNavigationTravelToTravelCouponDetail(item)
        findNavController().navigate(action)
    }
}