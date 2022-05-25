package com.tkbaze.theultradeluxealarm.alarm.ui.alarmlist

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.get
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.tkbaze.theultradeluxealarm.R
import com.tkbaze.theultradeluxealarm.alarm.AlarmViewModel
import com.tkbaze.theultradeluxealarm.database.alarm.Alarm
import com.tkbaze.theultradeluxealarm.databinding.AlarmListItemBinding
import kotlinx.coroutines.processNextEventInCurrentThread


class AlarmListAdapter(private val viewModel: AlarmViewModel) :
    ListAdapter<Alarm, AlarmListAdapter.AlarmViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlarmViewHolder {
        return AlarmViewHolder(
            AlarmListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), viewModel
        )
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            //onItemClicked(current)
        }
        holder.bind(current)
    }

    class AlarmViewHolder(
        private var binding: AlarmListItemBinding,
        private val viewModel: AlarmViewModel
    ) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor")
        fun bind(alarm: Alarm) {
            binding.apply {
                textTime.text = String.format("%02d:%02d", alarm.hour, alarm.minute)
                Log.d("AlarmViewHolder", "called " + alarm.recurring)
                switchRecurring.isChecked = alarm.recurring
                if (switchRecurring.isChecked) {
                    switchRecurring.setText(R.string.recurring)
                } else {
                    switchRecurring.setText(R.string.recurringX)
                }
                switchRecurring.setOnCheckedChangeListener { _, b ->
                    switchRecurring.isChecked = b
                    viewModel.changeAlarmRecurring(alarm, b)
                }


                switchScheduled.isChecked = alarm.set
                if (switchScheduled.isChecked) {
                    switchScheduled.setText(R.string.scheduled)
                } else {
                    switchScheduled.setText(R.string.scheduledX)
                }
                switchScheduled.setOnCheckedChangeListener { _, b ->
                    switchScheduled.isChecked = b
                    viewModel.changeAlarmSet(alarm, b)
                }

                imageDelete.setOnClickListener {
                    MaterialAlertDialogBuilder(binding.root.context)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete?")
                        .setCancelable(false)
                        .setNegativeButton("no") { _, _ -> }
                        .setPositiveButton("yes") { _, _ ->
                            viewModel.deleteAlarm(alarm)
                        }
                        .show()
                }

                textTime.setOnClickListener {
                    val picker =
                        MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H)
                            .setHour(alarm.hour)
                            .setMinute(alarm.minute)
                            .setTitleText(R.string.sel_time)
                            .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD).build()

                    picker.addOnPositiveButtonClickListener {
                        Log.d(
                            "AlarmListAdapter",
                            "time picker button" + picker.hour + ":" + picker.minute
                        )
                        viewModel.changeAlarmTime(alarm, picker.hour, picker.minute)
                    }
                    picker.show(
                        (binding.root.context as AppCompatActivity).supportFragmentManager,
                        "tag"
                    )
                }
            }
        }
    }

    companion object {

        private val DiffCallback = object : DiffUtil.ItemCallback<Alarm>() {
            override fun areItemsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
                return oldItem.id == newItem.id
                //        && oldItem.hour == newItem.hour
                //        && oldItem.minute == newItem.minute
                //        && oldItem.recurring == newItem.recurring
                //        && oldItem.set == newItem.set

            }
        }
    }
}



