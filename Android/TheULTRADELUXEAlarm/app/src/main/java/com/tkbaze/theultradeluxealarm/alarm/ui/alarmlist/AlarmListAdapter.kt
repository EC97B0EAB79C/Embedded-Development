package com.tkbaze.theultradeluxealarm.alarm.ui.alarmlist

import android.content.ClipData
import android.content.res.Resources
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tkbaze.theultradeluxealarm.R
import com.tkbaze.theultradeluxealarm.database.alarm.Alarm
import com.tkbaze.theultradeluxealarm.databinding.AlarmListItemBinding

class AlarmListAdapter(/*private val onItemClicked: (Alarm) -> Unit*/) :
    ListAdapter<Alarm, AlarmListAdapter.AlarmViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlarmViewHolder {
        return AlarmViewHolder(AlarmListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            //onItemClicked(current)
        }
        holder.bind(current)
    }

    class AlarmViewHolder(private var binding: AlarmListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(alarm: Alarm) {
            binding.apply {
                textTime.text = String.format("%02d:%02d", alarm.hour, alarm.minute)
                switchRecurring.isEnabled = alarm.recurring
//                switchRecurring.text =Resources.getSystem().getString(R.string.recurringX)

                switchScheduled.isEnabled = alarm.set
//                switchScheduled.text =Resources.getSystem().getString(R.string.scheduledX)
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
