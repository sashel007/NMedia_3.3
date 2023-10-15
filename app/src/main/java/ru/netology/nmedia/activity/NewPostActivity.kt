package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.PostEditLayoutBinding

class NewPostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = PostEditLayoutBinding.inflate(layoutInflater)
        val postContent = intent.getStringExtra(Intent.EXTRA_TEXT)
        val postId = intent.getLongExtra("postId", 0L)
        setContentView(binding.root)
        binding.edit.postDelayed({
            showKeyBoard(binding.edit)
        }, 200)
        binding.edit.requestFocus()
        binding.edit.setText(postContent)
        binding.ok.setOnClickListener {
            val content = binding.edit.text.toString()
            val resultIntent = Intent().apply {
                putExtra("postId", postId)
                putExtra(Intent.EXTRA_TEXT, content)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    fun showKeyBoard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}