package com.example.quiz.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.quiz.R
import com.example.quiz.view_model.adapters.DifficultyLevelAdapter
import com.example.quiz.view_model.vm.CategoryViewModel
import com.example.quiz.view_model.vm.DifficultyLevelViewModel
import kotlinx.android.synthetic.main.fragment_difficulty_level.*

class DifficultyLevelFragment : Fragment() {

    private lateinit var viewModel: DifficultyLevelViewModel
    private lateinit var viewModelCategory: CategoryViewModel
    private lateinit var myAdapter: DifficultyLevelAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val levels = listOf("Easy", "Medium", "Hard", "All")

        viewModel = ViewModelProvider(requireActivity()).get(DifficultyLevelViewModel::class.java)
        viewModelCategory = ViewModelProvider(requireActivity()).get(CategoryViewModel::class.java)
        myAdapter = DifficultyLevelAdapter(levels, viewModel, requireActivity(), requireContext())

        return inflater.inflate(R.layout.fragment_difficulty_level, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = recyclerView_difficulty.apply {
            adapter = myAdapter
        }

        textViewSelectedCategory.text = viewModelCategory.currentCategory.value!!.name
    }
}