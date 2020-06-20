package com.example.lumen.ui.main

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.lumen.R
import java.io.InputStream
import java.io.OutputStream
import java.util.*


class ColorTabFragment : Fragment(), View.OnClickListener {
    private val bt: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private lateinit var controller: BluetoothDevice
    private lateinit var btOutStream: OutputStream
    private lateinit var btInStream: InputStream
    private val btUUID = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee")

    // status vars
    private var lightsOn: Boolean = false
    private  var currBrightness: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // connect to pi
        setupController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // root view
        val root: View = inflater.inflate(R.layout.color_tab_fragment, container, false)

        // setup brightness slider
        val brightnessBar = root.findViewById<SeekBar>(R.id.brightness_bar)
        val statusText: TextView = root.findViewById<TextView>(R.id.status_text)
        statusText.text = getString(R.string.status_text, currBrightness)
        brightnessBar.progress = currBrightness
        brightnessBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            private val status: TextView = root.findViewById(R.id.status_text)
            override fun onProgressChanged(bar: SeekBar, progress: Int,
                                           fromUser: Boolean) {
                status.text = bar.progress.toString() + "%"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(bar: SeekBar) {
                status.text = bar.progress.toString() + "%"
                btOutStream.write(bar.progress.toString().toByteArray())
            }
        })

        // setup buttons and switches
        setColorButtonOnClicks(root as ViewGroup)
        val onSwitch: Switch = root.findViewById<Switch>(R.id.on_switch)
        onSwitch.isChecked = lightsOn
        setOnOffSwitchClick(onSwitch, brightnessBar, statusText)

        return root
    }

    override fun onClick(v: View) {
        val b: Button = v as Button
        btOutStream.write(b.text.toString().toByteArray())
    }

    private fun setColorButtonOnClicks(parent: ViewGroup) {
        for (i in 0 until parent.childCount) {
            val child: View = parent.getChildAt(i)
            if (child is Button) {
                child.setOnClickListener(this)
            } else if (child is ViewGroup) {
                setColorButtonOnClicks(child)
            }
        }
    }

    private fun setOnOffSwitchClick(switchOnOff: Switch, brightnessBar: SeekBar, statusText: TextView) {
        switchOnOff.setOnCheckedChangeListener { _, isChecked ->
            brightnessBar.isEnabled = isChecked
            if (isChecked) {
                btOutStream.write("ON".toByteArray())
                statusText.text = getString(R.string.status_text, currBrightness)
            } else {
                btOutStream.write("OFF".toByteArray())
                statusText.text = "OFF"
            }
        }
    }

    private fun setupController() {
        // turn on Bluetooth
        if (!bt.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, 1)
        }

        // get pi's BluetoothDevice
        val pairedDevices: Set<BluetoothDevice> = bt.bondedDevices
        for (device in pairedDevices) {
            if (device.address == "B8:27:EB:D4:41:D0") {
                controller = device
                break
            }
        }

        // connect to pi
        val socket: BluetoothSocket = controller.createRfcommSocketToServiceRecord(btUUID)
        socket.connect()
        btOutStream = socket.outputStream
        btInStream = socket.inputStream

        // read in status data
        var buf: ByteArray = ByteArray(2)
        btInStream.read(buf)
        lightsOn = buf[0] == 0x01.toByte()
        currBrightness = buf[1].toInt()
    }

    companion object {
        @JvmStatic
        fun newInstance(): ColorTabFragment { return ColorTabFragment() }
    }
}
