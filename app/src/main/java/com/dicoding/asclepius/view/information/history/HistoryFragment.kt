package com.dicoding.asclepius.view.information.history

import android.content.Intent
import android.nfc.NfcAdapter.EXTRA_ID
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.databinding.FragmentHistoryBinding
import com.dicoding.asclepius.view.ViewModelFactory

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val historyViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireContext()))[HistoryViewModel::class.java]

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root = binding.root

        val recyclerView: RecyclerView = binding.rVHistory
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        val adapter = HistoryAdapter()
        recyclerView.adapter = adapter

        historyViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // Show or hide loading indicator
            binding.progressBar4.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        historyViewModel.favoriteEventsEntity.observe(viewLifecycleOwner){ events ->
            // Update the UI with the list of favorite events
            if (events != null) {
                adapter.submitList(events)
                Log.d("FavoriteFragment", "RecyclerView loaded with ${events.size} items")
                if (events.isEmpty()){
                    binding.noEventText.visibility = View.VISIBLE
                    binding.eventHeader.visibility = View.GONE
                } else {
                    binding.noEventText.visibility = View.GONE
                    binding.eventHeader.visibility = View.VISIBLE
                }
            } else
            {
                Log.d("FavoriteFragment", "Events are null")
            }
        }

        historyViewModel.isLoading.observe(viewLifecycleOwner){ isLoading ->
            // Show or hide loading indicator
            binding.progressBar4.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}