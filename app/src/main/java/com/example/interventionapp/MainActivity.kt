package com.example.interventionapp

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.*


class MainActivity : AppCompatActivity(),Communicator {
    lateinit var typeList:MutableList<TypeIv>
    lateinit var plomList:MutableList<Plombier>
    lateinit var interventions:MutableList<Intervention>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        typeList= mutableListOf<TypeIv>(
            TypeIv("Reparation"),
            TypeIv("Depannage"),
            TypeIv("Installation"),
            TypeIv("Entretien")

        )

         plomList= mutableListOf<Plombier>(
            Plombier("Lagrid Houssem") ,
            Plombier("Lagrid Zakaria"),
            Plombier("Madani Anes"),
            Plombier("Djellab Mohamed")
        )
         interventions= mutableListOf<Intervention>(
            Intervention("1", "02/05/2020",plomList[1],typeList[1]),
            Intervention("2", "03/05/2020",plomList[2],typeList[2]),
            Intervention("3", "04/05/2020",plomList[3],typeList[3])
        )
        GlobalScope.launch {
            createInterventions(interventions)
        }

            var fragment = ConsulterFragment()
        supportFragmentManager.beginTransaction().replace(R.id.mainAct,fragment).commit()
    }

    fun readFromFile(): String? {
        var ret = ""
        var fis: FileInputStream? = null
        try {
            val file = File(filesDir, "intervention.json")
            if (!file.exists()) {
                Toast.makeText(this, "Failed: file does not exist", Toast.LENGTH_LONG).show()
            }
            else {
                fis = FileInputStream(file)
                if (fis != null) {
                    val inputStreamReader = InputStreamReader(fis)
                    val bufferedReader = BufferedReader(inputStreamReader)
                    var receiveString: String? = ""
                    val stringBuilder = StringBuilder()
                    while (bufferedReader.readLine().also { receiveString = it } != null) {
                        stringBuilder.append(receiveString)
                    }
                    ret = stringBuilder.toString()
                }
            }
        } catch (e: FileNotFoundException) {
            Log.e("login activity", "File not found: $e")
        } catch (e: IOException) {
            Log.e("login activity", "Can not read file: $e")
        } finally {
            try {
                fis!!.close()
                Log.d(ContentValues.TAG,"close file")

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return ret
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createInterventions(listInter:MutableList<Intervention>){
        var gson = GsonBuilder()
            .setPrettyPrinting()
            .create()

        var jsonString = gson.toJson(listInter)
        try {
            val yourFileName="intervention.json"
            val yourFilePath = this.filesDir.toString() + "/" + yourFileName
            val yourFile = File(yourFilePath)
            val fileout =
                openFileOutput(yourFileName, Context.MODE_PRIVATE)
            val outputWriter = OutputStreamWriter(fileout)
            outputWriter.write(jsonString)
            outputWriter.close()
            fileout.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getIntervention():MutableList<Intervention>
    {
        var jsonString:String?=readFromFile()
        var listIntervention= mutableListOf<Intervention>()
        var jsonArray= JSONArray(jsonString)
        var gson= Gson()
        var jsonObject= JSONObject()
        Log.d(ContentValues.TAG,"avant boucle ")
        for (i in 0 until jsonArray.length()) {
            jsonObject = jsonArray.getJSONObject(i)
            listIntervention.add(gson.fromJson(jsonObject.toString(), Intervention::class.java))
        }
        for (item in listIntervention) {
            print("rani dakhal la boucle ta3 get Intervention")
            Log.d(ContentValues.TAG,item.numero+" "+ item.date+" "+ item.Type?.intitule+" "+item.plombier?.nom)
        }
        Log.d(ContentValues.TAG,"kharej getIntervention")
        return listIntervention
    }
    fun getInterventionDt(date:String):MutableList<Intervention>
    {
        var jsonString:String?=readFromFile()
        var listIntervention= mutableListOf<Intervention>()
        var jsonArray= JSONArray(jsonString)
        var gson= Gson()
        var it:Intervention
        var jsonObject= JSONObject()
        Log.d(ContentValues.TAG,"avant boucle ")
        for (i in 0 until jsonArray.length()) {
            jsonObject = jsonArray.getJSONObject(i)
            it=gson.fromJson(jsonObject.toString(), Intervention::class.java)
            if(it.date==date){
                listIntervention.add(it)
            }
        }
        for (item in listIntervention) {
            Log.d(ContentValues.TAG,item.numero+" "+item.date+" "+item.Type+" "+item.plombier)
        }
        return listIntervention
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun passListIv(date: String) {

        val bundle1 = Bundle()
        val listIv:Serializable
        if(date==""){
            Log.d(ContentValues.TAG,"dakhallll pass List Iv")
            listIv= getIntervention() as ArrayList<Parcelable>
        }
        else{
            listIv= getInterventionDt(date) as ArrayList<Parcelable>

        }
        bundle1.putParcelableArrayList("ListInter",listIv)
        //bundle1.putSerializable("ListInter",listIv)
        val transaction = this.supportFragmentManager.beginTransaction()
        val frag = InterventionList()
        frag.arguments = bundle1
        transaction.replace(R.id.mainAct, frag)
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()}

    override fun passInterv(inter: Intervention,frag:Fragment) {
        val bundle1 = Bundle()
        bundle1.putSerializable("Intervention",inter)
        val transaction = this.supportFragmentManager.beginTransaction()
        //val frag = InterventionDetailsFrag()
        frag.arguments = bundle1
        transaction.replace(R.id.mainAct, frag)
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()
    }
}
