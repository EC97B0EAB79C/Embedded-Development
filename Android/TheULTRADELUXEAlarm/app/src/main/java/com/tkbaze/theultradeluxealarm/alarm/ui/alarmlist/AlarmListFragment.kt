package com.tkbaze.theultradeluxealarm.alarm.ui.alarmlist

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tkbaze.theultradeluxealarm.R
import com.tkbaze.theultradeluxealarm.alarm.AlarmViewModel
import com.tkbaze.theultradeluxealarm.alarm.AlarmViewModelFactory
import com.tkbaze.theultradeluxealarm.databinding.AlarmListFragmentBinding
import com.tkbaze.theultradeluxealarm.alarm.AlarmApplication
import com.tkbaze.theultradeluxealarm.alarm.ui.create.AlarmCreateFragment
import com.tkbaze.theultradeluxealarm.data.SettingsDataStore
import com.tkbaze.theultradeluxealarm.init.InitActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AlarmListFragment : Fragment() {
    private val viewModel: AlarmViewModel by activityViewModels {
        AlarmViewModelFactory(
            activity?.application!!,
            (activity?.application as AlarmApplication).database.alarmDao()
        )
    }

    private var _binding: AlarmListFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var SettingsDataStore: SettingsDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AlarmListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SettingsDataStore = SettingsDataStore(requireContext())
        val adapter = AlarmListAdapter(viewModel)
        adapter.setHasStableIds(true)
        binding.recyclerView.adapter = adapter

        viewModel.allAlarm.observe(this.viewLifecycleOwner) { alarm ->
            alarm.let {
                adapter.submitList(it)
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)

        // TODO item decoration

        binding.floatingActionButton.setOnClickListener {
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container, AlarmCreateFragment())
            fragmentTransaction.addToBackStack( "BackStackAlarm" )
            fragmentTransaction.commit()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.layout_menu,menu)
        val layoutButton = menu.findItem(R.id.action_switch_layout)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.action_switch_layout -> {
                MaterialAlertDialogBuilder(binding.root.context)
                    .setTitle(R.string.action_settings)
                    .setMessage(R.string.action_settings_question)
                    .setCancelable(false)
                    .setNegativeButton(R.string.no) { _, _ -> }
                    .setPositiveButton(R.string.yes) { _, _ ->
                        GlobalScope.launch {
                            SettingsDataStore.saveInitializedToPreferencesStore(false,requireContext())
                        }
                        val intent= Intent(requireContext(),InitActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                    .show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }
}