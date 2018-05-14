package com.oliver.weatherapp

import android.os.Handler
import android.os.Looper

import java.util.concurrent.Executor
import java.util.concurrent.Executors


class AppExecutors private constructor(
        private val diskIO: Executor,
        private val networkIO: Executor,
        private val mainThread: Executor
) {

    fun diskIO(): Executor {
        return diskIO
    }

    fun mainThread(): Executor {
        return mainThread
    }

    fun networkIO(): Executor {
        return networkIO
    }


    companion object {
        private val LOCK = Any()
        @Volatile
        private var sInstance: AppExecutors? = null

        fun getInstance(): AppExecutors =
                sInstance ?: synchronized(LOCK) {
                    sInstance ?: buildExecutors().also { sInstance = it }
                }


        private fun buildExecutors(): AppExecutors {
            return AppExecutors(Executors.newSingleThreadExecutor(),
                    Executors.newFixedThreadPool(3),
                    MainThreadExecutor())
        }
    }

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}