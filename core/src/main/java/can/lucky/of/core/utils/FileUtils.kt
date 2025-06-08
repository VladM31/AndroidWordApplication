package can.lucky.of.core.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.core.net.toFile
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale.getDefault

@SuppressLint("ConstantLocale")
private val timeStampFormatter = SimpleDateFormat("yyyyMMdd_HHmmss", getDefault())

fun Uri.getFilePathFromUri(context: Context,prefix : String): File? =
    if (this.path?.contains("file://") == true) this.toFile()
    else getFileFromContentUri(context, this, prefix)

private fun getFileFromContentUri(context: Context, contentUri: Uri,prefix : String): File? {
    val fileExtension = getFileExtension(context, contentUri) ?: ""
    val fileName =  "${prefix}_${timeStampFormatter.format(Date())}.$fileExtension"

    val tempFile = File(context.cacheDir, fileName)
    tempFile.createNewFile()


    try {
        FileOutputStream(tempFile).use { oStream ->
            context.contentResolver.openInputStream(contentUri)?.use { inputStream ->
                copy(inputStream, oStream)
            }
            oStream.flush()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        tempFile.runCatching { if (exists()) delete() }
        return null
    }

    return tempFile
}

private fun getFileExtension(context: Context, uri: Uri): String? =
    if (uri.scheme == ContentResolver.SCHEME_CONTENT)
        MimeTypeMap.getSingleton().getExtensionFromMimeType(context.contentResolver.getType(uri))
    else uri.path?.let { MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(it)).toString()) }

@Throws(IOException::class)
private fun copy(source: InputStream, target: OutputStream) {
    val buf = ByteArray(8192)
    var length: Int
    while (source.read(buf).also { length = it } > 0) {
        target.write(buf, 0, length)
    }
}