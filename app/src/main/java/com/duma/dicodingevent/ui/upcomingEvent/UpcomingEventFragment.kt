package com.duma.dicodingevent.ui.upcomingEvent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.duma.dicodingevent.databinding.FragmentUpcomingEventBinding

class UpcomingEventFragment : Fragment() {

    private var _binding: FragmentUpcomingEventBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UpcomingEventViewModel by viewModels()

    private lateinit var adapter: UpcomingEventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUpcomingEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = UpcomingEventAdapter()

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvReview.layoutManager = layoutManager
        binding.rvReview.adapter = adapter

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    val query = searchView.text.toString().trim()
                    if (query.isNotEmpty()) {
                        searchBar.setText(query)
                        searchView.hide()
                        viewModel.searchEvents(query)
                    } else {
                        Toast.makeText(requireContext(), "Please enter a search query", Toast.LENGTH_SHORT).show()
                    }
                    false
                }
        }

        viewModel.events.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events)
        }

        viewModel.upcomingEvent.observe(viewLifecycleOwner) { upcomingEvents ->
            adapter.submitList(upcomingEvents)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
