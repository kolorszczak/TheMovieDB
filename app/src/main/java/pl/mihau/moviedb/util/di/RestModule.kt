package pl.mihau.moviedb.util.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import pl.mihau.moviedb.BuildConfig
import pl.mihau.moviedb.api.APIService
import pl.mihau.moviedb.common.Values.INTERCEPTOR_AUTHORIZATION
import pl.mihau.moviedb.common.Values.INTERCEPTOR_CONTENT_TYPE
import pl.mihau.moviedb.util.provider.AppSchedulerProvider
import pl.mihau.moviedb.util.provider.SchedulerProvider
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


val restModule = module {
    single<OkHttpClient> {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClientBuilder = OkHttpClient.Builder()

        okHttpClientBuilder
            .addInterceptor(loggingInterceptor)
            .addInterceptor {
                it.request().newBuilder()
                    .header("Content-Type", INTERCEPTOR_CONTENT_TYPE)
                    .header("Authorization", INTERCEPTOR_AUTHORIZATION)
                    .build()
                    .let { request ->
                        it.proceed(request)
                    }
            }

        okHttpClientBuilder.build()
    }
    single<Gson> { GsonBuilder().create() }
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(get())
            .build()
    }
    single<APIService> { get<Retrofit>().create<APIService>(APIService::class.java) }
    single<SchedulerProvider> { AppSchedulerProvider() }
}
