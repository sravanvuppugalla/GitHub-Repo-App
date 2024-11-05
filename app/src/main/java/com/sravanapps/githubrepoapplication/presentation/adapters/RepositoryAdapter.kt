package com.sravanapps.githubrepoapplication.presentation.adapters

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import com.sravanapps.githubrepoapplication.R
import com.sravanapps.githubrepoapplication.domain.models.Repository

class RepositoryAdapter(
    private val onClick: (Repository) -> Unit,
    private val onLoadMoreCallback: () -> Unit
) : PagingAdapter<Repository>(REPOSITORY_COMPARATOR) {

    private var showLoadingIndicator = false

    override fun createViewHolder(view: View, viewType: Int): ViewHolder {
        return if (showLoadingIndicator && viewType == LOADING_VIEW_TYPE) {
            LoadingViewHolder(view)
        } else {
            RepositoryViewHolder(view, onClick)
        }
    }

    override fun getLayoutId(viewType: Int): Int {
        return if (viewType == LOADING_VIEW_TYPE) {
            R.layout.item_loading
        } else {
            R.layout.item_repository
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is RepositoryViewHolder) {
            getItem(position)?.let { holder.bind(it) }
        } else if (holder is LoadingViewHolder) {
            holder.bind()
        }
    }

    override fun getItemCount(): Int {
        // Adds an extra item for the loading indicator if it’s visible
        return super.getItemCount() + if (showLoadingIndicator) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1 && showLoadingIndicator) LOADING_VIEW_TYPE else ITEM_VIEW_TYPE
    }

    override fun onLoadMore() {
        if (!isLastPage) {
            showLoadingIndicator = true
            onLoadMoreCallback()
        }
    }

    fun setLoadingIndicator(show: Boolean) {
        if (showLoadingIndicator != show) {
            showLoadingIndicator = show
            if (show) {
                // Notify item inserted for the loading indicator
                notifyItemInserted(super.getItemCount())
            } else {
                // Notify item removed for the loading indicator
                notifyItemRemoved(super.getItemCount())
            }
        }
    }

    companion object {
        private const val ITEM_VIEW_TYPE = 1
        private const val LOADING_VIEW_TYPE = 2

        private val REPOSITORY_COMPARATOR = object : DiffUtil.ItemCallback<Repository>() {
            override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class RepositoryViewHolder(view: View, private val onClick: (Repository) -> Unit) : ViewHolder(view) {

        private val repositoryName: TextView = view.findViewById(R.id.repositoryName)
        private val repositoryDescription: TextView = view.findViewById(R.id.repositoryDescription)
        private val repositoryLanguage: TextView = view.findViewById(R.id.repositoryLanguage)
        private val repositoryStars: TextView = view.findViewById(R.id.repositoryStars)
        private val repositoryForks: TextView = view.findViewById(R.id.repositoryForks)

        override fun bind(item: Repository) {
            repositoryName.text = item.name
            repositoryDescription.text = item.description ?: "No description available"
            repositoryLanguage.text = item.language ?: "Unknown"
            repositoryStars.text = item.stars.toString()
            repositoryForks.text = item.forks.toString()

            itemView.setOnClickListener { onClick(item) }
        }
    }

    inner class LoadingViewHolder(view: View) : ViewHolder(view) {
        private val progressBar: ProgressBar = view.findViewById(R.id.progressBar)

        fun bind() {
            progressBar.visibility = View.VISIBLE
        }
    }
}
