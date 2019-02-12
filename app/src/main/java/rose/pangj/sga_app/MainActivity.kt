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

    val mSenatorFragment = SenatorFragment()
    val mEventFragment = EventFragment()
    val auth = FirebaseAuth.getInstance()
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
                fragmentTransaction(NewsFragment())
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


        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        fab.setOnClickListener { view ->
            mSenatorFragment.adapter.showAddEditDialog(auth.currentUser!!.uid, -1)
        }
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
            }
            R.id.news -> {
                switchTo = NewsFragment()

            }

            R.id.suggestion_box -> {
                switchTo = mSenatorFragment

            }
            R.id.documents -> {
                switchTo = DocumentsFragment()

            }
            R.id.FAQs -> {
                switchTo = FAQFragment()
            }
        }

        if (switchTo != null) {
            fragmentTransaction(switchTo)
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
        val fragment = EventFragment()
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, fragment)
        ft.addToBackStack("detail")
        ft.commit()
    }

    override fun onSenatorSelected(senator: Senator) {
        val fragment = SuggestionBoxFragment()
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
