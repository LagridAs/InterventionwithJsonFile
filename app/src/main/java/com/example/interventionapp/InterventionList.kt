package com.example.interventionapp

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

class InterventionList : Fragment() {
    private lateinit var interadap:IntervAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var dataList:MutableList<Intervention>
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
        ajouter=v?.findViewById(R.id.ajouterIv)
        dataList= arguments?.getSerializable("ListInter") as MutableList<Intervention>
        interadap= IntervAdapter(dataList)
        layoutManager= LinearLayoutManager(activity)
        recyclerInterv.apply {
            adapter=interadap
            layoutManager= layoutManager
        }
        communicator = activity as Communicator
        interadap.onItemClick = { intervention ->
            val frag=InterventionDetailsFrag()
            communicator.passInterv(intervention,frag)
        }
        ajouter?.setOnClickListener {
            val fragment=AddInterventionFrag()
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.mainAct,fragment).commit()
        }
    }

    companion object {
        fun newInstance():InterventionList= InterventionList()
    }
}
