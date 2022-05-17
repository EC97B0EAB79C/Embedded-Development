package com.tkbaze.raspberrypiledsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.ToggleButton
import com.tkbaze.raspberrypiledsample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    private lateinit var binding: ActivityMainBinding

    companion object{
        const val LED1=0
        const val LED2=1
        const val LED3=2
        const val LED4=3
        const val LED5=4
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toggleButtonLed1.setOnCheckedChangeListener(this);
        binding.toggleButtonLed2.setOnCheckedChangeListener(this);
        binding.toggleButtonLed3.setOnCheckedChangeListener(this);
        binding.toggleButtonLed4.setOnCheckedChangeListener(this);
        binding.toggleButtonLed5.setOnCheckedChangeListener(this);
    }

    private var ledNum=0
    private var stat=0;

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        when(p0!!.id){
            binding.toggleButtonLed1.id->ledNum=LED1
            binding.toggleButtonLed2.id->ledNum=LED2
            binding.toggleButtonLed3.id->ledNum=LED3
            binding.toggleButtonLed4.id->ledNum=LED4
            binding.toggleButtonLed5.id->ledNum=LED5
        }
        if(p1){
            stat=1;
        }
        else{
            stat=0;
        }
        val task:RaspberryPiLedHttpGetTask= RaspberryPiLedHttpGetTask(this)
        task.execute(ledNum,stat)
    }
}