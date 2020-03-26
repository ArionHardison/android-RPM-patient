package com.midokter.app.repositary

import com.midokter.app.BaseApplication
import com.midokter.app.data.PreferenceHelper


class BaseModuleRepository : BaseRepository() {

    // var listData: ArrayList<CreationData>? = null
    var preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)

    /* //getAssests api
     fun getAssests(model: LibraryViewModel): Disposable {
         return BaseRepository().createApiClient(BaseWebService::class.java)
             .getAssests()
             .observeOn(AndroidSchedulers.mainThread())
             .subscribeOn(Schedulers.io())
             .subscribe({
                 model.assestsItems.value = it.categories
             }, {
                 model.errorResponse.value = it.message
             })
     }*/


/*    fun getDataList(): ArrayList<CreationData> {
        listData = ArrayList<CreationData>()
        listData?.add(CreationData(1, "Test Name", "Test Post Title", 23, 34, 43, 90))
        listData?.add(CreationData(2, "Test Name", "Test Post Title", 23, 34, 43, 90))
        listData?.add(CreationData(3, "Test Name", "Test Post Title", 23, 34, 43, 90))
        listData?.add(CreationData(4, "Test Name", "Test Post Title", 23, 34, 43, 90))
        listData?.add(CreationData(5, "Test Name", "Test Post Title", 23, 34, 43, 90))
        return listData!!
    }*/


    companion object {
        private var appRepository: BaseModuleRepository? = null

        fun instance(): BaseModuleRepository {
            if (appRepository == null) synchronized(BaseModuleRepository) {
                appRepository = BaseModuleRepository()
            }
            return appRepository!!
        }
    }


}
