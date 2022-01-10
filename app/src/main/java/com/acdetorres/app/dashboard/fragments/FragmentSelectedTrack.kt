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
import androidx.activity.OnBackPressedCallback
import com.acdetorres.app.di.shared_pref.SharedPref
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class FragmentSelectedTrack : Fragment() {

    //View binding
    lateinit var binding : FragmentSelectedTrackBinding

    //Navigation arguments
    val args by navArgs<FragmentSelectedTrackArgs>()

    //Directly injects shared pref to fragment since it's small data. This should be on the repository though.
    @Inject
    lateinit var sharedPref : SharedPref

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

        //Setups the views
        binding.tvWrapperType.text = args.wrapperType

        binding.tvTrackname.text = args.trackName

        binding.tvGenre.text = args.genre

        binding.tvPrice.text = "$${args.price}"

        binding.tvDescription.text = args.description

        binding.btnBuy.text = "Buy($${args.price})"

        //Navigates up and clear the last selected track
        binding.icClose.setOnClickListener {
            findNavController().navigateUp()
            sharedPref.clearLastSelectedTrack()
        }

        //Handles back press to clear the Last Selected Track on back press
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    sharedPref.clearLastSelectedTrack()
                    findNavController().navigateUp()
                }
            }
        )



        //Opens the trackUrl on browser
        binding.btnBuy.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(args.trackUrl)
            startActivity(i)
        }


        //Loads the preview URL on web view
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