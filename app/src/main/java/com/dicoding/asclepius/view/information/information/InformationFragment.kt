package com.dicoding.asclepius.view.information.information

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.data.response.ArticlesItem
import com.dicoding.asclepius.databinding.FragmentInformationBinding
import com.dicoding.asclepius.helper.OnEventClickListener
import com.dicoding.asclepius.view.ViewModelFactory
import com.dicoding.asclepius.view.information.history.HistoryViewModel
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [InformationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InformationFragment : Fragment(), OnEventClickListener {
    private var _binding: FragmentInformationBinding? = null
    private lateinit var viewModel: InformationViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInformationBinding.inflate(inflater, container, false)
        val root = binding.root

        // Initialize ViewModel
        val viewModel = ViewModelProvider(this).get(InformationViewModel::class.java)
        // Set up RecyclerView
        val recyclerView: RecyclerView = binding.rvNews
        val adapter = InformationAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Observe LiveData
        viewModel.listEventsItem.observe(viewLifecycleOwner) { articles ->
            articles?.let {
                adapter.submitList(it)
            }
        }
        viewModel.listEventsItem.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events ?: emptyList())
            Log.d("UpcomingFragment", "RecyclerView loaded with ${events?.size ?: 0} items")
            binding.progressBar.visibility = View.GONE
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.showEvents()
        }

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onEventClick(news: ArticlesItem) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(news.url))
        startActivity(intent)
        Log.d("InformationFragment", "Opening ${news.url}")
    }
}