package com.vad.qrscanner.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.vad.qrscanner.R
import com.vad.qrscanner.domain.QRTools
import com.vad.qrscanner.navigation.CustomAction
import com.vad.qrscanner.navigation.HasCustomActions
import com.vad.qrscanner.navigation.HasCustomTitle
import com.vad.qrscanner.navigation.Navigator
import dev.sasikanth.colorsheet.ColorSheet


class ResultQrFragment : Fragment(), HasCustomTitle, HasCustomActions {

    private var bitmapQr: Bitmap? = null
    private var text: String? = null

    private lateinit var imageViewQr: ImageView
    private lateinit var textViewResult: TextView

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
                colors = colors
            ) { c ->
                bitmapQr = QRTools.changeColor(c, text)
                imageViewQr.setImageBitmap(bitmapQr)
                textViewResult.text = text
            }.show(requireActivity().supportFragmentManager)
        }
    }

    override fun setCustomAction(navigator: Navigator): List<CustomAction> {
        val listAction = mutableListOf<CustomAction>()
        val saveAction = CustomAction(R.drawable.ic_baseline_save_24) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                saveImage(bitmapQr!!)
            } else {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }

        val shareAction = CustomAction(R.drawable.ic_baseline_share_24) {
                val bitmapPath: String = MediaStore.Images.Media.insertImage(
                    context?.contentResolver,
                    bitmapQr,
                    "qr",
                    "share qr"
                )
                val bitmapUri = Uri.parse(bitmapPath)

                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "image/png"
                intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
                startActivity(Intent.createChooser(intent, resources.getString(R.string.share)))
        }

        listAction.add(saveAction)
        listAction.add(shareAction)

        return listAction
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                saveImage(bitmapQr!!)
            } else {
                Toast.makeText(context, "Permission not granted", Toast.LENGTH_SHORT).show()
            }
        }

    private fun saveImage(bitmap: Bitmap) {
        MediaStore.Images.Media.insertImage(
            context?.contentResolver,
            bitmap, "" + System.currentTimeMillis(), ""
        )

        Toast.makeText(context, getString(R.string.save_to) + "Gallery", Toast.LENGTH_LONG).show()
    }

    override fun getTitle() = R.string.fragment_result_qr
}