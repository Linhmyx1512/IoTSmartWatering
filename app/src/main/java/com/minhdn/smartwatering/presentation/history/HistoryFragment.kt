package com.minhdn.smartwatering.presentation.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.minhdn.smartwatering.data.local.entity.HistoryEntity
import com.minhdn.smartwatering.data.local.repo.HistoryRepository
import com.minhdn.smartwatering.databinding.FragmentHistoryBinding
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {

    private val binding: FragmentHistoryBinding by lazy {
        FragmentHistoryBinding.inflate(layoutInflater)
    }

    private val repository = HistoryRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvAdd.setOnClickListener {
            val historyEntity = HistoryEntity(
                id = 0,
                time = System.currentTimeMillis(),
                content = "This is a history"
            )
            viewLifecycleOwner.lifecycleScope.launch {
                repository.insertHistory(historyEntity)
            }
        }
    }
}