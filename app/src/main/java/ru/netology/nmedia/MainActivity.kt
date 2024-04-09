package ru.netology.nmedia

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val viewModel: PostViewModel by viewModels()
        val adapter = PostsAdapter( object : OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.cancelEdit(binding, post)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
            }

        }
        )
        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            val newPost = posts.size > adapter.currentList.size
            adapter.submitList(posts) {
                if (newPost) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }

        viewModel.edited.observe(this) {
            if (it.id != 0L) {
                binding.content.setText(it.content)
                binding.content.focusAndShowKeyboard()
            }
        }

        binding.save.setOnClickListener {
            val content = binding.content.text.toString()
            if (content.isBlank()) {
                Toast.makeText(this, R.string.error_empty_content, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            viewModel.changeContentAndSave(content)

            binding.content.setText("")
            binding.content.clearFocus()
            AndroidUtils.hideKeyboard(binding.content)
        }

        fun cancelEdit(post: Post) {
            binding.group.visibility = View.VISIBLE
            binding.messageBeforeEdited.text = post.content
            binding.cancelEdit.setOnClickListener {
                binding.group.visibility = View.GONE
                binding.content.setText("")
                binding.content.clearFocus()
                AndroidUtils.hideKeyboard(binding.content)
                viewModel.editDefault()
            }
            viewModel.edit(post)
        }
    }

}



fun textNumber(number: Int): String {
    val convertedNumber = when (number) {
        in 1000..1099 -> "${number / 1000}K"
        in 1100..9999 -> {
            val thousands = number / 1000
            val hundreds = (number % 1000) / 100
            "$thousands.$hundreds K"
        }

        in 10000..999999 -> "${number / 1000}K"
        in 1_000_000..Int.MAX_VALUE -> {
            val thousands = number / 1000000
            val hundreds = (number % 1000000) / 100000
            "$thousands.$hundreds M"
        }

        else -> number.toString()
    }
    return convertedNumber
}

