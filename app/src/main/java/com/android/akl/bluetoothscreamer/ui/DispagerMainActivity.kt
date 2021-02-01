package com.android.akl.bluetoothscreamer.ui

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import com.android.akl.bluetoothscreamer.R
import com.android.akl.bluetoothscreamer.WelcomeDialog
import com.android.akl.bluetoothscreamer.WelcomeDialog.OnDialogSubmit
import com.android.akl.bluetoothscreamer.adapter.BTNamesRecyclerAdapter
import com.android.akl.bluetoothscreamer.databinding.ActivityMainBinding
import com.android.akl.bluetoothscreamer.util.Constants.Companion.REQUEST_ENABLE_BT
import com.android.akl.bluetoothscreamer.util.Helper


class DispagerMainActivity : AppCompatActivity(), OnDialogSubmit {

    private val dispagerMainViewModel: DispagerMainViewModel by viewModels()
    private val tag = "DispagerMainActivity"
    private var mBluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private lateinit var binding: ActivityMainBinding
    private lateinit var mBTSelectedNamesRecyclerAdapter: BTNamesRecyclerAdapter
    private lateinit var mBTNamesNotSelectedRecyclerAdapter: BTNamesRecyclerAdapter
    private lateinit var dialog: WelcomeDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.toolbar.setTitleTextAppearance(this, R.style.RubikBoldTextAppearance)
        setSupportActionBar(binding.toolbar)

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if (!dispagerMainViewModel.checkSharedPreferences(this)) {
            setupSharedPreferences()
        }
        observeValues()
    }

    private fun observeValues() {
        dispagerMainViewModel.allDevicesLiveData.observe(this, {
            if (dispagerMainViewModel.selectedDevicesLiveData.value == null &&
                    dispagerMainViewModel.notSelectedDevicesLiveData.value == null &&
                    it != null)
                displayWelcomeDialog(it)
        })

        dispagerMainViewModel.selectedDevicesLiveData.observe(this, {
            if (it != null && it.isNotEmpty()){
                displaySelectedDevices(it.toList())
                binding.emptySelected.root.visibility = GONE
            }
            else{
                displaySelectedDevices(mutableListOf())
                binding.emptySelected.root.visibility = VISIBLE
            }
        })

        dispagerMainViewModel.notSelectedDevicesLiveData.observe(this, {
            if (it != null && it.isNotEmpty()){
                displayNotSelectedDevices(it.toList())
                binding.emptyNotSelected.root.visibility = GONE
            }
            else{
                displayNotSelectedDevices(mutableListOf())
                binding.emptyNotSelected.root.visibility = VISIBLE
            }
        })
    }

    private fun displayWelcomeDialog(mutableSet: MutableSet<String>){
        dialog = WelcomeDialog(this, mutableSet.toSet())
        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        dialog.window?.setLayout((width), (height))
        dialog.setDialogResult(this)
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BT) {
            mBluetoothAdapter.enable()
            dispagerMainViewModel.getPairedNames()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.edit){
            MaterialDialog(this).show {
                positiveButton(text = "Submit")
                listItemsMultiChoice(items = dispagerMainViewModel.allDevicesLiveData.value?.toList(),
                        initialSelection = dispagerMainViewModel.getSetOfChoicesIndices(), allowEmptySelection = true){ dialog, indices, items ->
                    val selectedDevices: List<String> = Helper.charSequenceToString(items)
                    val notSelectedDevices = dispagerMainViewModel.allDevicesLiveData.value!! - selectedDevices
                    dispagerMainViewModel.saveUserChoices(selectedDevices, notSelectedDevices.toList())
                }
                cancelable(false)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDialogSubmit(selectedNames: List<String>, notSelectedNames: List<String>) {
        dispagerMainViewModel.saveUserChoices(selectedNames, notSelectedNames)
        displaySelectedDevices(selectedNames)
        displayNotSelectedDevices(notSelectedNames)
        dialog.dismiss()
    }

    private fun setupSharedPreferences() {
        if (!mBluetoothAdapter.isEnabled) {
            val enableBTIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT)
        } else {
            dispagerMainViewModel.getPairedNames()
        }
    }

    private fun displaySelectedDevices(selectedNames: List<String>?) {
        mBTSelectedNamesRecyclerAdapter = BTNamesRecyclerAdapter(this, selectedNames)
        binding.btDevicesNamesRv.layoutManager = LinearLayoutManager(this)
        binding.btDevicesNamesRv.adapter = mBTSelectedNamesRecyclerAdapter
        Log.d(tag, "onDisplay: selected size" + selectedNames!!.size)
    }

    private fun displayNotSelectedDevices(notSelectedNames: List<String>?) {
        mBTNamesNotSelectedRecyclerAdapter = BTNamesRecyclerAdapter(this, notSelectedNames)
        binding.notSelectedDevicesRv.layoutManager = LinearLayoutManager(this)
        binding.notSelectedDevicesRv.adapter = mBTNamesNotSelectedRecyclerAdapter
        Log.d(tag, "onDisplay: not selected size" + notSelectedNames!!.size)
    }
}
