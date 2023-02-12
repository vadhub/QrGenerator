package com.vad.qrscanner.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.vad.qrscanner.R
import com.vad.qrscanner.domain.CheckEmptyText
import com.vad.qrscanner.navigation.CustomAction
import com.vad.qrscanner.navigation.HasCustomAction
import com.vad.qrscanner.navigation.HasCustomTitle
import com.vad.qrscanner.navigation.Navigator


class PhoneFragment : Fragment(), HasCustomTitle, HasCustomAction {

    private lateinit var phone: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_phone, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        phone = view.findViewById(R.id.editTextPhone1)
    }

    override fun setCustomAction(navigator: Navigator) = CustomAction(R.drawable.ic_baseline_done_24) {
        CheckEmptyText.check(requireContext().getString(R.string.required), phone) {
            val str = "${getString(R.string.phone_number)}: ${phone.text}, "

            val bundle = Bundle()
            bundle.putString("result_text", str)
            val fragment: Fragment = ResultQrFragment()
            fragment.arguments = bundle
            navigator.hide()
            navigator.startFragment(fragment)
        }
    }

    override fun getTitle() = R.string.phone_number

}