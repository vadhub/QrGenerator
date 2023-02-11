package com.vad.qrscanner.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.vad.qrscanner.R
import com.vad.qrscanner.domain.CheckEmptyText
import com.vad.qrscanner.navigation.CustomAction
import com.vad.qrscanner.navigation.HasCustomAction
import com.vad.qrscanner.navigation.HasCustomTitle
import com.vad.qrscanner.navigation.Navigator

class EmailFragment : Fragment(), HasCustomTitle, HasCustomAction {

    private lateinit var email: EditText
    private lateinit var subject: EditText
    private lateinit var body: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gmail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        email = view.findViewById(R.id.editTextEmail)
        subject = view.findViewById(R.id.editTextSubject)
        body = view.findViewById(R.id.editTextBody)
    }

    override fun getTitle() = R.string.email

    override fun setCustomAction(navigator: Navigator) = CustomAction(R.drawable.ic_baseline_done_24) {
        CheckEmptyText.check(requireContext().getString(R.string.required), email, subject, body) {
            val str = "${getString(R.string.email)}: ${email.text}, " +
                    "${getString(R.string.subject)}: ${subject.text}, " +
                    "${getString(R.string.body)}: ${body.text}"

            val bundle = Bundle()
            bundle.putString("result_text", str)
            val fragment: Fragment = ResultQrFragment()
            fragment.arguments = bundle
            navigator.hide()
            navigator.startFragment(fragment)
        }
    }

}