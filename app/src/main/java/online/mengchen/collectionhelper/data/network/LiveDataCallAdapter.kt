package online.mengchen.collectionhelper.data.network

import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import online.mengchen.collectionhelper.common.ApiResult
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

class LiveDataCallAdapter<R>(private val responseType: Type) :
    CallAdapter<R, LiveData<R>> {

    private val gson = Gson()

    override fun responseType() = responseType

    override fun adapt(call: Call<R>): LiveData<R> {
        return object : LiveData<R>() {
            private var started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()
                if (started.compareAndSet(false, true)) {
                    call.enqueue(object : Callback<R> {
                        override fun onResponse(call: Call<R>, response: Response<R>) {
                            if (response.isSuccessful) {
                                postValue(response.body())
                            } else {
                                val errorBody = response.errorBody()!!
                                val res: ApiResult<*> = gson.fromJson(errorBody.string(), ApiResult::class.java)
                                postValue(res as R)
                            }
                        }

                        override fun onFailure(call: Call<R>, throwable: Throwable) {
                            postValue(null)
                        }
                    })
                }
            }
        }
    }
}