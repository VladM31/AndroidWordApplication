package com.generagames.happy.town.farm.wordandroid.ui.handels

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import lens24.intent.Card
import lens24.intent.ScanCardCallback
import lens24.intent.ScanCardIntent

class ScanCardHandler(
    private val fragment: Fragment
) {

    private val scanCardCallback = ScanCardCallback.Builder()
        .setOnSuccess { card: Card, _ ->
            onSuccessScan?.invoke(card.cardNumber.replace(" ", ""))
        }
        .setOnError {
            Toast.makeText(fragment.requireContext(), "Error scan", Toast.LENGTH_SHORT).show()
        }
        .build()

    private val startScanLauncher = fragment.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        scanCardCallback.onActivityResult(result)
    }

    private var onSuccessScan: ((String) -> Unit)? = null

    fun checkCameraPermissionAndScan() {
        if (hasCameraPermission()) {
            startScan()
            return
        }

        ActivityCompat.requestPermissions(
            fragment.requireActivity(),
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    fun setOnSuccessScanListener(listener: (String) -> Unit) {
        onSuccessScan = listener
    }

    private fun startScan() {
        ScanCardIntent.Builder(fragment.requireContext())
            .setVibrationEnabled(true)
            .setHint("Point the camera at the card number")
            .setToolbarTitle("Scan Card")
            .setManualInputButtonText("Back")
            .setBottomHint("Move the card closer")

            .setMainColor(can.lucky.of.core.R.color.primary_back)
            .build().let {
                startScanLauncher.launch(it)
            }
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            fragment.requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 1001
    }
}