package com.example.lec29

import android.app.Activity
import android.content.Intent
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.lec29.databinding.ArticleListItemBinding

class NewsAdapter (val activity: Activity, val articles: List<ArticleModel>) :
RecyclerView.Adapter<NewsAdapter.ViewH>(){
    class ViewH (val binding: ArticleListItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewH {
        val a= ArticleListItemBinding.inflate(activity.layoutInflater, parent,false)
        return ViewH(a)
    }
    override fun getItemCount()= articles.size

    override fun onBindViewHolder(holder: ViewH, position: Int) {
        holder.binding.articlesText.text= articles[position].title
        Glide
            .with(holder.binding.image.context)
            .load(articles[position].urlToImage)
            .into(holder.binding.image)
        holder.binding.card.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, articles[position].url.toUri())
            activity.startActivity(i)
        }
    }



}