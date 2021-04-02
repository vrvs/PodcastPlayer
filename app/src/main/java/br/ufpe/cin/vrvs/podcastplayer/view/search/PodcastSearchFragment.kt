package br.ufpe.cin.vrvs.podcastplayer.view.search

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import br.ufpe.cin.vrvs.podcastplayer.R
import br.ufpe.cin.vrvs.podcastplayer.databinding.FragmentPodcastSearchBinding
import br.ufpe.cin.vrvs.podcastplayer.view.component.error.ErrorComponent
import br.ufpe.cin.vrvs.podcastplayer.view.component.podcast.PodcastListComponent
import br.ufpe.cin.vrvs.podcastplayer.viewmodel.podcasts.search.SearchPodcastsViewModel
import com.google.android.material.textfield.TextInputEditText
import org.koin.android.viewmodel.ext.android.viewModel


class PodcastSearchFragment : Fragment(R.layout.fragment_podcast_search) {

    private val spViewModel: SearchPodcastsViewModel by viewModel()
    private var mBinding: FragmentPodcastSearchBinding? = null
    private lateinit var list: PodcastListComponent
    private lateinit var error: ErrorComponent
    private lateinit var searchText: TextInputEditText

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        list = view.findViewById(R.id.subscribed_list)
        searchText = view.findViewById(R.id.search_text)
        error = view.findViewById(R.id.error_screen)

        FragmentPodcastSearchBinding.bind(view).apply {
            viewModel = spViewModel
            lifecycleOwner = viewLifecycleOwner
        }


        spViewModel.podcasts.observe(viewLifecycleOwner, Observer {
            list.changeDataSet(it)
        })

        list.itemClicked.observe(viewLifecycleOwner, Observer {
            val action = PodcastSearchFragmentDirections.actionPodcastSearchFragmentToPodcastDetailsFragment(it)
            findNavController().navigate(action)
        })
        searchText.setOnEditorActionListener { v, actionId, _ ->
            hideKeyboard(v)
            searchText.clearFocus()
            if (actionId == IME_ACTION_SEARCH && !v.text.isNullOrBlank()) {
                spViewModel.searchPodcasts(v.text.toString())
                true
            } else {
                Toast.makeText(context, R.string.podcast_search_empty_search_not_allowed, LENGTH_SHORT).show()
                false
            }
        }
        error.buttonClicked.observe(viewLifecycleOwner, Observer {
            searchText.text.let {
                if (it.isNullOrBlank()) {
                    spViewModel.getPodcastsFeed()
                } else {
                    spViewModel.searchPodcasts(it.toString())
                }
            }
        })

        spViewModel.getPodcastsFeed()
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager: InputMethodManager? = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}