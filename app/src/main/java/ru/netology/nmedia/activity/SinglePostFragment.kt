package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.FeedFragment.Companion.textArg
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.databinding.FragmentSinglePostBinding
import ru.netology.nmedia.dto.Post


class SinglePostFragment(
    private val onInteractionListener: OnInteractionListener,
) : Fragment() {

    private lateinit var binding: FragmentSinglePostBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSinglePostBinding.inflate(inflater)
        return binding.root
    }

    fun bind(post: Post) {
        binding.apply {
            val newContent = arguments?.textArg
            author.text = post.author
            published.text = post.published
            content.text = newContent
            ivLikes.text = textNumber(post.amountLikes)
            ivShare.text = textNumber(post.amountShares)
            amountWatches.text = textNumber(post.amountWatches)

            if (post.videoContent.isNotEmpty()) {
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

    }
}