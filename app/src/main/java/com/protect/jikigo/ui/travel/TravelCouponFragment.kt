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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.protect.jikigo.R
import com.protect.jikigo.data.model.Coupon
import com.protect.jikigo.databinding.FragmentTravelCouponBinding
import com.protect.jikigo.ui.adapter.CouponAdaptor
import com.protect.jikigo.ui.adapter.TravelCouponOnClickListener
import com.protect.jikigo.ui.extensions.toast
import com.protect.jikigo.ui.viewModel.TravelCouponViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TravelCouponFragment : Fragment(), TravelCouponOnClickListener {
    private var _binding: FragmentTravelCouponBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TravelCouponViewModel by viewModels()
    lateinit var adapter: CouponAdaptor
    private var isFabVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val categoryIndex = arguments?.getInt("categoryIndex") ?: 1
        viewModel.setCategory(categoryIndex)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTravelCouponBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 초기 화면이 로드될 때 추천순으로 필터링을 적용
        viewModel.applyFiltersAndSorting("추천순")

        setupObservers()
        setupSortMenu()
        setupFabBehavior()
    }

    private fun setupObservers() {
        viewModel.filteredCoupons.observe(viewLifecycleOwner) { coupons ->
            if (coupons.isNotEmpty()) {
                // 새로운 데이터를 가져올 때마다 어댑터를 새로 설정
                adapter = CouponAdaptor(coupons, this)
                binding.rvCouponList.adapter = adapter

                val totalCount = coupons.size.toString()
                binding.tvCouponCount.text = SpannableString("총 ${totalCount}개의 상품이 있습니다.").apply {
                    setSpan(
                        ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.black)),
                        2, 2 + totalCount.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                binding.sortContainer.visibility = View.VISIBLE
            } else {
                // 데이터가 없으면 표시할 텍스트 설정
                binding.cgCouponBrand.visibility = View.GONE
                binding.tvCouponCount.visibility = View.GONE
                binding.sortContainer.visibility = View.GONE
                binding.rvCouponList.visibility = View.GONE
                binding.tvNoCoupon.visibility = View.VISIBLE
            }
        }

        viewModel.brandList.observe(viewLifecycleOwner) { brands ->
            setupChips(brands)
        }
    }

    private fun setupSortMenu() {
        binding.sortContainer.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), it)
            popupMenu.menuInflater.inflate(R.menu.menu_sort_options, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                binding.tvSort.text = item.title
                viewModel.applyFiltersAndSorting(item.title.toString())
                true
            }
            popupMenu.show()
        }
    }

    private fun setupChips(brands: List<String>) {
        val cgCouponBrand = binding.cgCouponBrand
        cgCouponBrand.removeAllViews()

        val allChip = createChip("전체보기", true)
        allChip.setOnClickListener {
            val currentSortOption = binding.tvSort.text.toString()
            viewModel.selectBrand("전체보기",currentSortOption) }
        cgCouponBrand.addView(allChip)

        brands.forEach { brand ->
            val chip = createChip(brand, false)
            chip.setOnClickListener {
                val currentSortOption = binding.tvSort.text.toString()
                viewModel.selectBrand(brand, currentSortOption) }
            cgCouponBrand.addView(chip)
        }
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

    private fun setupFabBehavior() {
        binding.nestedScrollView2.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            if (scrollY > 1000 && !isFabVisible) {
                binding.fabTravelCoupon.visibility = View.VISIBLE
                isFabVisible = true
            } else if (scrollY < 100 && isFabVisible) {
                binding.fabTravelCoupon.visibility = View.INVISIBLE
                isFabVisible = false
            }
        }

        binding.fabTravelCoupon.setOnClickListener {
            binding.nestedScrollView2.scrollTo(0, 0)
            binding.fabTravelCoupon.visibility = View.INVISIBLE
            isFabVisible = false
        }
    }

    override fun onClickListener(item: Coupon) {
        requireContext().toast(item.couponName)
        val action = TravelFragmentDirections.actionNavigationTravelToTravelCouponDetail(item)
        findNavController().navigate(action)
    }
}
