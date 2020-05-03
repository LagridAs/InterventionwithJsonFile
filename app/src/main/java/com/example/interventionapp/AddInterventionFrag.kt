package com.example.interventionapp

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
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

class AddInterventionFrag : Fragment() {
    private var v:View?=null
    private var confirmer:Button?=null
    private var typeEd:EditText?=null
    private var numEd:EditText?=null
    private var plomEd:EditText?=null
    private var dtEd:EditText?=null
    private lateinit var comm:Communicator

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
        comm= activity as Communicator
        typeEd = v?.findViewById(R.id.typeEdit)
        numEd = v?.findViewById(R.id.numEdit)
        plomEd = v?.findViewById(R.id.plombierEdit)
        dtEd = v?.findViewById(R.id.dateEdit)
        confirmer= v?.findViewById(R.id.confAjou)

        val type = typeEd?.text.toString()
        val numero = Integer.parseInt(numEd?.text.toString())
        val plombier = plomEd?.text.toString()
        val date = dtEd?.text.toString()
        var interv = Intervention(numero, date, plombier, type)
        confirmer?.setOnClickListener {
            addIntervention(interv)
            comm.passListIv("")
        }
    }
        @RequiresApi(Build.VERSION_CODES.O)
        fun addIntervention(interv: Intervention){

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
            listIntervention.add(interv)
            (activity as MainActivity).createInterventions(listIntervention)

            for (item in listIntervention) {
                Log.d(ContentValues.TAG,item.numero.toString()+" "+item.date+" "+item.Type+" "+item.plombier)
            }
        }



    companion object {
        fun newInstance():AddInterventionFrag{
            return AddInterventionFrag()
        }
    }
}
