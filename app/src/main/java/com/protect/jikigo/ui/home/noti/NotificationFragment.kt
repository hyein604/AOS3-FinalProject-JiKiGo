package com.protect.jikigo.ui.home.noti

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.protect.jikigo.databinding.FragmentNotificationBinding
import com.protect.jikigo.ui.adapter.NotificationAdapter
import com.protect.jikigo.ui.viewModel.NotificationViewModel

class NotificationFragment : Fragment() {
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotificationViewModel by viewModels()
    private lateinit var adapter: NotificationAdapter

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
        observeViewModel()
    }

    private fun setLayout() {
        setupRecyclerView()
        onClickToolbar()
        setupSearch()
    }

    private fun setupRecyclerView() {
        binding.rvNotification.layoutManager = LinearLayoutManager(requireContext())

        viewModel.filteredList.observe(viewLifecycleOwner) { list ->
            adapter = NotificationAdapter(list) { selectedItem ->
                val action = NotificationFragmentDirections.actionNotificationToNotificationDetail(selectedItem)
                findNavController().navigate(action)
            }
            binding.rvNotification.adapter = adapter
        }
    }

    private fun onClickToolbar() {
        binding.toolbarNotification.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupSearch() {
        binding.ivSearchIcon.setOnClickListener {
            val query = binding.etSearch.text.toString().trim()
            viewModel.performSearch(query)
        }

        binding.tvNotificationViewAll.setOnClickListener {
            viewModel.resetSearch()
            binding.etSearch.setText("")  // 검색어 초기화
        }
    }

    private fun observeViewModel() {
        viewModel.isSearchResultVisible.observe(viewLifecycleOwner) { isVisible ->
            binding.tvNotificationSearchResult.visibility = if (isVisible) View.VISIBLE else View.GONE
        }

        viewModel.isViewAllVisible.observe(viewLifecycleOwner) { isVisible ->
            binding.tvNotificationViewAll.visibility = if (isVisible) View.VISIBLE else View.GONE
        }
    }
}
