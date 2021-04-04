package br.ufpe.cin.vrvs.podcastplayer.view.component.image

import android.content.Context
import android.util.AttributeSet
import br.ufpe.cin.vrvs.podcastplayer.R
import br.ufpe.cin.vrvs.podcastplayer.utils.Utils
import com.squareup.picasso.Picasso

class ImageComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {

    fun render(url: String) {
        Utils.processUrl(url).let {
            if (it.isNotEmpty()) {
                Picasso
                    .get()
                    .load(it)
                    .error(R.drawable.ic_announcement_white_18dp)
                    .into(this)
            } else {
                Picasso
                    .get()
                    .load(R.drawable.ic_announcement_white_18dp)
                    .into(this)
            }
        }
    }
}
