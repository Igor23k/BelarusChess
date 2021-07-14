package bobrchess.of.by.belaruschess.fragments


import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.dto.externalFide.TopPlayerWithImageDTO
import bobrchess.of.by.belaruschess.handler.EventHandler
import bobrchess.of.by.belaruschess.model.EventDate
import bobrchess.of.by.belaruschess.model.EventTournament
import bobrchess.of.by.belaruschess.util.Util
import bobrchess.of.by.belaruschess.view.activity.impl.MainActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_show_top_player_event.*
import kotlinx.android.synthetic.main.fragment_show_user_event.iv_avatar
import java.text.DateFormat
import java.util.*

class ShowTopPlayerEvent : ShowEventFragment() {

    private var topPlayer: TopPlayerWithImageDTO? = null
    private val topPlayerType = object : TypeToken<TopPlayerWithImageDTO>() {}.type

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        topPlayer = Gson().fromJson(arguments?.getString("topPlayer"), topPlayerType)
        (context as MainActivity).unlockAppBar()
        return inflater.inflate(R.layout.fragment_show_top_player_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    /**
     * traverseForFirstMonthEntry is a function to get the position of the month item position of the current month
     */
    private fun traverseForFirstMonthEntry(): Int {
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        for (i in 0 until EventHandler.getList().size) {
            if (EventHandler.getList()[i].getMonth() == currentMonth)
                return i
        }
        return 0
     }

    /**
     * updateUI updates all TextViews and other views to the current instance(Anniversary, Birthday) data
     */

    override fun updateUI() {
        (context as MainActivity).scrollable_toolbar.isTitleEnabled = true

        val rank = topPlayer!!.title
        val country = topPlayer!!.country
        val birthday = context!!.resources.getString(
                        R.string.bornOnYear,
                        "${topPlayer?.birthyear}"
                )
        this.top_player_country_and_rank_and_birthday.text = "$country, $rank, ${birthday}"

        this.top_player_standard_rating.visibility = TextView.VISIBLE
        this.top_player_standard_rating.text = context!!.resources.getString(
                R.string.standardRating,
                "${topPlayer!!.standardRating}"
        )

        this.top_player_rapid_rating.visibility = TextView.VISIBLE
        this.top_player_rapid_rating.text = context!!.resources.getString(
                R.string.rapidRating,
                "${topPlayer!!.rapidRating}"
        )

        this.top_player_blitz_rating.visibility = TextView.VISIBLE
        this.top_player_blitz_rating.text = context!!.resources.getString(
                R.string.blitzRating,
                "${topPlayer!!.blitzRating}"
        )

        var scrollRange = -1
        (context as MainActivity).app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appbarLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = appbarLayout.totalScrollRange
            }
            if (context != null) {
                if (scrollRange + verticalOffset == 0) {
                    setToolbarTitle(context!!.resources.getString(R.string.app_name))
                } else {
                    setToolbarTitle(topPlayer?.name!!)
                }
            }
        })

        val image = topPlayer?.imageFile?.imageFile
        //only set expanded title color to white, when background is not white, background is white when no avatar image is set
        if (image != null) {
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

        updateAvatarImage(image)
    }

    override fun onDetach() {
        closeExpandableToolbar()
        super.onDetach()
    }

    private fun updateAvatarImage(image: String?) {
        if (this.iv_avatar != null && this.eventID >= 0 && (context as MainActivity).collapsable_toolbar_iv != null) {
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
             /*   shareBirthdayMsg += "\n" + context!!.resources.getString(
                        R.string.share_tournament_date_next,
                        EventDate.parseDateToString(
                                EventDate.dateToCurrentTimeContext(birthday.eventDate),
                                DateFormat.FULL
                        )
                )*/

                val daysUntil = birthday.getDaysUntil()
              /*  shareBirthdayMsg += if (daysUntil == 0) {
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

    override fun editEvent() {}

    companion object {
        /**
         * newInstance returns a new instance of EventTournament
         */
        @JvmStatic
        fun newInstance(): ShowTopPlayerEvent {
            return ShowTopPlayerEvent()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.toolbar_show_event_lite, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}
