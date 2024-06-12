package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val shares: Int = 0,
    val videoContent: String
) {
    fun toDto() = Post(id, author, content, published, likedByMe, likes, shares, videoContent = videoContent)

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(
                dto.id,
                dto.authorAvatar,
                dto.content,
                dto.published,
                dto.likedByMe,
                dto.amountLikes,
                dto.amountShares,
                dto.videoContent,
            )
    }
}
