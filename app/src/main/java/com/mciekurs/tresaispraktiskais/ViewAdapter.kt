package com.mciekurs.tresaispraktiskais

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.recycler_view_single_repo.view.*

class ViewAdapter(private val userRepos: List<UserRepos>) : RecyclerView.Adapter<ViewAdapter.CosViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CosViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_view_single_repo, parent, false)
        return CosViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        Log.d("UserRepocCount", userRepos.size.toString())
        return userRepos.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CosViewHolder, position: Int) {
        val repos = userRepos[position]

        holder.view.textView_repoName.text = repos.name
        holder.view.textView_size.text = "${repos.size} KB"
        Glide.with(holder.view.context).load(repos.owner.avatar_url).into(holder.view.imageView_userImage)

        holder.view.setOnClickListener {
            val webBrowser = Intent(Intent.ACTION_VIEW, Uri.parse(repos.html_url))
            holder.view.context.startActivity(webBrowser)
        }

    }

    class CosViewHolder(val view: View): RecyclerView.ViewHolder(view)

}