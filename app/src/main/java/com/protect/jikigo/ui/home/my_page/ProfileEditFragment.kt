package com.protect.jikigo.ui.home.my_page

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.protect.jikigo.R
import com.protect.jikigo.databinding.FragmentProfileEditBinding
import com.protect.jikigo.utils.getUserId
import com.protect.jikigo.utils.showDialog
import com.protect.jikigo.ui.viewModel.ProfileEditViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileEditFragment : Fragment() {
    private var _binding: FragmentProfileEditBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileEditViewModel by viewModels()

    private lateinit var userId : String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileEditBinding.inflate(inflater,container,false)
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
        pickImage()
        observe()
        checkData()
        settingText()
    }
    // 시작 시 확인할 데이터
    private fun checkData() {
        lifecycleScope.launch {
            userId = requireContext().getUserId() ?: ""
            viewModel.loadProfile(userId)
        }
    }

    private fun onClickListener() {
        binding.apply {
            toolbarProfileEdit.setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            toolbarProfileEdit.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_profile_edit_done -> {
                        viewModel.isUsedNickName(etProfileEditName.text.toString())
                    }
                }
                true
            }

            btnProfileEditDelete.setOnClickListener {
                viewModel.checkChange()
                Glide.with(requireContext())
                    .load("https://www.studiopeople.kr/common/img/default_profile.png")
                    .circleCrop()
                    .into(ivProfileEditImage)
            }
        }
    }

    private fun settingText() {
        binding.apply {
            etProfileEditName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    s?.let {
                        val filteredText= it.toString().replace(" ", "")  // 공백 제거
                        if (it.toString() != filteredText) {
                            etProfileEditName.setText(filteredText)
                            etProfileEditName.setSelection(filteredText.length)  // 커서 위치 유지
                        }
                        tvProfileEditNameCount.text = "${filteredText.length}/10"  // 글자 수 표시
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    // 사진을 눌렀을 때 고르기
    private fun pickImage() {
        val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if(uri != null) {
                viewModel.setImageUri(uri)
            }
        }

        binding.ivProfileEditImage.setOnClickListener {
            galleryLauncher.launch("image/*")
        }
    }
    // 옵저버
    private fun observe() {
        binding.apply {
            viewModel.profile.observe(viewLifecycleOwner) { profile ->
                etProfileEditName.setText("${profile.userNickName}")
                Glide.with(requireContext())
                    .load(profile.userProfileImg)
                    .circleCrop()
                    .into(ivProfileEditImage)
            }

            viewModel.imageUri.observe(viewLifecycleOwner) {
                Glide.with(requireContext())
                    .load(it)
                    .circleCrop()
                    .into(ivProfileEditImage)
            }

            viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
                when(loading) {
                    false -> {
                        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        binding.layoutProfileEditLoading.visibility = View.GONE
                        findNavController().navigateUp()
                    }
                    true -> {
                        requireActivity().window.setFlags(
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        )
                        binding.layoutProfileEditLoading.visibility = View.VISIBLE
                    }
                }
            }

            viewModel.isUsedLoading.observe(viewLifecycleOwner) { loading ->
                when(loading) {
                    false -> {
                        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        binding.layoutProfileEditLoading.visibility = View.GONE
                    }
                    true -> {
                        requireActivity().window.setFlags(
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        )
                        binding.layoutProfileEditLoading.visibility = View.VISIBLE
                    }
                }
            }

            viewModel.isUsed.observe(viewLifecycleOwner) {
                when(it) {
                    false -> {
                        viewModel.saveProfile(userId, etProfileEditName.text.toString())
                    }
                    true -> {
                        requireContext().showDialog("닉네임 중복 확인", "중복된 닉네임입니다", "확인") {}
                    }
                }
            }
        }
    }
}