package com.grassterra.fitassist.ui.library

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.grassterra.fitassist.databinding.ActivityLibraryVideoBinding
import com.grassterra.fitassist.helper.ViewModelFactory
import com.grassterra.fitassist.response.ListVideoItem
import com.grassterra.fitassist.ui.mainMenu.MainMenu
import com.grassterra.fitassist.ui.adapter.LibraryVideoAdapter

class LibraryVideo : AppCompatActivity() {
    private lateinit var binding: ActivityLibraryVideoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val libraryVideoViewModel = obtainViewModel(this@LibraryVideo)

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.recyclerView.addItemDecoration(itemDecoration)

        libraryVideoViewModel.videoList.observe(this){listVideo ->
            if (listVideo != null) {
                setData(listVideo)
            }
        }
        binding.btnBack.setOnClickListener{
           GoToBack(this)
        }
    }
    private fun GoToBack(context: Context){
        val intent = Intent(context, MainMenu::class.java)
        context.startActivity(intent)
        finish()
    }

    private fun obtainViewModel(activity: AppCompatActivity): LibraryVideoViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(LibraryVideoViewModel::class.java)
    }

    private fun setData(videoList: List<ListVideoItem>) {
        if (videoList.isEmpty()) {
            return
        }
        val adapter = LibraryVideoAdapter()
        adapter.submitList(videoList)
        binding.recyclerView.adapter = adapter
    }
}