package com.vad.qrscanner.fragments.menu

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.vad.qrscanner.Common
import com.vad.qrscanner.CustomScannerActivity
import com.vad.qrscanner.R
import com.vad.qrscanner.fragments.*
import com.vad.qrscanner.navigation.HasCustomTitle
import com.vad.qrscanner.navigation.Navigator

class MenuFragment : Fragment(), HasCustomTitle {

    private lateinit var navigator: Navigator
    private lateinit var common: Common

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigator = activity as Navigator
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_menu, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        common = Common.getInstance()

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerMenu)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        val listTitle: Array<String> = resources.getStringArray(R.array.item_menu)
        val listIcon = listOf(
            resources.getDrawable(R.drawable.ic_baseline_qr_code_scanner_24),
            resources.getDrawable(R.drawable.ic_baseline_image_24),
            resources.getDrawable(R.drawable.ic_baseline_contact_page_24),
            resources.getDrawable(R.drawable.ic_baseline_text_fields_24),
            resources.getDrawable(R.drawable.ic_baseline_edit_location_24),
            resources.getDrawable(R.drawable.ic_baseline_wifi_24),
            resources.getDrawable(R.drawable.ic_baseline_email_24),
            resources.getDrawable(R.drawable.ic_baseline_sms_24),
            resources.getDrawable(R.drawable.ic_baseline_phone_24)
        )

        val list = listIcon.zip(listTitle).toList()

        val adapter = MenuAdapter(list)
        recyclerView.adapter = adapter

        adapter.setOnItemMenuClickListener(object: MenuAdapter.OnItemMenuClickListener{
            override fun onClick(position: Int) {
                getFragments(position)
            }
        })
    }

    fun getFragments(id: Int) {
        when (id) {
            0 -> startQrScanner()
            1 -> readImage()
            2 -> navigator.startFragment(PhoneFragmentGeneration())
            3 -> navigator.startFragment(TextFragmentGeneration())
            4 -> navigator.startFragment(LocationFragmentGeneration())
            5 -> navigator.startFragment(WifiFragment())
            6 -> navigator.startFragment(EmailFragment())
            7 -> navigator.startFragment(SmsFragment())
            8 -> navigator.startFragment(PhoneFragment())
        }
    }

    private fun readImage() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            common.start()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                common.start()
            } else {
                Toast.makeText(context, "Permission not granted", Toast.LENGTH_SHORT).show()
            }
        }

    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        startResult(result)
    }

    private fun startQrScanner() {
        val scanOptions = ScanOptions()
        scanOptions.setOrientationLocked(true)
        scanOptions.setPrompt("")
        scanOptions.setBarcodeImageEnabled(true)
        scanOptions.captureActivity = CustomScannerActivity::class.java
        barcodeLauncher.launch(scanOptions)
    }

    private fun startResult(scanIntentResult: ScanIntentResult) {
        val args = Bundle()
        args.putString("content", scanIntentResult.contents)
        args.putString("temp_image", scanIntentResult.barcodeImagePath)
        val fragmentResult: Fragment = ResultFragment()
        fragmentResult.arguments = args
        navigator.startFragment(fragmentResult)
    }

    override fun getTitle() = R.string.menu
}