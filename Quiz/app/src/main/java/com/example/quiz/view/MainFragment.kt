package com.example.quiz.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.quiz.R
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_start.setOnClickListener {
            it.findNavController().navigate(R.id.action_mainFragment_to_categoryFragment)
        }

        button_history.setOnClickListener {
            it.findNavController().navigate(R.id.action_mainFragment_to_historyFragment)
        }

        button_about.setOnClickListener {
            it.findNavController().navigate(R.id.action_mainFragment_to_aboutFragment)
        }
    }
}