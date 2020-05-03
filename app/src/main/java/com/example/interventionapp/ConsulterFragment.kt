package com.example.interventionapp

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class ConsulterFragment : Fragment() {
    private var v: View? = null
    lateinit var comm: Communicator
    private var tsBtn:Button?= null
    private var dtBtn:Button?= null
    private var dataChooseImg:ImageView?= null
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
        dataChooseImg=v?.findViewById(R.id.chooseDate)
        comm= activity as Communicator

        tsBtn?.setOnClickListener {
            dateInter?.let { it1 -> comm.passListIv(it1) }
        }
        dataChooseImg?.setOnClickListener{
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(activity!!, DatePickerDialog.OnDateSetListener {
                    view, year, monthOfYear, dayOfMonth ->
                dateInter= "$dayOfMonth-$monthOfYear-$year"
                Toast.makeText(context,"$dayOfMonth-$monthOfYear-$year".toString(),Toast.LENGTH_SHORT).show()
            }, year, month, day)
            dpd.show()
        }

        dtBtn?.setOnClickListener {
            dateInter?.let { it1 -> comm.passListIv(it1) }
        }
    }

    companion object {
        fun newInstance():ConsulterFragment{
            return ConsulterFragment()
        }
    }
}
