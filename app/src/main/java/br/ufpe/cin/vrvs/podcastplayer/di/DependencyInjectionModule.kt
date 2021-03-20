package br.ufpe.cin.vrvs.podcastplayer.di

import android.content.Context
import br.ufpe.cin.vrvs.podcastplayer.data.data_source.remote.podcast_index.PodcastIndexApi
import br.ufpe.cin.vrvs.podcastplayer.data.data_source.remote.podcast_index.PodcastIndexAuthInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val URL = "https://podcastindex-org.github.io/docs-api"

val apiModule = module {
    single {
        provideHttpLoggingInterceptor()
    }
    single {
        providePodcastIndexAuthInterceptor(get())
    }
    single {
        provideHttpClient(get(), get())
    }
    single {
        provideRetrofit(get())
    }
    single {
        providePodcastIndexApi(get())
    }
}

val databaseModule = module {

}

val repositoryModule = module {

}

val viewModelModule = module {

}

private fun provideHttpLoggingInterceptor() =
    HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

private fun providePodcastIndexAuthInterceptor(context: Context) =
    PodcastIndexAuthInterceptor(context)

private fun provideHttpClient(
    loggingInterceptor: HttpLoggingInterceptor,
    podcastIndexAuthInterceptor: PodcastIndexAuthInterceptor
) =
    OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(podcastIndexAuthInterceptor)
        .build()

private fun provideRetrofit(okHttpClient: OkHttpClient) =
    Retrofit.Builder()
        .baseUrl(PodcastIndexApi.URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

private fun providePodcastIndexApi(retrofit: Retrofit) =
    retrofit.create(PodcastIndexApi::class.java)