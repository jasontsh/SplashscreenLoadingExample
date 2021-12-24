package com.jasontsh.interviewkickstart.loadingexample

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.*
import androidx.lifecycle.Observer

/**
 * A fragment representing a list of Items.
 */
class ItemFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)
        val model: MyViewModel by activityViewModels()
        val recyclerView: RecyclerView = view.findViewById(R.id.list)
        val failureTextView: TextView = view.findViewById(R.id.fail_tv)
        model.data.observe(this, Observer {
            val dataGood = it.isNotEmpty()
            if (dataGood) {
                with(recyclerView) {
                    layoutManager = LinearLayoutManager(context)
                    adapter = MyItemRecyclerViewAdapter(it.toList())
                }
            }
        })
        model.failure.observe(this, Observer{
            if (it && model.data.value?.isNotEmpty() != true) {
                failureTextView.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                failureTextView.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        })
        return view
    }
}