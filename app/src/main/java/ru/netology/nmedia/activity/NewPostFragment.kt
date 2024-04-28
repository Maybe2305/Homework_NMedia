package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.EXTRA_TEXT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.FeedFragment.Companion.textArg
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.viewmodel.PostViewModel
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class NewPostFragment : Fragment() {

    companion object {
        private const val TEXT_KEY = "TEXT_KEY"
        var Bundle.textArg: String?
            set(value) = putString(TEXT_KEY,value)
            get() = getString(TEXT_KEY)
    }


    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNewPostBinding.inflate(
            inflater,
            container,
            false
        )
        arguments?.textArg
            ?.let(binding.content::setText)
        binding.content.requestFocus()
        binding.save.setOnClickListener {
           viewModel.changeContentAndSave(binding.content.text.toString())
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigate(R.id.action_newPostFragment_to_feedFragment)
        }
        binding.cancelButton.setOnClickListener {
            findNavController().navigate(R.id.action_newPostFragment_to_feedFragment)
        }
        return binding.root
    }

}

object StringArg: ReadWriteProperty<Bundle, String?> {

    override fun setValue(thisRef: Bundle, property: KProperty<*>, value: String?) {
        thisRef.putString(property.name, value)
    }

    override fun getValue(thisRef: Bundle, property: KProperty<*>): String? =
        thisRef.getString(property.name)
}