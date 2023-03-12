package com.vad.qrscanner.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.vad.qrscanner.R
import com.vad.qrscanner.domain.CheckLink
import com.vad.qrscanner.navigation.HasCustomTitle


class ResultFragment : Fragment(), HasCustomTitle {

    private lateinit var thisContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.thisContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val result = view.findViewById(R.id.resultQr) as EditText
        val copy = view.findViewById(R.id.copyImageView) as ImageView
        val imageReturn = view.findViewById(R.id.captureResult) as ImageView
        val search = view.findViewById(R.id.searchImageView) as ImageView
        val share = view.findViewById(R.id.shareImageView) as ImageView

        val temp = arguments?.getString("temp_image")
        val bmImg = BitmapFactory.decodeFile(temp)
        imageReturn.setImageBitmap(bmImg)
        result.setText(arguments?.getString("content"))


        if (CheckLink.checkLink(result.text.toString())) {
            val link = CheckLink.extractLink(result.text.toString())
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(resources.getString(R.string.goto_link))
            builder.setMessage(resources.getString(R.string.folow_link) + link)
            builder.setPositiveButton(android.R.string.ok) { dialog, which ->
                try {
                    val browserIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://${link}")
                    )
                    startActivity(browserIntent)
                } catch (e: java.lang.Exception) {
                }
            }

            builder.setNegativeButton(android.R.string.cancel) { dialog, which ->

            }

            builder.show()
        }

        copy.setOnClickListener {
            val clipboard: ClipboardManager? =
                thisContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?

            val clip = ClipData.newPlainText("copyLable", result.text.toString())
            clipboard!!.setPrimaryClip(clip)

            Toast.makeText(
                thisContext,
                thisContext.getText(R.string.textCopied),
                Toast.LENGTH_SHORT
            ).show()
        }

        search.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com/search?q=${result.text}")
            )
            startActivity(browserIntent)
        }

        share.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, result.text)
            startActivity(Intent.createChooser(intent, resources.getString(R.string.share)))
        }

    }

    override fun getTitle(): Int = R.string.fragment_result
}