package com.example.pegaproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pegaproject.model.Stock
import com.example.pegaproject.databinding.FragmentHomeBinding
import com.example.pegaproject.databinding.StockSummaryViewBinding
import com.example.pegaproject.model.Status
import com.example.pegaproject.utils.HomeViewModel


interface StockListOnClickListener {
    fun onStockClicked(symbol: String)
}

class HomeFragment : Fragment(R.layout.fragment_home), StockListOnClickListener {

    lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by activityViewModels()
    private val stockListAdapter = StockAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)
        binding.stockList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = stockListAdapter
        }
        viewModel.stockLiveData.observe(viewLifecycleOwner, {
            if (it.status == Status.SUCCESS) {
                stockListAdapter.update(it.map!!)
            } else {
                Toast.makeText(context, "API CALL ERROR", Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.getFavStock()
    }

    override fun onStockClicked(symbol: String) {
        val bundle = bundleOf("SYMBOL" to symbol)
        findNavController().navigate(R.id.homeFragment_to_detailFragment, bundle)
    }
}

class StockAdapter(private val onClickListener: StockListOnClickListener) :
    RecyclerView.Adapter<StockAdapter.ItemViewHolder>() {

    private val stocks = mutableListOf<Stock>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockAdapter.ItemViewHolder {
        val binding =
            StockSummaryViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StockAdapter.ItemViewHolder, position: Int) {
        with(holder.binding) {
            val curr = stocks[position]
            symbol.text = curr.symbol
            companyName.text = curr.name
            currentPrice.text = "$" + curr.price.toString()
            favoriteSection.setOnClickListener {
                onClickListener.onStockClicked(curr.symbol)
            }
        }
    }

    override fun getItemCount(): Int {
        return stocks.size
    }

    fun update(map: Map<String, Stock>) {
        stocks.clear()
        map.forEach {
            it.value.symbol = it.key
            stocks.add(it.value)
        }
        notifyDataSetChanged()
    }

    class ItemViewHolder(
        val binding: StockSummaryViewBinding
    ) : RecyclerView.ViewHolder(binding.root)
}

