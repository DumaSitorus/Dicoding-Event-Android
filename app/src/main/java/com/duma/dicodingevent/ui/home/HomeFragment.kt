package com.duma.dicodingevent.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.duma.dicodingevent.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var finishAdapter: HomeFinishAdapter
    private lateinit var upcomingAdapter: HomeUpcomingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        finishAdapter = HomeFinishAdapter()
        upcomingAdapter = HomeUpcomingAdapter()

        val layoutManagerUpcoming = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvUpcomingEvent.layoutManager = layoutManagerUpcoming
        binding.rvUpcomingEvent.adapter = upcomingAdapter

        viewModel.upcomingEvent.observe(viewLifecycleOwner) { upcomingEvents ->
            upcomingAdapter.submitList(upcomingEvents)
        }

        val layoutManagerFinish = LinearLayoutManager(requireContext())
        binding.rvFinishEvent.layoutManager = layoutManagerFinish
        binding.rvFinishEvent.adapter = finishAdapter

        viewModel.upcomingEvent.observe(viewLifecycleOwner) { upcomingEvents ->
            val limitedEvents = upcomingEvents.take(5)
            upcomingAdapter.submitList(limitedEvents)
        }

        viewModel.finishEvent.observe(viewLifecycleOwner) { finishEvents ->
            val limitedEvents = finishEvents.take(5)
            finishAdapter.submitList(limitedEvents)
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