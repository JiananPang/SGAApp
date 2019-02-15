package rose.pangj.sga_app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import edu.rosehulman.rosefire.Rosefire
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    DocumentsFragment.OnDocSelectedListener,
    NewsFragment.OnNewsSelectedListener,
    EventFragment.OnEventSelectedListener,
    SenatorFragment.OnSenatorSelectedListener,
    LogInFragment.OnLoginButtonPressedListener {

    var mSenatorFragment = SenatorFragment()
    val mEventFragment = EventFragment()
    var mDocumentFragment = DocumentsFragment()
    var mNewsFragment = NewsFragment()
    var mFAQFragment = FAQFragment()
    val auth = FirebaseAuth.getInstance()
    var isAdmin = false
    var isLogin = false
    lateinit var authListener: FirebaseAuth.AuthStateListener
    private val RC_ROSEFIRE_LOGIN = 1

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(authListener)
    }

    private fun initializeListeners() {
        authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = auth.currentUser

            if(user == null){
                fragmentTransaction(LogInFragment())
            }else {
                if(user.uid == "xix1"){
                    isAdmin = true
                }
                else{
                    fab.hide()
                    isAdmin = false
                }
                fragmentTransaction(mNewsFragment)
                mSenatorFragment = SenatorFragment()
                isLogin = true
            }
        }
    }

    override fun onRosefireLogin() {
        val signInIntent = Rosefire.getSignInIntent(this, getString(R.string.token_str))
        startActivityForResult(signInIntent, RC_ROSEFIRE_LOGIN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == RC_ROSEFIRE_LOGIN) {
            val result = Rosefire.getSignInResultFromIntent(data)
            if (result.isSuccessful) {
                auth.signInWithCustomToken(result.token)
            } else {
                Log.d(Constants.TAG, "Rosefire failed")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initializeListeners()
        if(isAdmin == true) {
            fab.setOnClickListener {
                mNewsFragment.adapter.showAddEditDialog()
            }
        }else{
            fab.hide()
        }


        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_logout ->{
                auth.signOut()
                if(item.title == "Login"){
                    item.setTitle("Logout")
                }
                else{
                    item.setTitle("Login")
                    isLogin = false
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var switchTo: Fragment? = null
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.upcoming_events -> {
                // Handle the camera action
                switchTo = mEventFragment
                fragmentTransaction(switchTo)
                if(isAdmin == true){
                    fab.show()
                    fab.setOnClickListener {
                        mEventFragment.adapter.showAddEditDialog()
                    }
                }

            }
            R.id.news -> {
                switchTo = mNewsFragment
                fragmentTransaction(switchTo)
                if(isAdmin == true) {
                    fab.show()
                    fab.setOnClickListener {
                        mNewsFragment.adapter.showAddEditDialog()
                    }
                }

            }

            R.id.suggestion_box -> {
                if(isLogin == true){
                    if (isAdmin){
                        switchTo = MessageFragment()
                    }else {
                        switchTo = mSenatorFragment
                    }
                    fragmentTransaction(switchTo)
                    fab.hide()

                }
                else{
                    Toast.makeText(this,
                        "Please log in first",
                        Toast.LENGTH_LONG
                    ).show()

                }

            }
            R.id.documents -> {
                switchTo = mDocumentFragment
                fragmentTransaction(switchTo)
                fab.hide()

            }
            R.id.FAQs -> {
                switchTo = mFAQFragment
                fragmentTransaction(switchTo)
                fab.hide()
            }
        }


        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun fragmentTransaction(switchTo: Fragment){
        if (switchTo != null) {
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.fragment_container, switchTo)
            for (i in 0 until supportFragmentManager.backStackEntryCount) {
                supportFragmentManager.popBackStackImmediate()
            }

            ft.commit()
        }
    }

    override fun onEventSelected(event: Event) {
        val fragment = EventDetailFragment.newInstance(event)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, fragment)
        ft.addToBackStack("detail")
        ft.commit()
    }

    override fun onSenatorSelected(senator: Senator) {
        val fragment = SuggestionBoxFragment.newInstance(senator.uid, auth.currentUser!!.uid)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, fragment)
        ft.addToBackStack("detail")
        ft.commit()
    }

    override fun onDocSelected(doc: Doc) {
        val fragment = DocDetailFragment.newInstance(doc)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, fragment)
        ft.addToBackStack("detail")
        ft.commit()
    }

    override fun onNewsSelected(pic: News) {
        val fragment = NewsDetailFragment.newInstance(pic)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, fragment)
        ft.addToBackStack("detail")
        ft.commit()
    }
}
