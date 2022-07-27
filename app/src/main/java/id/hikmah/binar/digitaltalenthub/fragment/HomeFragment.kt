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
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputLayout
import id.hikmah.binar.digitaltalenthub.AuthActivity
import id.hikmah.binar.digitaltalenthub.R
import id.hikmah.binar.digitaltalenthub.adapter.JobActionListener
import id.hikmah.binar.digitaltalenthub.adapter.JobAdapter
import id.hikmah.binar.digitaltalenthub.database.JobEntity
import id.hikmah.binar.digitaltalenthub.database.UserDatabase
import id.hikmah.binar.digitaltalenthub.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPref: SharedPreferences
    private lateinit var jobAdapter: JobAdapter
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

        initRecyclerView()
        getJobData()
        welcomeUsername()
        actionLogout()
        addJob()
    }

    private fun welcomeUsername() {
        val getUsername = sharedPref.getString("USERNAME", "Default Value")
        Toast.makeText(requireContext(), "Welcome, $getUsername", Toast.LENGTH_SHORT).show()
    }

    private fun initRecyclerView() {
        binding.apply {
            jobAdapter = JobAdapter(action)
            rvJobData.adapter = jobAdapter
            rvJobData.layoutManager = LinearLayoutManager(requireContext())
        }
    }
    private fun addJob() {
        binding.btnTambah.setOnClickListener {
            showCustomDialog(null)
        }
    }

    private fun showCustomDialog(job: JobEntity?) {
        val customLayout = LayoutInflater.from(requireContext()).inflate(R.layout.layout_tambah_job, null, false)
        val dialogBuilder = AlertDialog.Builder(requireContext())

        val tvTitleDialog = customLayout.findViewById<TextView>(R.id.title_dialog)
        val etTitle = customLayout.findViewById<TextInputLayout>(R.id.edit_title)
        val etNote = customLayout.findViewById<TextInputLayout>(R.id.edit_note)
        val etSalary = customLayout.findViewById<TextInputLayout>(R.id.edit_salary)
        val btnAddUpdate = customLayout.findViewById<Button>(R.id.btn_add_update)

        if (job != null) {
            etTitle.editText?.setText(job.jobTitle)
            etNote.editText?.setText(job.desc)
            etSalary.editText?.setText(job.salary)
            tvTitleDialog.text = "Edit"
            btnAddUpdate.text = "Perbarui"
        }

        dialogBuilder.setView(customLayout)
        val dialog = dialogBuilder.create()

        btnAddUpdate.setOnClickListener {
            val title = etTitle.editText?.text.toString()
            val desc = etNote.editText?.text.toString()
            val salary = etSalary.editText?.text.toString().toInt()

            if (job != null) {
                val newJob = JobEntity(job.id, job.idUser, title, desc, salary)
                updateToDb(newJob)
            } else {
                insertToDb(title, desc, salary )
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun insertToDb(title: String, desc: String, salary: Int) {
        val getUserId = sharedPref.getInt("USERID", 0)
        val job = JobEntity(null, getUserId, title, desc,salary)
        CoroutineScope(Dispatchers.IO).launch {
            val result = userDb?.userDao()?.addJob(job)
            if (result != 0L) {
                getJobData()
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(requireContext(),
                        "Berhasil menambahkan job", Toast.LENGTH_SHORT).show()
                }
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(requireContext(),
                        "Gagal menambahkan job", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateToDb(job: JobEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = userDb?.userDao()?.updateJob(job)
            if (result != 0) {
                getJobData()
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(requireContext(),
                        "Berhasil diperbarui", Toast.LENGTH_SHORT).show()
                }
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(requireContext(),
                        "Gagal diperbarui", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getJobData() {
        val getUserId = sharedPref.getInt("USERID", 0)
        CoroutineScope(Dispatchers.IO).launch {
            val result = userDb?.userDao()?.getAllJob(getUserId)
            if (!result.isNullOrEmpty()) {
                CoroutineScope(Dispatchers.Main).launch {
                    jobAdapter.updateDataRecycler(result)
                    binding.txtNoteEmpty.isGone = true
                }
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    jobAdapter.updateDataRecycler(result!!)
                    binding.txtNoteEmpty.isGone = false
                }
            }
        }
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

    //recycle view action listener
    private val action = object : JobActionListener {
        override fun onDelete(job: JobEntity) {
//            showDeleteDialog(job)
        }

        override fun onEdit(job: JobEntity) {
            showCustomDialog(job)
        }
    }


}