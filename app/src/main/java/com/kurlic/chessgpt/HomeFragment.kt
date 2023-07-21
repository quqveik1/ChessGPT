package com.kurlic.chessgpt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kurlic.chessgpt.game.NewGameCreateDialog

class HomeFragment : Fragment()
{
    private lateinit var localGameButton: Button;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val rootView: View = inflater.inflate(R.layout.home_fragment, container, false)

        localGameButton = rootView.findViewById(R.id.localGameButton)
        localGameButton.setOnClickListener(View.OnClickListener
        {
            findNavController().navigate(R.id.action_HomeToLocal)
        })


        val onlineGame: Button = rootView.findViewById(R.id.aiGameButton)
        onlineGame.setOnClickListener {
            val dialog = AIGameCreateDialog()

            dialog.show(requireContext(), layoutInflater)
        }


        return rootView
    }

    inner class AIGameCreateDialog : NewGameCreateDialog()
    {
        override fun onOk(bundle: Bundle, name: String)
        {
            super.onOk(bundle, name)
            findNavController().navigate(R.id.action_HomeFragment_to_AIFragment, bundle)

        }
    }
}