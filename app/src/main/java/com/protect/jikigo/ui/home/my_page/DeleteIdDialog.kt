package com.protect.jikigo.ui.home.my_page

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.protect.jikigo.ui.activity.LoginActivity
import com.protect.jikigo.R
import com.protect.jikigo.databinding.DialogDeleteIdBinding
import com.protect.jikigo.utils.clearUserId
import com.protect.jikigo.utils.getUserId
import com.protect.jikigo.ui.viewModel.DeleteIdViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DeleteIdDialog : DialogFragment() {

    private var _binding: DialogDeleteIdBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DeleteIdViewModel by viewModels()

    private lateinit var userId : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogDeleteIdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
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

    private fun setLayout() {
        observe()
        checkData()
        onClickListener()
    }

    private fun checkData() {
        lifecycleScope.launch {
            userId = requireContext().getUserId() ?: ""
        }
    }

    private fun observe() {
        binding.apply {
            viewModel.isAgreementChecked.observe(viewLifecycleOwner) {
                val checkColor = if (it) {
                    R.color.primary
                } else {
                    R.color.gray_10
                }
                binding.imbDeleteIdAgree.setColorFilter(getColor(requireContext(), checkColor))

                val buttonColor = if (it) {
                    R.color.gray_100
                } else {
                    R.color.gray_10
                }
                binding.btnDeleteIdDelete.setTextColor(getColor(requireContext(), buttonColor))

                binding.btnDeleteIdDelete.isEnabled = it

            }

            viewModel.isLoading.observe(viewLifecycleOwner) {
                when (it) {
                    true -> {
                        requireActivity().window.setFlags(
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        )
                        layoutDeleteIdLoading.isVisible = true
                    }

                    false -> {
                        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        layoutDeleteIdLoading.isVisible = false
                        // 로그인 화면으로 넘김
                        viewLifecycleOwner.lifecycleScope.launch {
                            requireContext().clearUserId()
                            requireActivity().finish()
                            val intent = Intent(requireContext(), LoginActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    private fun onClickListener() {
        binding.apply {
            btnDeleteIdDelete.setOnClickListener {
                viewModel.startLoading()
                viewModel.deleteId(userId)
            }

            btnDeleteIdCancel.setOnClickListener {
                dismiss()
            }

            imbDeleteIdAgree.setOnClickListener {
                if (viewModel.isAgreementChecked.value == true) {
                    viewModel.agreementChecked(false)
                }
                else {
                    viewModel.agreementChecked(true)
                }
            }
        }
    }
}