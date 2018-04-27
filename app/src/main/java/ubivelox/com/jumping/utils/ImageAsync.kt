package ubivelox.com.jumping.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.widget.ImageView
import java.lang.ref.WeakReference
import android.R.attr.data
import android.net.Uri
import android.provider.MediaStore



/**
 * Created by UBIVELOX on 2018-04-26.
 */
class ImageAsync(val context: Context, imageView: ImageView) : AsyncTask<String, Void, Bitmap>() {
    /*******************************************************************************
     * Variable.
     *******************************************************************************/
    val imageViewReference: WeakReference<ImageView>
    init {
        imageViewReference = WeakReference(imageView)
    }

    /*******************************************************************************
     * Implement.
     *******************************************************************************/
    override fun onPreExecute() {
        super.onPreExecute()
        // URI에서 읽어 온다, URI 초기화 시킴
        imageViewReference.get()?.setImageURI(null)
    }

    override fun doInBackground(vararg p0: String?): Bitmap {
         return MediaStore.Images.Media.getBitmap(context.contentResolver, Uri.parse(p0[0] as String))

    }

    override fun onPostExecute(result: Bitmap?) {
        super.onPostExecute(result)
        // 1. let을 ?. 과 함께 사용하면 if (obj != null)을 대체 가능
        // 2. val obj = objClass
        //    obj.setParam1(1)
        //    obj.setParam2(2)
        // let을 사용하는 경우
        // val obj = objClass.let { setParam1(1) setParam2(2) }로 obj.을 생략 가능
        result?.let { imageViewReference.get()?.setImageBitmap(result) }
    }

}