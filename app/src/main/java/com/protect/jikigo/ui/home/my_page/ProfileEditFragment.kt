package com.protect.jikigo.ui.home.my_page

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.protect.jikigo.databinding.FragmentProfileEditBinding


class ProfileEditFragment : Fragment() {
    private var _binding: FragmentProfileEditBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileEditViewModel by viewModels()

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
        onClickToolbar()
        pickImage()
        setObserve()
    }

    private fun onClickToolbar() {
        binding.toolbarProfileEdit.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun pickImage() {
        val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            viewModel.setImageUri(uri)
        }

        binding.ivProfileEditImage.setOnClickListener {
            galleryLauncher.launch("image/*")
        }
    }

    private fun setObserve() {
        viewModel.imageUri.observe(viewLifecycleOwner) { uri ->
            binding.ivProfileEditImage.setImageURI(uri)
        }

    }
}