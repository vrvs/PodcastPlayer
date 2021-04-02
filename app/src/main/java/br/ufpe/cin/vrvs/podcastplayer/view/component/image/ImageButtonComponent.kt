package br.ufpe.cin.vrvs.podcastplayer.view.component.image

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import br.ufpe.cin.vrvs.podcastplayer.R

class ImageButtonComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): androidx.appcompat.widget.AppCompatImageButton(context, attrs, defStyleAttr) {

    var state: Type = Type.NONE
        set(value) {
            field = value
            renderImage()
        }

    enum class Type {
        PLAY,
        PAUSE,
        DOWNLOAD,
        STOP,
        DOWNLOADED,
        NONE
    }

    init {
        renderImage()
    }

    private fun renderImage() {
        setImageResource(
            when (state) {
                Type.PLAY -> R.drawable.ic_play_circle_filled_white_24dp
                Type.PAUSE -> R.drawable.ic_pause_circle_filled_white_24dp
                Type.DOWNLOAD -> R.drawable.ic_download_for_offline_white_24dp
                Type.STOP -> R.drawable.ic_stop_circle_white_24dp
                Type.DOWNLOADED -> R.drawable.ic_check_circle_white_24dp
                Type.NONE -> R.drawable.ic_circle_white_24dp
            }
        )
        DrawableCompat.setTint(
            DrawableCompat.wrap(drawable),
            ResourcesCompat.getColor(
                resources,
                if (isEnabled && state != Type.NONE)
                    R.color.white
                else
                    R.color.grey_disabled,
                null
            )
        )
    }

    fun nextState() {
        state = when (state) {
            Type.PLAY -> Type.PAUSE
            Type.PAUSE -> Type.PLAY
            Type.DOWNLOAD -> Type.STOP
            Type.STOP -> Type.DOWNLOADED
            Type.DOWNLOADED -> Type.DOWNLOAD
            Type.NONE -> Type.NONE
        }
        renderImage()
    }
}
