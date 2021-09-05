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
import bobrchess.of.by.belaruschess.dto.CountryDTO
import bobrchess.of.by.belaruschess.dto.RankDTO
import bobrchess.of.by.belaruschess.dto.UserDTO
import bobrchess.of.by.belaruschess.dto.extended.ExtendedUserDTO
import bobrchess.of.by.belaruschess.handler.BitmapHandler
import bobrchess.of.by.belaruschess.handler.EventHandler
import bobrchess.of.by.belaruschess.model.EventDate
import bobrchess.of.by.belaruschess.model.EventUser
import bobrchess.of.by.belaruschess.presenter.impl.UserPresenterImpl
import bobrchess.of.by.belaruschess.util.Constants
import bobrchess.of.by.belaruschess.util.Constants.Companion.USER_BIRTHDAY_FORMAT
import bobrchess.of.by.belaruschess.util.Util
import bobrchess.of.by.belaruschess.view.activity.EditUserContractView
import bobrchess.of.by.belaruschess.view.activity.PackageModel
import bobrchess.of.by.belaruschess.view.activity.UserContractView
import bobrchess.of.by.belaruschess.view.activity.impl.MainActivity
import kotlinx.android.synthetic.main.fragment_edit_user.*
import org.springframework.util.StringUtils
import java.text.SimpleDateFormat
import java.util.*


/**
 *
 * UserInstanceFragment is a fragment class for adding/editing an instance of EventUser
 * This fragment shows up, when the users wants to add a new EventUser or edit an existing one
 * The fragment consists of several TextEdits to manage user data input
 *
 * This class inherits from android.support.v4.app.Fragment
 *
 */
class EditUserInstanceFragment : EventInstanceFragment(), EditUserContractView, UserContractView {

    private var progressDialog: ProgressDialog? = null
    private var users: List<UserDTO>? = null

    /**
     * eventID is the index of the clicked item in EventListFragments RecyclerView, this is handy to get the birthday instance from the EventHandler
     */
    var eventID = -1

    /**
     * userAvatarUri is a string to store the user picked image for the avatar
     */
    private var userAvatar: String? = null

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
        view!!.findViewById<EditText>(R.id.e_edit_name)
        //view!!.findViewById<EditText>(R.id.e_user_name)
    }

    private val editSurname: EditText by lazy {
        view!!.findViewById<EditText>(R.id.e_edit_surname)
    }

    private val editPatronymic: EditText by lazy {
        view!!.findViewById<EditText>(R.id.e_edit_patronymic)
    }

    private val editEmail: EditText by lazy {
        view!!.findViewById<EditText>(R.id.e_edit_email)
    }

    private val editPassword: EditText by lazy {
        view!!.findViewById<EditText>(R.id.e_edit_password)
    }

    private val editReEnterPassword: EditText by lazy {
        view!!.findViewById<EditText>(R.id.e_edit_re_enter_password)
    }

    private val editPhoneNumber: EditText by lazy {
        view!!.findViewById<EditText>(R.id.e_edit_phone_number)
    }

    private val editRating: EditText by lazy {
        view!!.findViewById<EditText>(R.id.e_edit_rating)
    }

    /**
     * editStartDateCalendarview is the TextEdit used for editing/ showing the startDate of the birthday but reprensented by the android calendar view
     * It is lazy initialized
     */
    private val editBirthdayCalendarview: TextView by lazy {
        view!!.findViewById<TextView>(R.id.t_edit_user_birthday)
    }

    /**
     * editDate is the TextEdit used for editing/ showing the startDate of the birthday
     * It is lazy initialized
     */
    private val editBirthday: TextView by lazy {
        view!!.findViewById<TextView>(R.id.e_user_birthday)
    }

    private val coachSpinner: Spinner by lazy {
        view!!.findViewById<Spinner>(R.id.s_coachSpinner)
    }

    private val rankSpinner: Spinner by lazy {
        view!!.findViewById<Spinner>(R.id.s_rankSpinner)
    }

    private val countrySpinner: Spinner by lazy {
        view!!.findViewById<Spinner>(R.id.s_countrySpinner)
    }

    private val genderSpinner: Spinner by lazy {
        view!!.findViewById<Spinner>(R.id.s_genderSpinner)
    }

    inner class CountryItemSelectedListener : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            userPresenterImpl?.setSelectedCountryIndex(position)
        }

        override fun onNothingSelected(arg: AdapterView<*>) {

        }
    }

    inner class GenderItemSelectedListener : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            userPresenterImpl?.setSelectedGenderIndex(position)
        }

        override fun onNothingSelected(arg: AdapterView<*>) {

        }
    }

    inner class RankItemSelectedListener : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            userPresenterImpl?.setSelectedRankIndex(position)
        }

        override fun onNothingSelected(arg: AdapterView<*>) {

        }
    }

    inner class CoachItemSelectedListener : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            userPresenterImpl?.setSelectedCoachIndex(position)
        }

        override fun onNothingSelected(arg: AdapterView<*>) {

        }
    }

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

    private val dateEditRegexWithYear by lazy {
        getDateRegexFromDateFormatSkeletonPattern("ddMMYYYY")
    }

    /**
     * switchIsYearGiven is the Switch to indicate wether the user wants to provide a startDate with a year or without a year
     * It is lazy initialized
     */
    /* private val switchIsYearGiven: Switch by lazy {
         view!!.findViewById<Switch>(R.id.sw_is_start_year_given)
     }*/

    private var ranks: List<RankDTO>? = null
    private var coaches: List<UserDTO>? = null
    private var countries: List<CountryDTO>? = null
    private val genders: MutableList<String>
        get() {
            val genders = ArrayList<String>()
            genders.add(Util.getInternalizedMessage(Constants.KEY_GENDER_MALE))
            genders.add(Util.getInternalizedMessage(Constants.KEY_GENDER_FEMALE))
            return genders
        }
    private var birthday: String? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_user, container, false)
    }

    private var coach: String? = null
    private var countryId: Int = 0
    private var genderId: Int = 0
    private var rankId: Int = 0
    private var user: UserDTO? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(false)

        coachSpinner.onItemSelectedListener = CoachItemSelectedListener()
        rankSpinner.onItemSelectedListener = RankItemSelectedListener()
        countrySpinner.onItemSelectedListener = CountryItemSelectedListener()
        genderSpinner.onItemSelectedListener = GenderItemSelectedListener()

        val activity: MainActivity? = activity as MainActivity?
        ranks = activity!!.getRanks()
        countries = activity.getCountries()
        user = activity.getUserData()

        userPresenterImpl = UserPresenterImpl()
        userPresenterImpl!!.attachView(this)
        userPresenterImpl!!.setPackageModel(PackageModel(this.context!!))
        userPresenterImpl!!.viewIsReady()
        userPresenterImpl!!.loadCoaches()

        setToolbarTitle(context!!.resources.getString(R.string.toolbar_title_edit_user))
        editBirthday.hint = EventDate.getLocalizedDateFormatPatternFromSkeleton("ddMMYYYY")
        eventID = activity.getUserData()!!.id!!.toInt()


        editBirthday.setOnFocusChangeListener { editTextView, hasFocus ->
            if (!hasFocus) {
                if (!validateAndSetEditTextDateInput((editTextView as EditText).text.toString())) {
                    editTextView.setTextColor(Color.RED)
                } else {
                    editBirthday.setTextColor(
                            ContextCompat.getColor(
                                    context!!,
                                    R.color.textVeryDark
                            )
                    )
                }
            }
        }

        editBirthday.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    //if (switchIsYearGiven.isChecked) {
                    if (!dateEditRegexWithYear.matches(s)) {
                        editBirthday.setTextColor(Color.RED)
                    } else {
                        editBirthday.setTextColor(
                                ContextCompat.getColor(
                                        context!!,
                                        R.color.textVeryDark
                                )
                        )
                    }
                }
            }
        })

        editBirthdayCalendarview.setOnClickListener {
            showStartDatePickerDialog(true)
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
                    iv_add_avatar_btn.setImageResource(R.drawable.ic_birthday_person)
                    imageWasEdited = true
                    userAvatar = null
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

            userAvatar = encodedImage
            imageWasEdited = true
        }
    }

    /**
     * acceptBtnPressed is a function which is called when the toolbars accept button is pressed
     */
    override fun acceptBtnPressed() {
        try {
            userPresenterImpl?.updateUser(getUserData())
        } catch (e: NumberFormatException) {
            hideProgress()
            showToast(R.string.incorrect_tours_count);
            enableButton();
        }
    }

    @Throws(NumberFormatException::class)
    private fun getUserData(): ExtendedUserDTO {
        val userData = ExtendedUserDTO()
        userData.id = eventID.toLong()
        userData.name = editName.text.toString()
        userData.surname = editSurname.text.toString()
        userData.patronymic = editPatronymic.text.toString()
        userData.email = editEmail.text.toString()

        if (!StringUtils.isEmpty(editPassword.text.toString())) {
            userData.password = editPassword.text.toString()
            userData.reEnterPassword = editReEnterPassword.text.toString()
        } else {
            userData.password = user?.password
            userData.reEnterPassword = user?.password
        }

        userData.phoneNumber = editPhoneNumber.text.toString()
        userData.rating = editRating.text.toString().toInt()
        userData.image = userAvatar
        userData.birthday = convertDateToString(eventStartDate)
        return userData
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
            if (userAvatar != null) {
                val bitmap = Util.getScaledBitMapByBase64(userAvatar, resources)
                this.iv_add_avatar_btn.setImageBitmap(bitmap)
                this.iv_add_avatar_btn.isEnabled = true
            }
        }
    }

    /**
     * showStartDatePickerDialog shows a dialog to let the user pick a startDate for the editStartDateCalendarview
     */
    private fun showStartDatePickerDialog(showYear: Boolean) {
        val c = Calendar.getInstance()
        //set calendar to the startDate which is stored in the edit field, when the edit is not empty
        if (!editBirthdayCalendarview.text.isNullOrBlank()) {
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
                                editBirthdayCalendarview.text =
                                        EventDate.getLocalizedDayMonthYearString(c.time)
                            } else {
                                editBirthdayCalendarview.text =
                                        EventDate.getLocalizedDayAndMonthString(c.time)
                            }
                            //}
                        },
                        year,
                        month,
                        day
                )
        dpd.datePicker.maxDate = System.currentTimeMillis()
        dpd.show()
    }

    private fun validateAndSetEditTextDateInput(dateInput: String): Boolean {
        if (dateInput.isBlank()) return false
        val dateRegEx: Regex?
        dateRegEx = dateEditRegexWithYear


        // startDate input does not match the required regex -> show error
        if (!dateInput.matches(dateRegEx)) {
            Toast.makeText(
                    context,
                    resources.getString(
                            R.string.verification_edit_date_input,
                            editBirthday.hint.toString()
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
         * USER_INSTANCE_FRAGMENT_TAG is the fragments tag as String
         */
        const val USER_INSTANCE_FRAGMENT_TAG = "USER_INSTANCE"

        /**
         * newInstance returns a new instance of UserInstanceFragment
         */
        @JvmStatic
        fun newInstance(): EditUserInstanceFragment {
            return EditUserInstanceFragment()
        }
    }

    override fun setGenderSpinnerAdapter(genders: MutableList<String>) {
        //genders.add(0, getString(R.string.chooseGender))
        val adapter = ArrayAdapter(this.context!!,
                android.R.layout.simple_spinner_dropdown_item, genders)
        genderSpinner.adapter = adapter
        genderSpinner.setSelection(genderId)
    }

    override fun setCountrySpinnerAdapter(countries: MutableList<out CountryDTO>?) {
        val countriesNames = Util.getCountriesBasicData(countries)
        //countriesNames.add(0, getString(R.string.chooseCountry))
        val adapter = ArrayAdapter<String>(this.context!!,
                android.R.layout.simple_spinner_dropdown_item, countriesNames)
        countrySpinner.adapter = adapter
        countrySpinner.setSelection(getCountryIndexById(countries, countryId))
        userPresenterImpl?.saveCountriesIndexes(countries)
    }

    override fun setCoachSpinnerAdapter(coaches: MutableList<out UserDTO>?) {
        val updatedCoaches = removeCurrentUserFromList(coaches)
        val coachNames = Util.getUsersBasicData(updatedCoaches)
        coachNames.add(0, getString(R.string.chooseCoachToUpdate))
        val adapter = ArrayAdapter<String>(this.context!!,
                android.R.layout.simple_spinner_dropdown_item, coachNames)
        coachSpinner.adapter = adapter
        //coachSpinner.setSelection(getUserIndexById(coaches, coach))
        userPresenterImpl?.saveCoachesIndexes(updatedCoaches)
    }

    fun removeCurrentUserFromList(coaches: MutableList<out UserDTO>?):MutableList<out UserDTO>? {
        if (user != null) {
            val id = user!!.id
            coaches?.removeIf { coach ->
                coach.id == id
            }
        }
        return coaches
    }

    override fun setRankSpinnerAdapter(ranks: MutableList<out RankDTO>?) {
        val rankNames = Util.getRanksNames(ranks!!)
        //rankNames.add(0, getString(R.string.chooseRank))
        val adapter = ArrayAdapter<String>(this.context!!,
                android.R.layout.simple_spinner_dropdown_item, rankNames)
        rankSpinner.adapter = adapter
        rankSpinner.setSelection(getRankIndexById(ranks, rankId))
        userPresenterImpl?.saveRanksIndexes(ranks)
    }

    private fun getCountryIndexById(list: MutableList<out CountryDTO>?, id: Int): Int {
        if (list != null) {
            for ((i, value) in list.withIndex()) {
                if (value.id == id) {
                    return i
                }
            }
        }
        return -1
    }

    private fun getUserIndexById(list: MutableList<out UserDTO>?, coachName: String?): Int {
        if (list != null) {
            for ((i, value) in list.withIndex()) {
                if (value.name + " " + value.surname == coachName) {
                    return i
                }
            }
        }
        return -1
    }

    private fun getRankIndexById(list: MutableList<out RankDTO>, id: Int): Int {
        for ((i, value) in list.withIndex()) {
            if (value.id == id) {
                return i
            }
        }
        return -1
    }

    override fun startActivity(userDTO: UserDTO?) {//todo тут надо ззаполниь ну и остальные методы. Прогресс показывать и снимать и тд + дата в неверном формате сохраняется
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

    override fun enableButton() {
    }

    override fun dialogConfirmButtonClicked() {

    }

    override fun userUpdated(userDTO: UserDTO?) {
        //create new instance from edit fields
        val userEvent = EventUser(
                userDTO!!.id!!.toInt(),
                this.eventStartDate!!,
                userDTO.name!!,
                userDTO.surname!!
        )

        userEvent.imageUri = userDTO.image
        userEvent.patronymic = userDTO.patronymic
        userEvent.phoneNumber = userDTO.phoneNumber
        userEvent.rating = userDTO.rating
        userEvent.coach = userDTO.coach
        userEvent.countryId = userDTO.country?.id
        userEvent.rankId = userDTO.rank?.id
        userEvent.beMale = userDTO.beMale //userEvent.genderId = userDTO.get?.id todo coach тоже
                //и остальных проверить

        /* if (!isEditedUser) {
            *//* EventHandler.addEvent(userEvent, this.context!!, true)
            Snackbar.make(
                    view!!,
                    context!!.resources.getString(R.string.user_edited_notification),
                    Snackbar.LENGTH_LONG
            ).show()
            closeBtnPressed()*//*

            //already existent birthday entry, overwrite old entry in map
        } else {*/
        EventHandler.getEventToEventIndex(eventID)?.let { event ->
            if (event is EventUser) {
                EventHandler.changeEventAt(eventID, userEvent, context!!, true)
                Snackbar.make(
                        view!!,
                        context!!.resources.getString(R.string.user_edited_notification),
                        Snackbar.LENGTH_LONG
                ).show()
            }
            closeBtnPressed()
        }
        //}
    }

    override fun showUsers(coaches: List<UserDTO>?) {
        this.coaches = coaches
        updateUI()
    }

    private fun getGenderId(beMale: Boolean?): Int {
        if (beMale != null) {
            return if (beMale == true) {
                0
            } else 1;
        }

        return -1;
    }

    private var isCalendarViewSelected: Boolean = true

    private fun updateUI() {
        coach = user?.coach
        rankId = user?.rank?.id!!
        genderId = getGenderId(user?.beMale)
        countryId = user?.country?.id!!
        this.eventStartDate = EventDate.parseStringToDateWithPattern("ddMMYYYY", user!!.birthday!!)

        if (this.eventStartDate!!.after(Calendar.getInstance().time)) {
            val cal = Calendar.getInstance()
            cal.time = this.eventStartDate
            cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - 1)
            this.eventStartDate = cal.time
        }

        // the value which should be assigned to the startDate edit box
        val startDate: String?

        startDate = EventDate.getLocalizedDayMonthYearString(this.eventStartDate)

        /* if (!isCalendarViewSelected) {
             editBirthday.setText(startDate)
             editBirthday.hint = startDate
         } else {*/
        editBirthdayCalendarview.text = startDate
        editBirthdayCalendarview.hint = startDate
        //  }


        editName.setText(user!!.name)
        editSurname.setText(user!!.surname)
        editPatronymic.setText(user!!.patronymic)
        editRating.setText(user!!.rating.toString())
        editEmail.setText(user!!.email.toString())
        editPhoneNumber.setText(user!!.phoneNumber.toString())
        userAvatar = user!!.image

        btn_user_edit_fragment.setOnClickListener {
            val alertBuilder = AlertDialog.Builder(context)
            alertBuilder.setTitle(resources.getString(R.string.confirmation))
            alertBuilder.setMessage(resources.getString(R.string.alert_dialog_body_message_user))

            // Set a positive button and its click listener on alert dialog
            alertBuilder.setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                acceptBtnPressed()
            }

            // don't do anything on negative button
            alertBuilder.setNegativeButton(resources.getString(R.string.no)) { _, _ ->
            }

            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = alertBuilder.create()

            // Display the alert dialog on app interface
            dialog.show()
        }

        userPresenterImpl!!.saveRanksIndexes(ranks)
        userPresenterImpl!!.saveCoachesIndexes(coaches)
        userPresenterImpl!!.saveCountriesIndexes(countries)

        this.updateAvatarImage()

        setRankSpinnerAdapter(ranks as MutableList<out RankDTO>?)
        setCoachSpinnerAdapter(coaches as MutableList<out UserDTO>?)
        setCountrySpinnerAdapter(countries as MutableList<out CountryDTO>?)
        setGenderSpinnerAdapter(genders)
    }

    override fun showIncorrectUserNameText() {
        TODO("Not yet implemented")
    }

    override fun updateUser() {
        TODO("Not yet implemented")
    }

    override fun showUser(userDTO: UserDTO?) {
        val activity: MainActivity? = activity as MainActivity?
        activity?.updateUserData(userDTO)

        Snackbar.make(
                view!!,
                context!!.resources.getString(R.string.user_data_updated_notification),
                Snackbar.LENGTH_LONG
        ).show()

        closeBtnPressed()
    }
}
