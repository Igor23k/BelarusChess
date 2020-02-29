package com.procrastimax.birthdaybuddy.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.adapter.EventInstanceFragment
import bobrchess.of.by.belaruschess.dto.PlaceDTO
import bobrchess.of.by.belaruschess.dto.TournamentDTO
import bobrchess.of.by.belaruschess.dto.UserDTO
import bobrchess.of.by.belaruschess.handler.BitmapHandler
import bobrchess.of.by.belaruschess.handler.EventHandler
import bobrchess.of.by.belaruschess.handler.IOHandler
import bobrchess.of.by.belaruschess.model.EventDate
import bobrchess.of.by.belaruschess.presenter.impl.AddTournamentPresenterImpl
import bobrchess.of.by.belaruschess.presenter.impl.TournamentPresenterImpl
import bobrchess.of.by.belaruschess.util.Util
import bobrchess.of.by.belaruschess.view.activity.AddTournamentContractView
import bobrchess.of.by.belaruschess.view.activity.PackageModel
import bobrchess.of.by.belaruschess.view.activity.impl.MainActivity
import com.procrastimax.birthdaybuddy.models.EventTournament
import kotlinx.android.synthetic.main.fragment_add_new_tournament.*
import kotlinx.android.synthetic.main.fragment_event_list.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 *
 * TournamentInstanceFragment is a fragment class for adding/editing an instance of EventTournament
 * This fragment shows up, when the users wants to add a new EventTournament or edit an existing one
 * The fragment consists of several TextEdits to manage user data input
 *
 * This class inherits from android.support.v4.app.Fragment
 *
 * TODO:
 *  - add possibility to take new pictures with camera
 */
class TournamentInstanceFragment : EventInstanceFragment(), AddTournamentContractView {

    /**
     * isEditedBirthday is a boolean flag to indicate whether this fragment is in "edit" mode aka. the user wants to edit an existing instance of EventTournament
     */
    private var isEditedBirthday: Boolean = false

    /**
     * eventID is the index of the clicked item in EventListFragments RecyclerView, this is handy to get the birthday instance from the EventHandler
     */
    var eventID = -1

    /**
     * tournamentAvatarUri is a string to store the user picked image for the avatar
     */
    private var tournamentAvatarUri: String? = null

    /**
     * avatarImgWasEdited is a boolean flag to store the information whether the avatar img has been changed
     */
    private var avatarImgWasEdited = false

    /**
     * REQUEST_IMAGE_GET is an intent code used for open the photo gallery
     */
    private val REQUEST_IMAGE_GET = 1

    /**
     * editName is the TextEdit used for editing/ showing the forename of the birthday
     * It is lazy initialized
     */
    private val editName: EditText by lazy {
        view!!.findViewById<EditText>(R.id.e_add_tournament_name)
    }

    /**
     * editFullDescription is the TextEdit used for editing/ showing the fullDescription of the birthday
     * It is lazy initialized
     */
    private val editFullDescription: EditText by lazy {
        view!!.findViewById<EditText>(R.id.e_add_tournament_full_description)
    }

    /**
     * editShortDescription is the TextEdit used for editing/ showing the shortDescription of the birthday
     * It is lazy initialized
     */
    private val editShortDescription: EditText by lazy {
        view!!.findViewById<EditText>(R.id.e_add_tournament_short_description)
    }

    /**
     * editStartDateCalendarview is the TextEdit used for editing/ showing the startDate of the birthday but reprensented by the android calendar view
     * It is lazy initialized
     */
    private val editStartDateCalendarview: TextView by lazy {
        view!!.findViewById<TextView>(R.id.t_add_tournament_start_date)
    }

    /**
     * editEndDateCalendarview is the TextEdit used for editing/ showing the startDate of the birthday but reprensented by the android calendar view
     * It is lazy initialized
     */
    private val editEndDateCalendarview: TextView by lazy {
        view!!.findViewById<TextView>(R.id.t_add_tournament_end_date)
    }

    /**
     * editDate is the TextEdit used for editing/ showing the startDate of the birthday
     * It is lazy initialized
     */
    private val editDate: EditText by lazy {
        view!!.findViewById<EditText>(R.id.e_add_tournament_start_date)
    }

    /**
     * editNote is the TextEdit used for editing/ showing the note of the birthday
     * It is lazy initialized
     */
//    private val editNote: EditText by lazy {
//        view!!.findViewById<EditText>(R.id.edit_add_fragment_note)
//    }

    private val refereeSpinner: Spinner by lazy {
        view!!.findViewById<Spinner>(R.id.s_refereeSpinner1)
    }

    private val placeSpinner: Spinner by lazy {
        view!!.findViewById<Spinner>(R.id.s_placeSpinner1)
    }

    inner class PlaceItemSelectedListener : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            addTournamentPresenter?.setSelectedPlaceIndex(position)
        }

        override fun onNothingSelected(arg: AdapterView<*>) {

        }
    }

    inner class RefereeItemSelectedListener : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            addTournamentPresenter?.setSelectedRefereeIndex(position)
        }

        override fun onNothingSelected(arg: AdapterView<*>) {

        }
    }

    private var addTournamentPresenter: AddTournamentPresenterImpl? = null

    private fun getDateRegexFromDateFormatSkeletonPattern(skeletonPattern: String): Regex {
        val dateFormatPattern = EventDate.getLocalizedDateFormatPatternFromSkeleton(skeletonPattern)
        var dateRegex = dateFormatPattern

        var dateRegexArray = dateRegex.split("""\W""".toRegex())
        dateRegexArray = dateRegexArray.map {
            it.replace("""[a-zA-Z]""".toRegex(), """\\d""")
        }

        dateRegex = dateRegexArray.joinToString("""\W""")

        return dateRegex.toRegex()
    }

    private val dateEditRegexNoYear by lazy {
        getDateRegexFromDateFormatSkeletonPattern("ddMM")
    }
    private val dateEditRegexWithYear by lazy {
        getDateRegexFromDateFormatSkeletonPattern("ddMMYYYY")
    }

    /**
     * switchIsYearGiven is the Switch to indicate wether the user wants to provide a startDate with a year or without a year
     * It is lazy initialized
     */
    private val switchIsYearGiven: Switch by lazy {
        view!!.findViewById<Switch>(R.id.sw_is_start_year_given)
    }

    private var isCalendarViewSelected: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_tournament, container, false)
    }

    private var refereeId: Int = 0
    private var placeId: Int = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(false)

        refereeSpinner.onItemSelectedListener = RefereeItemSelectedListener()
        placeSpinner.onItemSelectedListener = PlaceItemSelectedListener()


        if (IOHandler.getBooleanFromKey(IOHandler.SharedPrefKeys.key_date_as_calendar_view) == false) {
            isCalendarViewSelected = false
            editStartDateCalendarview.visibility = EditText.INVISIBLE
            editEndDateCalendarview.visibility = EditText.INVISIBLE
            editDate.visibility = EditText.VISIBLE

        } else {
            isCalendarViewSelected = true
            editStartDateCalendarview.visibility = EditText.VISIBLE
            editEndDateCalendarview.visibility = EditText.VISIBLE
            editDate.visibility = EditText.INVISIBLE
        }

        addTournamentPresenter = AddTournamentPresenterImpl(this)
        addTournamentPresenter!!.setPackageModel(PackageModel(this.context!!))
        addTournamentPresenter!!.attachView(this)
        addTournamentPresenter!!.loadReferees()
        addTournamentPresenter!!.loadPlaces()// todo я хз но походу когда наживаешь редактироватть этот код вызывается и не думаю что это хорошо ....... хотяяяяяяяяяяяяяяяяяя хз

   /*     editName.hint =
            "${context?.getText(R.string.event_property_forename)} ${context?.getText(R.string.necessary)}"*/

        //retrieve fragment parameter when edited instance
        if (arguments != null) {
            isEditedBirthday = true

            setToolbarTitle(context!!.resources.getString(R.string.toolbar_title_edit_tournament))


            eventID = (arguments!!.getInt(MainActivity.FRAGMENT_EXTRA_TITLE_EVENTID))
            EventHandler.getEventToEventIndex(eventID)?.let { tournament ->
                if (tournament is EventTournament) {
                    refereeId = tournament.refereeId!!.toInt()
                    placeId = tournament.placeId!!
                    this.eventStartDate = tournament.eventDate
                    if (this.eventStartDate.after(Calendar.getInstance().time)) {
                        val cal = Calendar.getInstance()
                        cal.time = this.eventStartDate
                        cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - 1)
                        this.eventStartDate = cal.time
                    }


                    // the value which should be assigned to the startDate edit box
                    val startDate: String?
                    val endDate: String?

                    startDate =
                            EventDate.getLocalizedDayMonthYearString(this.eventStartDate)
                    endDate =
                            EventDate.getLocalizedDayMonthYearString(this.eventStartDate)

                    if (!isCalendarViewSelected) {
                        editDate.setText(startDate)
                        editDate.hint = startDate
                    } else {
                        editStartDateCalendarview.text = startDate
                        editStartDateCalendarview.hint = startDate
                        editEndDateCalendarview.text = endDate
                        editEndDateCalendarview.hint = endDate
                    }


                    editShortDescription.setText(tournament.shortDescription)
                    editName.setText(tournament.name)
                    switchIsYearGiven.isChecked = true//birthday.isYearGiven
                    tournamentAvatarUri = tournament.imageUri


                    if (!tournament.fullDescription.isNullOrBlank()) {
                        //cb_nickname.isChecked = true
                        editFullDescription.setText(tournament.fullDescription)
                        editFullDescription.visibility = EditText.VISIBLE
                    }

                    //title.text = resources.getText(R.string.toolbar_title_edit_birthday)
                    btn_birthday_add_fragment_delete.visibility = Button.VISIBLE
                    //delete functionality
                    btn_birthday_add_fragment_delete.setOnClickListener {
                        val alertBuilder = AlertDialog.Builder(context)
                        alertBuilder.setTitle(resources.getString(R.string.alert_dialog_title_delete_birthday))
                        alertBuilder.setMessage(resources.getString(R.string.alert_dialog_body_message))

                        val contextTemp = context

                        // Set a positive button and its click listener on alert dialog
                        alertBuilder.setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                            // delete birthday on positive button
                            Snackbar
                                .make(//todo это надо перенести, чтобы показывалось уже после удаления с сервака, и в действие ниже добавить добавление и на сервак тоже
                                    view,
                                    resources.getString(R.string.tournament_deleted_notification),
                                    Snackbar.LENGTH_LONG
                                )
                                .setAction(R.string.undo) {
                                    EventHandler.addEvent(tournament, contextTemp!!, true)
                                    //get last fragment in stack list, which should be EventListFragment, so we can update the recycler view
                                    val fragment =
                                        (contextTemp as MainActivity).supportFragmentManager.fragments.last()
                                    if (fragment is EventListFragment) {
                                        fragment.recyclerView.adapter!!.notifyDataSetChanged()
                                        fragment.tv_no_events.visibility = TextView.GONE
                                    }
                                }
                                .show()

                            addTournamentPresenter?.removeTournament(eventID.toLong())
                        }

                        // don't do anything on negative button
                        alertBuilder.setNegativeButton(resources.getString(R.string.no)) { _, _ ->
                        }

                        // Finally, make the alert dialog using builder
                        val dialog: AlertDialog = alertBuilder.create()

                        // Display the alert dialog on app interface
                        dialog.show()
                    }
                }

                this.updateAvatarImage()
            }
            //new birthday is going to be added
        } else {
            setToolbarTitle(context!!.resources.getString(R.string.toolbar_title_add_tournament))
            btn_birthday_add_fragment_delete.visibility = Button.INVISIBLE

            if (isCalendarViewSelected) {
                editStartDateCalendarview.hint =
                    EventDate.getLocalizedDateFormatPatternFromSkeleton("ddMMYYYY")
                editEndDateCalendarview.hint =
                        EventDate.getLocalizedDateFormatPatternFromSkeleton("ddMMYYYY")
            } else {
                editDate.hint = EventDate.getLocalizedDateFormatPatternFromSkeleton("ddMMYYYY")
            }
        }

        editDate.setOnFocusChangeListener { editTextView, hasFocus ->
            if (!hasFocus) {
                if (!validateAndSetEditTextDateInput((editTextView as EditText).text.toString())) {
                    editTextView.setTextColor(Color.RED)
                } else {
                    editDate.setTextColor(
                        ContextCompat.getColor(
                            context!!,
                            R.color.textVeryDark
                        )
                    )
                }
            }
        }

        editDate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    //if (switchIsYearGiven.isChecked) {
                        if (!dateEditRegexWithYear.matches(s)) {
                            editDate.setTextColor(Color.RED)
                        } else {
                            editDate.setTextColor(
                                ContextCompat.getColor(
                                    context!!,
                                    R.color.textVeryDark
                                )
                            )
                        }
                  /*  } else {
                        if (!dateEditRegexNoYear.matches(s)) {
                            editDate.setTextColor(Color.RED)
                        } else {
                            editDate.setTextColor(
                                ContextCompat.getColor(
                                    context!!,
                                    R.color.textVeryDark
                                )
                            )
                        }
                    }*/
                }
            }
        })

        editStartDateCalendarview.setOnClickListener {
            showStartDatePickerDialog(true/*switchIsYearGiven.isChecked*/)
        }

        editEndDateCalendarview.setOnClickListener {
            showEndDatePickerDialog(true/*switchIsYearGiven.isChecked*/)
        }

        //add image from gallery
        this.frame_layout_add_avatar_image.setOnClickListener {
            val bottomSheetDialog =
                layoutInflater.inflate(R.layout.fragment_bottom_sheet_dialog, null)

            val dialog = BottomSheetDialog(context!!)
            dialog.setContentView(bottomSheetDialog)

            dialog.findViewById<ConstraintLayout>(R.id.layout_bottom_sheet_choose).apply {
                this?.setOnClickListener {
                    dialog.dismiss()
                    getImageFromFiles()
                }
            }

            dialog.findViewById<ConstraintLayout>(R.id.layout_bottom_sheet_delete).apply {
                this?.setOnClickListener {
                    dialog.dismiss()
                    if (isEditedBirthday && tournamentAvatarUri != null && (EventHandler.getEventToEventIndex(
                            eventID
                        ) as EventTournament).imageUri != null
                    ) {
                        iv_add_avatar_btn.setImageResource(R.drawable.ic_birthday_person)
                        avatarImgWasEdited = true
                        tournamentAvatarUri = null
                        BitmapHandler.removeBitmap(eventID, context!!)
                    } else {
                        iv_add_avatar_btn.setImageResource(R.drawable.ic_birthday_person)
                        tournamentAvatarUri = null
                    }
                }
            }

            dialog.show()
        }

       /* switchIsYearGiven.setOnCheckedChangeListener { _, isChecked ->
            val dateText: String
            val dateHint: String
            //year is given
            if (isChecked) {
                val cal = Calendar.getInstance()
                if (this.eventStartDate.after(cal.time)) {
                    cal.time = this.eventStartDate
                    cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - 1)
                    this.eventStartDate = cal.time
                }

                dateText =
                    EventDate.getLocalizedDayMonthYearString(this.eventStartDate)
                dateHint = EventDate.getLocalizedDateFormatPatternFromSkeleton("ddMMYYYY")

                //year is not given
            } else {
                dateText = EventDate.getLocalizedDayAndMonthString(this.eventStartDate)
                dateHint = EventDate.getLocalizedDateFormatPatternFromSkeleton("ddMM")
            }

            if (isCalendarViewSelected) {
                if (editStartDateCalendarview.text.isNotBlank()) editStartDateCalendarview.text = dateText
                editStartDateCalendarview.hint = dateHint
            } else {
                if (editDate.text.isNotBlank()) editDate.setText(dateText)
                editDate.hint = dateHint
            }
        }*/
    }

    /**
     * getImageFromFiles opens an intent to request a photo from the gallery
     * This function is called after the user clicks on the iv_add_avatar_btn
     */
    private fun getImageFromFiles(): String {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/*"
        }
        intent.addFlags(
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
        if (intent.resolveActivity(context!!.packageManager) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET)
        }
        return "0"
    }

    /**
     * onActivityResult is the result of the gallery intent, here the uri of the photo is processed
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //handle image/photo file choosing
        if (requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {
            val fullPhotoUri: Uri = data!!.data!!

            Thread(Runnable {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(context!!.contentResolver, fullPhotoUri)
                (context as MainActivity).runOnUiThread {
                    iv_add_avatar_btn.setImageBitmap(
                        BitmapHandler.getCircularBitmap(
                            BitmapHandler.getScaledBitmap(
                                bitmap
                            ), resources
                        )
                    )
                }
            }).start()

            tournamentAvatarUri = fullPhotoUri.toString()
            avatarImgWasEdited = true
        }
    }

    /**
     * acceptBtnPressed is a function which is called when the toolbars accept button is pressed
     */
    override fun acceptBtnPressed() {
        val tournamentName = editName.text.toString()
        val tournamentShortDescription = editShortDescription.text.toString()

        val date = if (isCalendarViewSelected) {
            editStartDateCalendarview.text.toString()
        } else {
            if (!validateAndSetEditTextDateInput(editDate.text.toString())) return
            editDate.text.toString()
        }

      //  val note = editNote.text.toString()
        val tournamentFullDescription = editFullDescription.text.toString()
        val isYearGiven = true//switchIsYearGiven.isChecked


        /*if (tournamentName.isBlank() || date.isBlank()) {
            Toast.makeText(
                context,
                context!!.resources.getText(R.string.empty_fields_error_tournament),
                Toast.LENGTH_LONG
            )
                .show()
        } else {*/
        addTournamentPresenter?.addTournament(getTournamentData())
       // }
    }

    private fun getTournamentData(): TournamentDTO {
        val tournamentData = TournamentDTO()
        tournamentData.id = eventID.toLong()
        tournamentData.name = editName.text.toString()
        tournamentData.shortDescription = editShortDescription.text.toString()
        tournamentData.fullDescription = editFullDescription.text.toString()
        tournamentData.countPlayersInTeam = 1
        tournamentData.image = tournamentAvatarUri
        tournamentData.startDate = convertDateToString(eventStartDate)
        tournamentData.finishDate = convertDateToString(eventEndDate)
        return tournamentData
    }

    fun convertDateToString(date: Date):String {
       // var pattern = "yyyy-MM-dd HH:mm:00"
        var pattern = "dd/MM/yyyy"
        var df = SimpleDateFormat(pattern)
        var dateString = df.format(date)
        return dateString
    }

    private fun updateAvatarImage() {
        if (this.iv_add_avatar_btn != null && this.eventID >= 0) {
            //load maybe already existent avatar photo
            EventHandler.getEventToEventIndex(eventID)?.let { event ->
                if (event is EventTournament && event.imageUri != null) {
                    this.iv_add_avatar_btn.setImageBitmap(BitmapHandler.getBitmapAt(eventID))
                    this.iv_add_avatar_btn.isEnabled = true
                }
            }
        }
    }

    /**
     * showStartDatePickerDialog shows a dialog to let the user pick a startDate for the editStartDateCalendarview
     */
    private fun showStartDatePickerDialog(showYear: Boolean) {
        val c = Calendar.getInstance()
        //set calendar to the startDate which is stored in the edit field, when the edit is not empty
        if (!editStartDateCalendarview.text.isNullOrBlank()) {
            c.time = this.eventStartDate
        }
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd =
            DatePickerDialog(
                context!!,
                DatePickerDialog.OnDateSetListener { view, year_, monthOfYear, dayOfMonth ->
                    // Display Selected startDate in Toast
                    c.set(Calendar.YEAR, year_)
                    c.set(Calendar.MONTH, monthOfYear)
                    c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                  /*  if (c.time.after(Calendar.getInstance().time) && showYear) {
                        showFutureDateErrorToast(view.context)
                    } else {*/
                        this.eventStartDate = c.time
                        if (showYear) {
                            editStartDateCalendarview.text =
                                EventDate.getLocalizedDayMonthYearString(c.time)
                        } else {
                            editStartDateCalendarview.text =
                                EventDate.getLocalizedDayAndMonthString(c.time)
                        }
                    //}
                },
                year,
                month,
                day
            )
        dpd.show()
    }

    /**
    * showEndDatePickerDialog shows a dialog to let the user pick a startDate for the editStartDateCalendarview
    */
    private fun showEndDatePickerDialog(showYear: Boolean) {
        val c = Calendar.getInstance()
        //set calendar to the startDate which is stored in the edit field, when the edit is not empty
        if (!editEndDateCalendarview.text.isNullOrBlank()) {
            c.time = this.eventEndDate
        }
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd =
                DatePickerDialog(
                        context!!,
                        DatePickerDialog.OnDateSetListener { view, year_, monthOfYear, dayOfMonth ->
                            // Display Selected startDate in Toast
                            c.set(Calendar.YEAR, year_)
                            c.set(Calendar.MONTH, monthOfYear)
                            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                          /*  if (c.time.after(Calendar.getInstance().time) && showYear) {
                                showFutureDateErrorToast(view.context)
                            } else {*/
                                this.eventStartDate = c.time
                                if (showYear) {
                                    editEndDateCalendarview.text =
                                            EventDate.getLocalizedDayMonthYearString(c.time)
                                } else {
                                    editEndDateCalendarview.text =
                                            EventDate.getLocalizedDayAndMonthString(c.time)
                                }
                           // }
                        },
                        year,
                        month,
                        day
                )
        dpd.show()
    }

    private fun showFutureDateErrorToast(context: Context) {
        Toast.makeText(
            context,
            context.resources.getText(R.string.future_tournament_error),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun validateAndSetEditTextDateInput(dateInput: String): Boolean {
        if (dateInput.isBlank()) return false
        val dateRegEx: Regex?
     //  if (switchIsYearGiven.isChecked) {
            dateRegEx = dateEditRegexWithYear
      /*  } else {
            dateRegEx = dateEditRegexNoYear
        }*/

        // startDate input does not match the required regex -> show error
        if (!dateInput.matches(dateRegEx)) {
            Toast.makeText(
                context,
                resources.getString(
                    R.string.verification_edit_date_input,
                    editDate.hint.toString()
                ),
                Toast.LENGTH_LONG
            ).show()
            return false
        } else {

            // input matches regex, then set it as birthdayevent startDate
            this.eventStartDate = //if (switchIsYearGiven.isChecked) {
                EventDate.parseStringToDateWithPattern("ddMMYYYY", dateInput)
          /*  } else {
                //check if last character in the string is a startDate seperator char, if not, then append one before adding the year
                if (checkForLastDateSeperatorChar(dateInput)) {
                    EventDate.parseStringToDateWithPattern("ddMMYYYY", "${dateInput}2016")
                } else {
                    EventDate.parseStringToDateWithPattern("ddMMYYYY", """${dateInput}/2016""")
                }
            }*/
            if (this.eventStartDate.before(
                    EventDate.parseStringToDate(
                        "01.01.0001",
                        DateFormat.DATE_FIELD,
                        Locale.GERMAN
                    )
                )
            ) {
                Toast.makeText(context, "Man this is too old!", Toast.LENGTH_SHORT).show()
                this.eventStartDate =
                    EventDate.parseStringToDate("01.01.0001", DateFormat.DATE_FIELD, Locale.GERMAN)
            }
        }
        return true
    }

    private fun checkForLastDateSeperatorChar(dateString: String): Boolean {
        return (dateString.last().toString().matches("""\W""".toRegex()))
    }

    companion object {
        /**
         * BIRTHDAY_INSTANCE_FRAGMENT_TAG is the fragments tag as String
         */
        const val BIRTHDAY_INSTANCE_FRAGMENT_TAG = "BIRTHDAY_INSTANCE"

        /**
         * newInstance returns a new instance of TournamentInstanceFragment
         */
        @JvmStatic
        fun newInstance(): TournamentInstanceFragment {
            return TournamentInstanceFragment()
        }
    }

    override fun setRefereeSpinnerAdapter(referees: MutableList<UserDTO>) {
        val refereeNames = Util.getUsersBasicData(referees)
        refereeNames.add(0, getString(R.string.chooseReferee))
        val adapter = ArrayAdapter<String>(this.context!!,
                android.R.layout.simple_spinner_dropdown_item, refereeNames)
        refereeSpinner.adapter = adapter
        refereeSpinner.setSelection(getUserIndexById(referees, refereeId) + 1 )
    }

    override fun setPlaceSpinnerAdapter(places: MutableList<PlaceDTO>?) {
        val placeNames = Util.getPlacesNames(places!!)
        placeNames.add(0, getString(R.string.choosePlace))
        val adapter = ArrayAdapter<String>(this.context!!,
                android.R.layout.simple_spinner_dropdown_item, placeNames)
        placeSpinner.adapter = adapter
        placeSpinner.setSelection(getPlaceIndexById(places, placeId) + 1)
    }

    private fun getUserIndexById(list: MutableList<UserDTO>, id:Int): Int{
        for ((i, value) in list.withIndex()){
            if(value.id?.toInt() == id){
                return i
            }
        }
        return -1
    }

    private fun getPlaceIndexById(list: MutableList<PlaceDTO>, id:Int): Int{
        for ((i, value) in list.withIndex()){
            if(value.id == id){
                return i
            }
        }
        return -1
    }

    override fun startActivity(tournamentDTO: TournamentDTO?) {//тут надо ззаполниь ну и остальные методы. Прогресс показывать и снимать и тд + дата в неверном формате сохраняется
    }

    override fun dismissAlertDialog() {
    }

    override fun showToast(resId: Int?) {
    }

    override fun showToast(message: String?) {
    }

    override fun disableButton() {
    }

    override fun showAlertDialog(title: Int, message: Int, buttonText: Int, cancelable: Boolean) {
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun setConnectionStatus(connectivityStatus: Int?) {
    }

    override fun showIncorrectTournamentNameText() {
    }

    override fun addTournament() {
    }

    override fun enableButton() {
    }

    override fun dialogConfirmButtonClicked() {

    }

    fun createTournamentEvent(tournamentDTO: TournamentDTO?){

    }

    override fun removeTournamentFromLocalStorage(id: Long) {
        EventHandler.removeEventByID(eventID, context!!, true)
        closeBtnPressed()
    }

    override fun tournamentAdded(tournamentDTO: TournamentDTO?) {
        //create new instance from edit fields
          val tournamentEvent = EventTournament(
                  tournamentDTO!!.id.toInt(),
              this.eventStartDate,
              tournamentDTO.name!!
          )


        tournamentEvent.shortDescription = tournamentDTO.shortDescription
        tournamentEvent.fullDescription = tournamentDTO.fullDescription
        tournamentEvent.finishDate = eventEndDate//todo проверить что так можно брать, не из сущности, у старт то же самое
        tournamentEvent.imageUri = tournamentDTO.image
        tournamentEvent.refereeId = tournamentDTO.referee?.id
        tournamentEvent.placeId = tournamentDTO.place?.id

          //new birthday entry, just add a new entry in map
          if (!isEditedBirthday) {
              EventHandler.addEvent(tournamentEvent, this.context!!, true)
              Snackbar.make(
                  view!!,
                  context!!.resources.getString(R.string.tournament_added_notification),
                  Snackbar.LENGTH_LONG
              ).show()
              closeBtnPressed()

              //already existent birthday entry, overwrite old entry in map
          } else {
              EventHandler.getEventToEventIndex(eventID)?.let { event ->
                  if (event is EventTournament) {
                      EventHandler.changeEventAt(eventID, tournamentEvent, context!!, true)
                      Snackbar.make(
                          view!!,
                          context!!.resources.getString(R.string.tournament_changed_notification),
                          Snackbar.LENGTH_LONG
                      ).show()
                  }
                  closeBtnPressed()
              }
          }
    }
}
