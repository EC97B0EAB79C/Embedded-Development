package com.tkbaze.theultradeluxealarm.alarm.ui.alarmlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tkbaze.theultradeluxealarm.R
import com.tkbaze.theultradeluxealarm.alarm.AlarmViewModel
import com.tkbaze.theultradeluxealarm.alarm.AlarmViewModelFactory
import com.tkbaze.theultradeluxealarm.databinding.AlarmListFragmentBinding
import com.tkbaze.theultradeluxealarm.alarm.AlarmApplication
import com.tkbaze.theultradeluxealarm.alarm.ui.create.AlarmCreateFragment

class AlarmListFragment : Fragment() {
    private val viewModel:AlarmViewModel by activityViewModels {
        AlarmViewModelFactory((activity?.application as AlarmApplication).database.alarmDao())
    }

    private var _binding:AlarmListFragmentBinding?=null
    private val binding get()=_binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= AlarmListFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter=AlarmListAdapter(viewModel)
        adapter.setHasStableIds(true)
        binding.recyclerView.adapter=adapter

        viewModel.allAlarm.observe(this.viewLifecycleOwner){alarm->
            alarm.let {
                adapter.submitList(it)
            }
        }

        binding.recyclerView.layoutManager=LinearLayoutManager(this.context)

        // TODO item decoration

        binding.floatingActionButton.setOnClickListener {
            val fragmentTransaction  = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container,AlarmCreateFragment())
            fragmentTransaction.commit()
        }
    }
}