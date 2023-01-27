package com.vad.qrscanner.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.vad.qrscanner.R
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
        result.setText(arguments?.getString("content"))

        val copy = view.findViewById(R.id.copyImageView) as ImageView

        copy.setOnClickListener {
            val clipboard: ClipboardManager? =
                thisContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?

            val clip = ClipData.newPlainText("copyLable", result.text.toString())
            clipboard!!.setPrimaryClip(clip)

            Toast.makeText(thisContext, thisContext.getText(R.string.textCopied), Toast.LENGTH_SHORT).show()
        }
    }

    override fun getTitle(): Int = R.string.fragment_result
}