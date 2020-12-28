package com.telehealthmanager.app.repositary.pagination

import androidx.paging.PagingSource
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.repositary.ApiInterface
import com.telehealthmanager.app.repositary.BaseRepository
import com.telehealthmanager.app.repositary.model.Hospital
import com.telehealthmanager.app.ui.activity.findDoctors.FindDoctorsViewModel
import com.telehealthmanager.app.ui.activity.searchDoctor.SearchViewModel

class DoctorDataSource(
    private val apiRepository: BaseRepository,
    private val default: Int,
    private val searchViewModel: SearchViewModel
) : PagingSource<Int, Hospital>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Hospital> {
        return try {
            val nextPageNumber = params.key ?: default
            val response = apiRepository.createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java).getDoctorsList(nextPageNumber)
            searchViewModel.postCount(response.search_doctors.size)
            LoadResult.Page(
                data = response.search_doctors,
                prevKey = if (default > 0) response.search_doctors[response.search_doctors.size - default].id else null,
                nextKey = if (response.search_doctors.isNotEmpty()) response.search_doctors[response.search_doctors.size - 1].id else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

class DoctorSearchDataSource(
    private val apiRepository: BaseRepository,
    private val query: String,
    private val searchViewModel: SearchViewModel
) : PagingSource<Int, Hospital>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Hospital> {
        return try {
            val nextPageNumber = params.key ?: 0
            val response = apiRepository.createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java).getDoctorsList(nextPageNumber, query)
            searchViewModel.postSearchCount(response.search_doctors.size)
            LoadResult.Page(
                data = response.search_doctors,
                prevKey = if (nextPageNumber > 0) response.search_doctors[response.search_doctors.size - 1].id else null,
                nextKey = if (response.search_doctors.isNotEmpty()) response.search_doctors[response.search_doctors.size - 1].id else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

class FindDoctorSource(
    private val apiRepository: BaseRepository,
    private val query: String,
    private val findDoctorsViewModel: FindDoctorsViewModel
) : PagingSource<Int, Hospital>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Hospital> {
        return try {
            val nextPageNumber = params.key ?: 0
            val response = apiRepository.createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java).getDoctorsList(nextPageNumber, query)
            LoadResult.Page(
                data = response.search_doctors,
                prevKey = if (nextPageNumber > 0) response.search_doctors[response.search_doctors.size - 1].id else null,
                nextKey = if (response.search_doctors.isNotEmpty()) response.search_doctors[response.search_doctors.size - 1].id else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

