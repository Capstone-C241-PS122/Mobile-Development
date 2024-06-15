package com.grassterra.fitassist.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.fragment.app.DialogFragment
import com.grassterra.fitassist.R

class FullScreenDialogFragment : DialogFragment() {

    private lateinit var videoUri: Uri

    companion object {
        private const val VIDEO_URI_KEY = "video_uri_key"

        fun newInstance(videoUri: Uri): FullScreenDialogFragment {
            val fragment = FullScreenDialogFragment()
            val args = Bundle()
            args.putParcelable(VIDEO_URI_KEY, videoUri)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        videoUri = requireArguments().getParcelable(VIDEO_URI_KEY)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dialog, container, false)
        val videoView: VideoView = view.findViewById(R.id.videoViewFullScreen)

        videoView.setVideoURI(videoUri)
        videoView.setMediaController(android.widget.MediaController(requireContext()))
        videoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.start()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }
}