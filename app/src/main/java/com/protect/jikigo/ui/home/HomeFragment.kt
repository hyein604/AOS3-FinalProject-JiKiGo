package com.protect.jikigo.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.protect.jikigo.R
import com.protect.jikigo.data.Storage
import com.protect.jikigo.data.Store
import com.protect.jikigo.databinding.FragmentHomeBinding
import com.protect.jikigo.ui.HomeAdapter
import com.protect.jikigo.ui.HomeStoreItemClickListener
import com.protect.jikigo.ui.extensions.applyNumberFormat
import com.protect.jikigo.ui.extensions.spannable
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
                    requireContext().toast(messages[index])
                    val action = HomeFragmentDirections.actionNavigationHomeToNewsEnvironment()
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
            val action = HomeFragmentDirections.actionNavigationHomeToTravel()
            findNavController().navigate(action)
        }
    }

    private fun homeTextSpannable() {
        // 닉네임의 길이를 넣어줌
        binding.tvHomeNickname.spannable(0, 3, 3, Color.WHITE)
        binding.tvHomeClickRank.spannable(binding.tvHomeClickRank.length(), 0, 0, Color.BLACK)
    }

    // 데이테 베이스 생성시 삭제되는 메서드
    private fun tempMethod() {
        val notiList = Storage.notiList
        with(binding) {
            // 유저 포인트
            tvHomePoint.applyNumberFormat(3456)
            // 공지사항
            tvHomeNotice1.text = notiList[0]
            tvHomeNotice2.text = notiList[1]
            tvHomeNotice3.text = notiList[2]
        }
    }

    private fun moveToDetailNoti() {
        with(binding) {
            listOf(tvHomeNotice1, tvHomeNotice2, tvHomeNotice3).forEach { textView ->
                textView.setOnClickListener {
                    requireContext().toast(textView.text.toString())
                    val action = HomeFragmentDirections.actionNavigationHomeToNotificationDetail()
                    findNavController().navigate(action)
                }
            }
        }
    }


    private fun setRecyclerView() {
        val storeList = Storage.storeList
        val adapter = HomeAdapter(storeList, this)
        binding.rvHomeStore.adapter = adapter
    }

    // store 이동 리스너
    override fun onClickStore(store: Store) {
        Toast.makeText(requireContext(), store.title, Toast.LENGTH_SHORT).show()
    }
}