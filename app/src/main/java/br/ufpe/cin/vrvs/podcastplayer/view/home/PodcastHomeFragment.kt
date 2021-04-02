package br.ufpe.cin.vrvs.podcastplayer.view.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import br.ufpe.cin.vrvs.podcastplayer.R
import br.ufpe.cin.vrvs.podcastplayer.databinding.FragmentPodcastHomeBinding
import br.ufpe.cin.vrvs.podcastplayer.view.component.error.ErrorComponent
import br.ufpe.cin.vrvs.podcastplayer.view.component.podcast.PodcastListComponent
import br.ufpe.cin.vrvs.podcastplayer.view.search.PodcastSearchFragment
import br.ufpe.cin.vrvs.podcastplayer.viewmodel.podcast.SubscribedPodcastViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.android.viewmodel.ext.android.viewModel

class PodcastHomeFragment : Fragment(R.layout.fragment_podcast_home) {

    private val spViewModel: SubscribedPodcastViewModel by viewModel()
    private var mBinding: FragmentPodcastHomeBinding? = null
    private lateinit var list: PodcastListComponent
    private lateinit var error: ErrorComponent
    private lateinit var refresh: SwipeRefreshLayout
    private lateinit var searchButton: FloatingActionButton

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        list = view.findViewById(R.id.subscribed_list)
        refresh = view.findViewById(R.id.swipe_refresh)
        error = view.findViewById(R.id.error_screen)
        searchButton = view.findViewById(R.id.floating_button)

        FragmentPodcastHomeBinding.bind(view).apply {
            viewModel = spViewModel
            lifecycleOwner = viewLifecycleOwner
        }


        spViewModel.podcasts.observe(viewLifecycleOwner, Observer {
            list.changeDataSet(it)
        })
        refresh.setOnRefreshListener {
            refresh.isRefreshing = false
            spViewModel.refreshSubscribedPodcast()
        }
        list.itemClicked.observe(viewLifecycleOwner, Observer {
            // Navigate to Podcast Page
        })
        error.buttonClicked.observe(viewLifecycleOwner, Observer {
            spViewModel.refreshSubscribedPodcast()
        })
        searchButton.setOnClickListener {
            val action = PodcastHomeFragmentDirections.actionPodcastHomeFragmentToPodcastSearchFragment()
            findNavController().navigate(action)
        }

        spViewModel.refreshSubscribedPodcast()
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}