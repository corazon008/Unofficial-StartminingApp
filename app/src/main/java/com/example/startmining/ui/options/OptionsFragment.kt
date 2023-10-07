package com.example.startmining.ui.options

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

        val button = binding.submit
        button.setOnClickListener {
            val btc_address = binding.BTCinput.text
            val eth_address = binding.ETHinput.text
            val sharedPref = activity?.getSharedPreferences(getString(R.string.file_name), Context.MODE_PRIVATE)
            if (sharedPref != null) {
                with (sharedPref.edit()) {
                    putString(getString(R.string.btc_address), btc_address.toString())
                    putString(getString(R.string.eth_address), eth_address.toString())
                    commit()

                }
            }

        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}