package com.example.foodie_guardv0.Activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.foodie_guard0.R
import com.google.android.material.bottomnavigation.BottomNavigationView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [search_Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class search_Fragment : Fragment() {
    companion object {
        fun newInstance(iconId: Int): home_fragment {
            val fragment = home_fragment()
            val args = Bundle()
            args.putInt("iconId", iconId)
            fragment.arguments = args
            return fragment
        }
    }
        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el diseño del fragmento
        val view = inflater.inflate(R.layout.fragment_search_, container, false)


        return view
    }
}