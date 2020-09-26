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
import bobrchess.of.by.belaruschess.handler.EventHandler
import bobrchess.of.by.belaruschess.model.EventPlace
import bobrchess.of.by.belaruschess.util.Constants.Companion.COUNTRIES
import bobrchess.of.by.belaruschess.util.Util
import bobrchess.of.by.belaruschess.view.activity.impl.MainActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_show_place_event.*
import kotlinx.android.synthetic.main.fragment_show_user_event.iv_avatar
import org.springframework.util.StringUtils

class ShowPlaceEvent : ShowEventFragment() {

    private var countries: List<CountryDTO>? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val activity: MainActivity? = activity as MainActivity?
        countries = activity?.getCountries()
        (context as MainActivity).unlockAppBar()
        return inflater.inflate(R.layout.fragment_show_place_event, container, false)
    }

    /**
     * updateUI updates all TextViews and other views to the current instance(Anniversary, Birthday) data
     */
    override fun updateUI() {
        (context as MainActivity).scrollable_toolbar.isTitleEnabled = true
        EventHandler.getEventToEventIndex(eventID)?.let { place ->
            if (place is EventPlace) {
                var country = place.countryId?.minus(1)?.let { countries?.get(it)?.name }
                if (StringUtils.isEmpty(country)) {
                    country = resources.getString(R.string.country_absence)
                }
                this.place_country_and_city.text = "${country}, ${place.city}"


                this.place_street_and_building.visibility = TextView.VISIBLE
                this.place_street_and_building.text = "${place.street}, ${place.building}"

                var scrollRange = -1
                (context as MainActivity).app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appbarLayout, verticalOffset ->
                    if (scrollRange == -1) {
                        scrollRange = appbarLayout.totalScrollRange
                    }
                    if (context != null) {
                        if (scrollRange + verticalOffset == 0) {
                            setToolbarTitle(context!!.resources.getString(R.string.app_name))
                        } else {
                            setToolbarTitle(place.name)
                        }
                    }
                })

                //only set expanded title color to white, when background is not white, background is white when no avatar image is set
                if (place.imageUri != null) {
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

                place_capacity.text = "" + resources.getString(R.string.capacity) + ":" + place.capacity
                updateAvatarImage(place.imageUri)
            }
        }
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
    override fun shareEvent() {//todo
        EventHandler.getEventToEventIndex(eventID)?.let { birthday ->
            if (birthday is EventPlace) {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                var sharePlaceMsg =
                        /*  if (birthday.fullDescription != null) {
                              context!!.resources.getString(
                                      R.string.share_place_name,
                                      "${birthday.name} \"${birthday.fullDescription}\" ${birthday.shortDescription}"
                              )
                          } else if (birthday.shortDescription != null) {
                              context!!.resources.getString(
                                      R.string.share_place_name,
                                      "${birthday.name} ${birthday.shortDescription}"
                              )
                          } else {
                              context!!.resources.getString(
                                      R.string.share_place_name,
                                      birthday.name
                              )
                          }*/

                //   if (birthday.isYearGiven) {
                //startDate person was born
                        /*shareBirthdayMsg += "\n" + context!!.resources.getString(
                                R.string.share_place_date_start,
                                EventDate.parseDateToString(birthday.eventDate, DateFormat.FULL)
                        )
                        //      }

                        //next birthday
                        shareBirthdayMsg += "\n" + context!!.resources.getString(
                                R.string.share_place_date_next,
                                EventDate.parseDateToString(
                                        EventDate.dateToCurrentTimeContext(birthday.eventDate),
                                        DateFormat.FULL
                                )
                        )

                        val daysUntil = birthday.getDaysUntil()
                        shareBirthdayMsg += if (daysUntil == 0) {
                            //today
                            "\n" + context!!.resources.getString(
                                    R.string.share_place_days_today
                            )
                        } else {
                            // in X days
                            "\n" + context!!.resources.getQuantityString(
                                    R.plurals.share_place_days,
                                    daysUntil,
                                    daysUntil
                            )
                        }*/

                        /*shareBirthdayMsg += "\n" + context!!.resources.getQuantityString(
                                R.plurals.person_years_old,
                                birthday.getYearsSince() + 1,
                                birthday.name,
                                birthday.getYearsSince() + 1
                        )*/

                        /*intent.putExtra(Intent.EXTRA_TEXT, shareBirthdayMsg)*/
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
        val newBirthdayFragment = PlaceInstanceFragment.newInstance()
        newBirthdayFragment.arguments = bundle
        ft.replace(
                R.id.fragment_placeholder,
                newBirthdayFragment,
                PlaceInstanceFragment.PLACE_INSTANCE_FRAGMENT_TAG
        )
        ft.addToBackStack(null)
        ft.commit()
        closeExpandableToolbar()
    }

    companion object {
        /**
         * newInstance returns a new instance of EventPlace
         */
        @JvmStatic
        fun newInstance(): ShowPlaceEvent {
            return ShowPlaceEvent()
        }
    }
}