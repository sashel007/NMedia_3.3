package ru.netology.nmedia.intent

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import ru.netology.nmedia.activity.NewPostActivity

class NewPostResultContract : ActivityResultContract<Pair<Long, String>, Pair<Long, String>?>() {

    override fun createIntent(context: Context, input: Pair<Long, String>): Intent =
        Intent(context, NewPostActivity::class.java).apply {
            putExtra("postId", input.first)
            putExtra(Intent.EXTRA_TEXT, input.second)
        }


    override fun parseResult(resultCode: Int, intent: Intent?): Pair<Long, String>? =
        if (resultCode != Activity.RESULT_OK) null
        else Pair(
            intent?.getLongExtra("postId", 0L) ?: 0L,
            intent?.getStringExtra(Intent.EXTRA_TEXT) ?: ""
        )

}


