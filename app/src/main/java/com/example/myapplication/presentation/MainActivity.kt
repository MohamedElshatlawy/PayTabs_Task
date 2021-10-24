package com.example.myapplication.presentation

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.domain.entities.common.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
 private lateinit var viewModel :MainActivityViewModel
    private lateinit var textView:TextView
    private lateinit var progress:ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView=findViewById(R.id.helloText)
        progress=findViewById(R.id.progress)
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        collectRepos()

    }


    private  fun collectRepos() {
        lifecycleScope.launchWhenStarted {
        viewModel.statusRepos.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collect{ s ->
            when (s) {
                is Status.Loading -> toLoading()
                is Status.Success -> {
                    dismissLoading()
                    textView.text=s.data.toString()
                    Log.d("MainAcivity", s.data.toString())
                }
                is Status.Error -> {
                    dismissLoading()
                }
            }
        }
    }
    }

    private fun dismissLoading() {
        progress.visibility= View.GONE
    }

    private fun toLoading() {
       progress.visibility= View.VISIBLE
    }
}