import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.myapplication.onlineshopapp.R
import java.io.IOException

class GlideLoader(val context:Context) {
    fun loadUserPicture(imageUri: Uri, imageView: ImageView){
        try {
            Glide.with(context)
                .load(imageUri)
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_account_box_24)
                .into(imageView)
        }catch (
            e: IOException
        ){
            e.printStackTrace()
        }
    }
}