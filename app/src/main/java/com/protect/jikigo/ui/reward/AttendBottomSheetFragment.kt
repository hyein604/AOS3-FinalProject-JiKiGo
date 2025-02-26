package com.protect.jikigo.ui.reward

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.protect.jikigo.databinding.FragmentAttendBottomSheetBinding
import com.protect.jikigo.ui.home.my_page.CouponDetailFragmentArgs


class AttendBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentAttendBottomSheetBinding? = null
    private val binding get() = _binding!!
    private val args: AttendBottomSheetFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAttendBottomSheetBinding.inflate(inflater, container, false)
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
        onClickListener()
        setText()
    }

    private fun onClickListener() {
        binding.apply {
            btnAttendBottomSheetDone.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun setText() {
        binding.tvAttendBottomSheetPoint.text = "+${args.attendPoint}P"
    }
}