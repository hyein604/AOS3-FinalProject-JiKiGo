package com.protect.jikigo.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.protect.jikigo.databinding.FragmentWebViewBinding

class WebViewFragment : Fragment() {
    private var _binding: FragmentWebViewBinding? = null
    private val binding get() = _binding!!
    private val args: WebViewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebViewBinding.inflate(inflater, container, false)
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
        moveToWebSite()
    }

    private fun moveToWebSite() {
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.webViewClient = WebViewClient()
        binding.webView.loadUrl(args.urlMethod.url)
        Log.d("TEST", args.urlMethod.url)
    }

}