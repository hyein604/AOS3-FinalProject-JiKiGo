package com.protect.jikigo.ui.rank.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.protect.jikigo.databinding.DialogRankingBinding

class RankingHelpDialog : DialogFragment() {

    private var _binding: DialogRankingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogRankingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pagerAdapter = RankingBannerAdapter(this)
        binding.vpDialogRanking.adapter = pagerAdapter

        // 인디케이터 연결
        TabLayoutMediator(binding.indicatorDialogRainking, binding.vpDialogRanking) { _, _ -> }.attach()

        // 닫기 버튼
        binding.ivDialogRankingCloseDialog.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext()).apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경을 투명하게 설정
        }
    }

}
