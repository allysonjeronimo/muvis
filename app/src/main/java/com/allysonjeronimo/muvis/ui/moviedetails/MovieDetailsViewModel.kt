package com.allysonjeronimo.muvis.ui.moviedetails

import androidx.lifecycle.*
import com.allysonjeronimo.muvis.R
import com.allysonjeronimo.muvis.model.db.entity.Movie
import com.allysonjeronimo.muvis.repository.MovieRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _movieLiveData = MutableLiveData<Movie>()
    private val _isLoadingLiveData = MutableLiveData<Boolean>()
    private val _errorOnLoadingLiveData = MutableLiveData<Int>()
    private val _errorOnUpdateLiveData = MutableLiveData<Int>()

    val movieLiveData: LiveData<Movie>
        get() = _movieLiveData

    val isLoadingLiveData:LiveData<Boolean>
        get() = _isLoadingLiveData

    val errorOnLoadingLiveData:LiveData<Int>
        get() = _errorOnLoadingLiveData

    val errorOnUpdateLiveData:LiveData<Int>
        get() = _errorOnUpdateLiveData

    fun togglesFavorite(movie:Movie){
        viewModelScope.launch {
            try{
                movie.togglesFavorite()
                repository.update(movie)
                _movieLiveData.value = movie
            }catch(ex: Exception){
                _errorOnUpdateLiveData.value = R.string.movie_details_error_on_update
            }
        }
    }

    fun loadMovieDetails(id:Int){
        try{
            _isLoadingLiveData.value = true
            compositeDisposable.add(
                repository.getMovie(id)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _movieLiveData.postValue(it)
                        },
                        {
                            it.printStackTrace()
                            _errorOnLoadingLiveData.value = R.string.movie_details_error_on_loading
                        })
            )
            _isLoadingLiveData.value = false
        }catch(ex:Exception){
            ex.printStackTrace()
            _isLoadingLiveData.value = false
            _errorOnLoadingLiveData.value = R.string.movie_details_error_on_loading
        }
    }

    class MovieDetailsViewModelFactory(
        private val repository: MovieRepository
        ) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>) =
            MovieDetailsViewModel(repository) as T
    }
}