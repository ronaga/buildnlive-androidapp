package buildnlive.com.buildlive.utils;


import android.annotation.TargetApi
import android.app.ProgressDialog
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Typeface
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.io.IOException
import java.lang.Long

/**
 * Created by Server on 2/1/2018.
 */
class UtilityofActivity(activity: AppCompatActivity) {
    var appCompatActivity = activity
    var mConetxt: Context? =activity.applicationContext
    var progressDialog: ProgressDialog? = null

    fun setTypeFaceToEditText(editText: EditText){
        val myTypeface = Typeface.createFromAsset(mConetxt!!.assets, "fonts/Nexa-Bold.otf")
        editText.setTypeface(myTypeface)
    }
    fun setTypeFaceToTextView(textView: TextView){
        val myTypeface = Typeface.createFromAsset(mConetxt!!.assets, "fonts/Nexa-Bold.otf")
        textView.setTypeface(myTypeface)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun getPath(context: Context, uri: Uri): String? {
        try {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR2) {
                return getRealPathFromURI(context, uri)

            }
            val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    return if ("primary".equals(type, ignoreCase = true)) {
                        Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                    } else {
                        "/storage/" + type + "/" + split[1]

                    }
                } else if (isDownloadsDocument(uri)) {
                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id)!!)
                    return getDataColumn(context, contentUri, null, null)
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]
                    var contentUri: Uri? = null
                    if ("image" == type) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])
                    return getDataColumn(context, contentUri, selection, selectionArgs)
                }// MediaProvider
                // DownloadsProvider
            } else if ("content".equals(uri.scheme, ignoreCase = true)) {
                // Return the remote address
                return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(context, uri, null, null)
            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                return uri.path
            }// File
            // MediaStore (and general)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        return null
    }

    fun decodeScaledBitmapFromSdCard(filePath: String): Bitmap {

        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filePath, options)

        // Calculate inSampleSize
        val reqWidth = 800
        val reqHeight = 650
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight)
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        val bitmap = BitmapFactory.decodeFile(filePath, options)
        return checkOrientationOfImage(bitmap, filePath)
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        val totalPixels = (width * height).toFloat()
        val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }
        return inSampleSize
    }

    fun checkOrientationOfImage(bitmap: Bitmap, imgurl: String): Bitmap {
        var bitmap = bitmap


        var ei: ExifInterface? = null
        try {
            ei = ExifInterface(imgurl)

        } catch (e: IOException) {
            e.printStackTrace()
        }

        assert(ei != null)
        val orientation = ei!!.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> bitmap = rotateImage(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> bitmap = rotateImage(bitmap, 180f)
        }// etc.
        return bitmap
    }


    fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val retVal: Bitmap

        val matrix = Matrix()
        matrix.postRotate(angle)
        retVal = Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)

        return retVal
    }

    fun getRealPathFromURI(context: Context, contentUri: Uri): String {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
    }

    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            if (cursor != null)
                cursor.close()
        }
        return null
    }


    fun toast(message: String) {
        Toast.makeText(appCompatActivity, message,
                Toast.LENGTH_LONG).show()

    }

    fun showProgressDialog() {

        appCompatActivity.runOnUiThread(Runnable {
            if (progressDialog == null) {
                progressDialog = ProgressDialog(appCompatActivity)
                progressDialog?.getWindow()
                progressDialog?.setCancelable(false)
            }
            progressDialog?.setMessage("Please Wait...")
            progressDialog?.setIndeterminate(false)

            progressDialog?.show()
        })
    }

    fun dismissProgressDialog() {
        try {
            if (progressDialog != null) {
                progressDialog?.dismiss()
                progressDialog = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showResponse(body: String) {
        Log.e("Full response => ", body)
    }


}