package online.mengchen.collectionhelper.data.network.api

import androidx.lifecycle.LiveData
import retrofit2.http.GET

interface BookMarkService {
    @GET()
    fun getBookMark(): LiveData<>
}