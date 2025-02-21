package com.protect.jikigo.ui.home.noti

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.protect.jikigo.databinding.FragmentNotificationDetailBinding

class NotificationDetailFragment : Fragment() {
    private var _binding: FragmentNotificationDetailBinding? = null
    private val binding get() = _binding!!
    private val args: NotificationDetailFragmentArgs by navArgs() // SafeArgs를 통해 데이터 받기

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
        displayNotificationDetails()
    }

    private fun setLayout() {
        onClickToolbar()
    }

    private fun onClickToolbar() {
        binding.toolbarNotificationDetail.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
    private fun displayNotificationDetails() {
        val notification = args.notification
        binding.tvNotificationDetailContentTitle.text = notification.title
        binding.tvNotificationDetailDate.text = notification.date
        binding.tvNotificationDetailContent.text = notification.content.replace("\\n", "\n")

        // 이미지가 있으면 로드
        if (!notification.image.isNullOrEmpty()) {
            Glide.with(this)
                .load(notification.image)
                .into(binding.ivNotificationDetailImage)
        } else {
            binding.ivNotificationDetailImage.visibility = View.GONE
        }
    }
}