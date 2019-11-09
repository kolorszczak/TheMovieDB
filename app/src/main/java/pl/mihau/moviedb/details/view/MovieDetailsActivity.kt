package pl.mihau.moviedb.details.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.mihau.moviedb.R
import pl.mihau.moviedb.common.Keys
import pl.mihau.moviedb.common.view.BaseActivity
import pl.mihau.moviedb.databinding.ActivityMovieDetailsBinding
import pl.mihau.moviedb.details.model.MovieDetails
import pl.mihau.moviedb.details.viewmodel.MovieDetailsViewModel
import pl.mihau.moviedb.list.model.Movie
import pl.mihau.moviedb.util.application.FavoritesManager
import pl.mihau.moviedb.util.databinding.contentView
import pl.mihau.moviedb.util.extension.requiredParcelable

class MovieDetailsActivity : BaseActivity() {

    private val movieId by lazy { intent.requiredParcelable<Movie>(Keys.MOVIE).id }

    private val binding by contentView<MovieDetailsActivity, ActivityMovieDetailsBinding>(R.layout.activity_movie_details)

    private val viewModel by viewModel<MovieDetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this

        viewModel.state.observe(this, Observer {
            when (it) {
                is MovieDetailsViewModel.DetailsState.DataLoaded -> setupMovieDetails(it.data)
                is MovieDetailsViewModel.DetailsState.Error -> handleError(it.throwable)
            }
        })

        init()
    }

    override fun finish() {
        setResult(Activity.RESULT_OK, Intent().apply { putExtra(Keys.MOVIE_ID, movieId.toLong()) })
        super.finish()
    }

    private fun init() {
        setupFavorite()
        getDetails()
    }

    private fun setupFavorite() {
        val favoritesManager = get<FavoritesManager>()

        binding.apply {
            isFavorite = favoritesManager.isFavorite(movieId)
            favoritesImageView.setOnClickListener {
                favoritesManager.toggleFavorite(movieId)
                isFavorite = favoritesManager.isFavorite(movieId)
            }
        }
    }

    private fun setupMovieDetails(movieDetails: MovieDetails) {
        binding.movie = movieDetails
    }

    private fun handleError(throwable: Throwable) {
        dialogManager.handleError(throwable.message, getString(R.string.go_back), buttonAction = {
            finish()
        })
    }

    private fun getDetails() {
        viewModel.invokeAction(MovieDetailsViewModel.DetailsEvent.Action.Load(movieId))
    }

    companion object {
        fun intent(context: Context, movie: Movie) = Intent(context, MovieDetailsActivity::class.java).apply {
            putExtra(Keys.MOVIE, movie)
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
    }
}