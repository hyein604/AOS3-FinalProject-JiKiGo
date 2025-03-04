package com.protect.jikigo.ui.home.noti

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.protect.jikigo.databinding.FragmentNotificationBinding
import com.protect.jikigo.ui.adapter.NotificationAdapter
import com.protect.jikigo.ui.viewModel.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : Fragment() {
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotificationViewModel by activityViewModels()
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
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        binding.ivSearchIcon.setOnClickListener {
            val query = binding.etSearch.text.toString().trim()
            viewModel.performSearch(query)

            // 🔽 키보드 내리기
            inputMethodManager.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
        }

        binding.etSearch.setOnEditorActionListener { _, _, _ ->
            val query = binding.etSearch.text.toString().trim()
            viewModel.performSearch(query)

            // 🔽 키보드 내리기
            inputMethodManager.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
            true
        }

        binding.tvNotificationViewAll.setOnClickListener {
            viewModel.resetSearch()
            binding.etSearch.setText("")  // 검색어 초기화
        }
    }


    private fun observeViewModel() {
        viewModel.searchResultCount.observe(viewLifecycleOwner) { count ->
            if (viewModel.hasSearched.value == true) {
                binding.tvNotificationSearchResult.text = "총 ${count}건의 검색결과가 있습니다."
                binding.tvNotificationSearchResult.visibility = View.VISIBLE
            } else {
                binding.tvNotificationSearchResult.visibility = View.GONE
            }
        }

        viewModel.isSearchResultVisible.observe(viewLifecycleOwner) { isVisible ->
            binding.tvNoSearchResult.visibility = if (!isVisible && viewModel.searchResultCount.value == 0 && viewModel.hasSearched.value == true) View.VISIBLE else View.GONE
        }

        viewModel.isViewAllVisible.observe(viewLifecycleOwner) { isVisible ->
            binding.tvNotificationViewAll.visibility = if (viewModel.hasSearched.value == true) View.VISIBLE else View.GONE
        }

        binding.tvNotificationViewAll.setOnClickListener {
            viewModel.resetSearch()
            binding.etSearch.setText("")
            binding.tvNotificationSearchResult.visibility = View.GONE
            binding.tvNotificationViewAll.visibility = View.GONE
        }
    }
}
