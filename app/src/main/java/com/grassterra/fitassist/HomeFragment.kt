package com.grassterra.fitassist
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.grassterra.fitassist.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchView = binding.searchView
        context?.let { ctx ->
            searchView.background = ContextCompat.getDrawable(ctx, R.drawable.background_searchview)
        }
        val searchIcon: ImageView? = searchView.findViewById(androidx.appcompat.R.id.search_mag_icon)
        val closeIcon: ImageView? = searchView.findViewById(androidx.appcompat.R.id.search_close_btn)
        searchIcon?.setOnClickListener {
            searchView.setQuery("", false)
            searchView.clearFocus()
            searchIcon.setImageResource(R.drawable.search_24px)
        }
        closeIcon?.setOnClickListener {
            searchView.setQuery("", false)
            searchView.clearFocus()
            searchIcon?.setImageResource(R.drawable.search_24px)
        }
        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                searchIcon?.setImageResource(R.drawable.close_24px)
            } else {
                searchIcon?.setImageResource(R.drawable.search_24px)
            }
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(context, "Search for: $query", Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
