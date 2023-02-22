package com.vad.qrscanner.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import com.vad.qrscanner.R
import com.vad.qrscanner.domain.CheckEmptyText
import com.vad.qrscanner.navigation.CustomAction
import com.vad.qrscanner.navigation.HasCustomAction
import com.vad.qrscanner.navigation.HasCustomTitle
import com.vad.qrscanner.navigation.Navigator


class WifiFragment : Fragment(), HasCustomTitle, HasCustomAction {

    private lateinit var textName: EditText
    private lateinit var textPassword: EditText
    private lateinit var hidden: CheckBox
    private lateinit var type: String
    private var isPassword = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wifi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arrayType = resources.getStringArray(R.array.type)
        type = arrayType.get(0)
        val adapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, arrayType)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        textName = view.findViewById(R.id.editTextNameWifi)
        textPassword = view.findViewById(R.id.editTextPasswordWifi)
        hidden = view.findViewById(R.id.hidden)

        val spinner = view.findViewById<Spinner>(R.id.spinner)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = (object : OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                type = arrayType.get(position)
                isPassword = position == 2
                textPassword.isEnabled = position != 2

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        })
    }

    override fun setCustomAction(navigator: Navigator) = CustomAction(R.drawable.ic_baseline_done_24) {
            CheckEmptyText.check(requireContext().getString(R.string.required), textName, textPassword) {
                val hid = if(hidden.isChecked)  resources.getString(R.string.hidden) else ""

                val password =
                    if (!isPassword)
                        "${resources.getString(R.string.password)}: ${textPassword.text}\n"
                    else
                        ""

                val str = "${resources.getString(R.string.ssid_network_name)}: ${textName.text}\n" +
                        password +
                        "\n$type\n$hid"

                val bundle = Bundle()
                bundle.putString("result_text", str)
                val fragment: Fragment = ResultQrFragment()
                fragment.arguments = bundle
                navigator.hide()
                navigator.startFragment(fragment)
            }
    }

    override fun getTitle() = R.string.wifi
}