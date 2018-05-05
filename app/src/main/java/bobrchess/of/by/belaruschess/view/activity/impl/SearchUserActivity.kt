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
import bobrchess.of.by.belaruschess.util.Constants.EMPTY_STRING
import bobrchess.of.by.belaruschess.view.activity.SearchUserContractView
import bobrchess.of.by.colibritweet.adapter.UsersAdapter
import bobrchess.of.by.colibritweet.pojo.UserTweet
import butterknife.ButterKnife
import java.util.*

/**
 * Created by Igor on 25.03.2018.
 */
class SearchUserActivity : AppCompatActivity(), SearchUserContractView {

    private var usersRecyclerView: RecyclerView? = null
    private var usersAdapter: UsersAdapter? = null
    private var presenter: SearchUserPresenter? = null
    private var progressDialog: ProgressDialog? = null

   // @BindView(R.id.e_query_text)
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
        presenter!!.loadUsers()
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

        val onUserClickListener = object : UsersAdapter.OnUserClickListener {
            override fun onUserClick(user: UserDTO) {
                val intent = Intent(this@SearchUserActivity, UserInfoActivity::class.java)
                intent.putExtra(UserInfoActivity.USER_ID, user.id)
                startActivity(intent)
            }
        }
        usersAdapter = UsersAdapter(onUserClickListener)
        usersRecyclerView!!.adapter = usersAdapter
    }

    fun getSearchText() : String {
        return queryEditText!!.text.toString()
    }

    private fun loadUsers() {
        presenter!!.loadUsers()
    }

    private fun loadUsers(count: Int) {
        presenter!!.loadUsers(count)
    }

    fun showUsers(users: List<UserDTO>) {
        usersAdapter!!.clearItems()
        usersAdapter!!.setItems(users)
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

    override fun onConnectionError() {
        showToast(R.string.connection_error)
    }

    private fun getUsers(): Collection<UserTweet> {
        return Arrays.asList(
                UserTweet(
                        929257819349700608L,
                        "http://i.imgur.com/DvpvklR.png",
                        "DevColibri",
                        "@devcolibri",
                        "Sample description",
                        "USA",
                        42,
                        42
                ),

                UserTweet(
                        44196397L,
                        "https://pbs.twimg.com/profile_images/782474226020200448/zDo-gAo0_400x400.jpg",
                        "Elon Musk",
                        "@elonmusk",
                        "Hat Salesman",
                        "Boring",
                        14,
                        13
                )
        )
    }
}