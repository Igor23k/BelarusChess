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
import bobrchess.of.by.belaruschess.handler.BitmapHandler
import bobrchess.of.by.belaruschess.handler.EventHandler
import bobrchess.of.by.belaruschess.model.EventDate
import bobrchess.of.by.belaruschess.view.activity.impl.MainActivity
import bobrchess.of.by.belaruschess.model.EventTournament
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_show_tournament_event.*
import java.text.DateFormat

class ShowTournamentEvent : ShowEventFragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        (context as MainActivity).unlockAppBar()
        return inflater.inflate(R.layout.fragment_show_tournament_event, container, false)
    }

    /**
     * updateUI updates all TextViews and other views to the current instance(Anniversary, Birthday) data
     */

    override fun updateUI() {
        (context as MainActivity).scrollable_toolbar.isTitleEnabled = true
        EventHandler.getEventToEventIndex(eventID)?.let { tournamentEvent ->
            if (tournamentEvent is EventTournament) {
                this.user_country_and_rank_and_rating.text = tournamentEvent.name
                this.user_coach.visibility = TextView.VISIBLE
                this.user_coach.text = tournamentEvent.shortDescription

                var scrollRange = -1
                (context as MainActivity).app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appbarLayout, verticalOffset ->
                    if (scrollRange == -1) {
                        scrollRange = appbarLayout.totalScrollRange
                    }
                    if (context != null) {
                        if (scrollRange + verticalOffset == 0) {
                            setToolbarTitle(context!!.resources.getString(R.string.app_name))
                        } else {
                            setToolbarTitle(tournamentEvent.name!!)
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

                tv_show_birthday_years_old.text = tournamentEvent.fullDescription

                user_birthday.text = date
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
                shareBirthdayMsg += "\n" + context!!.resources.getString(
                        R.string.share_tournament_date_next,
                        EventDate.parseDateToString(
                                EventDate.dateToCurrentTimeContext(tournament.eventDate),
                                DateFormat.FULL
                        )
                )

                val daysUntil = tournament.getDaysUntil()
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
