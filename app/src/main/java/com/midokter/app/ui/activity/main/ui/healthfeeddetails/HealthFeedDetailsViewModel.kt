package com.midokter.doctor.ui.activity.healthfeeddetails

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.midokter.app.base.BaseViewModel
import com.midokter.app.repositary.model.ArticleResponse

class HealthFeedDetailsViewModel : BaseViewModel<HealthFeedDetailsNavigator>() {

    var mArticleResponse = MutableLiveData<ArticleResponse.Article>()

    var title : ObservableField<String> = ObservableField("")
    var description : ObservableField<String> = ObservableField("")
    var date : ObservableField<String> = ObservableField("")
}