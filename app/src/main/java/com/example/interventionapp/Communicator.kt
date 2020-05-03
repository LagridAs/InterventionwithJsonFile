package com.example.interventionapp

import androidx.fragment.app.Fragment
import java.time.LocalDate

interface Communicator {
    fun passListIv(date: String)
    fun passInterv(inter: Intervention,frag:Fragment)

}