package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.intent.NewPostResultContract
import ru.netology.nmedia.recyclerview.OnInteractionListener
import ru.netology.nmedia.recyclerview.PostAdapter
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModel by viewModels<PostViewModel>()

        val newPostLauncher = registerForActivityResult(NewPostResultContract()) { result ->
            result ?: return@registerForActivityResult
            val (postId, content) = result
            if (postId == 0L) {
                viewModel.addNewPost(content)
            } else {
                viewModel.updatePost(postId, content)
            }
        }
        val adapter = PostAdapter(object : OnInteractionListener {
            override fun like(post: Post) {
                viewModel.like(post.id)
            }

            override fun remove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun edit(post: Post) {
                newPostLauncher.launch(post.id to post.content)
            }

            override fun share(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }

                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }

            override fun playVideo(videoUrl: String?) {
                val intent = Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse(videoUrl)
                }

                val resolveActivity = intent.resolveActivity(packageManager)
                if (resolveActivity != null) {
                    Log.d("ResolveActivity", "$resolveActivity")
                } else {
                    Log.d("ResolveActivity", "null")
                }

                val activities = packageManager.queryIntentActivities(intent, 0)
                if (activities.isNotEmpty()) {
                    val names = activities.map { it.activityInfo.name }
                    Log.d("QueryIntentActivities", "$names")
                } else {
                    Log.d("QueryIntentActivities", "null")
                }
                val shareIntent = Intent.createChooser(intent, "Выберите приложение")
                startActivity(shareIntent)
            }
        })

        binding.postList?.layoutManager = LinearLayoutManager(this)
        binding.postList?.adapter = adapter
        viewModel.data.observe(this) { posts ->
            val newPost = posts.size > adapter.currentList.size
            adapter.submitList(posts) {
                if (newPost) binding.postList?.smoothScrollToPosition(0)
            }
        }
        binding.addButton?.setOnClickListener {
            newPostLauncher.launch(0L to "")
        }
    }


}