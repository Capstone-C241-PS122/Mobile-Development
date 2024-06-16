package com.grassterra.fitassist.ui.history

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.grassterra.fitassist.database.history.HistoryItem
import com.grassterra.fitassist.databinding.ActivityHistoryBinding
import com.grassterra.fitassist.helper.ViewModelFactory
import com.grassterra.fitassist.ui.mainMenu.MainMenu
import com.grassterra.fitassist.ui.adapter.HistoryItemAdapter

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val historyViewModel = obtainViewModel(this@HistoryActivity)
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.recyclerView.addItemDecoration(itemDecoration)
        historyViewModel.historyList.observe(this){listHistory ->
            setData(listHistory)
        }
        
        binding.btnBack.setOnClickListener{
            GoToBack(this)
        }
    }
    private fun GoToBack(context: Context){
        val intent = Intent(context, MainMenu::class.java)
        context.startActivity(intent)
    }

    private fun obtainViewModel(activity: AppCompatActivity): HistoryViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(HistoryViewModel::class.java)
    }

    private fun setData(historyList: List<HistoryItem>) {
        if (historyList.isEmpty()) {
            return
        }
        val adapter = HistoryItemAdapter()
        adapter.submitList(historyList)
        binding.recyclerView.adapter = adapter
    }
}