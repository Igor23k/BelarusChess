package bobrchess.of.by.belaruschess.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
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
import bobrchess.of.by.belaruschess.util.Util
import bobrchess.of.by.belaruschess.util.Util.Companion.transformUriToFile
import bobrchess.of.by.belaruschess.view.activity.AddTournamentContractView
import bobrchess.of.by.belaruschess.view.activity.PackageModel
import bobrchess.of.by.belaruschess.view.activity.UserContractView
import bobrchess.of.by.belaruschess.view.activity.impl.MainActivity
import kotlinx.android.synthetic.main.fragment_add_new_tournament.*
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
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
class EditTournamentInstanceFragment : EventInstanceFragment(), AddTournamentContractView, UserContractView {

    private var progressDialog: ProgressDialog? = null

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
    private var tournamentImageFile: File? = null
    private var tournamentImageByteArr: ByteArray? = null

    /**
     * avatarImgWasEdited is a boolean flag to store the information whether the avatar img has been changed
     */
    private var imageWasEdited = false

    /**
     * REQUEST_IMAGE_GET is an intent code used for open the photo gallery
     */
    private val REQUEST_IMAGE_GET = 1

    /**
     * REQUEST_TXT_GET is an intent code used for open the photo gallery
     */
    private val REQUEST_TXT_GET = 2

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

            // Set Buttons on Click Listeners
            editName.setOnClickListener {
                checkPermission(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        101)
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
                    if (isEditedTournament && tournamentImageFile != null && (EventHandler.getEventToEventIndex(
                                    eventID
                            ) as EventTournament).image != null
                    ) {
                        iv_add_avatar_btn.setImageResource(R.drawable.ic_birthday_person)
                        imageWasEdited = true
                        tournamentImageFile = null
                    } else {
                        iv_add_avatar_btn.setImageResource(R.drawable.ic_birthday_person)
                        tournamentImageFile = null
                    }
                }
            }

            dialog.show()
        }

        //load txt from gallery
        this.tournament_load_full_description_text.setOnClickListener {
            val bottomSheetDialog =
                    layoutInflater.inflate(R.layout.fragment_bottom_sheet_txt_dialog, null)

            val dialog = BottomSheetDialog(context!!)
            dialog.setContentView(bottomSheetDialog)

            dialog.findViewById<ConstraintLayout>(R.id.layout_bottom_sheet_choose).apply {
                this?.setOnClickListener {
                    dialog.dismiss()
                    getTxtFromFiles()
                }
            }

            dialog.show()
        }
    }

    @Throws(IOException::class)
    fun readTextFromUri(context: Context, uri: Uri): String {
        val stringBuilder = StringBuilder()
        context.contentResolver.openInputStream(uri).use { inputStream ->
            BufferedReader(
                    InputStreamReader(Objects.requireNonNull(inputStream))).use { reader ->
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    stringBuilder.append(line?.plus("\n"))
                }
            }
        }
        return stringBuilder.toString()
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
     * getImageFromFiles opens an intent to request a photo from the gallery
     * This function is called after the user clicks on the iv_add_avatar_btn
     */
    private fun getTxtFromFiles(): String {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "*/*"
        }
        intent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
        if (intent.resolveActivity(context!!.packageManager) != null) {
            startActivityForResult(intent, REQUEST_TXT_GET)
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
            Thread {
                val bitmap =
                        MediaStore.Images.Media.getBitmap(context!!.contentResolver, fullPhotoUri)

                if (bitmap != null) {
                    (context as MainActivity).runOnUiThread {
                        iv_add_avatar_btn.setImageBitmap(
                                BitmapHandler.getCircularBitmap(
                                        BitmapHandler.getScaledBitmap(
                                                bitmap
                                        ), resources
                                )
                        )
                    }
                    tournamentImageFile = this.context?.let { transformUriToFile(it, fullPhotoUri) }
                    imageWasEdited = true
                } else {
                    (context as MainActivity).runOnUiThread {
                        this.showToast(R.string.incorrect_image)
                    }
                }
            }.start()
        } else if (requestCode == REQUEST_TXT_GET && resultCode == Activity.RESULT_OK) {
            val fullPhotoUri: Uri = data!!.data!!
            val text = readTextFromUri(this.context!!, fullPhotoUri)
            editFullDescription.setText(text)
        }
    }

    // Function to check and request permission.
    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this.context!!, permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(this.activity!!, arrayOf(permission), requestCode)
        } else {
            Toast.makeText(this.context, "Permission already granted", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * acceptBtnPressed is a function which is called when the toolbars accept button is pressed
     */
    override fun acceptBtnPressed() {
        try {
            addTournamentPresenter?.addTournament(getTournamentData(), tournamentImageFile)
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
        tournamentData.createdBy = (context as MainActivity).getUserData()
        tournamentData.startDate = eventStartDate?.time.toString()
        tournamentData.finishDate = eventEndDate?.time.toString()
        return tournamentData
    }

    private fun updateImage() {
        if (this.iv_add_avatar_btn != null && this.eventID >= 0) {
            //load maybe already existent avatar photo
            EventHandler.getEventToEventIndex(eventID)?.let { event ->
                if (event is EventTournament && event.image != null) {
                    val bitmap = context?.let { Util.getScaledBitMapByByteArr(event.image, context!!.resources) };
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
        dpd.datePicker.minDate = System.currentTimeMillis()
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
        dpd.datePicker.minDate = System.currentTimeMillis()
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
        fun newInstance(): EditTournamentInstanceFragment {
            return EditTournamentInstanceFragment()
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

    override fun startActivity(tournamentDTO: TournamentDTO?) {
    }

    override fun disableButton() {
    }

    override fun showProgress() {
        progressDialog = ProgressDialog.show(this.context, Constants.EMPTY_STRING, this.getString(R.string.please_wait))
    }

    override fun hideProgress() {
        if (progressDialog != null) {
            progressDialog?.dismiss()
        }
    }

    override fun setConnectionStatus(connectivityStatus: Int?) {
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
        tournamentEvent.image = tournamentDTO.image
        tournamentEvent.refereeId = tournamentDTO.referee?.id
        tournamentEvent.createdBy = tournamentDTO.createdBy?.id
        tournamentEvent.placeId = tournamentDTO.place?.id
        tournamentEvent.toursCount = tournamentDTO.toursCount

        //new tournament entry, just add a new entry in map
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
                this.eventEndDate = tournament.finishDate

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
                tournamentImageByteArr = tournament.image


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

            this.updateImage()
        }

        setPlaceSpinnerAdapter(places as MutableList<out PlaceDTO>?)
        setRefereeSpinnerAdapter(referees as MutableList<out UserDTO>?)
    }
}
