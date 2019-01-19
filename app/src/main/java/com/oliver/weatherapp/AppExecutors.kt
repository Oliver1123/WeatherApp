package com.oliver.weatherapp

import android.os.Handler
import android.os.Looper
import org.koin.dsl.module.module
import java.util.concurrent.Executor
import java.util.concurrent.Executors


val utilsModule = module {
    single {
        AppExecutors(
            Executors.newSingleThreadExecutor(),
            Executors.newFixedThreadPool(3),
            AppExecutors.MainThreadExecutor()
        )
    }
}

class AppExecutors(
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

    class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}