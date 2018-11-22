package bobrchess.of.by.belaruschess.view.activity.impl

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.dto.GameDTO
import bobrchess.of.by.belaruschess.presenter.GamePresenter
import bobrchess.of.by.belaruschess.presenter.impl.GamePresenterImpl
import bobrchess.of.by.belaruschess.util.Constants.Companion.EMPTY_STRING
import bobrchess.of.by.belaruschess.util.Constants.Companion.GAME_PARAMETER
import bobrchess.of.by.belaruschess.view.activity.GameContractView
import com.squareup.picasso.Picasso
import java.util.*


/**
 * Created by Igor on 25.03.2018.
 */
class GameActivity : AppCompatActivity(), GameContractView {

    private var firstPlayerImageView: ImageView? = null
    private var secondPlayerImageView: ImageView? = null
    private var firstPlayerNameTextView: TextView? = null
    private var secondPlayerNameTextView: TextView? = null
    private var data: TextView? = null
    private var toolbar: Toolbar? = null

    private var game = GameDTO()

    private var progressDialog: ProgressDialog? = null
    private val avatarList = ArrayList<String>()
    private var presenter: GamePresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        initAvatarList()
        firstPlayerNameTextView = findViewById(R.id.first_player__name_text_view)
        secondPlayerNameTextView = findViewById(R.id.second_player__name_text_view)
        firstPlayerImageView = findViewById(R.id.first_player__image_view)
        secondPlayerImageView = findViewById(R.id.second_player_image_view)
        data = findViewById(R.id.game_data_text_view)


        toolbar = findViewById(R.id.toolbar)
        presenter = GamePresenterImpl()
        presenter!!.attachView(this)
        setSupportActionBar(toolbar)
        presenter!!.viewIsReady()
        loadGameData()


        val actionBar = actionBar

        actionBar?.title = "50"
    }

    private fun loadGameData() {
        game = getGameData(intent)
        displayGameData()
    }

    private fun displayGameData() {
        //Picasso.with(this).load("https://www.w3schools.com/w3css/img_fjords.jpg").into(secondPlayerImageView)
        var avatarNumber = (0..30).random()
        Picasso.with(this).load(avatarList[avatarNumber]/*user.imageUrl*/).into(firstPlayerImageView)
        avatarNumber = (0..30).random()
        Picasso.with(this).load(avatarList[avatarNumber]/*user.imageUrl*/).into(secondPlayerImageView)
        firstPlayerNameTextView!!.text = getString(R.string.user_full_name, game.firstChessPlayer!!.name, game.firstChessPlayer!!.surname)
        secondPlayerNameTextView!!.text = getString(R.string.user_full_name, game.secondChessPlayer!!.name, game.secondChessPlayer!!.surname)
        data!!.text = game.gameRecord
    }

    private fun initAvatarList() {
        avatarList.add("http://priscree.ru/img/3e250315e81d1e.jpg")
        avatarList.add("http://priscree.ru/img/5dc25ed76ea661.jpg")
        avatarList.add("http://priscree.ru/img/95cbe6b870f873.jpg")
        avatarList.add("http://priscree.ru/img/f85bddf83dfca1.jpg")
        avatarList.add("http://priscree.ru/img/9dd16d3aefc6cf.jpg")
        avatarList.add("http://priscree.ru/img/2748950ade3697.jpg")
        avatarList.add("http://priscree.ru/img/44e20348a43cc2.jpg")
        avatarList.add("http://priscree.ru/img/0169c41d3800b1.jpg")
        avatarList.add("http://priscree.ru/img/cbbb1c090bcea8.png")
        avatarList.add("http://priscree.ru/img/e76d8a0443e683.jpg")
        avatarList.add("http://priscree.ru/img/cbbb17685dd8a9.jpg")
        avatarList.add("http://priscree.ru/img/013c16d74f1cd6.jpg")
        avatarList.add("http://priscree.ru/img/013c1ca2d5a510.jpg")
        avatarList.add("http://priscree.ru/img/7180e245e24566.jpg")
        avatarList.add("http://priscree.ru/img/722a716ca92a0d.jpg")
        avatarList.add("http://priscree.ru/img/752eac33141eb4.jpg")
        avatarList.add("http://priscree.ru/img/3804bfaf17169c.jpg")
        avatarList.add("http://priscree.ru/img/3804b8a5b874a7.jpg")
        avatarList.add("http://priscree.ru/img/3804b3cad12fd8.jpg")
        avatarList.add("http://priscree.ru/img/c4a98f942fb210.jpg")
        avatarList.add("http://priscree.ru/img/d14195087d41d8.jpg")
        avatarList.add("http://priscree.ru/img/a62a0bb34ba8d2.jpg")
        avatarList.add("http://priscree.ru/img/7c8aa7a1077d5f.jpg")
        avatarList.add("http://priscree.ru/img/12906cab3343fb.jpg")
        avatarList.add("http://priscree.ru/img/7a20ffc7ea595e.jpg")
        avatarList.add("http://priscree.ru/img/18064ef9e07a27.jpg")
        avatarList.add("http://priscree.ru/img/69c41ee4659246.jpg")
        avatarList.add("http://priscree.ru/img/5a5a2e1421c5bf.jpg")
        avatarList.add("http://priscree.ru/img/5a5a216979f035.jpg")
        avatarList.add("http://priscree.ru/img/1f2b8c263c4758.jpg")
    }

    fun ClosedRange<Int>.random() =
            Random().nextInt(endInclusive - start) + start

    interface OnGameClickListener {
        fun onGameClick(game: GameDTO)
    }

    private fun getGameData(intent: Intent?): GameDTO {
        return intent!!.getSerializableExtra(GAME_PARAMETER) as GameDTO
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
            R.id.action_tournaments_search -> {
                val intent = Intent(this, SearchTournamentActivity::class.java)
                startActivity(intent)
            }
            R.id.action_search -> {
                val intent = Intent(this, SearchUserActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

    override fun showProgress() {
        progressDialog = ProgressDialog.show(this, EMPTY_STRING, this.getString(R.string.please_wait))
    }

    override fun hideProgress() {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }

    override fun showToast(resId: Int?) {
        val toast = Toast.makeText(this, resId!!, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    override fun showToast(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}