package ru.netology.nmedia

import android.view.View
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    published = "",
    videoContent = ""
)
class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.getAll()
    val edited = MutableLiveData(empty)

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContentAndSave(text: String) {
        edited.value?.let {
            if (it.content != text.trim()) {
                repository.save(it.copy(content = text))
            }

        }
        edited.value = empty
    }
    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)
    fun editDefault() {
        edited.value = empty
    }

}