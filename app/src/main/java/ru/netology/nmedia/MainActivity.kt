package ru.netology.nmedia

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.databinding.CardPostBinding

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        val adapter = PostsAdapter(
            likeListener = { viewModel.likeById(it.id) },
            shareListener = { viewModel.shareById(it.id) }
        )
        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
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

