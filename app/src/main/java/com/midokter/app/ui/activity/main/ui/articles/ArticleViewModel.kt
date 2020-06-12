package com.midokter.app.ui.activity.main.ui.articles

import androidx.lifecycle.MutableLiveData
import com.midokter.app.base.BaseViewModel
import com.midokter.app.repositary.AppRepository
import com.midokter.app.repositary.model.ArticleResponse

class ArticleViewModel : BaseViewModel<ArticleNavigator>(){
    private val appRepository = AppRepository.instance()

    var mArticleResponse = MutableLiveData<ArticleResponse>()
    var loadingProgress = MutableLiveData<Boolean>()
    var mAllArticles :MutableList<ArticleResponse.Article>? = arrayListOf()

    fun getArticles() {
        loadingProgress.value = true
        getCompositeDisposable().add(appRepository.getArticles(this))
    }
}