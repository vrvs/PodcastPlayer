package br.ufpe.cin.vrvs.podcastplayer.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel

abstract class BaseViewModel<I, S>(initialState: S) : ViewModel() {

    val userIntent = Channel<I>(Channel.UNLIMITED)
    protected val protectedState = MutableLiveData<S>(initialState)
    val state: LiveData<S>
        get() = protectedState

}