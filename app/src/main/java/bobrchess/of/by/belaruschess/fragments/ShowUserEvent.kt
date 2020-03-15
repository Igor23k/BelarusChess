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
import bobrchess.of.by.belaruschess.dto.RankDTO
import bobrchess.of.by.belaruschess.dto.UserDTO
import bobrchess.of.by.belaruschess.handler.BitmapHandler
import bobrchess.of.by.belaruschess.handler.EventHandler
import bobrchess.of.by.belaruschess.model.EventDate
import bobrchess.of.by.belaruschess.model.EventTournament
import bobrchess.of.by.belaruschess.model.EventUser
import bobrchess.of.by.belaruschess.view.activity.impl.MainActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_show_user_event.*
import org.springframework.util.StringUtils
import java.text.DateFormat

/**
 * ShowTournamentEvent is a fragment to show all known data from a instance of EventTournament
 *
 * TODO:
 * - add tiny animation for opening this fragment
 */
class ShowUserEvent : ShowEventFragment() {

    private var ranks: List<RankDTO>? = null
    private var countries: List<CountryDTO>? = null
    private var coach: UserDTO? = null
    var rankItemsListType = object : TypeToken<List<RankDTO>>() {}.type
    var countryItemsListType = object : TypeToken<List<CountryDTO>>() {}.type
    var userItemsListType = object : TypeToken<UserDTO>() {}.type

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        ranks = Gson().fromJson(arguments?.getString("ranks"), rankItemsListType)
        countries = Gson().fromJson(arguments?.getString("countries"), countryItemsListType)
        coach = Gson().fromJson(arguments?.getString("coach"), userItemsListType)
        (context as MainActivity).unlockAppBar()
        return inflater.inflate(R.layout.fragment_show_tournament_event, container, false)
    }

    /**
     * updateUI updates all TextViews and other views to the current instance(Anniversary, Birthday) data
     */

    override fun updateUI() {
        (context as MainActivity).scrollable_toolbar.isTitleEnabled = true
        EventHandler.getEventToEventIndex(eventID)?.let { userEvent ->
            if (userEvent is EventUser) {
                var rank = userEvent.rankId?.minus(1)?.let { ranks?.get(it)?.name }
                if (StringUtils.isEmpty(rank)) {
                    rank = resources.getString(R.string.rank_absence)
                }
                val country = userEvent.countryId?.minus(1)?.let { countries?.get(it)?.name }

                this.user_rank.text = "$country, $rank, ${userEvent.rating}"


                this.user_coach.visibility = TextView.VISIBLE
                if (coach != null) {
                    this.user_coach.text = resources.getString(R.string.coach)+ "   ${coach?.name} ${coach?.surname}"
                } else {
                    this.user_coach.text = resources.getString(R.string.coach_absence)
                }

                var scrollRange = -1
                (context as MainActivity).app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appbarLayout, verticalOffset ->
                    if (scrollRange == -1) {
                        scrollRange = appbarLayout.totalScrollRange
                    }
                    if (context != null) {
                        if (scrollRange + verticalOffset == 0) {
                            setToolbarTitle(context!!.resources.getString(R.string.app_name))
                        } else {
                            setToolbarTitle(userEvent.name + " " + userEvent.surname)
                        }
                    }
                })

                //only set expanded title color to white, when background is not white, background is white when no avatar image is set
                if (userEvent.imageUri != null) {
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
                date = userEvent.dateToPrettyString(DateFormat.LONG)

                tv_show_birthday_years_old.text = "haha"//userEvent.surname

                user_start_date.text = resources.getString(R.string.person_show_date, date)
                // context!!.resources.getString(R.string.person_show_date, startDate)

                /*   //show adapted string for 1 day, not 1 days
                   when (userEvent.getDaysUntil()) {
                       0 -> {
                           tv_show_birthday_days.text = resources.getString(
                               R.string.person_days_until_today,
                               userEvent.getName()
                           )
                       }
                       1 -> {
                           tv_show_birthday_days.text =
                               resources.getQuantityString(
                                   R.plurals.person_days_until,
                                   userEvent.getDaysUntil(),
                                   userEvent.getName(),
                                   userEvent.getDaysUntil(),
                                   EventDate.parseDateToString(
                                       EventDate.dateToCurrentTimeContext(userEvent.eventDate),
                                       DateFormat.FULL
                                   ), userEvent.getWeeksUntilAsString()
                               )
                       }
                       else -> {
                           tv_show_birthday_days.text =
                               resources.getQuantityString(
                                   R.plurals.person_days_until,
                                   userEvent.getDaysUntil(),
                                   userEvent.getName(),
                                   userEvent.getDaysUntil(),
                                   EventDate.parseDateToString(
                                       EventDate.dateToCurrentTimeContext(userEvent.eventDate),
                                       DateFormat.FULL
                                   ), userEvent.getWeeksUntilAsString()
                               )
                       }
                   }*/

                /* if (!userEvent.note.isNullOrBlank()) {
                     this.tv_show_birthday_note.text =
                         "${resources.getString(R.string.event_property_note)}: ${userEvent.note}"
                     this.tv_show_birthday_note.visibility = TextView.VISIBLE
                 } else {
                     this.tv_show_birthday_note.visibility = TextView.GONE
                 }*/
                updateAvatarImage()
            }
        }
    }

    override fun onDetach() {
        closeExpandableToolbar()
        super.onDetach()
    }

    private fun updateAvatarImage() {
        if (this.iv_avatar != null && this.eventID >= 0 && (context as MainActivity).collapsable_toolbar_iv != null) {
            val bitmap = BitmapHandler.getBitmapFromFile(context!!, this.eventID)
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
        EventHandler.getEventToEventIndex(eventID)?.let { birthday ->
            if (birthday is EventTournament) {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                var shareBirthdayMsg =
                        if (birthday.fullDescription != null) {
                            context!!.resources.getString(
                                    R.string.share_tournament_name,
                                    "${birthday.name} \"${birthday.fullDescription}\" ${birthday.shortDescription}"
                            )
                        } else if (birthday.shortDescription != null) {
                            context!!.resources.getString(
                                    R.string.share_tournament_name,
                                    "${birthday.name} ${birthday.shortDescription}"
                            )
                        } else {
                            context!!.resources.getString(
                                    R.string.share_tournament_name,
                                    birthday.name
                            )
                        }

                //   if (birthday.isYearGiven) {
                //startDate person was born
                shareBirthdayMsg += "\n" + context!!.resources.getString(
                        R.string.share_tournament_date_start,
                        EventDate.parseDateToString(birthday.eventDate, DateFormat.FULL)
                )
                //      }

                //next birthday
                shareBirthdayMsg += "\n" + context!!.resources.getString(
                        R.string.share_tournament_date_next,
                        EventDate.parseDateToString(
                                EventDate.dateToCurrentTimeContext(birthday.eventDate),
                                DateFormat.FULL
                        )
                )

                val daysUntil = birthday.getDaysUntil()
                shareBirthdayMsg += if (daysUntil == 0) {
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
                }

                // if (birthday.isYearGiven) {
                //person will be years old
                shareBirthdayMsg += "\n" + context!!.resources.getQuantityString(
                        R.plurals.person_years_old,
                        birthday.getYearsSince() + 1,
                        birthday.name,
                        birthday.getYearsSince() + 1
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

        val bundle = Bundle()
        //do this in more adaptable way
        bundle.putInt(
                MainActivity.FRAGMENT_EXTRA_TITLE_EVENTID,
                eventID
        )
        val ft = (context as MainActivity).supportFragmentManager.beginTransaction()
        // add arguments to fragment
        val newBirthdayFragment = TournamentInstanceFragment.newInstance()
        newBirthdayFragment.arguments = bundle
        ft.replace(
                R.id.fragment_placeholder,
                newBirthdayFragment,
                TournamentInstanceFragment.BIRTHDAY_INSTANCE_FRAGMENT_TAG
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
        fun newInstance(): ShowUserEvent {
            return ShowUserEvent()
        }
    }
}
