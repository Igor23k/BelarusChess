package bobrchess.of.by.belaruschess.fragments


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
import bobrchess.of.by.belaruschess.util.Constants
import bobrchess.of.by.belaruschess.util.Util
import bobrchess.of.by.belaruschess.view.activity.impl.MainActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_show_tournament_event.*
import java.text.DateFormat

class ShowTournamentEvent : ShowEventFragment() {

    private var places: List<PlaceDTO>? = null
    private var countries: List<CountryDTO>? = null
    private var referees: List<UserDTO>? = null

    var placeItemsListType = object : TypeToken<List<PlaceDTO>>() {}.type
    var countryItemsListType = object : TypeToken<List<CountryDTO>>() {}.type
    var refereeItemsListType = object : TypeToken<List<UserDTO>>() {}.type

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        places = Gson().fromJson(arguments?.getString(Constants.PLACES), placeItemsListType)
        countries = Gson().fromJson(arguments?.getString(Constants.COUNTRIES), countryItemsListType)
        referees = Gson().fromJson(arguments?.getString(Constants.REFEREES), refereeItemsListType)
        (context as MainActivity).unlockAppBar()
        return inflater.inflate(R.layout.fragment_show_tournament_event, container, false)
    }

    override fun updateUI() {
        (context as MainActivity).scrollable_toolbar.isTitleEnabled = true
        EventHandler.getEventToEventIndex(eventID)?.let { tournamentEvent ->
            if (tournamentEvent is EventTournament) {
                this.tournament_name.text = tournamentEvent.name
                this.tournament_short_description.visibility = TextView.VISIBLE
                this.tournament_short_description.text = tournamentEvent.shortDescription

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
        val refereesList = gson.toJson(referees)

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

        bundle.putString(
                Constants.REFEREES,
                refereesList
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
}
