package com.protect.jikigo.ui.home.noti

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.protect.jikigo.data.Notification
import com.protect.jikigo.databinding.FragmentNotificationBinding
import com.protect.jikigo.ui.adapter.NotificationAdapter

class NotificationFragment : Fragment() {
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: NotificationAdapter

    // 공지사항 데이터
    private val notificationList = listOf(
        Notification("공지사항 1", "2025.02.10"),
        Notification("중요 공지", "2025.02.09"),
        Notification("새로운 업데이트", "2025.02.08")
    )

    // 검색된 결과를 저장할 리스트 (초기값은 전체 공지사항)
    private var filteredList = notificationList.toList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
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
        setupRecyclerView() // RecyclerView 설정
        onClickToolbar() // 툴바 이벤트 설정
        setupSearch() // 검색 기능 설정
    }

    // RecyclerView 설정 함수
    private fun setupRecyclerView() {
        binding.rvNotification.layoutManager = LinearLayoutManager(requireContext())
        adapter = NotificationAdapter(filteredList) { selectedItem ->
            // 공지사항 클릭 시 상세 페이지로 이동
            val action = NotificationFragmentDirections.actionNotificationToNotificationDetail()
            findNavController().navigate(action)
        }
        binding.rvNotification.adapter = adapter
    }

    // 툴바의 뒤로가기 버튼 클릭 이벤트 설정
    private fun onClickToolbar() {
        binding.toolbarNotification.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    // 검색 기능 설정 함수
    private fun setupSearch() {
        // 검색 버튼 클릭 시 검색 수행
        binding.ivSearchIcon.setOnClickListener {
            val query = binding.etSearch.text.toString().trim()
            performSearch(query)
        }

        // "전체보기" 버튼 클릭 시 검색 초기화
        binding.tvNotificationViewAll.setOnClickListener {
            resetSearch()
        }
    }

    // 검색 기능 수행 함수
    private fun performSearch(query: String) {
        // 검색어가 포함된 공지사항만 필터링
        filteredList = if (query.isNotEmpty()) {
            notificationList.filter { it.title.contains(query, ignoreCase = true) }
        } else {
            notificationList
        }

        updateRecyclerView() // 검색 결과 적용
    }

    // 검색 초기화 함수 (전체 공지사항 목록으로 복원)
    private fun resetSearch() {
        binding.etSearch.setText("")  // 검색어 초기화
        filteredList = notificationList // 전체 목록 복원
        updateRecyclerView() // RecyclerView 갱신
        binding.tvNotificationSearchResult.visibility = View.GONE // 검색 결과 텍스트 숨김
        binding.tvNotificationViewAll.visibility = View.GONE // "전체보기" 버튼 숨김
    }

    // RecyclerView 갱신 함수
    private fun updateRecyclerView() {
        adapter = NotificationAdapter(filteredList) { selectedItem ->
            val action = NotificationFragmentDirections.actionNotificationToNotificationDetail()
            findNavController().navigate(action)
        }
        binding.rvNotification.adapter = adapter

        // 검색 결과에 따른 UI 업데이트
        if (filteredList.isNotEmpty()) {
            binding.tvNotificationSearchResult.text = "총 ${filteredList.size}건의 검색결과가 있습니다."
            binding.tvNotificationSearchResult.visibility = View.VISIBLE
            binding.tvNotificationViewAll.visibility = View.VISIBLE
        } else {
            binding.tvNotificationSearchResult.text = "총 0건의 검색결과가 있습니다."
            binding.tvNotificationSearchResult.visibility = View.VISIBLE
            binding.tvNotificationViewAll.visibility = View.VISIBLE
        }
    }
}
