package com.dicoding.asclepius.view.information

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.FragmentInformationBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InformationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InformationFragment : Fragment() {
    private var _binding: FragmentInformationBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInformationBinding.inflate(inflater, container, false)
        val root = binding.root

//        val favoriteViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireContext()))[FavoriteViewModel::class.java]
//
//        val recyclerView: RecyclerView = binding.rVFavorite
//        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        recyclerView.layoutManager = layoutManager

//        val adapter = FavoriteAdapter(onFavoriteClick = { eventEntity ->
//            // Handle the favorite click event
//            Log.d("FinishedFragmentClickTest", "Event clicked: ${eventEntity.id}")
//            eventEntity.id.let { id ->
//                Log.d("FinishedFragmentClickTest", "Navigating to DetailActivity with Event ID: $id")
//                val intentToDetail = Intent(requireActivity(), DetailActivity::class.java).apply {
//                    putExtra(EXTRA_ID, id)
//                }
//                startActivity(intentToDetail)
//            }
//        })
//        recyclerView.adapter = adapter
//
//        favoriteViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
//            // Show or hide loading indicator
//            binding.progressBar4.visibility = if (isLoading) View.VISIBLE else View.GONE
//        }
//
//        favoriteViewModel.favoriteEventsEntity.observe(viewLifecycleOwner){ events ->
//            // Update the UI with the list of favorite events
//            if (events != null) {
//                adapter.submitList(events)
//                Log.d("FavoriteFragment", "RecyclerView loaded with ${events.size} items")
//                if (events.isEmpty()){
//                    binding.noEventText.visibility = View.VISIBLE
//                    binding.eventHeader.visibility = View.GONE
//                } else {
//                    binding.noEventText.visibility = View.GONE
//                    binding.eventHeader.visibility = View.VISIBLE
//                }
//            }
//        }
//
//        favoriteViewModel.isLoading.observe(viewLifecycleOwner){ isLoading ->
//            // Show or hide loading indicator
//            binding.progressBar4.visibility = if (isLoading) View.VISIBLE else View.GONE
//        }

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}