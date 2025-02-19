package com.protect.jikigo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.protect.jikigo.R
import com.protect.jikigo.data.Coupon
import com.protect.jikigo.data.Storage
import com.protect.jikigo.data.Store
import com.protect.jikigo.databinding.FragmentHomeBinding
import com.protect.jikigo.ui.HomeAdapter
import com.protect.jikigo.ui.HomeStoreItemClickListener
import com.protect.jikigo.ui.extensions.applyNumberFormat
import com.protect.jikigo.ui.extensions.applySpannableStyles
import com.protect.jikigo.ui.extensions.statusBarColor
import com.protect.jikigo.ui.extensions.toast


class HomeFragment : Fragment(), HomeStoreItemClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
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
        setStatusBarColor()
        moveToMyPage()
        moveToNews()
        moveToNotification()
        moveToQR()
        moveToTravel()
        moveToDetailNoti()
        homeTextSpannable()
        tempMethod()
        moveToRank()
    }

    private fun setStatusBarColor() {
        requireActivity().statusBarColor(R.color.primary)
    }

    private fun moveToNotification() {
        binding.tvHomeNoticeMore.setOnClickListener {
            val action = HomeFragmentDirections.actionNavigationHomeToNotification()
            findNavController().navigate(action)
        }
    }

    private fun moveToNews() {
        with(binding) {
            val messages = listOf("환경", "여행", "건강")
            listOf(viewHomeEnvironment, viewHomeTravel, viewHomeHealth).forEachIndexed { index, view ->
                view.setOnClickListener {
                    val action = HomeFragmentDirections.actionNavigationHomeToNews(messages[index])
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun moveToQR() {
        binding.ivHomeQr.setOnClickListener {
            val action = HomeFragmentDirections.actionNavigationHomeToPaymentQR()
            findNavController().navigate(action)
        }
    }

    private fun moveToMyPage() {
        binding.toolbarHome.setOnMenuItemClickListener { menu ->
            if (menu.itemId == R.id.menu_my_page) {
                val action = HomeFragmentDirections.actionNavigationHomeToMyPage()
                findNavController().navigate(action)
                true
            } else {
                false
            }
        }
    }

    private fun moveToTravel() {
        binding.tvHomeStoreMore.setOnClickListener {
            val bottomNavHome = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav_home)
            bottomNavHome.selectedItemId = R.id.navigation_travel
        }
    }

    private fun moveToRank() {
        binding.tvHomeClickRank.setOnClickListener {
            val action = HomeFragmentDirections.actionNavigationHomeToRanking()
            findNavController().navigate(action)
        }
    }

    private fun homeTextSpannable() {
        // 닉네임의 길이를 넣어줌
        binding.tvHomeNickname.applySpannableStyles(0, 3, R.color.white)
        binding.tvHomeClickRank.applySpannableStyles(0, binding.tvHomeClickRank.length(), R.color.black, true, true)
    }

    // 데이테 베이스 생성시 삭제되는 메서드
    private fun tempMethod() {
        val topNotices = Storage.notificationList.take(3) // 상위 3개만 가져오기
        with(binding) {
            // 유저 포인트
            tvHomePoint.applyNumberFormat(3456)
            // 공지사항
            tvHomeNotice1.text = topNotices.getOrNull(0)?.title ?: ""
            tvHomeNotice2.text = topNotices.getOrNull(1)?.title ?: ""
            tvHomeNotice3.text = topNotices.getOrNull(2)?.title ?: ""
        }
    }

    private fun moveToDetailNoti() {
        val topNotices = Storage.notificationList.take(3) // 상위 3개만 사용
        with(binding) {
            listOf(tvHomeNotice1, tvHomeNotice2, tvHomeNotice3).forEachIndexed { index, textView ->
                textView.setOnClickListener {
                    val action = HomeFragmentDirections
                        .actionNavigationHomeToNotificationDetail(topNotices[index])
                    findNavController().navigate(action)
                }
            }
        }
    }


    private fun setRecyclerView() {
        val storeList = Storage.coupon
        val adapter = HomeAdapter(storeList, this)
        binding.rvHomeStore.adapter = adapter
    }

    // store 이동 리스너

    override fun onClickStore(coupon: Coupon) {
        //        val action = HomeFragmentDirections.actionNavigationHomeToTravelCouponDetail()
//        findNavController().navigate(action)
        Toast.makeText(requireContext(), coupon.name, Toast.LENGTH_SHORT).show()
    }
}