package rose.pangj.sga_app

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,  DocumentsFragment.OnDocSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragmentTransaction(LogInFragment())
        setSupportActionBar(toolbar)


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
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var switchTo: Fragment? = null
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.upcoming_events -> {
                // Handle the camera action
                switchTo = EventFragment()
            }
            R.id.news -> {
                switchTo = NewsFragment()

            }

            R.id.suggestion_box -> {
                switchTo = SuggestionBoxFragment()

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

    override fun onDocSelected(doc: Doc) {
        val fragment = DocDetailFragment.newInstance(doc)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, fragment)
        ft.addToBackStack("detail")
        ft.commit()
    }
}
