package id.hikmah.binar.digitaltalenthub.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputLayout
import id.hikmah.binar.digitaltalenthub.AuthActivity
import id.hikmah.binar.digitaltalenthub.R
import id.hikmah.binar.digitaltalenthub.database.UserDatabase
import id.hikmah.binar.digitaltalenthub.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPref: SharedPreferences
    private var userDb: UserDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = requireContext().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        userDb = UserDatabase.getInstance(requireContext())

        actionLogout()
    }

    private fun actionLogout() {
        binding.btnLogout.setOnClickListener {
            val editor = sharedPref.edit()
            editor.apply {
                clear()
                putBoolean("LOGIN_STATE", false)
                apply()
                startActivity(Intent(requireContext(), AuthActivity::class.java))
                requireActivity().finish()
            }

        }
    }


}