package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.AppActivity
import ru.netology.nmedia.activity.NewPostFragment
//import ru.netology.nmedia.activity.SinglePostFragment
import ru.netology.nmedia.activity.textNumber
import ru.netology.nmedia.databinding.CardPostBinding

interface OnInteractionListener {
    fun onLike(post: Post)
    fun onEdit(post: Post)
    fun onRemove(post: Post)
    fun onShare(post: Post)
    fun playVideo(file: String)
    fun onPostClick(post: Post)


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
    private var index = 0

    fun bind(post: Post) = binding.apply {


        content.setOnClickListener {
            onInteractionListener.onPostClick(post)
        }

        authorAvatar.text = post.authorAvatar
        published.text = post.published
        content.text = post.content
        ivLikes.text = textNumber(post.amountLikes)
        ivShare.text = textNumber(post.amountShares)
        amountWatches.text = textNumber(post.amountWatches)

        val name = post.authorAvatar
        val url = "http://10.0.2.2:9999/avatars/$name"

        Glide.with(binding.avatar)
            .load(url)
            .placeholder(R.drawable.ic_loading_100dp)
            .circleCrop()
            .error(R.drawable.ic_error_100dp)
            .timeout(30_000)
            .into(binding.avatar)


        if (post.videoContent != null) {
            playVideo.visibility = View.VISIBLE
            contentVideo.visibility = View.VISIBLE
        } else {
            playVideo.visibility = View.GONE
            contentVideo.visibility = View.GONE
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