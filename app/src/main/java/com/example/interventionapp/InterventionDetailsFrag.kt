package com.example.interventionapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class InterventionDetailsFrag : Fragment() {
    private var v:View?=null
    private lateinit var commm:Communicator
    private var supprimerBtn:Button?=null
    private var modifierBtn:Button?=null
    private var typeView:TextView?=null
    private var numeroView:TextView?=null
    private var dateView:TextView?=null
    private var plombierView:TextView?=null
    private lateinit var intervention:Intervention


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        v=inflater.inflate(R.layout.fragment_intervention_details, container, false)
        return v
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        typeView?.text=intervention.Type?.intitule
        numeroView?.text=intervention.numero.toString()
        plombierView?.text=intervention.plombier?.nom
        dateView?.text=intervention.date
    }

    fun init(){
        intervention=arguments?.getSerializable("Intervention") as Intervention
        commm= activity as Communicator
        supprimerBtn= v?.findViewById(R.id.suppIv)
        modifierBtn=v?.findViewById(R.id.modifIv)
        typeView=v?.findViewById(R.id.typeTxt)
        numeroView=v?.findViewById(R.id.numeroTxt)
        dateView=v?.findViewById(R.id.dateTxt)
        plombierView=v?.findViewById(R.id.plombierTxt)

        supprimerBtn?.setOnClickListener { supprimerDialog() }
        modifierBtn?.setOnClickListener {
            GlobalScope.launch {
                val frag=EditInterventionFrag()
                intervention.let { it1 -> commm.passInterv(it1,frag) }}
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun supprimerDialog() {
        val alertDialog: AlertDialog
        val inflater: LayoutInflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.dialog_supprimer_view, null)

        val confirmerBtn = dialogView.findViewById<Button>(R.id.confSupp)
        confirmerBtn.setOnClickListener {
            GlobalScope.launch {
            intervention.let { it1 -> suppIntervention(it1) }}
        }
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
        dialogBuilder.setOnDismissListener {commm.passListIv("")}
        dialogBuilder.setView(dialogView)
        alertDialog = dialogBuilder.create()
        //alertDialog.window!!.getAttributes().windowAnimations = R.style.PauseDialogAnimation
        alertDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun suppIntervention(inter:Intervention)
    {
        var jsonString:String?=(activity as MainActivity?)?.readFromFile()
        var listIntervention= mutableListOf<Intervention>()
        var jsonArray= JSONArray(jsonString)
        var gson= Gson()
        var jsonObject= JSONObject()
        Log.d(ContentValues.TAG,"avant boucle ")
        for (i in 0 until jsonArray.length()) {
            jsonObject = jsonArray.getJSONObject(i)
            listIntervention.add(gson.fromJson(jsonObject.toString(), Intervention::class.java))
        }
        listIntervention.remove(inter)
        GlobalScope.launch {(activity as MainActivity?)?.createInterventions(listIntervention)}
    }



    companion object {
        fun newInstance():InterventionDetailsFrag {
            return InterventionDetailsFrag()
        }
    }
}
