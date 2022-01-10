package com.acdetorres.app.dashboard.fragments

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.acdetorres.app.MainActivity
import com.acdetorres.app.R
import com.acdetorres.app.databinding.FragmentSelectedTrackBinding
import timber.log.Timber
import android.content.Intent
import android.net.Uri


class FragmentSelectedTrack : Fragment() {

    lateinit var binding : FragmentSelectedTrackBinding

    val args by navArgs<FragmentSelectedTrackArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSelectedTrackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvWrapperType.text = args.wrapperType

        binding.tvTrackname.text = args.trackName

        binding.tvGenre.text = args.genre

        binding.tvPrice.text = "$${args.price}"

        binding.tvDescription.text = args.description

        binding.btnBuy.text = "Buy($${args.price})"

        binding.icClose.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnBuy.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(args.trackUrl)
            startActivity(i)
        }


        val webView = binding.wvVideoPlayer

        val link = args.previewUrl

        if (link.isNotBlank()) {
            webView.settings.javaScriptEnabled = true
            webView.settings.loadWithOverviewMode = true;
            webView.settings.useWideViewPort = true;
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    webView.loadUrl(url!!)
                    return true
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                }
            }
            webView.loadUrl(link)
            Timber.e(link)
        }
    }
}