package com.duma.dicodingevent.ui.finishedEvent

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.duma.dicodingevent.databinding.FragmentFinishedEventBinding

class FinishedEventFragment : Fragment() {

        private var _binding: FragmentFinishedEventBinding? = null
        private val binding get() = _binding!!

        private val viewModel: FinishedEventViewModel by viewModels()

        private lateinit var adapter: FinishedEventAdapter

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
        ): View {
            _binding = FragmentFinishedEventBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            adapter = FinishedEventAdapter()

            val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            binding.rvReview.layoutManager = layoutManager

            binding.rvReview.adapter = adapter

            viewModel.events.observe(viewLifecycleOwner) { events ->
                adapter.submitList(events)
            }

            viewModel.finishedEvent.observe(viewLifecycleOwner) { finishedEvent ->
                adapter.submitList(finishedEvent)
            }

            viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                showLoading(isLoading)
            }

            viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
                errorMessage?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }

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
        }

        private fun showLoading(isLoading: Boolean) {
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }