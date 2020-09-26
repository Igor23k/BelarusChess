package bobrchess.of.by.belaruschess.fragments


import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import bobrchess.of.by.belaruschess.view.activity.PackageModel
import bobrchess.of.by.belaruschess.view.activity.UserContractView
import bobrchess.of.by.belaruschess.view.activity.impl.MainActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_show_tournament_event.*
import java.text.DateFormat

class ShowTournamentEvent : ShowEventFragment(), UserContractView {

    private var userPresenterImpl: UserPresenterImpl? = null
    private var places: List<PlaceDTO>? = null
    private var countries: List<CountryDTO>? = null
    private var tournament: EventTournament? = null
    private var placeItemsListType = object : TypeToken<List<PlaceDTO>>() {}.type
    private var countryItemsListType = object : TypeToken<List<CountryDTO>>() {}.type
    private var progressDialog: ProgressDialog? = null
    private var referee: UserDTO? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        initTournament()
        userPresenterImpl = UserPresenterImpl()
        userPresenterImpl?.attachView(this)
        userPresenterImpl?.packageModel = PackageModel(this.context!!)
        userPresenterImpl?.viewIsReady()
        tournament?.refereeId?.toInt()?.let { userPresenterImpl?.loadUserById(it) }

        places = Gson().fromJson(arguments?.getString(Constants.PLACES), placeItemsListType)
        countries = Gson().fromJson(arguments?.getString(Constants.COUNTRIES), countryItemsListType)
        (context as MainActivity).unlockAppBar()
        return inflater.inflate(R.layout.fragment_show_tournament_event, container, false)
    }

    override fun updateUI() {
        if (referee != null) {
            (context as MainActivity).scrollable_toolbar.isTitleEnabled = true
            EventHandler.getEventToEventIndex(eventID)?.let { tournamentEvent ->
                if (tournamentEvent is EventTournament) {
                    this.tournament_name.text = tournamentEvent.name
                    this.tournament_short_description.visibility = TextView.VISIBLE
                    this.tournament_short_description.text = tournamentEvent.shortDescription
                    this.tournament_referee.text = Util.getInternalizedMessage(Constants.REFEREES_TOURNAMENT_TEXT) + referee?.name + " " + referee?.surname

                    var scrollRange = -1
                    (context as MainActivity).app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appbarLayout, verticalOffset ->
                        if (scrollRange == -1) {
                            scrollRange = appbarLayout.totalScrollRange
                        }
                        if (context != null) {
                            if (scrollRange + verticalOffset == 0) {
                                setToolbarTitle(context!!.resources.getString(R.string.app_name))
                            } else {
                                if (places != null) {
                                    setToolbarTitle(places!![tournamentEvent.placeId!! - 1].name!!)
                                }
                            }
                        }
                    })

                    //only set expanded title color to white, when background is not white, background is white when no avatar image is set
                    if (tournamentEvent.imageUri != null) {
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

                    val date: String
                    date = tournamentEvent.dateToPrettyString(DateFormat.FULL)

                    tournament_full_description.text = tournamentEvent.fullDescription

                    tournament_date.text = date

                    if (places != null) {
                        val place = places!![tournamentEvent.placeId!! - 1]
                        tournament_location.text = place.city + ", " + place.country?.name
                    }

                    updateAvatarImage(tournamentEvent.imageUri)
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

    private fun updateAvatarImage(image: String?) {
        if (this.iv_avatar != null && (context as MainActivity).collapsable_toolbar_iv != null) {
            val bitmap = Util.getBitMapByBase64(image)
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
                var shareBirthdayMsg =
                        if (tournament.fullDescription != null) {
                            context!!.resources.getString(
                                    R.string.share_tournament_name,
                                    "${tournament.name} \"${tournament.fullDescription}\" ${tournament.shortDescription}"
                            )
                        } else if (tournament.shortDescription != null) {
                            context!!.resources.getString(
                                    R.string.share_tournament_name,
                                    "${tournament.name} ${tournament.shortDescription}"
                            )
                        } else {
                            context!!.resources.getString(
                                    R.string.share_tournament_name,
                                    tournament.name
                            )
                        }

                //   if (tournament.isYearGiven) {
                //startDate person was born
                shareBirthdayMsg += "\n" + context!!.resources.getString(
                        R.string.share_tournament_date_start,
                        EventDate.parseDateToString(tournament.eventDate, DateFormat.FULL)
                )
                //      }

                //next tournament
                /* shareBirthdayMsg += "\n" + context!!.resources.getString(
                         R.string.share_tournament_date_next,
                         EventDate.parseDateToString(
                                 EventDate.dateToCurrentTimeContext(tournament.eventDate),
                                 DateFormat.FULL
                         )
                 )*/

                val daysUntil = tournament.getDaysUntil()
                /*   shareBirthdayMsg += if (daysUntil == 0) {
                       //today
                       "\n" + context!!.resources.getString(
                               R.string.share_tournament_days_today
                       )
                   } else {
                       // in X days
                       "\n" + context!!.resources.getQuantityString(
                               R.plurals.share_tournament_days,
                               daysUntil,
                               daysUntil
                       )
                   }*/

                // if (tournament.isYearGiven) {
                //person will be years old
                shareBirthdayMsg += "\n" + context!!.resources.getQuantityString(
                        R.plurals.person_years_old,
                        tournament.getYearsSince() + 1,
                        tournament.name,
                        tournament.getYearsSince() + 1
                )
                //}

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

        val gson = GsonBuilder().setPrettyPrinting().create()
        val placesList = gson.toJson(places)

        val bundle = Bundle()
        //do this in more adaptable way
        bundle.putInt(
                MainActivity.FRAGMENT_EXTRA_TITLE_EVENTID,
                eventID
        )

        bundle.putString(
                Constants.PLACES,
                placesList
        )

        val ft = (context as MainActivity).supportFragmentManager.beginTransaction()
        // add arguments to fragment
        val newBirthdayFragment = TournamentInstanceFragment.newInstance()
        newBirthdayFragment.arguments = bundle
        ft.replace(
                R.id.fragment_placeholder,
                newBirthdayFragment,
                TournamentInstanceFragment.TOURNAMENT_INSTANCE_FRAGMENT_TAG
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

    override fun showUsers(users: MutableList<out UserDTO>?) {
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
}
