package com.vad.qrscanner.fragments

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.fondesa.kpermissions.PermissionStatus
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.anyPermanentlyDenied
import com.fondesa.kpermissions.anyShouldShowRationale
import com.fondesa.kpermissions.extension.isPermissionGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.request.PermissionRequest
import com.vad.qrscanner.R
import com.vad.qrscanner.domain.QRTools
import com.vad.qrscanner.navigation.CustomAction
import com.vad.qrscanner.navigation.HasCustomActions
import com.vad.qrscanner.navigation.HasCustomTitle
import com.vad.qrscanner.navigation.Navigator
import com.vad.qrscanner.showGrantedToast
import com.vad.qrscanner.showPermanentlyDeniedDialog
import com.vad.qrscanner.showRationaleDialog
import dev.sasikanth.colorsheet.ColorSheet


class ResultQrFragment : Fragment(), HasCustomTitle, HasCustomActions, PermissionRequest.Listener {

    private var bitmapQr: Bitmap? = null
    private var text: String? = null

    private lateinit var imageViewQr: ImageView
    private lateinit var textViewResult: TextView

    private val request by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsBuilder(Manifest.permission.READ_MEDIA_IMAGES).build()
        } else {
            permissionsBuilder(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).build()
        }
    }

    private val isPermissionGranted by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireContext().isPermissionGranted(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            requireContext().isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                    requireContext().isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        request.addListener(this)
    }

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
            if (isPermissionGranted) {
                saveImage(bitmapQr!!)
            } else {
                request.send()
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

    private fun saveImage(bitmap: Bitmap) {
        MediaStore.Images.Media.insertImage(
            context?.contentResolver,
            bitmap, "" + System.currentTimeMillis(), ""
        )

        Toast.makeText(context, getString(R.string.save_to) + "Gallery", Toast.LENGTH_LONG).show()
    }

    override fun getTitle() = R.string.fragment_result_qr
    override fun onPermissionsResult(result: List<PermissionStatus>) {
        when {
            result.anyPermanentlyDenied() -> requireContext().showPermanentlyDeniedDialog()
            result.anyShouldShowRationale() ->  requireContext().showRationaleDialog(request)
            result.allGranted() -> {
                saveImage(bitmapQr!!)
                requireContext().showGrantedToast()
            }
        }
    }
}