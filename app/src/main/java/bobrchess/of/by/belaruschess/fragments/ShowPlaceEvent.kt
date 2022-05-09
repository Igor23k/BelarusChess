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
import bobrchess.of.by.belaruschess.dto.CountryDTO
import bobrchess.of.by.belaruschess.handler.EventHandler
import bobrchess.of.by.belaruschess.model.EventPlace
import bobrchess.of.by.belaruschess.util.Util
import bobrchess.of.by.belaruschess.view.activity.impl.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_show_place_event.*
import kotlinx.android.synthetic.main.fragment_show_user_event.iv_avatar
import org.springframework.util.StringUtils

class ShowPlaceEvent : ShowEventFragment() {

    private var countries: List<CountryDTO>? = null
    private var place: EventPlace? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        initPlace()
        val activity: MainActivity? = activity as MainActivity?
        countries = activity?.getCountries()
        (context as MainActivity).unlockAppBar()
        return inflater.inflate(R.layout.fragment_show_place_event, container, false)
    }

    private fun initPlace() {
        eventID = arguments!!.getInt(MainActivity.FRAGMENT_EXTRA_TITLE_EVENTID)
        val event = EventHandler.getEventToEventIndex(eventID)
        if (event is EventPlace) {
            place = event
        }
    }

    /**
     * updateUI updates all TextViews and other views to the current instance(Anniversary, Birthday) data
     */
    override fun updateUI() {
        (context as MainActivity).scrollable_toolbar.isTitleEnabled = true
        EventHandler.getEventToEventIndex(eventID)?.let { place ->
            if (place is EventPlace) {
                val country = countries?.find { it.id == place.countryId}
                var countryName = country?.name
                if (StringUtils.isEmpty(countryName)) {
                    countryName =  resources.getString(R.string.country_absence)
                }
                this.place_country_and_city.text = "${countryName}, ${place.city}"


                this.place_street_and_building.visibility = TextView.VISIBLE
                this.place_street_and_building.text = "${place.street}, ${place.building}"

                var scrollRange = -1
                (context as MainActivity).app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appbarLayout, verticalOffset ->
                    if (scrollRange == -1) {
                        scrollRange = appbarLayout.totalScrollRange
                    }
                    if (context != null) {
                       // if (scrollRange + verticalOffset == 0) {
                        //    setToolbarTitle(context!!.resources.getString(R.string.app_name))
                      //  } else {
                            setToolbarTitle(place.name)
                     //   }
                    }
                })

                //only set expanded title color to white, when background is not white, background is white when no avatar image is set
                if (place.image != null) {
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
                updateAvatarImage(place.image)
            }
        }
    }

    override fun onDetach() {
        closeExpandableToolbar()
        super.onDetach()
    }

    private fun updateAvatarImage(image: ByteArray?) {
        if (this.iv_avatar != null && this.eventID >= 0 && (context as MainActivity).collapsable_toolbar_iv != null) {
            val bitmap = Util.getBitMapByByteArr(image)
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
        EventHandler.getEventToEventIndex(eventID)?.let { place ->
            if (place is EventPlace) {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"

                var shareBirthdayMsg = resources.getString(R.string.location) + ": " + place.name
                shareBirthdayMsg += "\n" + resources.getString(R.string.address) + ": " + countries?.first { it.id!! == place.countryId!! }?.name + ", " + place.city + ", " + place.street + ", " + place.building
                shareBirthdayMsg += "\n" + resources.getString(R.string.capacity) + ": " + place.capacity

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
        val newBirthdayFragment = EditPlaceInstanceFragment.newInstance()
        newBirthdayFragment.arguments = bundle
        ft.replace(
                R.id.fragment_placeholder,
                newBirthdayFragment,
                EditPlaceInstanceFragment.PLACE_INSTANCE_FRAGMENT_TAG
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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        val isAdmin = (context as MainActivity).getUserData()?.isAdmin()
        val isOrganizer = (context as MainActivity).getUserData()?.isOrganizer()
        val id = (context as MainActivity).getUserData()?.id
        if (isAdmin == true || (isOrganizer == true && id == place?.createdBy)) {
            inflater?.inflate(R.menu.toolbar_show_event_full, menu)
        } else {
            inflater?.inflate(R.menu.toolbar_show_event_lite, menu)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }
}