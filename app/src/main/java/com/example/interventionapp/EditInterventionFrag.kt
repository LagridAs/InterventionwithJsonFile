package com.example.interventionapp

import android.app.DatePickerDialog
import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class EditInterventionFrag : Fragment() {
    private var v:View?=null
    var numEdit:TextView?= null
    var typeEdit:EditText?= null
    var plEdit:EditText?= null
    var dtEdit:EditText?=null
    var confBtn:Button?=null
    private lateinit var comm:Communicator
    private lateinit var intervention:Intervention
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_intervention, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        intervention= arguments?.getSerializable("Intervention") as Intervention
        init()
        confBtn?.setOnClickListener {
            val numIv= intervention.numero
            val ty= typeEdit?.text.toString()
            val plom= plEdit?.text.toString()
            val dateIv= dtEdit?.text.toString()
            var plombierIv:Plombier?=null
            var typeIv:TypeIv?=null

            for(item in (activity as MainActivity).plomList)
            {
                if(item.nom==plom){
                    plombierIv=item
                }
            }
            for(item in (activity as MainActivity).typeList)
            {
                if(item.intitule==ty){
                    typeIv=item
                }
            }
            val NV= Intervention(numIv,dateIv,plombierIv,typeIv)
            editIntervention(intervention,NV)

        }



    }
    fun init(){
        comm= activity as Communicator
        numEdit= v?.findViewById(R.id.numTxMod)
        typeEdit=v?.findViewById(R.id.typeMod)
        plEdit=v?.findViewById(R.id.plombierMod)
        dtEdit=v?.findViewById(R.id.dateMod)
        confBtn=v?.findViewById(R.id.confEdit)

        numEdit?.text= intervention.numero
        typeEdit?.text = Editable.Factory.getInstance().newEditable(intervention.Type?.intitule.toString())
        plEdit?.text = Editable.Factory.getInstance().newEditable(intervention.plombier?.nom.toString())
        dtEdit?.text = Editable.Factory.getInstance().newEditable(intervention.date.toString())
    }
    fun editIntervention(inter:Intervention,interNv:Intervention)
    {
        var jsonString:String?=(activity as MainActivity).readFromFile()
        var listIntervention= mutableListOf<Intervention>()
        var jsonArray= JSONArray(jsonString)
        var gson= Gson()
        var jsonObject= JSONObject()
        Log.d(ContentValues.TAG,"avant boucle edit")
        for (i in 0 until jsonArray.length()) {
            jsonObject = jsonArray.getJSONObject(i)
            listIntervention.add(gson.fromJson(jsonObject.toString(), Intervention::class.java))
        }

        listIntervention.find {it.numero == inter.numero  }?.Type =interNv.Type
        listIntervention.find {it.numero == inter.numero  }?.date =interNv.date
        listIntervention.find {it.numero == inter.numero  }?.plombier =interNv.plombier
        for(item in listIntervention )
        {
            Log.d(ContentValues.TAG,item.date + item.Type?.intitule + item.numero + item.plombier?.nom)
        }

        GlobalScope.launch {(activity as MainActivity).createInterventions(listIntervention)
            val frag= InterventionDetailsFrag()
            comm.passListIv("")}
    }

    companion object {
        fun newInstance():EditInterventionFrag{
            return EditInterventionFrag()
        }

    }

}
