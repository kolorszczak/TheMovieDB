package pl.mihau.moviedb.util.application

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import pl.mihau.moviedb.common.Keys
import pl.mihau.moviedb.common.Strings

class FavoritesManager(private val sharedPreferences: SharedPreferences, private val gson: Gson){

    fun toggleFavorite(id: Int): Boolean {
        getFavorites().let { currentFav ->
           when (id) {
               in currentFav -> currentFav.removeAt(currentFav.indexOf(id))
               !in currentFav -> currentFav.add(id)
           }

            return sharedPreferences.edit()
                .putString(Keys.FAVORITES, gson.toJson(currentFav))
                .commit()
        }
    }

    fun isFavorite(id: Int) = id in getFavorites()

    private fun getFavorites() = when(val hasStoredFavorites = !sharedPreferences.getString(Keys.FAVORITES, Strings.empty).isNullOrEmpty()) {
        hasStoredFavorites -> gson.fromJson<MutableList<Int>>(sharedPreferences.getString(Keys.FAVORITES, Strings.empty), object : TypeToken<MutableList<Int>>() {}.type)
        else -> mutableListOf()
    }
}