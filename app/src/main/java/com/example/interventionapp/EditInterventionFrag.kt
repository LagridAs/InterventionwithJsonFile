package com.example.interventionapp

import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

class EditInterventionFrag : Fragment() {
    private var v:View?=null
    private var confirmer: Button?=null
    private var typeEd: EditText?=null
    private var numEd: TextView?=null
    private var plomEd: EditText?=null
    private var dtEd: EditText?=null
    private lateinit var comm:Communicator
    private var intervention= arguments?.getSerializable("Intervention") as Intervention
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
        comm= activity as Communicator
        typeEd = v?.findViewById(R.id.typeEditMod)
        numEd = v?.findViewById(R.id.numEditMod)
        plomEd = v?.findViewById(R.id.plombierEditMod)
        dtEd = v?.findViewById(R.id.dateEditMod)
        confirmer=v?.findViewById(R.id.confEdit)

        typeEd?.text = Editable.Factory.getInstance().newEditable(intervention.Type)
        numEd?.text = intervention.numero.toString()
        plomEd?.text = Editable.Factory.getInstance().newEditable(intervention.plombier)
        dtEd?.text = Editable.Factory.getInstance().newEditable(intervention.date)

        val type = typeEd?.text.toString()
        val numero = intervention.numero
        val plombier = plomEd?.text.toString()
        val date = dtEd?.text.toString()
        var intervNv = Intervention(numero, date, plombier, type)
        confirmer?.setOnClickListener {
            editIntervention(intervention,intervNv)
            comm.passListIv("")
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun editIntervention(inter:Intervention,interNv:Intervention)
    {
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

        /*val condition: Predicate<Intervention> =
            Predicate<Intervention> { interv: Intervention ->
                (interv.numero==numero)
            }*/
        listIntervention.find {it.numero == inter.numero  }?.Type =interNv.Type
        listIntervention.find {it.numero == inter.numero  }?.date =interNv.date
        listIntervention.find {it.numero == inter.numero  }?.plombier =interNv.plombier

        (activity as MainActivity).createInterventions(listIntervention)
    }

    companion object {
        fun newInstance():EditInterventionFrag{
            return EditInterventionFrag()
        }

    }
}
