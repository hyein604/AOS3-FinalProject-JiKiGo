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
    private val notificationList = listOf(
        Notification("공지사항 1", "2025.02.10"),
        Notification("중요 공지", "2025.02.09"),
        Notification("새로운 업데이트", "2025.02.08")
    )

    private var filteredList = notificationList.toList() // 검색된 목록 저장용

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
        setupRecyclerView()
        onClickToolbar()
        setupSearch()  // 🔍 검색 기능 추가
    }

    private fun setupRecyclerView() {
        binding.rvNotification.layoutManager = LinearLayoutManager(requireContext())
        adapter = NotificationAdapter(filteredList) { selectedItem ->
            val action =
                NotificationFragmentDirections.actionNotificationToNotificationDetail()
            findNavController().navigate(action)
        }
        binding.rvNotification.adapter = adapter
    }

    private fun onClickToolbar() {
        binding.toolbarNotification.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupSearch() {
        binding.ivSearchIcon.setOnClickListener {
            val query = binding.etSearch.text.toString().trim()
            performSearch(query)
        }

        binding.tvNotificationViewAll.setOnClickListener {
            resetSearch()
        }
    }

    private fun performSearch(query: String) {
        filteredList = if (query.isNotEmpty()) {
            notificationList.filter { it.title.contains(query, ignoreCase = true) }
        } else {
            notificationList
        }

        updateRecyclerView()
    }

    private fun resetSearch() {
        binding.etSearch.setText("")  // 검색어 초기화
        filteredList = notificationList // 전체 목록 복원
        updateRecyclerView()
        binding.tvNotificationSearchResult.visibility = View.GONE
        binding.tvNotificationViewAll.visibility = View.GONE
    }

    private fun updateRecyclerView() {
        adapter = NotificationAdapter(filteredList) { selectedItem ->
            val action =
                NotificationFragmentDirections.actionNotificationToNotificationDetail()
            findNavController().navigate(action)
        }
        binding.rvNotification.adapter = adapter

        // 검색 결과 텍스트 업데이트
        if (filteredList.isNotEmpty()) {
            binding.tvNotificationSearchResult.text = "총 ${filteredList.size}건의 검색결과가 있습니다."
            binding.tvNotificationSearchResult.visibility = View.VISIBLE
            binding.tvNotificationViewAll.visibility = View.VISIBLE
        } else {
            binding.tvNotificationSearchResult.text = "총 0건의 검색결과가 있습니다."
            binding.tvNotificationSearchResult.visibility = View.VISIBLE
            binding.tvNotificationViewAll.visibility = View.GONE
        }
    }
}
