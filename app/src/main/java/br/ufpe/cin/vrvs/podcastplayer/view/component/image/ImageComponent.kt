package br.ufpe.cin.vrvs.podcastplayer.view.component.image

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import br.ufpe.cin.vrvs.podcastplayer.R
import com.squareup.picasso.Picasso

class ImageComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): FrameLayout(context, attrs, defStyleAttr) {

    private val imageView: ImageView by lazy { findViewById<ImageView>(R.id.icon) }

    init {
        LayoutInflater.from(context).inflate(R.layout.image_component, this, true)
    }

    fun render(url: String) {
        process(url).let {
            if (it.isNotEmpty()) {
                Picasso
                    .get()
                    .load(it)
                    .error(R.drawable.ic_announcement_white_18dp)
                    .into(imageView)
            } else {
                Picasso
                    .get()
                    .load(R.drawable.ic_announcement_white_18dp)
                    .into(imageView)
            }
        }
    }

    private fun process(url: String): String {
        if ("https" in url)
            return url
        return url.replace("http", "https")
    }
}
