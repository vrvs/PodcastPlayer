package br.ufpe.cin.vrvs.podcastplayer.view.component.episode.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import br.ufpe.cin.vrvs.podcastplayer.R
import br.ufpe.cin.vrvs.podcastplayer.data.model.Episode
import br.ufpe.cin.vrvs.podcastplayer.view.component.image.ImageButtonComponent
import br.ufpe.cin.vrvs.podcastplayer.view.component.image.ImageComponent

internal class EpisodeAdapter(val context: Context) : RecyclerView.Adapter<EpisodeAdapter.ViewHolder>() {

    var dataSet: List<Episode> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val _itemClicked = MutableLiveData<String>()
    val itemClicked: LiveData<String> = _itemClicked
    private val _buttonClicked = MutableLiveData<Pair<String, ImageButtonComponent.Type>>()
    val buttonClicked: LiveData<Pair<String, ImageButtonComponent.Type>> = _buttonClicked

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView by lazy { view.findViewById<TextView>(R.id.title) }
        val date: TextView by lazy { view.findViewById<TextView>(R.id.date) }
        val imageComponent: ImageComponent by lazy { view.findViewById<ImageComponent>(R.id.image_component) }
        val description: TextView by lazy { view.findViewById<TextView>(R.id.description) }
        val duration: TextView by lazy { view.findViewById<TextView>(R.id.duration) }
        val playPause: ImageButtonComponent by lazy { view.findViewById<ImageButtonComponent>(R.id.play_pause) }
        val downloadStopDelete: ImageButtonComponent by lazy { view.findViewById<ImageButtonComponent>(R.id.download_stop_delete) }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(
                    R.layout.episode_component,
                    viewGroup,
                    false)
        )

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val data = dataSet[position]
        viewHolder.apply {
            title.text = data.title
            date.text = data.datePublished.toString()
            imageComponent.render(data.imageUrl)
            description.text = data.description
            duration.text = "${data.duration} MIN"

            if (data.downloaded) {
                playPause.setOnClickListener {
                    _buttonClicked.postValue(Pair(data.id, playPause.state))
                    playPause.nextState()
                }

                downloadStopDelete.state = ImageButtonComponent.Type.DOWNLOADED
            } else {
                playPause.isEnabled = false
                downloadStopDelete.state = ImageButtonComponent.Type.DOWNLOAD
            }
            playPause.state = ImageButtonComponent.Type.PLAY

            downloadStopDelete.setOnClickListener {
                _buttonClicked.postValue(Pair(data.id, downloadStopDelete.state))
                downloadStopDelete.nextState()
            }

            // clicked item action
            itemView.setOnClickListener {
                _itemClicked.postValue(data.id)
            }
        }
    }

    override fun getItemCount() = dataSet.size
}