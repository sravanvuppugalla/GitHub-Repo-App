package com.sravanapps.githubrepoapplication.presentation.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class PagingAdapter<T>(
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, PagingAdapter<T>.ViewHolder>(diffCallback) {

    private var isLoading = false
    internal var isLastPage = false

    open inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(item: T) {}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(getLayoutId(viewType), parent, false)
        return createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))

        if (!isLastPage && position == itemCount - 1 && !isLoading) {
            isLoading = true
            onLoadMore()
        }
    }

    abstract fun createViewHolder(view: View, viewType: Int): ViewHolder
    abstract fun getLayoutId(viewType: Int): Int
    abstract fun onLoadMore()

    fun setLoading(isLoading: Boolean) {
        this.isLoading = isLoading
    }

    fun setLastPage(isLastPage: Boolean) {
        this.isLastPage = isLastPage
    }
    fun getLastPage(): Boolean  = isLastPage
}
