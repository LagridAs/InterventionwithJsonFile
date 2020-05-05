package com.example.interventionapp

import android.app.DatePickerDialog
import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class ConsulterFragment : Fragment() {
    private var v: View? = null
    lateinit var comm: Communicator
    private var tsBtn:Button?= null
    private var dtBtn:Button?= null
    private var dataChooseEdit:EditText?= null
    private  var dateInter:String?=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        v=inflater.inflate(R.layout.fragment_consulter, container, false)
        return v
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tsBtn= v?.findViewById(R.id.tousInterBtn)
        dtBtn= v?.findViewById(R.id.dateInterBtn)
        dataChooseEdit=v?.findViewById(R.id.chooseDate)
        comm= activity as Communicator

        tsBtn?.setOnClickListener {
            GlobalScope.launch {
            dateInter?.let { it1 -> comm.passListIv(it1) }}
        }

        dtBtn?.setOnClickListener {
            GlobalScope.launch {
                val dt= dataChooseEdit?.text.toString()
             comm.passListIv(dt) }
        }
    }

    companion object {
        fun newInstance():ConsulterFragment{
            return ConsulterFragment()
        }
    }
}
