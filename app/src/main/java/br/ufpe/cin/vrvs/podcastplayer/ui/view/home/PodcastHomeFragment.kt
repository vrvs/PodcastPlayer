package br.ufpe.cin.vrvs.podcastplayer.ui.view.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import br.ufpe.cin.vrvs.podcastplayer.R
import br.ufpe.cin.vrvs.podcastplayer.databinding.FragmentPodcastHomeBinding
import br.ufpe.cin.vrvs.podcastplayer.ui.contracts.home.PodcastHome
import br.ufpe.cin.vrvs.podcastplayer.ui.view.component.error.ErrorComponent
import br.ufpe.cin.vrvs.podcastplayer.ui.view.component.podcast.PodcastListComponent
import br.ufpe.cin.vrvs.podcastplayer.ui.viewmodel.home.PodcastHomeViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.android.viewmodel.ext.android.viewModel

class PodcastHomeFragment : Fragment(R.layout.fragment_podcast_home) {

    private val spViewModel: PodcastHomeViewModel by viewModel()
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

        mBinding = FragmentPodcastHomeBinding.bind(view).apply {
            viewModel = spViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        spViewModel.state.observe(viewLifecycleOwner, Observer { state ->
            state.data?.let {
                list.changeDataSet(it)
            }
            state.error?.let {
                error.errorText(it)
            }
        })

        refresh.setOnRefreshListener {
            refresh.isRefreshing = false
            spViewModel.userIntent.offer(PodcastHome.Intent.GetSubscribedPodcasts)
        }
        list.itemClicked.observe(viewLifecycleOwner, Observer {
            val action = PodcastHomeFragmentDirections.actionPodcastHomeFragmentToPodcastDetailsFragment(it)
            findNavController().navigate(action)
        })
        error.buttonClicked.observe(viewLifecycleOwner, Observer { button ->
            when (button) {
                ErrorComponent.Button.TRY_AGAIN -> spViewModel.userIntent.offer(PodcastHome.Intent.GetSubscribedPodcasts)
                ErrorComponent.Button.CLOSE -> error.visibility = View.GONE
            }
        })
        searchButton.setOnClickListener {
            val action = PodcastHomeFragmentDirections.actionPodcastHomeFragmentToPodcastSearchFragment()
            findNavController().navigate(action)
        }
        spViewModel.userIntent.offer(PodcastHome.Intent.GetSubscribedPodcasts)
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }


}