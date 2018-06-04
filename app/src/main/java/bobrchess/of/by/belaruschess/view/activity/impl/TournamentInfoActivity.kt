package bobrchess.of.by.belaruschess.view.activity.impl

import android.animation.ValueAnimator
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.dto.TournamentDTO
import bobrchess.of.by.belaruschess.fragments.*
import bobrchess.of.by.belaruschess.presenter.impl.TournamentPresenterImpl
import bobrchess.of.by.belaruschess.util.Constants.TOURNAMENT_PARAMETER
import bobrchess.of.by.belaruschess.view.activity.TournamentContractView
import bobrchess.of.by.belaruschess.presenter.TournamentPresenter
import com.dd.CircularProgressButton
import com.squareup.picasso.Picasso
import java.util.*


/**
 * Created by Igor on 25.03.2018.
 */
class TournamentInfoActivity : AppCompatActivity(), TournamentContractView {

    private var tournamentImageView: ImageView? = null
    private var nameTextView: TextView? = null
    private var descriptionTextView: TextView? = null
    private var judgeTextView: TextView? = null
    private var locationTextView: TextView? = null
    private var toolbar: Toolbar? = null
    private var tournament = TournamentDTO()
    private val imageList = ArrayList<String>()

    private var progressDialog: ProgressDialog? = null

    private var presenter: TournamentPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tournament_info)
        initImagesList()
        tournamentImageView = findViewById(R.id.tournament_image_view)
        nameTextView = findViewById(R.id.tournament_name_text_view)
        descriptionTextView = findViewById(R.id.tournament_full_description_text_view)
        judgeTextView = findViewById(R.id.judge_text_view)
        locationTextView = findViewById(R.id.tournament_location_text_view)

        toolbar = findViewById(R.id.toolbar)

                //   supportActionBar!!.setDisplayHomeAsUpEnabled(false)//кнопка назад, хз какой способ лучше, проверить

        presenter = TournamentPresenterImpl()
        //presenter!!.attachView(this)
        presenter!!.viewIsReady()
        setSupportActionBar(toolbar)
        //loadTournamentData()


        val actionBar = actionBar

        actionBar?.title = "50"


        val circularButton1 = findViewById<View>(R.id.circularButton1) as CircularProgressButton
        circularButton1.text = "Зарегистрироваться"
        circularButton1.setOnClickListener {
            if (circularButton1.progress == 0) {
                circularButton1.text = ""
                simulateSuccessProgress(circularButton1)
            } else {
                circularButton1.progress = 0
            }
        }
    }

    private fun simulateSuccessProgress(button: CircularProgressButton) {
        var value = 0
        val widthAnimation = ValueAnimator.ofInt(1, 100)
        widthAnimation.duration = 1500
        widthAnimation.interpolator = AccelerateDecelerateInterpolator()
        widthAnimation.addUpdateListener { animation ->
            value = animation.animatedValue as Int
            button.progress = value

        }
        widthAnimation.start()
        android.os.Handler().postDelayed(
                {
                    button.text = "Вы зарегистрированы!"
                }, 2000)
    }

    private fun simulateErrorProgress(button: CircularProgressButton) {
        val widthAnimation = ValueAnimator.ofInt(1, 99)
        widthAnimation.duration = 1500
        widthAnimation.interpolator = AccelerateDecelerateInterpolator()
        widthAnimation.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            button.progress = value
            if (value == 99) {
                button.progress = -1
            }
        }
        widthAnimation.start()
    }

    private fun loadTournamentData() {
        tournament = getTournamentData(intent)//мб проверку?
        displayTournamentData()
    }

    private fun displayTournamentData() {
       // Picasso.with(this).load("https://www.w3schools.com/w3css/img_fjords.jpg").into(tournamentImageView)
        val avatarNumber = (0..3).random()
        Picasso.with(this).load(imageList[avatarNumber]/*user.imageUrl*/).into(tournamentImageView)
        nameTextView!!.text = tournament.name
        descriptionTextView!!.text = tournament.fullDescription
        judgeTextView!!.text = tournament.referee!!.name + " " + tournament.referee!!.surname//проверку и локэйшн тоже
        locationTextView!!.text = tournament.place!!.country!!.name + ", " + tournament.place!!.city + ", " + tournament.place!!.street + ", " + tournament.place!!.building
    }

    private fun getTournamentData(intent: Intent?): TournamentDTO {
        return intent!!.getSerializableExtra(TOURNAMENT_PARAMETER) as TournamentDTO
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_info_menu, menu)
        menuInflater.inflate(R.menu.menu_tournament, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_games -> {
                val intent = Intent(this, GamesListActivity::class.java)
                startActivity(intent)
            }
            R.id.action_add_tournament -> {
                val intent = Intent(this, AddTournamentActivity::class.java)
                startActivity(intent)
            }
            R.id.action_tournaments_search -> {
                val intent = Intent(this, SearchTournamentActivity::class.java)
                startActivity(intent)
            }
            R.id.action_table-> {
                val intent = Intent(this, SearchTournamentActivity::class.java)
                startActivity(intent)
            }
            R.id.action_participants -> {
                val intent = Intent(this, SearchTournamentActivity::class.java)
                startActivity(intent)
            }
            R.id.action_info -> {
                val intent = Intent(this, TournamentInfoActivity::class.java)
                //putTournamentData(intent, userDTO)
               // startActivityForResult(intent, AUTHORIZATION_REQUEST)
                startActivity(intent)
            }
        }
        return true
    }

    private fun initImagesList(){
        imageList.add("https://www.w3schools.com/w3css/img_fjords.jpg")
        imageList.add("http://priscree.ru/img/0bae62a5b4004b.jpg")
        imageList.add("http://priscree.ru/img/129060a88b433a.jpg")
        imageList.add("http://priscree.ru/img/7c8aaa9735d29f.jpg")
    }

    fun ClosedRange<Int>.random() =
            Random().nextInt(endInclusive - start) +  start

    override fun showProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun enableButton() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun disableButton() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showToast(resId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(OneFragment(), "FIRST")
        adapter.addFragment(TwoFragment(), "SECOND")
        viewPager.adapter = adapter
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }
    }
}