/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.telehealthmanager.app.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.telehealthmanager.app.R
import com.telehealthmanager.app.repositary.model.Hospital

/**
 * View Holder for a [Repo] RecyclerView list item.
 */
class RepoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private var repo: Hospital? = null

    init {
        view.setOnClickListener {

        }
    }

    fun bind(repo: Hospital?) {
        if (repo == null) {
            val resources = itemView.resources
        } else {
            showRepoData(repo)
        }
    }

    private fun showRepoData(repo: Hospital) {

    }

    companion object {
        fun create(parent: ViewGroup): RepoViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_doctor_list, parent, false)
            return RepoViewHolder(view)
        }
    }
}
