package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.FeedFragment.Companion.textArg
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostViewHolder
import ru.netology.nmedia.databinding.FragmentSinglePostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel


//class SinglePostFragment : Fragment() {
//    companion object {
//        private const val TEXT_KEY = "TEXT_KEY"
//        var Bundle.textArg: String?
//            set(value) = putString(TEXT_KEY,value)
//            get() = getString(TEXT_KEY)
//    }
//
//    private lateinit var binding: FragmentSinglePostBinding
//    val viewModel: PostViewModel by viewModels(
//        ownerProducer = ::requireParentFragment
//    )
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentSinglePostBinding.inflate(inflater)
//
//        viewModel.data.observe(viewLifecycleOwner) { model ->
//            val post = posts.find { model.id == arguments?.textArg?.toLong() } ?: return null
//        }
//
//
//
//            PostViewHolder(binding.singlePost, object : OnInteractionListener {
//                override fun onLike(post: Post) {
//                    viewModel.likeById(post.id)
//                }
//
//                override fun onEdit(post: Post) {
//                    findNavController().navigate(
//                        R.id.action_singlePostFragment_to_newPostFragment,
//                        Bundle().apply {
//                            textArg = post.content
//                        }
//                    )
//                    viewModel.edit(post)
//                }
//
//                override fun onRemove(post: Post) {
//                    viewModel.removeById(post.id)
//                    findNavController().navigateUp()
//                }
//
//                override fun onShare(post: Post) {
//                    val intent = Intent().apply {
//                        action = Intent.ACTION_SEND
//                        putExtra(Intent.EXTRA_TEXT, post.content)
//                        type = "text/plain"
//                    }
//                    val shareIntent =
//                        Intent.createChooser(intent, "Share post")
//                    startActivity(shareIntent)
//                }
//
//                override fun playVideo(file: String) {
//                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(file))
//                    startActivity(intent)
//                }
//
//                override fun onPostClick(post: Post) {
//
//                }
//
//            }).bind(post)
//        return binding.root
//    }
//
//    }