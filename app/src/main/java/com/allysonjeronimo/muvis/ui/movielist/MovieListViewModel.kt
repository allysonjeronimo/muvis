package com.allysonjeronimo.muvis.ui.movielist

import androidx.lifecycle.*
import com.allysonjeronimo.muvis.model.db.entity.Movie
import com.allysonjeronimo.muvis.repository.MovieRepository
import kotlinx.coroutines.launch
import com.allysonjeronimo.muvis.R

class MovieListViewModel(
    private val repository:MovieRepository
) : ViewModel() {

    private val _moviesLiveData = MutableLiveData<List<Movie>>()
    private val _isLoadingLiveData = MutableLiveData<Boolean>()
    private val _errorOnLoadingLiveData = MutableLiveData<Int>()

    val moviesLiveData:LiveData<List<Movie>>
        get() = _moviesLiveData

    val isLoadingLiveData:LiveData<Boolean>
        get() = _isLoadingLiveData

    val errorOnLoadingLiveData:LiveData<Int>
        get() = _errorOnLoadingLiveData

    fun loadMovies(){
        viewModelScope.launch {
            try{
                _isLoadingLiveData.value = true
                _moviesLiveData.value = repository.getMovies()
                _isLoadingLiveData.value = false
            }catch(ex:Exception){
                _isLoadingLiveData.value = false
                _errorOnLoadingLiveData.value = R.string.movie_list_error_on_loading
            }
        }
    }

    class MovieListViewModelFactory(
        private val repository: MovieRepository
    ) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MovieListViewModel(repository) as T
        }
    }
}