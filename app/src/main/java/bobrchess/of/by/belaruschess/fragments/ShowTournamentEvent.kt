package bobrchess.of.by.belaruschess.fragments


import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.dto.CountryDTO
import bobrchess.of.by.belaruschess.dto.PlaceDTO
import bobrchess.of.by.belaruschess.dto.UserDTO
import bobrchess.of.by.belaruschess.handler.EventHandler
import bobrchess.of.by.belaruschess.model.EventDate
import bobrchess.of.by.belaruschess.model.EventTournament
import bobrchess.of.by.belaruschess.presenter.impl.UserPresenterImpl
import bobrchess.of.by.belaruschess.util.Constants
import bobrchess.of.by.belaruschess.util.Util
import bobrchess.of.by.belaruschess.util.Util.Companion.getBitMapByByteArr
import bobrchess.of.by.belaruschess.view.activity.PackageModel
import bobrchess.of.by.belaruschess.view.activity.UserContractView
import bobrchess.of.by.belaruschess.view.activity.impl.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_show_tournament_event.*
import java.text.DateFormat

class ShowTournamentEvent : ShowEventFragment(), UserContractView {

    private var userPresenterImpl: UserPresenterImpl? = null
    private var places: List<PlaceDTO>? = null
    private var countries: List<CountryDTO>? = null
    private var tournament: EventTournament? = null
    private var progressDialog: ProgressDialog? = null
    private var referee: UserDTO? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        initTournament()
        userPresenterImpl = UserPresenterImpl()
        userPresenterImpl?.attachView(this)
        userPresenterImpl?.setPackageModel(PackageModel(this.context!!))
        userPresenterImpl?.viewIsReady()
        tournament?.refereeId?.toInt()?.let { userPresenterImpl?.loadUserById(it) }

        val activity: MainActivity? = activity as MainActivity?
        places = activity!!.getPlaces()
        countries = activity.getCountries()
        (context as MainActivity).unlockAppBar()
        return inflater.inflate(R.layout.fragment_show_tournament_event, container, false)
    }

    override fun updateUI() {
        if (referee != null) {
            if (context != null) {
                (context as MainActivity).scrollable_toolbar.isTitleEnabled = true
                EventHandler.getEventToEventIndex(eventID)?.let { tournamentEvent ->
                    if (tournamentEvent is EventTournament) {
                        setToolbarTitle(tournamentEvent.name)
                        this.tournament_short_description.visibility = TextView.VISIBLE
                        this.tournament_short_description.text = tournamentEvent.shortDescription
                        this.tournament_referee.text = Util.getInternalizedMessage(Constants.REFEREES_TOURNAMENT_TEXT) + referee?.name + " " + referee?.surname

                        var scrollRange = -1
                        (context as MainActivity).app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appbarLayout, verticalOffset ->
                            if (scrollRange == -1) {
                                scrollRange = appbarLayout.totalScrollRange
                            }
                            if (context != null) {
                                /*if (scrollRange + verticalOffset == 0) {
                                    setToolbarTitle(context!!.resources.getString(R.string.app_name))
                                } else {*/
                                    if (places != null) {
                                        val place = places?.find { it.id == tournamentEvent.placeId }

                                        if (place != null && this.place_name!= null) {
                                            this.place_name.text = place.name
                                        }
                                    }
                               // }
                            }
                        })

                        //only set expanded title color to white, when background is not white, background is white when no avatar image is set
                        if (tournamentEvent.image != null) {
                            (context as MainActivity).scrollable_toolbar.setExpandedTitleColor(
                                    ContextCompat.getColor(
                                            context!!,
                                            R.color.white
                                    )
                            )
                        } else {
                            (context as MainActivity).scrollable_toolbar.setExpandedTitleColor(
                                    ContextCompat.getColor(
                                            context!!,
                                            R.color.darkGrey
                                    )
                            )
                        }

                      /*  val date: String111
                        date = tournamentEvent.dateToPrettyString(DateFormat.FULL)*/

                        tournament_full_description.text = tournamentEvent.fullDescription

                   //     tournament_date.text = date

                        tournament_start_date.text =  resources.getString(R.string.tournament_start_date) + ": " + EventDate.parseDateToString(tournament?.startDate, DateFormat.FULL)
                        tournament_end_date.text = resources.getString(R.string.tournament_end_date) + ": " + EventDate.parseDateToString(tournament?.finishDate, DateFormat.FULL)

                        if (places != null) {
                            val place = places?.find { it.id == tournamentEvent.placeId }
                            tournament_location.text = place?.city + ", " + place?.country?.name
                        }

                        updateAvatarImage(tournamentEvent.image)
                    }
                }
            }
        }
    }

    private fun initTournament() {
        eventID = arguments!!.getInt(MainActivity.FRAGMENT_EXTRA_TITLE_EVENTID)
        val event = EventHandler.getEventToEventIndex(eventID)
        if (event is EventTournament) {
            tournament = event
        }
    }

    override fun onDetach() {
        closeExpandableToolbar()
        super.onDetach()
    }

    private fun updateAvatarImage(image: ByteArray?) {
        if (this.iv_avatar != null && (context as MainActivity).collapsable_toolbar_iv != null) {
            val bitmap = getBitMapByByteArr(image)
            setBitmapToToolbar(bitmap)
        }
    }

    private fun setBitmapToToolbar(bitmap: Bitmap?) {
        (context as MainActivity).collapsable_toolbar_iv.visibility = ImageView.VISIBLE
        if (bitmap != null) {
            (context as MainActivity).collapsable_toolbar_iv.setImageBitmap(bitmap)
            (context as MainActivity).collapsable_toolbar_iv.scaleType =
                    ImageView.ScaleType.CENTER_CROP
            (context as MainActivity).app_bar.setExpanded(true, true)
        } else {
            (context as MainActivity).app_bar.setExpanded(false, false)
            (context as MainActivity).collapsable_toolbar_iv.scaleType =
                    ImageView.ScaleType.FIT_CENTER
            (context as MainActivity).collapsable_toolbar_iv.setImageResource(R.drawable.ic_birthday_person)
        }
    }

    private fun closeExpandableToolbar() {
        setToolbarTitle(context!!.resources.getString(R.string.app_name))
        (context as MainActivity).collapsable_toolbar_iv.visibility = ImageView.GONE
        (context as MainActivity).lockAppbar()
    }

    /**
     * shareEvent a function which is called after the share button has been pressed
     * It provides a simple intent to share data as plain text in other apps
     */
    override fun shareEvent() {
        EventHandler.getEventToEventIndex(eventID)?.let { tournament ->
            if (tournament is EventTournament) {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"

                var shareBirthdayMsg = tournament.name
                val place = places?.first { it.id!! == tournament.placeId!! }

                shareBirthdayMsg += "\n\n" + resources.getString(R.string.tournament_start_date) + ": " + EventDate.parseDateToString(tournament.startDate, DateFormat.FULL)
                shareBirthdayMsg += "\n" + resources.getString(R.string.tournament_end_date) + ": " + EventDate.parseDateToString(tournament.finishDate, DateFormat.FULL)
                shareBirthdayMsg += "\n" + resources.getString(R.string.tours_count) + ": " + tournament.toursCount
                shareBirthdayMsg += "\n\n" + resources.getString(R.string.location) + ": " + place?.name + " (" + place?.country!!.name + ", " + place.city + ", " + place.street + ", " + place.building + ")"
                shareBirthdayMsg += "\n\n" + tournament.fullDescription.toString()

                intent.putExtra(Intent.EXTRA_TEXT, shareBirthdayMsg)
                startActivity(
                        Intent.createChooser(
                                intent,
                                resources.getString(R.string.intent_share_chooser_title)
                        )
                )
            }
        }
    }

    override fun editEvent() {
        (context as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        val bundle = Bundle()
        //do this in more adaptable way
        bundle.putInt(
                MainActivity.FRAGMENT_EXTRA_TITLE_EVENTID,
                eventID
        )

        val ft = (context as MainActivity).supportFragmentManager.beginTransaction()
        // add arguments to fragment
        val fragment = EditTournamentInstanceFragment.newInstance()
        fragment.arguments = bundle
        ft.replace(
                R.id.fragment_placeholder,
                fragment,
                EditTournamentInstanceFragment.TOURNAMENT_INSTANCE_FRAGMENT_TAG
        )
        ft.addToBackStack(null)
        ft.commit()
        closeExpandableToolbar()
    }

    companion object {
        /**
         * newInstance returns a new instance of EventTournament
         */
        @JvmStatic
        fun newInstance(): ShowTournamentEvent {
            return ShowTournamentEvent()
        }
    }

    override fun showAlertDialog(title: Int, message: Int, buttonText: Int, cancelable: Boolean) {

    }

    override fun showToast(resId: Int?) {
    }

    override fun showToast(message: String?) {
    }

    override fun showUsers(users: List<UserDTO>?) {
    }

    override fun showProgress() {
        progressDialog = ProgressDialog.show(this.context, Constants.EMPTY_STRING, this.getString(R.string.please_wait))
    }

    override fun hideProgress() {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }

    override fun setConnectionStatus(connectivityStatus: Int?) {
    }

    override fun showUser(user: UserDTO?) {
        this.referee = user
        updateUI()
    }

    override fun dismissAlertDialog() {
    }

    override fun showSnackbar(resId: Int?) {
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        val isAdmin = (context as MainActivity).getUserData()?.beAdmin
        val isOrganizer = (context as MainActivity).getUserData()?.beOrganizer
        val id = (context as MainActivity).getUserData()?.id
        if (isAdmin == true || (isOrganizer == true && id == tournament?.createdBy)) {
            inflater?.inflate(R.menu.toolbar_show_event_full, menu)
        } else {
            inflater?.inflate(R.menu.toolbar_show_event_lite, menu)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }
}
