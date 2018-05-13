package ubivelox.com.jumping.ui.common

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log
import android.widget.Toast
import ubivelox.com.jumping.R
import ubivelox.com.jumping.utils.AppConsts
import ubivelox.com.jumping.utils.TimeUtility
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import android.content.pm.ResolveInfo
import android.content.pm.PackageManager



/**
 * Created by UBIVELOX on 2018-04-25.
 */
interface IRegistrationPresenter {
    /*******************************************************************************
     * common method.
     *******************************************************************************/
    fun getImagePathUri() : Uri?
    fun setImagePathUri(uri: Uri?)

    /**
     * 카메라를 호출 한다.
     */
    fun doTakePhotoAction(mActivity : Activity) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // 파일 경로 생성

        if (intent.resolveActivity(mActivity.packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile(mActivity)
            } catch (ex: IOException) {
                Log.e("shyook", "createImageFile Error")
            }

            // intent를 넘겨도 activity에서 data가 null임 이유를 모르겠음.
            // 클래스 변수에 uri를 저장 후 사용
            if (photoFile != null) {
                setImagePathUri(FileProvider.getUriForFile(mActivity, mActivity.packageName, photoFile));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, getImagePathUri())

                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                    val resInfoList = mActivity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
                    for (resolveInfo in resInfoList) {
                        val packageName = resolveInfo.activityInfo.packageName
                        mActivity.grantUriPermission(packageName, getImagePathUri(), Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                }

                mActivity.startActivityForResult(intent, AppConsts.PICK_FROM_CAMERA)
            }
        }
    }

    /**
     * 사진 촬영 후 이미지를 크롭 한다.
     * 안드로이드 N의 경우 URI 퍼미션 필요
     */
    fun cropImageForCamera(mActivity : Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mActivity.grantUriPermission("com.android.camera", getImagePathUri(),
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(getImagePathUri(), "image/*")

        val list = mActivity.packageManager.queryIntentActivities(intent, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mActivity.grantUriPermission(list.get(0).activityInfo.packageName, getImagePathUri(),
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val size = list.size
        if (size == 0) run {
            Toast.makeText(mActivity, mActivity.getText(R.string.photo_cancel), Toast.LENGTH_SHORT).show()
            return
        } else {
            // crop
            intent.putExtra("aspectX", 1)
            intent.putExtra("aspectY", 1)
            //Crop the output image size
            intent.putExtra("outputX", 200)
            intent.putExtra("outputY", 200)
            intent.putExtra("crop", "true")
            intent.putExtra("scale", true)

            var croppedFileName: File? = null
            try {
                croppedFileName = createImageFile(mActivity)
            } catch (e: IOException) {
                e.printStackTrace()
            }


            val folder = mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val tempFile = File(folder.toString(), croppedFileName!!.name)

            setImagePathUri(FileProvider.getUriForFile(mActivity,
                    mActivity.packageName, tempFile))

            intent.putExtra("return-data", false)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, getImagePathUri())
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())

            val i = Intent(intent)
            val res = list[0]
            // 안드로이드 N버전 이상부터 flag로 uri permission 전달 해야 함.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

                mActivity.grantUriPermission(res.activityInfo.packageName, getImagePathUri(),
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            i.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
            mActivity.startActivityForResult(i, AppConsts.PICK_FROM_IMAGE)
        }

    }

    /**
     * 갤러리에서 선택한 사진을 크롭 한다.
     */
    fun cropImageForGallery(mActivity: Activity, data: Intent?) {
        if (data == null) {
            return
        }

        //file from gallery
        val sourceFile = File(getRealPathFromURI(mActivity, data.data))
        val destFile = createImageFile(mActivity)
        Log.e("shyook", "path : " + data.data.path)
        try {
            //copy file from gallery
            // otherwise crop method can't cut image without write permission
            copyFile(sourceFile, destFile)
            //Android N need use FileProvider to get file uri
            val photoURI = FileProvider.getUriForFile(mActivity, mActivity.packageName, destFile)
            //cut image
            cropWithUri(mActivity, photoURI)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * 갤러리를 호출 한다.
     */
    fun getGalleryAction(mActivity: Activity) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE)
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        mActivity.startActivityForResult(intent, AppConsts.PICK_FROM_GALLERY)
    }

    /*******************************************************************************
     * Inner method.
     *******************************************************************************/
    /**
     * 이미지 파일을 생성 한다.
     */
    @Throws(IOException::class)
    private fun createImageFile(mActivity : Activity): File {
        val timeStamp = TimeUtility.getCurrentTimeWithFormat(TimeUtility.DATE_FORMAT_YYYYMMDDHHMMSS)
        val imageFileName = "Jumping_" + timeStamp + "_"
        val storageDir = mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir          /* directory */
        )
        // mImagePath = "file:" + image.absolutePath
        return image
    }

    /**
     * 전달 받은 URI를 통해 사진을 크롭 한다.
     */
    private fun cropWithUri(mActivity : Activity, uri: Uri) {
        mActivity.grantUriPermission("com.android.camera", uri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(uri, "image/*")
        //Android N need set permission to uri otherwise system camera don't has permission to access file wait crop
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        intent.putExtra("crop", "true")
        //The proportion of the crop box is 1:1
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)
        //Crop the output image size
        intent.putExtra("outputX", 200)
        intent.putExtra("outputY", 200)
        //image type
        intent.putExtra("outputFormat", "JPEG")
        intent.putExtra("noFaceDetection", true)
        //true - don't return uri |  false - return uri
        intent.putExtra("return-data", true)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        mActivity.startActivityForResult(intent, AppConsts.PICK_FROM_IMAGE)
    }

    /**
     * URI에서 이미지 Path를 얻어 온다.
     */
    private fun getRealPathFromURI(mActivity : Activity, contentURI: Uri): String {
        val cursor = mActivity.contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) {
            // Source is Dropbox or other similar local file path
            return contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            return cursor.getString(idx)
        }
    }

    /**
     * 파일을 복사 한다.
     */
    //copy sourceFile to destFile
    @Throws(IOException::class)
    private fun copyFile(sourceFile: File, destFile: File?) {
        if (!sourceFile.exists()) {
            return
        }
        val source = FileInputStream(sourceFile).getChannel()
        val destination = FileOutputStream(destFile).getChannel()
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size())
        }
        if (source != null) {
            source.close()
        }
        if (destination != null) {
            destination.close()
        }
    }
}