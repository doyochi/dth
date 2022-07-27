package id.hikmah.binar.digitaltalenthub.adapter

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import id.hikmah.binar.digitaltalenthub.R
import id.hikmah.binar.digitaltalenthub.database.JobEntity
import id.hikmah.binar.digitaltalenthub.helper.Converter

class JobAdapter(
    private val listener: JobActionListener
) : RecyclerView.Adapter<JobAdapter.JobViewHolder>() {
    val diffCallback = object: DiffUtil.ItemCallback<JobEntity>() {
        override fun areItemsTheSame(oldItem: JobEntity, newItem: JobEntity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: JobEntity, newItem: JobEntity): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private var differ = AsyncListDiffer(this, diffCallback)

    fun updateDataRecycler(job: List<JobEntity>) = differ.submitList(job)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_job, parent, false)
        return JobViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class JobViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvCompany = view.findViewById<TextView>(R.id.txt_company)
        val tvTitle = view.findViewById<TextView>(R.id.txt_title)
        val tvSalary = view.findViewById<TextView>(R.id.txt_salary)
//        val btnEdit = view.findViewById<ImageView>(R.id.btn_edit)
//        val btnDelete = view.findViewById<ImageView>(R.id.btn_delete)

//        val getUsername = sharedPref.getString("USERNAME", "Default Value")
        fun bind(job: JobEntity) {
//            tvCompany.text = getUsername
            tvTitle.text = job.jobTitle
            tvSalary.text = Converter.dollar(job.salary)
//            tvNote.text = job.desc

//            btnEdit.setOnClickListener {
//                listener.onEdit(note)
//            }
//
//            btnDelete.setOnClickListener {
//                listener.onDelete(note)
//            }
        }
    }
}

interface JobActionListener {
    fun onDelete(note: JobEntity)
    fun onEdit(note: JobEntity)
}