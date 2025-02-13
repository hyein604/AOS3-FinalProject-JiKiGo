package com.protect.jikigo.ui.home.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.protect.jikigo.databinding.FragmentNewsEnvironmentBinding


class NewsEnvironmentFragment : Fragment() {
    private var _binding: FragmentNewsEnvironmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsEnvironmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayout = binding.tabNewsEnvorionment

        // Tab 동적 추가
        tabLayout.addTab(tabLayout.newTab().setText("전체"))
        tabLayout.addTab(tabLayout.newTab().setText("대기"))
        tabLayout.addTab(tabLayout.newTab().setText("물"))
        tabLayout.addTab(tabLayout.newTab().setText("생태계"))
        tabLayout.addTab(tabLayout.newTab().setText("정책"))
        setLayout()
    }

    private fun setLayout() {
        onClickToolbar()
    }

    private fun onClickToolbar() {
        binding.toolbarNewsEnvorionment.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}