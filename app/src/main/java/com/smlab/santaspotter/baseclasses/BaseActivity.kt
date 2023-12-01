package com.smlab.santaspotter.baseclasses

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.smlab.santaspotter.AddSantaActivity
import com.smlab.santaspotter.ImagePickerActivity
import com.smlab.santaspotter.ImagePickerActivity.PickerOptionListener
import com.smlab.santaspotter.R
import com.smlab.santaspotter.filter.AppConstants
import java.io.ByteArrayOutputStream

open class BaseActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "BaseActivity"
        var couponCode: String = ""
    }


    fun getCouponCode(): String {
        return couponCode
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = FirebaseFirestore.getInstance()
        db.collection("coupons").document("couponDocument").get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val codeValue = documentSnapshot.getString("code")
                    Log.d(TAG, "Coupon Code: $codeValue")
                    if (codeValue != null) {
                        couponCode = codeValue
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Coupon Code: Error: " + e.message)
            }


    }


    fun getEncoded64ImageStringFromBitmap(bitmap: Bitmap): String? {
        Log.d(ContentValues.TAG, "photoSelection: getEncoded64ImageStringFromBitmap: ")
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        val byteFormat = stream.toByteArray()
        // get the base 64 string
        return Base64.encodeToString(byteFormat, Base64.NO_WRAP)
    }

    fun pickImage(activity: Activity) {
        Log.d(ContentValues.TAG, "pickImage: ")
        Dexter.withActivity(activity)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    Log.d(ContentValues.TAG, "onPermissionsChecked: $report")
                    //                        if (report.areAllPermissionsGranted()) {
//                            Log.d(TAG, "onPermissionsChecked: pass:" +(report.areAllPermissionsGranted()) );
                    showImagePickerOptions(activity)
                    //                        }

//                        if (report.isAnyPermissionPermanentlyDenied()) {
//                            Log.d(TAG, "onPermissionsChecked: fail: "+(report.isAnyPermissionPermanentlyDenied()));
//                            showSettingsDialog(activity);
//                        }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }


    fun showImagePickerOptions(context: Context) {
        ImagePickerActivity.showImagePickerOptions(this, object : PickerOptionListener {
            override fun onTakeCameraSelected() {
                launchCameraIntent(context)
            }

            override fun onChooseGallerySelected() {
                launchGalleryIntent(context)
            }
        })
    }

    fun launchCameraIntent(context: Context) {
        val intent = Intent(context, AddSantaActivity::class.java)
        intent.putExtra(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_IMAGE_CAPTURE
        )

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000)
        startActivityForResult(intent, AppConstants.REQUEST_CODE_For_IMAGE)
    }


    fun launchGalleryIntent(context: Context) {
        val intent = Intent(context, AddSantaActivity::class.java)
        intent.putExtra(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_GALLERY_IMAGE
        )

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)
        startActivityForResult(intent, AppConstants.REQUEST_CODE_For_IMAGE)
    }


    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    fun showSettingsDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(resources.getString(R.string.dialog_permission_title))
        builder.setMessage(resources.getString(R.string.dialog_permission_message))
        builder.setPositiveButton(resources.getString(R.string.go_to_settings)) { dialog: DialogInterface, which: Int ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton(
            resources.getString(android.R.string.cancel)
        ) { dialog: DialogInterface, which: Int -> dialog.cancel() }
        builder.show()
    }

    // navigating user to app settings
    fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    fun loadProfile(context: Context?, url: String, imageView: ImageView?) {
        Log.d(ContentValues.TAG, "Image cache path: $url")
        if (imageView != null) {
            Glide.with(context!!).load(url).into(imageView)
            imageView.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent))
        } else {
            // Handle the case where the ImageView is null
        }
    }


}