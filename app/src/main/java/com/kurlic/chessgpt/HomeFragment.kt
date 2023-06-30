package com.kurlic.chessgpt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class HomeFragment : Fragment()
{
    private lateinit var localGameButton: Button;
    private lateinit var gptGameButton: Button;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val rootView: View = inflater.inflate(R.layout.home_fragment, container, false)

        localGameButton = rootView.findViewById(R.id.localGameButton)
        localGameButton.setOnClickListener(View.OnClickListener
        {
            findNavController().navigate(R.id.action_HomeToLocal)
        })

        gptGameButton = rootView.findViewById(R.id.chatGPTGameButton)

        gptGameButton.setOnClickListener {
            findNavController().navigate(R.id.action_HomeFragment_to_GPTFragment)
        }


        return rootView
    }
}