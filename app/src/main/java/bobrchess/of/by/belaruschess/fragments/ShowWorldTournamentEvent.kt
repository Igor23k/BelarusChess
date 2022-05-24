package bobrchess.of.by.belaruschess.fragments


import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.view.*
import android.widget.ImageView
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.dto.externalFide.WorldTournamentDTO
import bobrchess.of.by.belaruschess.handler.EventHandler
import bobrchess.of.by.belaruschess.model.EventWorldTournament
import bobrchess.of.by.belaruschess.util.Constants
import bobrchess.of.by.belaruschess.util.Util
import bobrchess.of.by.belaruschess.view.activity.impl.MainActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_show_tournament_event.iv_avatar
import kotlinx.android.synthetic.main.fragment_show_world_tournament_event.*
import org.springframework.util.StringUtils

class ShowWorldTournamentEvent : ShowEventFragment() {

    private var worldTournament: WorldTournamentDTO? = null
    private val worldTournamentType = object : TypeToken<WorldTournamentDTO>() {}.type
    private val dataSeparator = "\n"


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        worldTournament = Gson().fromJson(arguments?.getString("worldTournament"), worldTournamentType)
        (context as MainActivity).unlockAppBar()
        return inflater.inflate(R.layout.fragment_show_world_tournament_event, container, false)
    }

    private fun getConcatStringValueWithEnding(hardcodedText: String, valueText: String?, defaultValue: String): String {
        if (!StringUtils.isEmpty(valueText)) {
            return "$hardcodedText $valueText$defaultValue"
        }
        return defaultValue
    }

    /**
     * updateUI updates all TextViews and other views to the current instance(Anniversary, Birthday) data
     */

    override fun updateUI() {
        (context as MainActivity).scrollable_toolbar.isTitleEnabled = true


        this.world_tournament_name.text = worldTournament?.name
        this.world_tournament_category.text = worldTournament?.category?.name
        this.world_tournament_date_start.text = worldTournament?.dateStart?.substringBefore(" ")
        this.world_tournament_date_end.text = worldTournament?.dateEnd?.substringBefore(" ")
        this.world_tournament_place.text = worldTournament?.place

        var descriptionData = getConcatStringValueWithEnding(Util.getInternalizedMessage(Constants.KEY_NUMBER_OF_PLAYERS), worldTournament?.numberOfPlayers, dataSeparator)
        descriptionData += getConcatStringValueWithEnding(Util.getInternalizedMessage(Constants.KEY_TIME_CONTROL), worldTournament?.timeControl, dataSeparator)
        descriptionData += getConcatStringValueWithEnding(Util.getInternalizedMessage(Constants.KEY_TIME_CONTROL_DESCRIPTION), worldTournament?.timeControlDescription, dataSeparator)
        descriptionData += getConcatStringValueWithEnding(Util.getInternalizedMessage(Constants.KEY_TIME_CONTROL_DESCRIPTION), getTimeControlByType(worldTournament?.timeControlTyp), dataSeparator)
        descriptionData += getConcatStringValueWithEnding(Util.getInternalizedMessage(Constants.KEY_NUMBER_OF_ROUNDS), worldTournament?.numberOfPlayers, dataSeparator)
        descriptionData += getConcatStringValueWithEnding(Util.getInternalizedMessage(Constants.KEY_ORGANIZER), worldTournament?.organizer, dataSeparator)
        descriptionData += getConcatStringValueWithEnding(Util.getInternalizedMessage(Constants.KEY_ARBITER), worldTournament?.chiefArbiter, dataSeparator)
        this.world_tournament_description_data.text = descriptionData

        var contactData = getConcatStringValueWithEnding(Util.getInternalizedMessage(Constants.KEY_EMAIL), worldTournament?.email, dataSeparator)
        contactData += getConcatStringValueWithEnding(Util.getInternalizedMessage(Constants.KEY_WEBSITE), worldTournament?.website, dataSeparator)
        contactData += getConcatStringValueWithEnding(Util.getInternalizedMessage(Constants.KEY_PHONE), worldTournament?.tel, dataSeparator)
        this.world_tournament_contact_data.text = contactData


        var scrollRange = -1
        (context as MainActivity).app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appbarLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = appbarLayout.totalScrollRange
            }
            if (context != null) {
               /* if (scrollRange + verticalOffset == 0) {
                    setToolbarTitle(context!!.resources.getString(R.string.app_name))
                } else {*/
                    setToolbarTitle(worldTournament?.name!!)
              //  }
            }
        })

        //only set expanded title color to white, when background is not white, background is white when no avatar image is set

        (context as MainActivity).scrollable_toolbar.setExpandedTitleColor(
                ContextCompat.getColor(
                        context!!,
                        R.color.darkGrey
                )
        )

        updateAvatarImage(null)
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

    private fun getTimeControlByType(type: String?): String {
        when (type) {
            "s" -> {
                return Util.getInternalizedMessage(Constants.KEY_TIME_CONTROL_STANDARD_TYPE)
            }
            "r" -> {
                return Util.getInternalizedMessage(Constants.KEY_TIME_CONTROL_RAPID_TYPE)
            }
            "b" -> {
                return Util.getInternalizedMessage(Constants.KEY_TIME_CONTROL_BLITZ_TYPE)
            }
        }
        return ""
    }

    /**
     * shareEvent a function which is called after the share button has been pressed
     * It provides a simple intent to share data as plain text in other apps
     */
    override fun shareEvent() {
        EventHandler.getEventToEventIndex(eventID)?.let { worldTournament ->
            if (worldTournament is EventWorldTournament) {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"

                var shareWorldTournamentMsg = worldTournament.name

                if (worldTournament.toursCount != null) {
                    shareWorldTournamentMsg += "\n" + context!!.resources.getString(R.string.world_tournament_tours_count, worldTournament.toursCount.toString())
                }
                shareWorldTournamentMsg += "\n" + context!!.resources.getString(R.string.world_tournament_start_date, worldTournament.startDate.toString())
                shareWorldTournamentMsg += "\n" + context!!.resources.getString(R.string.world_tournament_end_date, worldTournament.finishDate.toString())
                shareWorldTournamentMsg += "\n" + context!!.resources.getString(R.string.world_tournament_country, worldTournament.country.toString())
                shareWorldTournamentMsg += "\n" + context!!.resources.getString(R.string.world_tournament_city, worldTournament.city.toString())

                intent.putExtra(Intent.EXTRA_TEXT, shareWorldTournamentMsg)
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
        fun newInstance(): ShowWorldTournamentEvent {
            return ShowWorldTournamentEvent()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.toolbar_show_event_lite, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}
