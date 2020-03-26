package com.midokter.app.di.component


import com.midokter.app.di.modules.NetworkModule
import com.midokter.app.repositary.BaseRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface BaseComponent {
    fun inject(repository: BaseRepository)
}
