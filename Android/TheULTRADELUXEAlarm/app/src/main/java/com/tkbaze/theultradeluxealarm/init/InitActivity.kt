package com.tkbaze.theultradeluxealarm.init

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.tkbaze.theultradeluxealarm.R
import com.tkbaze.theultradeluxealarm.alarm.AlarmActivity
import com.tkbaze.theultradeluxealarm.data.SettingsDataStore
import com.tkbaze.theultradeluxealarm.databinding.ActivityInitBinding
import com.tkbaze.theultradeluxealarm.init.ui.light.InitLightFragment
import com.tkbaze.theultradeluxealarm.init.ui.location.InitLocationFragment
import com.tkbaze.theultradeluxealarm.init.ui.motion.InitMotionFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/*
runs initial settings for the app
1. check if sensors exists
2. get check if sensor works appropriately
3. obtain threshold for each sensor
 */
class InitActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInitBinding

    private val viewModel: InitViewModel by viewModels()

    private lateinit var SettingsDataStore: SettingsDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityInitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        SettingsDataStore = SettingsDataStore(applicationContext)

        binding.buttonNext.setOnClickListener {
            viewModel.nextProgress()
            updateUI()
        }

        binding.buttonPrev.setOnClickListener {
            viewModel.prevProgress()
            updateUI()
        }

        binding.buttonFin.setOnClickListener {
            GlobalScope.launch {
                SettingsDataStore.saveInitializedToPreferencesStore(true, applicationContext)
            }
            val intent: Intent = Intent(this, AlarmActivity::class.java)
            startActivity(intent)
            finish()
        }

        updateUI()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (viewModel.progress != 0) {
                supportFragmentManager.popBackStack()
                viewModel.prevProgress()
                updateUI()
                return true
            } else {
                finish()
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    /*
        UI for InitActivity
        1. set progress bar accordingly
        2. hide and show navigation buttons according to current process
         */
    private fun updateUI() {
        // Progress bar
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            binding.progressBar.setProgress(
                ((viewModel.progress.toFloat() / viewModel.totalProgress.toFloat()) * 100).toInt(),
                true
            )
        } else {
            binding.progressBar.progress =
                ((viewModel.progress.toFloat() / viewModel.totalProgress.toFloat()) * 100).toInt()
        }

        // Previous button enable
        binding.buttonPrev.isEnabled = viewModel.progress > 0

        // Next and Finish button visibility
        if (viewModel.progress == viewModel.totalProgress) {
            binding.buttonFin.animate()
            binding.buttonFin.visibility = Button.VISIBLE
            binding.buttonNext.visibility = Button.INVISIBLE
        } else {
            binding.buttonFin.visibility = Button.INVISIBLE
            binding.buttonNext.visibility = Button.VISIBLE
        }

        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        when (viewModel.progress) {
            0 -> transaction.replace(R.id.settingFragment, InitLightFragment())
            1 -> {
                transaction.replace(R.id.settingFragment, InitMotionFragment())
                transaction.addToBackStack("BackStackInit")
            }
            2 -> {
                transaction.replace(R.id.settingFragment, InitLocationFragment())
                transaction.addToBackStack("BackStackInit")
            }
        }

        transaction.commit()
    }
}