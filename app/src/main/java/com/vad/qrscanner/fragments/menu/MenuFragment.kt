package com.vad.qrscanner.fragments.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vad.qrscanner.R

class MenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerMenu)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        val listTitle: Array<String> = resources.getStringArray(R.array.item_menu)
        val listIcon = listOf(
            resources.getDrawable(R.drawable.ic_baseline_qr_code_scanner_24),
            resources.getDrawable(R.drawable.ic_baseline_contact_page_24),
            resources.getDrawable(R.drawable.ic_baseline_text_fields_24),
            resources.getDrawable(R.drawable.ic_baseline_edit_location_24)
        )

        val list = listIcon.zip(listTitle).toList()

        val adapter = MenuAdapter(list)
        recyclerView.adapter = adapter
    }
}