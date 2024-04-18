package ru.netology.nmedia.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.constraintlayout.widget.Group
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.NewPostContract
import ru.netology.nmedia.Post
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.textNumber

interface OnInteractionListener {
    fun onLike(post: Post)
    fun onEdit(post: Post)
    fun onRemove(post: Post)
    fun onShare(post: Post)
    fun playVideo(file: String)
}


class PostsAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Post, PostViewHolder>(PostDiffUtil) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) = binding.apply {
        author.text = post.author
        published.text = post.published
        content.text = post.content
        ivLikes.text = textNumber(post.amountLikes)
        ivShare.text = textNumber(post.amountShares)
        amountWatches.text = textNumber(post.amountWatches)





        if (post.videoContent.isNotEmpty()) {
            playVideo.visibility = View.VISIBLE
            contentVideo.visibility = View.VISIBLE
        }
        ivLikes.isChecked = post.likedByMe
        ivLikes.setOnClickListener {
            onInteractionListener.onLike(post)
        }
        ivShare.setOnClickListener {
            onInteractionListener.onShare(post)
        }
        playVideo.setOnClickListener {
        onInteractionListener.playVideo(post.videoContent)
    }
        moreVert.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.options_post)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.edit -> {

                            onInteractionListener.onEdit(post)
                            true

                        }
                        R.id.remove -> {
                            onInteractionListener.onRemove(post)
                            true
                        }
                        else -> false
                    }
                }
            }.show()
        }
    }
}

object PostDiffUtil : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem

}