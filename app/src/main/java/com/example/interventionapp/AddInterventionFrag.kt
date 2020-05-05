package com.example.interventionapp

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class AddInterventionFrag : Fragment() {
    var v:View?=null
    var numEdit:EditText?= null
    var tySpinner:Spinner?= null
    var plSpinner:Spinner?= null
    var dtImage:EditText?=null
    var confBtn:Button?=null
    var comm:Communicator?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_add_intervention, container, false)
        return v
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        confBtn?.setOnClickListener {
            val numIv= numEdit?.text.toString()
            val dateeIv = dtImage?.text.toString()
                    //
            var typeIv:TypeIv?= null
            var plomIv:Plombier?= null

            val ty: String = tySpinner?.selectedItem.toString()
            val plom: String = plSpinner?.selectedItem.toString()
            for(item in (activity as MainActivity).plomList)
            {
                if(item.nom==plom){
                    plomIv=item
                }
            }
            for(item in (activity as MainActivity).typeList)
            {
                if(item.intitule==ty){
                    typeIv=item
                }
            }

            val inter=Intervention(numIv,dateeIv,plomIv,typeIv)
                    //
            addIntervention(inter)

        }
    }
    fun init(){
        comm= activity as Communicator
        numEdit= v?.findViewById(R.id.numEditAj)
        dtImage= v?.findViewById(R.id.dateImgAj)
        confBtn= v?.findViewById(R.id.confAjou)
        //spinner
        val plombierNames:ArrayList<String> = arrayListOf()
        val plombiers = (activity as MainActivity).plomList
        for (element in plombiers){
            plombierNames.add(element.nom)
        }

        // Initializing an ArrayAdapter
        val adapter = context?.let {
            ArrayAdapter(
                it, // Context
                android.R.layout.simple_spinner_item, // Layout
                plombierNames // Array
            )
        }

        // Set the drop down view resource
        adapter?.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        // Finally, data bind the spinner object with dapter
        plSpinner= v?.findViewById<Spinner>(R.id.plSpinnerAj)
        plSpinner?.adapter = adapter
        //fin spinner
        //spinner
        val typeNames:ArrayList<String> = arrayListOf()
        val types = (activity as MainActivity).typeList
        for (element in types){
            typeNames.add(element.intitule)
        }

        // Initializing an ArrayAdapter
        val adapterT = context?.let {
            ArrayAdapter(
                it, // Context
                android.R.layout.simple_spinner_item, // Layout
                typeNames // Array
            )
        }

        // Set the drop down view resource
        adapterT?.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        // Finally, data bind the spinner object with dapter
        tySpinner= v?.findViewById<Spinner>(R.id.typeSpinnerAj)
        tySpinner?.adapter = adapterT
        //fin spinner
    }
         @RequiresApi(Build.VERSION_CODES.O)
        fun addIntervention(intervention: Intervention){

            var jsonString:String?=(activity as MainActivity).readFromFile()
            var listIntervention= mutableListOf<Intervention>()
            var jsonArray= JSONArray(jsonString)
            var gson= Gson()
            var jsonObject= JSONObject()
            Log.d(ContentValues.TAG,"avant boucle ")
            for (i in 0 until jsonArray.length()) {
                jsonObject = jsonArray.getJSONObject(i)
                listIntervention.add(gson.fromJson(jsonObject.toString(), Intervention::class.java))
            }
            listIntervention.add(intervention)
            GlobalScope.launch {  (activity as MainActivity).createInterventions(listIntervention)
                comm?.passListIv("")
            }

        }



    companion object {
        fun newInstance():AddInterventionFrag{
            return AddInterventionFrag()
        }
    }
}
