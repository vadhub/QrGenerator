package com.vad.qrscanner.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.vad.qrscanner.R

class DialogFragment : androidx.fragment.app.DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_result_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val textOk = view.findViewById<TextView>(R.id.ok)
        textOk.setOnClickListener {
            dismiss()
        }
    }

}