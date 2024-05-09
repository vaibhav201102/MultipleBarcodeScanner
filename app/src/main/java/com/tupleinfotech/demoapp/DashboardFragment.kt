package com.tupleinfotech.demoapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.tupleinfotech.demoapp.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding : FragmentDashboardBinding ?= null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(LayoutInflater.from(context))

        init()

        return binding.root
    }

    private fun init(){
        onScannerButtonPressed()
        onBackPressed()
    }

    private fun onScannerButtonPressed(){
        binding.btnOpenScanner.setOnClickListener {
            findNavController().navigate(R.id.scannerFragment)
        }
    }

    private fun onBackPressed(){
        val onBackPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                activity?.finishAffinity()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }
}