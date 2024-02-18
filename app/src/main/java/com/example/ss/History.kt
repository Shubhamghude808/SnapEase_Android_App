package com.example.ss

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class History : Fragment() {

    private lateinit var dbref: DatabaseReference
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<Userdata>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        userRecyclerView = view.findViewById(R.id.historylist)
        userRecyclerView.layoutManager = LinearLayoutManager(this.context)
        userRecyclerView.setHasFixedSize(true)

        userArrayList = arrayListOf<Userdata>()
        getUserData()

        return view

    }

    private fun getUserData() {
        dbref=FirebaseDatabase.getInstance().getReference("History")
        dbref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){
                    for (userSnapshot in snapshot.children){
                        val hist = userSnapshot.getValue(String::class.java)

                        // Create a Userdata object with the URL
                        val user = Userdata(hist)

                        // Add the Userdata object to the list
                        userArrayList.add(user)

                    }

                    userRecyclerView.adapter=MyAdapter(userArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


}