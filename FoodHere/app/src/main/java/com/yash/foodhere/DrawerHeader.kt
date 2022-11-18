package com.yash.foodhere

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class DrawerHeader : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var Navigationview: NavigationView
    lateinit var frame_layout: FrameLayout
    var previousMenuItem: MenuItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer_header)

        toolbar = findViewById(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawerLayout)
        Navigationview = findViewById(R.id.Navigationview)
        frame_layout = findViewById(R.id.frame_layout)

        setupToolBaar()

        val actionBarDrawerToggle = ActionBarDrawerToggle(this@DrawerHeader,drawerLayout,R.string.open_drawer,
            R.string.close_drawer)

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        home()

        Navigationview.setNavigationItemSelectedListener {

            if(previousMenuItem!=null){
                previousMenuItem?.isChecked = false
            }
            it.isCheckable= true
            it.isChecked=true
            previousMenuItem = it

            when(it.itemId){
                R.id.home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, Home())
                        .commit()

                    supportActionBar?.title = "Home"
                    drawerLayout.closeDrawers()

                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, Profile())
                        .commit()

                    supportActionBar?.title = "Profile"
                    drawerLayout.closeDrawers()

                }
                R.id.Favourate -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, Favorite())
                        .commit()
                    supportActionBar?.title = "Favourite"
                    drawerLayout.closeDrawers()

                }
                R.id.FAQS -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, Faq())
                        .commit()

                    supportActionBar?.title = "FAQs"
                    drawerLayout.closeDrawers()
                }
                R.id.Logout -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, LogOut())
                        .commit()
                    supportActionBar?.title = "Log Out"
                    drawerLayout.closeDrawers()
                }

                else -> {
                    Toast.makeText(this@DrawerHeader,"Some unexpected error occurred",Toast.LENGTH_LONG)
                        .show()
                }


            }
            return@setNavigationItemSelectedListener true

        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val  frag = supportFragmentManager.findFragmentById(R.id.frame_layout)
        when(frag){
            !is Home -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout, Home())
                    .commit()
                supportActionBar?.title = "Home"
                Navigationview.setCheckedItem(R.id.home)
                drawerLayout.closeDrawers()


            }
            else -> {
                super.onBackPressed()

            }

        }

    }
    fun setupToolBaar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Home"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun home(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, Home())
            .commit()
        supportActionBar?.title = "Home"
        Navigationview.setCheckedItem(R.id.home)
        drawerLayout.closeDrawers()
    }

    }






