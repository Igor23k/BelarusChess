package bobrchess.of.by.belaruschess.view.activity.impl

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.dto.UserDTO
import bobrchess.of.by.belaruschess.presenter.SearchUserPresenter
import bobrchess.of.by.belaruschess.presenter.impl.SearchUserPresenterImpl
import bobrchess.of.by.belaruschess.util.Constants.Companion.EMPTY_STRING
import bobrchess.of.by.belaruschess.util.Constants.Companion.USER_PARAMETER
import bobrchess.of.by.belaruschess.util.Util.Companion.TOURNAMENT_PARTICIPANTS_REQUEST
import bobrchess.of.by.belaruschess.util.Util.Companion.TOURNAMENT_TABLE_REQUEST
import bobrchess.of.by.belaruschess.util.Util.Companion.USER_INFO
import bobrchess.of.by.belaruschess.view.activity.SearchUserContractView
import bobrchess.of.by.colibritweet.adapter.TournamentParticipantsAdapter
import bobrchess.of.by.colibritweet.adapter.TournamentTableAdapter
import bobrchess.of.by.colibritweet.adapter.UsersAdapter
import butterknife.BindView
import butterknife.ButterKnife

/**
 * Created by Igor on 25.03.2018.
 */
class SearchUserActivity : AppCompatActivity(), SearchUserContractView {

    private var usersRecyclerView: RecyclerView? = null
    private var usersAdapter: UsersAdapter? = null
    private var participantsAdapter: TournamentParticipantsAdapter? = null
    private var tableAdapter: TournamentTableAdapter? = null
    private var presenter: SearchUserPresenter? = null
    private var progressDialog: ProgressDialog? = null

    @BindView(R.id.e_query_text)
    private var queryEditText: EditText? = null

    //@BindView(R.id.toolbar)
    private var toolbar: Toolbar? = null

    //@BindView(R.id.e_search_button)
    private var searchButton: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_user)
        ButterKnife.bind(this)
        initRecyclerView()

        toolbar = findViewById(R.id.toolbar)
        searchButton = toolbar!!.findViewById(R.id.e_search_button)
        queryEditText = toolbar!!.findViewById(R.id.e_query_text)

        searchButton!!.setOnClickListener(View.OnClickListener { presenter!!.searchUsers() })

        queryEditText!!.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                presenter!!.searchUsers()
                return@OnEditorActionListener true
            }
            false
        })

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        presenter = SearchUserPresenterImpl()
        presenter!!.attachView(this)
        loadUsers()
        presenter!!.viewIsReady()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initRecyclerView() {
        usersRecyclerView = findViewById(R.id.users_recycler_view)
        usersRecyclerView!!.layoutManager = LinearLayoutManager(this)
        val requestCode = intent.getIntExtra("requestCode", 0)
        when (requestCode) {
            TOURNAMENT_PARTICIPANTS_REQUEST -> {
                val onUserClickListener = object : TournamentParticipantsAdapter.OnUserClickListener {
                    override fun onUserClick(user: UserDTO) {
                        val intent = Intent(this@SearchUserActivity, UserInfoActivity::class.java)
                        intent.putExtra(USER_PARAMETER, user)
                        startActivity(intent)
                    }
                }
                participantsAdapter = TournamentParticipantsAdapter(onUserClickListener)
                usersRecyclerView!!.adapter = participantsAdapter
            }
            USER_INFO -> {
                val onUserClickListener = object : UsersAdapter.OnUserClickListener {
                    override fun onUserClick(user: UserDTO) {
                        val intent = Intent(this@SearchUserActivity, UserInfoActivity::class.java)
                        intent.putExtra(USER_PARAMETER, user)
                        startActivity(intent)
                    }
                }
                usersAdapter = UsersAdapter(onUserClickListener)
                usersRecyclerView!!.adapter = usersAdapter
            }
            TOURNAMENT_TABLE_REQUEST -> {
                val onUserClickListener = object : TournamentTableAdapter.OnUserClickListener {
                    override fun onUserClick(user: UserDTO) {
                        val intent = Intent(this@SearchUserActivity, UserInfoActivity::class.java)
                        intent.putExtra(USER_PARAMETER, user)
                        startActivity(intent)
                    }
                }
                tableAdapter = TournamentTableAdapter(onUserClickListener)
                usersRecyclerView!!.adapter = tableAdapter
            }
        }

    }

    fun getSearchText(): String {
        return queryEditText!!.text.toString()
    }

    private fun loadUsers() {
        presenter!!.loadUsers()
    }

    private fun loadUsers(count: Int) {
        presenter!!.loadUsers(count)
    }

    fun showUsers(users: List<UserDTO>) {
        if (usersAdapter != null) {//переписать
            usersAdapter!!.clearItems()
            usersAdapter!!.setItems(users)
        } else if (participantsAdapter != null) {
            participantsAdapter!!.clearItems()
            participantsAdapter!!.setItems(users)
        } else if (tableAdapter != null) {
            tableAdapter!!.clearItems()
            tableAdapter!!.setItems(users)
        }
    }

    override fun showToast(resId: Int?) {
        val toast = Toast.makeText(this, resId!!, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    override fun showToast(message: String?) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    override fun showProgress() {
        progressDialog = ProgressDialog.show(this, EMPTY_STRING, this.getString(R.string.please_wait))
    }

    override fun hideProgress() {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }
}