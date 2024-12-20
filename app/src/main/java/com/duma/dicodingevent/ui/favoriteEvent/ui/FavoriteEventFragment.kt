package com.duma.dicodingevent.ui.favoriteEvent.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.duma.dicodingevent.databinding.FragmentFavoriteEventBinding

class FavoriteEventFragment : Fragment() {

    private var _binding: FragmentFavoriteEventBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteEventViewModel by viewModels()
    private lateinit var adapter: FavoriteEventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FavoriteEventAdapter()
        binding.rvReview.layoutManager = LinearLayoutManager(requireContext())
        binding.rvReview.adapter = adapter

        viewModel.favoriteEvents.observe(viewLifecycleOwner) { favoriteEvents ->
            adapter.submitList(favoriteEvents)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
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

