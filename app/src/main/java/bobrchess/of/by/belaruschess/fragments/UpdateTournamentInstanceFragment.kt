package bobrchess.of.by.belaruschess.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
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
import bobrchess.of.by.belaruschess.dto.PlaceDTO
import bobrchess.of.by.belaruschess.dto.TournamentDTO
import bobrchess.of.by.belaruschess.dto.UserDTO
import bobrchess.of.by.belaruschess.dto.extended.ExtendedTournamentDTO
import bobrchess.of.by.belaruschess.handler.BitmapHandler
import bobrchess.of.by.belaruschess.handler.EventHandler
import bobrchess.of.by.belaruschess.handler.IOHandler
import bobrchess.of.by.belaruschess.model.EventDate
import bobrchess.of.by.belaruschess.model.EventTournament
import bobrchess.of.by.belaruschess.presenter.impl.AddTournamentPresenterImpl
import bobrchess.of.by.belaruschess.presenter.impl.UserPresenterImpl
import bobrchess.of.by.belaruschess.util.Constants
import bobrchess.of.by.belaruschess.util.Constants.Companion.USER_BIRTHDAY_FORMAT
import bobrchess.of.by.belaruschess.util.Util
import bobrchess.of.by.belaruschess.view.activity.AddTournamentContractView
import bobrchess.of.by.belaruschess.view.activity.PackageModel
import bobrchess.of.by.belaruschess.view.activity.UserContractView
import bobrchess.of.by.belaruschess.view.activity.impl.MainActivity
import kotlinx.android.synthetic.main.fragment_add_new_tournament.*
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
 */
class UpdateTournamentInstanceFragment : EventInstanceFragment(), AddTournamentContractView, UserContractView {

    private var progressDialog: ProgressDialog? = null
    private var users: List<UserDTO>? = null

    /**
     * isEditedBirthday is a boolean flag to indicate whether this fragment is in "edit" mode aka. the user wants to edit an existing instance of EventTournament
     */
    private var isEditedTournament: Boolean = false

    /**
     * eventID is the index of the clicked item in EventListFragments RecyclerView, this is handy to get the birthday instance from the EventHandler
     */
    var eventID = -1

    /**
     * tournamentAvatarUri is a string to store the user picked image for the avatar
     */
    private var tournamentImage: String? = null

    /**
     * avatarImgWasEdited is a boolean flag to store the information whether the avatar img has been changed
     */
    private var imageWasEdited = false

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


    private val editToursCount: EditText by lazy {
        view!!.findViewById<EditText>(R.id.e_add_tournament_toursCount)
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
    private var userPresenterImpl: UserPresenterImpl? = null

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

    private var places: List<PlaceDTO>? = null
    private var referees: List<UserDTO>? = null

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

        val activity: MainActivity? = activity as MainActivity?
        places = activity!!.getPlaces()

        addTournamentPresenter = AddTournamentPresenterImpl(this)
        addTournamentPresenter!!.setPackageModel(PackageModel(this.context!!))
        addTournamentPresenter!!.attachView(this)
        addTournamentPresenter!!.viewIsReady()

        userPresenterImpl = UserPresenterImpl()
        userPresenterImpl!!.attachView(this)
        userPresenterImpl!!.setPackageModel(PackageModel(this.context!!))
        userPresenterImpl!!.viewIsReady()
        userPresenterImpl!!.loadReferees()

        //retrieve fragment parameter when edited instance
        if (arguments != null) {
            isEditedTournament = true

            setToolbarTitle(context!!.resources.getString(R.string.toolbar_title_edit_tournament))
            eventID = (arguments!!.getInt(MainActivity.FRAGMENT_EXTRA_TITLE_EVENTID))
            //new birthday is going to be added
        } else {
            setToolbarTitle(context!!.resources.getString(R.string.toolbar_title_add_tournament))
            btn_tournament_add_fragment_delete.visibility = Button.INVISIBLE

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
                }
            }
        })

        editStartDateCalendarview.setOnClickListener {
            showStartDatePickerDialog(true)
        }

        editEndDateCalendarview.setOnClickListener {
            showEndDatePickerDialog(true)
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
                    if (isEditedTournament && tournamentImage != null && (EventHandler.getEventToEventIndex(
                                    eventID
                            ) as EventTournament).imageUri != null
                    ) {
                        iv_add_avatar_btn.setImageResource(R.drawable.ic_birthday_person)
                        imageWasEdited = true
                        tournamentImage = null
                    } else {
                        iv_add_avatar_btn.setImageResource(R.drawable.ic_birthday_person)
                        tournamentImage = null
                    }
                }
            }

            dialog.show()
        }
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

            val imageInputStream = context!!.contentResolver.openInputStream(data.data!!)
            val encodedImage = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageInputStream?.readBytes())

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

            tournamentImage = encodedImage
            imageWasEdited = true
        }
    }

    /**
     * acceptBtnPressed is a function which is called when the toolbars accept button is pressed
     */
    override fun acceptBtnPressed() {
        try {
            addTournamentPresenter?.addTournament(getTournamentData())
        } catch (e: NumberFormatException) {
            hideProgress()
            showToast(R.string.incorrect_tours_count);
            enableButton();
        }
    }

    @Throws(NumberFormatException::class)
    private fun getTournamentData(): ExtendedTournamentDTO {
        val tournamentData = ExtendedTournamentDTO()
        tournamentData.id = eventID.toLong()
        tournamentData.name = editName.text.toString()
        tournamentData.shortDescription = editShortDescription.text.toString()
        tournamentData.fullDescription = editFullDescription.text.toString()
        tournamentData.toursCount = Integer.parseInt(e_add_tournament_toursCount.text.toString())
        tournamentData.countPlayersInTeam = 1
        tournamentData.image = tournamentImage
        tournamentData.createdBy = (context as MainActivity).getUserData()
        tournamentData.startDate = convertDateToString(eventStartDate)
        tournamentData.finishDate = convertDateToString(eventEndDate)
        return tournamentData
    }

    fun convertDateToString(date: Date?): String? {
        if (date != null) {
            var pattern = USER_BIRTHDAY_FORMAT
            var df = SimpleDateFormat(pattern)
            var dateString = df.format(date)
            return dateString
        }
        return null
    }

    private fun updateAvatarImage() {
        if (this.iv_add_avatar_btn != null && this.eventID >= 0) {
            //load maybe already existent avatar photo
            EventHandler.getEventToEventIndex(eventID)?.let { event ->
                if (event is EventTournament && event.imageUri != null) {
                    val bitmap = Util.getScaledBitMapByBase64(event.imageUri, resources)
                    this.iv_add_avatar_btn.setImageBitmap(bitmap)
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
                        DatePickerDialog.OnDateSetListener { _, year_, monthOfYear, dayOfMonth ->
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
                        DatePickerDialog.OnDateSetListener { _, year_, monthOfYear, dayOfMonth ->
                            // Display Selected startDate in Toast
                            c.set(Calendar.YEAR, year_)
                            c.set(Calendar.MONTH, monthOfYear)
                            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                            this.eventEndDate = c.time
                            if (showYear) {
                                editEndDateCalendarview.text =
                                        EventDate.getLocalizedDayMonthYearString(c.time)
                            } else {
                                editEndDateCalendarview.text =
                                        EventDate.getLocalizedDayAndMonthString(c.time)
                            }
                        },
                        year,
                        month,
                        day
                )
        dpd.show()
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
            this.eventStartDate = EventDate.parseStringToDateWithPattern("ddMMYYYY", dateInput)
        }
        return true
    }

    companion object {
        /**
         * TOURNAMENT_INSTANCE_FRAGMENT_TAG is the fragments tag as String
         */
        const val TOURNAMENT_INSTANCE_FRAGMENT_TAG = "TOURNAMENT_INSTANCE"

        /**
         * newInstance returns a new instance of TournamentInstanceFragment
         */
        @JvmStatic
        fun newInstance(): UpdateTournamentInstanceFragment {
            return UpdateTournamentInstanceFragment()
        }
    }

    override fun setRefereeSpinnerAdapter(referees: MutableList<out UserDTO>?) {
        val refereeNames = Util.getUsersBasicData(referees)
        refereeNames.add(0, getString(R.string.chooseReferee))
        val adapter = ArrayAdapter<String>(this.context!!,
                android.R.layout.simple_spinner_dropdown_item, refereeNames)
        refereeSpinner.adapter = adapter
        refereeSpinner.setSelection(getUserIndexById(referees, refereeId) + 1)
        addTournamentPresenter?.saveRefereesIndexes(referees)
    }

    override fun setPlaceSpinnerAdapter(places: MutableList<out PlaceDTO>?) {
        val placeNames = Util.getPlacesNames(places!!)
        placeNames.add(0, getString(R.string.chooseLocation))
        val adapter = ArrayAdapter<String>(this.context!!,
                android.R.layout.simple_spinner_dropdown_item, placeNames)
        placeSpinner.adapter = adapter
        placeSpinner.setSelection(getPlaceIndexById(places, placeId) + 1)
        addTournamentPresenter?.savePlacesIndexes(places)
    }

    private fun getUserIndexById(list: MutableList<out UserDTO>?, id: Int): Int {
        for ((i, value) in list!!.withIndex()) {
            if (value.id?.toInt() == id) {
                return i
            }
        }
        return -1
    }

    private fun getPlaceIndexById(list: MutableList<out PlaceDTO>, id: Int): Int {
        for ((i, value) in list.withIndex()) {
            if (value.id == id) {
                return i
            }
        }
        return -1
    }

    override fun startActivity(tournamentDTO: TournamentDTO?) {//todo тут надо ззаполниь ну и остальные методы. Прогресс показывать и снимать и тд + дата в неверном формате сохраняется
    }

    override fun disableButton() {
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

    override fun showIncorrectTournamentNameText() {
        //todo to check работает ли и почему нету проверок на остальные поля
    }

    override fun enableButton() {
    }

    override fun dialogConfirmButtonClicked() {

    }

    override fun removeTournamentFromLocalStorage(id: Long) {
        EventHandler.removeEventByID(eventID, context!!, true)
        closeBtnPressed()
    }

    override fun tournamentAdded(tournamentDTO: TournamentDTO?) {
        //create new instance from edit fields
        val tournamentEvent = EventTournament(
                tournamentDTO!!.id.toInt(),
                this.eventStartDate!!,
                tournamentDTO.name!!
        )


        tournamentEvent.shortDescription = tournamentDTO.shortDescription
        tournamentEvent.fullDescription = tournamentDTO.fullDescription
        tournamentEvent.finishDate = eventEndDate
        tournamentEvent.imageUri = tournamentDTO.image
        tournamentEvent.refereeId = tournamentDTO.referee?.id
        tournamentEvent.createdBy = tournamentDTO.createdBy?.id
        tournamentEvent.placeId = tournamentDTO.place?.id
        tournamentEvent.toursCount = tournamentDTO.toursCount

        //new birthday entry, just add a new entry in map
        if (!isEditedTournament) {
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

    override fun showUsers(users: List<UserDTO>?) {
        referees = users
        updateUI()
    }

    override fun showUser(user: UserDTO?) {

    }

    private fun updateUI() {
        EventHandler.getEventToEventIndex(eventID)?.let { tournament ->
            if (tournament is EventTournament) {
                refereeId = tournament.refereeId!!.toInt()
                placeId = tournament.placeId!!
                this.eventStartDate = tournament.eventDate
                if (this.eventStartDate!!.after(Calendar.getInstance().time)) {
                    val cal = Calendar.getInstance()
                    cal.time = this.eventStartDate
                    cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - 1)
                    this.eventStartDate = cal.time
                }

                this.eventEndDate = tournament.finishDate
                if (this.eventEndDate!!.after(Calendar.getInstance().time)) {
                    val cal = Calendar.getInstance()
                    cal.time = this.eventEndDate
                    cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - 1)
                    this.eventEndDate = cal.time
                }


                // the value which should be assigned to the startDate edit box
                val startDate: String?
                val endDate: String?

                startDate = EventDate.getLocalizedDayMonthYearString(this.eventStartDate)
                endDate = EventDate.getLocalizedDayMonthYearString(this.eventEndDate)

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
                editToursCount.setText(tournament.toursCount.toString())
                editName.setText(tournament.name)
                switchIsYearGiven.isChecked = true
                tournamentImage = tournament.imageUri


                if (!tournament.fullDescription.isNullOrBlank()) {
                    //cb_nickname.isChecked = true
                    editFullDescription.setText(tournament.fullDescription)
                    editFullDescription.visibility = EditText.VISIBLE
                }

                btn_tournament_add_fragment_delete.visibility = Button.VISIBLE
                //delete functionality
                btn_tournament_add_fragment_delete.setOnClickListener {
                    val alertBuilder = AlertDialog.Builder(context)
                    alertBuilder.setTitle(resources.getString(R.string.confirmation))
                    alertBuilder.setMessage(resources.getString(R.string.alert_dialog_body_message_tournament))

                    // Set a positive button and its click listener on alert dialog
                    alertBuilder.setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
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

                addTournamentPresenter!!.savePlacesIndexes(places)
                addTournamentPresenter!!.saveRefereesIndexes(referees)
            }

            this.updateAvatarImage()
        }

        setPlaceSpinnerAdapter(places as MutableList<out PlaceDTO>?)
        setRefereeSpinnerAdapter(referees as MutableList<out UserDTO>?)
    }
}
