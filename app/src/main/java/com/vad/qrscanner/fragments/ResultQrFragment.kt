package com.vad.qrscanner.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.vad.qrscanner.R
import com.vad.qrscanner.domain.QRTools
import com.vad.qrscanner.navigation.CustomAction
import com.vad.qrscanner.navigation.HasCustomAction
import com.vad.qrscanner.navigation.HasCustomTitle
import com.vad.qrscanner.navigation.Navigator
import dev.sasikanth.colorsheet.ColorSheet

class ResultQrFragment : Fragment(), HasCustomTitle, HasCustomAction {

    private var bitmapQr: Bitmap? = null
    private var text: String? = null

    private lateinit var imageViewQr: ImageView
    private lateinit var textViewResult: TextView

    val REQUEST_CODE = 24356

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result_qr, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageViewQr = view.findViewById(R.id.imageViewQrResult)
        textViewResult = view.findViewById(R.id.textViewResult)

        val buttonPickColor = view.findViewById<Button>(R.id.buttonPickColor)

        text = arguments?.getString("result_text") ?: ""
        bitmapQr = QRTools.generate(text)

        val res = resources.getStringArray(R.array.colors)
        val colors = IntArray(res.size)

        for (i in res.indices) {
            colors[i] = Color.parseColor(res[i])
        }

        imageViewQr.setImageBitmap(bitmapQr)
        textViewResult.text = text

        buttonPickColor.setOnClickListener {
            ColorSheet().colorPicker(
                colors = colors, noColorOption = true
            ) { c ->
                bitmapQr = QRTools.changeColor(c, text)
                imageViewQr.setImageBitmap(bitmapQr)
                textViewResult.text = text
            }.show(requireActivity().supportFragmentManager)
        }
    }

    override fun setCustomAction(navigator: Navigator) = CustomAction(R.drawable.ic_baseline_save_24) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            val outFile: String = MediaStore.Images.Media.insertImage(
                context?.contentResolver,
                bitmapQr, "" + System.currentTimeMillis(), ""
            )

            Toast.makeText(context, getString(R.string.save_to) + outFile, Toast.LENGTH_LONG).show()
        } else {
            val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(requireActivity(), permissions, REQUEST_CODE)
        }
    }

    override fun getTitle() = R.string.fragment_result_qr
}