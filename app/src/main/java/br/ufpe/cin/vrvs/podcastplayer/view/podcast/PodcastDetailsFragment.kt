package br.ufpe.cin.vrvs.podcastplayer.view.podcast

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import br.ufpe.cin.vrvs.podcastplayer.R
import br.ufpe.cin.vrvs.podcastplayer.databinding.FragmentPodcastDetailsBinding
import br.ufpe.cin.vrvs.podcastplayer.view.component.episode.EpisodeListComponent
import br.ufpe.cin.vrvs.podcastplayer.viewmodel.podcast.PodcastDetailsViewModel
import com.squareup.picasso.Picasso
import org.koin.android.viewmodel.ext.android.viewModel

class PodcastDetailsFragment : Fragment(R.layout.fragment_podcast_details) {

    private val pdViewModel: PodcastDetailsViewModel by viewModel()
    val args: PodcastDetailsFragmentArgs by navArgs()
    private var mBinding: FragmentPodcastDetailsBinding? = null
    private lateinit var list: EpisodeListComponent
    private lateinit var image: ImageView

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        image = view.findViewById(R.id.image_cover)
        list = view.findViewById(R.id.episodes_list)

        FragmentPodcastDetailsBinding.bind(view).apply {
            viewModel = pdViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        pdViewModel.podcast.observe(viewLifecycleOwner, Observer {
            Picasso
                .get()
                .load(it.imageUrl)
                .fit()
                .centerCrop()
                .error(R.drawable.ic_announcement_white_18dp)
                .into(image)
            list.changeDataSet(it.episodes)
        })

        pdViewModel.getPodcast(args.id)
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}