package com.telehealthmanager.app.ui.activity.main.ui.articles

import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.ArticleResponse

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