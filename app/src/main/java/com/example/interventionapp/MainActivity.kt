package com.example.interventionapp

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.util.function.Predicate


class MainActivity : AppCompatActivity(),Communicator {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var listInter= mutableListOf<Intervention>(
            Intervention(1, "02/05/2020","Mohamed","Reparation"),
            Intervention(2, "03/05/2020","Imad","Entretien"),
            Intervention(3, "04/05/2020","Houssem","Installation")
        )
        createInterventions(listInter)
        dateBtn.setOnClickListener {
            val str="03/05/2020"
            Log.d(ContentValues.TAG,"avant ")

            val listDate:MutableList<Intervention> = getInterventionDt(str)
            for (item in listDate){
                Log.d(ContentValues.TAG,"Les intervention de la date" + str + "sont:")
                Log.d(ContentValues.TAG,"numero:" + item.numero.toString() + "date:"+ item.date + "plombier:"+
                        item.plombier + "type:"+ item.Type)

            }

        }

        addBtn.setOnClickListener {
            //addIntervention(4,"09-05-2020","Zakaria","Reparation")
        }
        removeBtn.setOnClickListener {
            suppIntervention(1,"02/05/2020","Mohamed","Reparation")
        }
        //var fragment = ConsulterFragment()
        //supportFragmentManager.beginTransaction().replace(R.id.mainAct,fragment).commit()
    }
    private fun readInternalDemo() {
        val file = File(filesDir, "intervention.json")
        if (!file.exists()) {
            Toast.makeText(this, "Failed: file does not exist", Toast.LENGTH_LONG).show()
            return
        }
        var fis: FileInputStream? = null
        var textContent = ""
        try {
            fis = FileInputStream(file)
            val br = BufferedReader(InputStreamReader(fis))
            textContent = br.readLine()
        } catch (e: java.lang.Exception) {
            Toast.makeText(this, "Failed: " + e.message, Toast.LENGTH_LONG).show()
        } finally {
            if (fis != null) {
                Toast.makeText(this, "Read Successfully: $textContent", Toast.LENGTH_LONG).show()
                try {
                    fis.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Failed to read!", Toast.LENGTH_LONG).show()
                }
            }
        }
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

            //display file saved message
            Toast.makeText(
                baseContext, "File saved successfully!",
                Toast.LENGTH_SHORT
            ).show()
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
            Log.d(ContentValues.TAG,item.numero.toString()+" "+item.date+" "+item.Type+" "+item.plombier)
        }
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
            Log.d(ContentValues.TAG,item.numero.toString()+" "+item.date+" "+item.Type+" "+item.plombier)
        }
        return listIntervention
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun suppIntervention(numero:Int, date:String, plombier:String, Type:String)
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

                /*val condition: Predicate<Intervention> =
                    Predicate<Intervention> { interv: Intervention ->
                        (interv.numero==numero)
                    }*/
        val it=Intervention(numero,date,plombier,Type)
        listIntervention.remove(it)
        createInterventions(listIntervention)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun passListIv(date: String) {
        val bundle1 = Bundle()
        val listIv:Serializable = if(date==""){
            getIntervention() as Serializable

        } else{
            getInterventionDt(date) as Serializable
        }
        bundle1.putSerializable("ListInter",listIv)
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
