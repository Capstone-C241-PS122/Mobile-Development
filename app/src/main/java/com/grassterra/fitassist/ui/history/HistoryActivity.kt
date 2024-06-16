package com.grassterra.fitassist.ui.history

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.grassterra.fitassist.database.history.HistoryItem
import com.grassterra.fitassist.databinding.ActivityHistoryBinding
import com.grassterra.fitassist.databinding.AlertCheckDeleteBinding
import com.grassterra.fitassist.helper.ViewModelFactory
import com.grassterra.fitassist.ui.adapter.HistoryItemAdapter
import com.grassterra.fitassist.ui.mainMenu.MainMenu
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var adapter: HistoryItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        historyViewModel = obtainViewModel(this@HistoryActivity)
        adapter = HistoryItemAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        binding.recyclerView.adapter = adapter

        historyViewModel.historyList.observe(this) { listHistory ->
            adapter.submitList(listHistory)
        }

        binding.btnBack.setOnClickListener {
            goToBack(this)
        }

        binding.showDialogButton.setOnClickListener {
            showYesNoDialog(historyViewModel, this)
        }
    }

    private fun showYesNoDialog(historyViewModel: HistoryViewModel, context: Context) {
        val dialogBinding = AlertCheckDeleteBinding.inflate(layoutInflater)
        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()

        dialogBinding.btnYes.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    historyViewModel.clearHistory()
                }
                alertDialog.dismiss()
                context.startActivity(Intent(context, HistoryActivity::class.java))
                finish()
            }
        }

        dialogBinding.btnNo.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun goToBack(context: Context) {
        val intent = Intent(context, MainMenu::class.java)
        context.startActivity(intent)
    }

    private fun obtainViewModel(activity: AppCompatActivity): HistoryViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(HistoryViewModel::class.java)
    }
}
