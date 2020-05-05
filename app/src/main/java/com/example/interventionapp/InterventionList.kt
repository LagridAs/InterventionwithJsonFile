package com.example.interventionapp

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_intervention_list.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class InterventionList : Fragment() {
    private lateinit var interadap:IntervAdapter
    private lateinit var layoutMgr: LinearLayoutManager
    lateinit var dataList:MutableList<Intervention>
    private var v:View?= null
    private lateinit var communicator: Communicator
    private var ajouter: Button?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v= inflater.inflate(R.layout.fragment_intervention_list, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataList = mutableListOf()
        ajouter=v?.findViewById(R.id.ajouterIv)
        Log.d(ContentValues.TAG,"Dakhellllll on view created")
        dataList= arguments?.getParcelableArrayList<Intervention>("ListInter") as MutableList<Intervention>
        Log.d(ContentValues.TAG,"avant boucle of dalalist")
        for (item in dataList)
        {
            Log.d(ContentValues.TAG,"datalist" + item.plombier?.nom + item.Type?.intitule + item.numero)
        }
        interadap= IntervAdapter(dataList)
        layoutMgr= LinearLayoutManager(activity)
        recyclerInterv.apply {
            adapter=interadap
            layoutManager= layoutMgr
        }
        communicator = activity as Communicator
        interadap.onItemClick = { intervention ->
            GlobalScope.launch {
            val frag=InterventionDetailsFrag()
            communicator.passInterv(intervention,frag)}
        }
        ajouter?.setOnClickListener {
            GlobalScope.launch {
            val fragment=AddInterventionFrag()
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.mainAct,fragment).commit()}
        }
    }

    companion object {
        fun newInstance():InterventionList= InterventionList()
    }
}
