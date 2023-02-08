package com.vad.qrscanner.fragments.menu

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.vad.qrscanner.CustomScannerActivity
import com.vad.qrscanner.R
import com.vad.qrscanner.fragments.*
import com.vad.qrscanner.navigation.HasCustomTitle
import com.vad.qrscanner.navigation.Navigator

class MenuFragment : Fragment(), HasCustomTitle {

    private lateinit var navigator: Navigator

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
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerMenu)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        val listTitle: Array<String> = resources.getStringArray(R.array.item_menu)
        val listIcon = listOf(
            resources.getDrawable(R.drawable.ic_baseline_qr_code_scanner_24),
            resources.getDrawable(R.drawable.ic_baseline_contact_page_24),
            resources.getDrawable(R.drawable.ic_baseline_text_fields_24),
            resources.getDrawable(R.drawable.ic_baseline_edit_location_24),
            resources.getDrawable(R.drawable.ic_baseline_wifi_24)
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
            1 -> navigator.startFragment(PhoneFragmentGeneration())
            2 -> navigator.startFragment(TextFragmentGeneration())
            3 -> navigator.startFragment(LocationFragmentGeneration())
            4 -> navigator.startFragment(WifiFragment())
        }
    }

    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents != null) {
            startResult(result.contents)
        }
    }

    private fun startQrScanner() {
        val scanOptions = ScanOptions()
        scanOptions.setOrientationLocked(true)
        scanOptions.setPrompt("")
        scanOptions.captureActivity = CustomScannerActivity::class.java
        barcodeLauncher.launch(scanOptions)
    }

    private fun startResult(content: String) {
        val args = Bundle()
        args.putString("content", content)
        val fragmentResult: Fragment = ResultFragment()
        fragmentResult.arguments = args
        navigator.startFragment(fragmentResult)
    }

    override fun getTitle() = R.string.menu
}