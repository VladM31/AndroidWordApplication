package can.lucky.of.core.choosers

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import java.io.File
import java.io.IOException
import java.util.Date

class ImageChooser(
    fragment: Fragment
) : AbstractChooser(fragment,"image_temp_"){

    private val cameraWorker = fragment.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        handleActivityResult(result)
    }

    private val permissionWorker = fragment.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { result ->
        runChooser(result)
    }

    override fun start() {
        mutableChooserState.value =
            chooserState.value.copy(file = null, isSet = false, isSusses = false)
        if (launchPermission()){
            runChooser(true)
        }else{
            permissionWorker.launch(Manifest.permission.CAMERA)
        }
    }

    private fun runChooser(cameraGranted: Boolean) {
        var illustrationIntent: Intent? = null
        if (cameraGranted) {
            illustrationIntent = createFileIntent()
        }

        val contentIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }


        val intentArray: Array<Intent?> = illustrationIntent?.let {
            arrayOf(it)
        } ?: arrayOfNulls(0)

        Intent(Intent.ACTION_CHOOSER).run {
            putExtra(Intent.EXTRA_TITLE, "Image Chooser")
            putExtra(Intent.EXTRA_INTENT, contentIntent)
            putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
            cameraWorker.launch(this)
        }
    }

    private fun launchPermission() : Boolean{
        return ContextCompat.checkSelfPermission(fragment.requireContext(), Manifest.permission.CAMERA).run {
            this == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun createFileIntent() : Intent?{
        var artFile: File? = null
        val illustrationIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            artFile = createFile()
            illustrationIntent.putExtra("PhotoPath", buffer)
        } catch (_: IOException) {
        }
        return if (artFile != null) {
            buffer = "file:${artFile.absolutePath}".toUri()
            val uri = FileProvider.getUriForFile(
                fragment.requireContext(), "${fragment.requireContext().packageName}.provider", artFile
            )
            illustrationIntent.apply {
                putExtra(MediaStore.EXTRA_OUTPUT, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        } else {
            null
        }
    }

    @Throws(IOException::class)
    private fun createFile(): File {
        val storageDir = fragment.requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "Word_Image_" + "${Date().toString().replace(" ","_")}_", ".jpg", storageDir
        )
    }
}