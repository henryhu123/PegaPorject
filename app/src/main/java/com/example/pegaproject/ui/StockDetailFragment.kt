package com.example.pegaproject.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.pegaproject.utils.HomeViewModel
import com.example.pegaproject.R
import com.example.pegaproject.model.Status
import com.example.pegaproject.databinding.FragmentStockDetailBinding


/**
 * A simple [Fragment] subclass.
 * Use the [StockDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StockDetailFragment : Fragment(R.layout.fragment_stock_detail) {

    private val targetSymbol by lazy {
        arguments?.getString("SYMBOL")
    }

    private val viewModel: HomeViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setBarTitle(targetSymbol!!)

        val binding = FragmentStockDetailBinding.bind(view)
        viewModel.stockLiveData.observe(viewLifecycleOwner, {
            if (it.status == Status.SUCCESS) {
                val target = it.map?.get(targetSymbol)
                if (target != null) {
                    with(binding) {
                        name.text = target.name
                        symbol.text = targetSymbol
                        currentPrice.text = "$" + target.price.toString()
                        dailyLow.text = "$" + target.low.toString()
                        dailyHigh.text = "$" + target.high.toString()
                    }
                }
            }
        })
    }

}