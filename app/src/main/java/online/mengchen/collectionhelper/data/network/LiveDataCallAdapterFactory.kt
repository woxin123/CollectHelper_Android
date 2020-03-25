package online.mengchen.collectionhelper.data.network

import androidx.lifecycle.LiveData
import online.mengchen.collectionhelper.common.ApiResult
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.lang.reflect.Parameter
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class LiveDataCallAdapterFactory : CallAdapter.Factory() {
    override fun get(returnType: Type, annotations: Array<out Annotation>?, retrofit: Retrofit?): CallAdapter<*, *> {
        val responseType: Type
        if (getRawType(returnType) != LiveData::class.java) {
            throw IllegalStateException("return type must be parameterized")
        }
        val observerType = getParameterUpperBound(0, returnType as ParameterizedType)
        val rawObservableType = getRawType(observerType)
        responseType = if (rawObservableType == LiveData::class.java) {
            if (observerType !is ParameterizedType) {
                throw IllegalStateException("Response must be paramterized")
            }
            getParameterUpperBound(0, observerType)
        } else {
            observerType
        }
        return LiveDataCallAdapter<Any>(responseType)
    }
}