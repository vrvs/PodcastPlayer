package br.ufpe.cin.vrvs.podcastplayer.view.podcast

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import br.ufpe.cin.vrvs.podcastplayer.R
import br.ufpe.cin.vrvs.podcastplayer.databinding.FragmentPodcastDetailsBinding
import br.ufpe.cin.vrvs.podcastplayer.services.download.DownloadUtils.startPodcastDownload
import br.ufpe.cin.vrvs.podcastplayer.services.download.DownloadUtils.cancel
import br.ufpe.cin.vrvs.podcastplayer.services.download.DownloadUtils.getDownloadCompleteBroadcastReceiver
import br.ufpe.cin.vrvs.podcastplayer.services.download.DownloadUtils.getDownloadManager
import br.ufpe.cin.vrvs.podcastplayer.view.component.episode.EpisodeListComponent
import br.ufpe.cin.vrvs.podcastplayer.view.component.image.ImageButtonComponent.Type.DOWNLOAD
import br.ufpe.cin.vrvs.podcastplayer.view.component.image.ImageButtonComponent.Type.DOWNLOADED
import br.ufpe.cin.vrvs.podcastplayer.view.component.image.ImageButtonComponent.Type.PAUSE
import br.ufpe.cin.vrvs.podcastplayer.view.component.image.ImageButtonComponent.Type.PLAY
import br.ufpe.cin.vrvs.podcastplayer.view.component.image.ImageButtonComponent.Type.STOP
import br.ufpe.cin.vrvs.podcastplayer.view.component.image.ImageComponent
import br.ufpe.cin.vrvs.podcastplayer.viewmodel.podcast.PodcastDetailsViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class PodcastDetailsFragment : Fragment(R.layout.fragment_podcast_details) {

    private val pdViewModel: PodcastDetailsViewModel by viewModel()
    val args: PodcastDetailsFragmentArgs by navArgs()
    private var mBinding: FragmentPodcastDetailsBinding? = null
    private lateinit var list: EpisodeListComponent
    private lateinit var imageComponent: ImageComponent
    private var downloadManager: DownloadManager? = null
    private val broadcastReceiversMaps = mutableMapOf<Long, BroadcastReceiver>()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        imageComponent = view.findViewById(R.id.image_cover)
        list = view.findViewById(R.id.episodes_list)
        downloadManager = context?.getDownloadManager()

        FragmentPodcastDetailsBinding.bind(view).apply {
            viewModel = pdViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        pdViewModel.podcast.observe(viewLifecycleOwner, Observer {
            imageComponent.render(it.imageUrl)
            list.changeDataSet(it.episodes)
        })

        list.buttonClicked.observe(viewLifecycleOwner, Observer {
            val episode = it.first
            when (it.second) {
                PLAY, PAUSE -> {

                }
                DOWNLOAD -> {
                    context?.let { context ->
                        context.startPodcastDownload(
                            episode.audioUrl,
                            episode.audioType,
                            episode.title,
                            episode.id,
                            episode.podcastId
                        )?.let { downloadId ->
                            broadcastReceiversMaps[downloadId] = getDownloadCompleteBroadcastReceiver(downloadId) {
                                updateInterface(episode.podcastId)
                            }
                            context.registerReceiver(
                                broadcastReceiversMaps[downloadId],
                                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
                            )
                        }
                    }
                }
                DOWNLOADED, STOP -> {
                    episode.downloadId?.let { downloadId ->
                        downloadManager?.cancel(
                            episode.podcastId,
                            episode.id,
                            downloadId
                        )
                    }
                    updateInterface(episode.podcastId)
                }
                else -> {

                }
            }
        })

        pdViewModel.getPodcast(args.id)
    }

    override fun onDestroy() {
        mBinding = null
        downloadManager = null
        broadcastReceiversMaps.values.forEach {
            context?.unregisterReceiver(it)
        }
        super.onDestroy()
    }

    private fun updateInterface(id: String) {
        pdViewModel.updatePodcast(id)
    }
}