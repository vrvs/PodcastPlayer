package br.ufpe.cin.vrvs.podcastplayer.view.component.podcast.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import br.ufpe.cin.vrvs.podcastplayer.R
import br.ufpe.cin.vrvs.podcastplayer.data.model.Podcast
import br.ufpe.cin.vrvs.podcastplayer.view.component.image.ImageComponent
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

internal class PodcastAdapter(val context: Context) : RecyclerView.Adapter<PodcastAdapter.ViewHolder>() {

    var dataSet: List<Podcast> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val _itemClicked = MutableLiveData<String>()
    val itemClicked: LiveData<String> = _itemClicked

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView by lazy { view.findViewById<TextView>(R.id.title) }
        val imageComponent: ImageComponent by lazy { view.findViewById<ImageComponent>(R.id.image_component) }
        val chipGroup: ChipGroup by lazy { view.findViewById<ChipGroup>(R.id.chip_group)}
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(
                    R.layout.podcast_component,
                    viewGroup,
                    false)
        )

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.title.text = dataSet[position].title
        viewHolder.imageComponent.render(dataSet[position].imageUrl)

        viewHolder.chipGroup.apply {
            this.removeAllViews()
        }.also { chipGroup ->
            dataSet[position].categories.values.ifEmpty {
                val categoryNone = context.getString(R.string.none_category)
                listOf(categoryNone)
            }.map { category ->
                val chip = Chip(context)
                chip.apply {
                    text = category
                    setTextAppearance(R.style.ChipTextStyle)
                    chipBackgroundColor = ResourcesCompat.getColorStateList(context.resources, R.color.black, null)
                }
            }.forEach {
                chipGroup.addView(it)
            }
        }

        // clicked item action
        viewHolder.itemView.setOnClickListener {
            _itemClicked.postValue(dataSet[position].id)
        }
    }

    override fun getItemCount() = dataSet.size
}
