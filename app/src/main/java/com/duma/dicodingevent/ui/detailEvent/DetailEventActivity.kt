package com.duma.dicodingevent.ui.detailEvent

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.duma.dicodingevent.R
import com.duma.dicodingevent.databinding.ActivityDetailEventBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailEventBinding
    private lateinit var viewModel: DetailEventViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_event)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventId = intent.getIntExtra(EXTRA_EVENT_ID, -1)
        if (eventId == -1) {
            finish()
            return
        }

        viewModel = ViewModelProvider(this)[DetailEventViewModel::class.java]
        viewModel.checkFavoriteStatus(eventId)

        viewModel.errorMessage.observe(this) { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                viewModel.clearErrorMessage()
            }
        }

        viewModel.isFavorite.observe(this) { isFavorite ->
            binding.fabAdd.setImageResource(
                if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_unfavorite
            )
        }

        binding.fabAdd.setOnClickListener {
            viewModel.detailEvent.value?.let { event ->
                viewModel.toggleFavorite(event)
            }
        }

        viewModel.detailEvent.observe(this) { detail ->
            binding.tvName.text = detail.name
            binding.tvOwnerName.text = detail.ownerName

            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())
            val date: Date? = inputFormat.parse(detail.beginTime)
            val formattedDate = date?.let { outputFormat.format(it) + " WIB" } ?: detail.beginTime
            binding.tvTimeBegin.text = formattedDate

            binding.tvRemainingQuota.text = (detail.quota - detail.registrants).toString()
            binding.tvDescription.text = HtmlCompat.fromHtml(
                detail.description,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            Glide.with(this)
                .load(detail.mediaCover)
                .into(binding.imageViewEvent)


            binding.btnRegister.setOnClickListener {
                val url = detail.link
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(browserIntent)
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.errorMessage.observe(this) { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }
        viewModel.fetchDetailEvent(eventId)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
    }
}