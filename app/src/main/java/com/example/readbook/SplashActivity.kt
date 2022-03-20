package com.example.readbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SplashActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    private val splasScreentimeout : Long= 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)

        firebaseAuth = FirebaseAuth.getInstance()


        val gm = AnimationUtils.loadAnimation(this, R.anim.gm);
        val btt = AnimationUtils.loadAnimation(this, R.anim.btt);

        val logo = findViewById(R.id.logo) as ImageView
        val nama = findViewById(R.id.nama) as TextView
        val pencipta = findViewById(R.id.pencipta) as TextView


        logo.startAnimation(gm)
        nama.startAnimation(gm)
        pencipta.startAnimation(btt)

        Handler().postDelayed({
            checkUser()

        }, splasScreentimeout)


    }

    private fun checkUser() {
        //get current usr, if logged in or not
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null){
            //user not logged in, gota main screen
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        else{
            //user logged in, check user type, same as done in login screen
            val ref = FirebaseDatabase.getInstance().getReference("Users")
            ref.child(firebaseUser.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {


                        //get user type e.g. user or admin
                        val userType = snapshot.child("userType").value
                        if (userType == "user"){
                            //its simpe user, open user dashboard
                            startActivity(Intent(this@SplashActivity, DashboardUserActivity::class.java))
                            finish()
                        }
                        else if (userType == "admin"){
                            //its admin, open admin dashboard
                            startActivity(Intent(this@SplashActivity, DashboardAdminActivity::class.java))
                            finish()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
        }
    }
}