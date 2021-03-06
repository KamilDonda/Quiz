package com.example.quiz.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.quiz.R
import com.example.quiz.view_model.adapters.AnswerAdapter
import com.example.quiz.view_model.vm.CategoryViewModel
import com.example.quiz.view_model.vm.DifficultyLevelViewModel
import com.example.quiz.view_model.vm.QuestionViewModel
import com.example.quiz.view_model.vm.ResultViewModel
import kotlinx.android.synthetic.main.fragment_question.*


class QuestionFragment : Fragment() {

    private lateinit var viewModel: QuestionViewModel
    private lateinit var viewModelResult: ResultViewModel
    private lateinit var viewModelCategory: CategoryViewModel
    private lateinit var viewModelDifficulty: DifficultyLevelViewModel
    private lateinit var myAdapter: AnswerAdapter
    private lateinit var recyclerView: RecyclerView

    private val TIME = 20000
    private lateinit var progressBar: ProgressBar
    private lateinit var animator: ValueAnimator

    private lateinit var timerSound: MediaPlayer

    private var isReleased = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {}
        })

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(requireActivity()).get(QuestionViewModel::class.java)
        viewModelResult = ViewModelProvider(requireActivity()).get(ResultViewModel::class.java)
        viewModelCategory = ViewModelProvider(requireActivity()).get(CategoryViewModel::class.java)
        viewModelDifficulty =
            ViewModelProvider(requireActivity()).get(DifficultyLevelViewModel::class.java)

        return inflater.inflate(R.layout.fragment_question, container, false)
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timerSound = MediaPlayer.create(context, R.raw.timer)
        timerSound.isLooping = true
        timerSound.start()

        timer()

        viewModel.quizList.observe(viewLifecycleOwner, Observer {
            myAdapter = AnswerAdapter(
                viewModel.getCurrentAnswers(),
                viewModel,
                viewModelResult,
                Html.fromHtml(viewModel.getCurrentCorrectAnswer(), Html.FROM_HTML_MODE_LEGACY)
                    .toString(),
                requireContext(),
                viewLifecycleOwner,
                view,
                animator,
                viewModelCategory.currentCategory.value?.id ?: 0,
                viewModelDifficulty.currentDifficultyLevel.value!!,
                timerSound
            )
            myAdapter.notifyDataSetChanged()

            textView_Question.text =
                Html.fromHtml(viewModel.getCurrentQuestion(), Html.FROM_HTML_MODE_LEGACY)
            textView_currentQuestion.text =
                "${getString(R.string.question)} ${viewModel.currentQuizNumber + 1}"
            textViewCategory.text =
                Html.fromHtml(viewModel.getCurrentCategory(), Html.FROM_HTML_MODE_LEGACY)

            recyclerView = recyclerView_answer.apply {
                adapter = myAdapter
            }
        })
    }

    private fun timer() {
        progressBar = view?.findViewById(R.id.progressBar_timer) as ProgressBar

        animator = ValueAnimator.ofInt(viewModel.progressStatus, progressBar.max)
        animator.duration = TIME.toLong()
        animator.addUpdateListener { animation ->
            progressBar.progress = (animation.animatedValue as Int)
            viewModel.incrementProgressStatus(progressBar.progress)
        }
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)

                if (progressBar.progress == progressBar.max) {
                    viewModel.setTimeIsUp(true)
                    timerSound.release()
                    MediaPlayer.create(context, R.raw.time_is_up).start()
                }
            }
        })
        animator.start()
    }

    override fun onPause() {
        super.onPause()
        animator.pause()
        timerSound.release()
        isReleased = true
    }

    override fun onResume() {
        super.onResume()
        animator.resume()
        if (isReleased) {
            timerSound = MediaPlayer.create(context, R.raw.timer)
            timerSound.isLooping = true
            timerSound.start()

            isReleased = false
        }
    }
}