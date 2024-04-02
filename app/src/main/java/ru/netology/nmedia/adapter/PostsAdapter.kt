package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.Post
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.textNumber

typealias OnLikeListener = (Post) -> Unit
typealias OnShareListener = (Post) -> Unit

class PostsAdapter(
    private val likeListener: OnLikeListener,
    private val shareListener: OnShareListener
) : ListAdapter<Post, PostViewHolder>(PostDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, likeListener, shareListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val likeListener: OnLikeListener,
    private val shareListener: OnShareListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) = binding.apply {
        author.text = post.author
        published.text = post.published
        content.text = post.content
        amountLikes.text = textNumber(post.amountLikes)
        amountShares.text = textNumber(post.amountShares)
        amountWatches.text = textNumber(post.amountWatches)

        ivLikes.setImageResource(
            if (post.likedByMe) R.drawable.baseline_favorite_24 else R.drawable.baseline_favorite_border_24
        )
        ivLikes.setOnClickListener {
            likeListener(post)
        }
        ivShare.setOnClickListener {
            shareListener(post)
        }
    }
}

object PostDiffUtil : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem

}