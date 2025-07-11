package com.example.startmining.ui.options

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.startmining.Bitcoin
import com.example.startmining.Datas
import com.example.startmining.R
import com.example.startmining.databinding.FragmentOptionsBinding


class OptionsFragment : Fragment() {

    private var _binding: FragmentOptionsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOptionsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.BTCinput.setText(Datas.btc_wallet)
        binding.ETHinput.setText(Datas.eth_wallet)

        val info = binding.halvingInfo.text.toString()
        binding.halvingInfo.text = info.replace("DATE", Bitcoin.date_halving)

        val button = binding.submit
        button.setOnClickListener {
            Datas.SaveWalletsAddress(activity, binding.BTCinput.text.toString(), binding.ETHinput.text.toString())
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}