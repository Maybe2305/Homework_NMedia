package ru.netology.nmedia

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ru.netology.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            1,
            "Нетология. Университет интернет-профессий будущего",
            "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb/",
            "21 Мая в 18:36",
            false
        )

        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content

            amountLikes.text = "999"
            amountShares.text = "1095"
            amountWatches.text = "1000"

            if (post.likedByMe) {
                ivLikes.setImageResource(R.drawable.baseline_favorite_24)
            }

            val intAmountLikes = amountLikes.text.toString().toInt()
            var intAmountShares = amountShares.text.toString().toInt()
            val intAmountWatches = amountWatches.text.toString().toInt()

            amountLikes.text = textNumber(intAmountLikes)
            amountShares.text = textNumber(intAmountShares)
            amountWatches.text = textNumber(intAmountWatches)

            ivLikes.setOnClickListener {
                post.likedByMe = !post.likedByMe

                ivLikes.setImageResource(
                    if (post.likedByMe) R.drawable.baseline_favorite_24
                    else R.drawable.baseline_favorite_border_24
                )

                if (post.likedByMe) amountLikes.text = textNumber(intAmountLikes + 1)
                else amountLikes.text = textNumber(intAmountLikes - 1)
            }

            ivShare.setOnClickListener {
                amountShares.text = textNumber(++intAmountShares)
            }

        }


    }

    fun textNumber(number: Int): String {
        return when (number) {
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
    }
}