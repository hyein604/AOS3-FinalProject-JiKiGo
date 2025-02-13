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
        Notification("공지사항 2", "2025.02.09"),
        Notification("공지사항 3", "2025.02.08")
    )

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
    }

    private fun setupRecyclerView() {
        binding.rvNotification.layoutManager = LinearLayoutManager(requireContext()) // 추가된 코드
        adapter = NotificationAdapter(notificationList) { selectedItem ->
            // 클릭 시 이동 처리
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
}