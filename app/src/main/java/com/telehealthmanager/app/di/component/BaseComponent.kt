package com.telehealthmanager.app.di.component


import com.telehealthmanager.app.di.modules.NetworkModule
import com.telehealthmanager.app.repositary.BaseRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface BaseComponent {
    fun inject(repository: BaseRepository)
}
