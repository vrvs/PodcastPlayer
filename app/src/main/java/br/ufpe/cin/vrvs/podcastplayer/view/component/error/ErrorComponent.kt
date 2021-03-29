package br.ufpe.cin.vrvs.podcastplayer.view.component.error

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.ufpe.cin.vrvs.podcastplayer.R
import com.google.android.material.button.MaterialButton

class ErrorComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {

    private val errorText: TextView by lazy { findViewById<TextView>(R.id.text_info) }
    private val buttonError: MaterialButton by lazy { findViewById<MaterialButton>(R.id.button_error) }

    private val _buttonClicked = MutableLiveData<Boolean>()
    val buttonClicked: LiveData<Boolean> = _buttonClicked

    init {
        LayoutInflater.from(context).inflate(R.layout.error_component, this, true)
        buttonError.setOnClickListener {
            _buttonClicked.postValue(true)
        }
    }

    fun errorText(text: String) {
        errorText.text = text
    }
}