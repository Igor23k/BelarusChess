package bobrchess.of.by.belaruschess.view.activity.impl

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.dto.UserDTO
import bobrchess.of.by.belaruschess.dto.extended.ExtendedUserDTO
import bobrchess.of.by.belaruschess.presenter.RegistrationPresenter
import bobrchess.of.by.belaruschess.presenter.impl.RegistrationPresenterImpl
import bobrchess.of.by.belaruschess.util.Constants.Companion.DATE_PICKER_DIALOG
import bobrchess.of.by.belaruschess.util.Constants.Companion.USER_PARAMETER
import bobrchess.of.by.belaruschess.util.Util.Companion.REGISTRATION_REQUEST
import bobrchess.of.by.belaruschess.util.Util.Companion.TYPE_NOT_CONNECTED
import bobrchess.of.by.belaruschess.util.Util.Companion.genders
import bobrchess.of.by.belaruschess.view.activity.PackageModel
import bobrchess.of.by.belaruschess.view.activity.RegistrationContractView
import bobrchess.of.by.belaruschess.view.activity.impl.MainActivity
import butterknife.BindView
import butterknife.ButterKnife
import com.arellomobile.mvp.presenter.InjectPresenter
import com.borax12.materialdaterangepicker.date.DatePickerDialog
import java.util.*

class RegistrationActivity : AbstractActivity(), RegistrationContractView, DatePickerDialog.OnDateSetListener {
    @JvmField
    var presenter: RegistrationPresenterImpl = RegistrationPresenterImpl()

    @JvmField
    @BindView(R.id.e_name_input)
    var nameText: EditText? = null

    @JvmField
    @BindView(R.id.e_surname_input)
    var surnameText: EditText? = null

    @JvmField
    @BindView(R.id.e_patronymic_input)
    var patronymicText: EditText? = null

    @JvmField
    @BindView(R.id.e_rating_input)
    var ratingText: EditText? = null

    @JvmField
    @BindView(R.id.e_email_input)
    var emailText: EditText? = null

    @JvmField
    @BindView(R.id.e_number_input)
    var mobileText: EditText? = null

    @JvmField
    @BindView(R.id.e_password_input)
    var passwordText: EditText? = null

    @JvmField
    @BindView(R.id.e_reEnterPassword_input)
    var reEnterPasswordText: EditText? = null

    @JvmField
    @BindView(R.id.b_registration)
    var registrationButton: Button? = null

    @JvmField
    @BindView(R.id.t_link_authorization)
    var authorizationLink: TextView? = null

    @JvmField
    @BindView(R.id.i_calendar_birthday)
    var calendarImage: ImageView? = null
    private var progressDialog: ProgressDialog? = null
    private var view: ScrollView? = null
    private var genderSpinner: Spinner? = null
    private var coachSpinner: Spinner? = null
    private var rankSpinner: Spinner? = null
    private var countrySpinner: Spinner? = null
    var birthday: String? = null
        private set

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        ButterKnife.bind(this)
        view = findViewById(R.id.scrollViewRegistration)
        presenter.attachViewComponent(findViewById(R.id.scrollViewRegistration))
        initButtonsListeners()
        registerInternetCheckReceiver()
        genderSpinner = findViewById(R.id.s_genderSpinner)
        genderSpinner!!.setOnItemSelectedListener(GenderItemSelectedListener())
        setGenderSpinnerAdapter(genders)
        coachSpinner = findViewById(R.id.s_coachSpinner)
        coachSpinner!!.setOnItemSelectedListener(CoachItemSelectedListener())
        rankSpinner = findViewById(R.id.s_rankSpinner)
        rankSpinner!!.setOnItemSelectedListener(RankItemSelectedListener())
        countrySpinner = findViewById(R.id.s_countrySpinner)
        countrySpinner!!.setOnItemSelectedListener(CountryItemSelectedListener())
        presenter.setPackageModel(PackageModel(this))
    }

    private fun initButtonsListeners() {
        registrationButton!!.setOnClickListener { v: View? ->
            val progressDialog = ProgressDialog(this@RegistrationActivity,
                    R.style.AppTheme_NoActionBar)
            progressDialog.isIndeterminate = true
            registration()
        }
        authorizationLink!!.setOnClickListener { v: View? ->
            val intent = Intent(applicationContext, AuthorizationActivity::class.java)
            startActivityForResult(intent, REGISTRATION_REQUEST)
            finish()
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }
        calendarImage!!.setOnClickListener { v: View? ->
            val now = Calendar.getInstance()
            val dpd = DatePickerDialog.newInstance(
                    this@RegistrationActivity,
                    now[Calendar.YEAR],
                    now[Calendar.MONTH],
                    now[Calendar.DAY_OF_MONTH]
            )
            dpd.show(fragmentManager, DATE_PICKER_DIALOG)
        }
    }

    override fun onDestroy() {
        presenter!!.detachView()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REGISTRATION_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                finish()
            }
        } /*else if (requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {
            Uri fullPhotoUri = data.getData();

            InputStream imageInputStream = null;
            try {
                imageInputStream = this.getApplicationContext().getContentResolver().openInputStream(data.getData());
                String encodedImage = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageInputStream?.readBytes())
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Thread(Runnable {
                val bitmap =
                        MediaStore.Images.Media.getBitmap(context!!.contentResolver, fullPhotoUri)
                (this.getApplicationContext() instanceof MainActivity).runOnUiThread {
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
        }*/
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    override fun startActivity(userDTO: UserDTO) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        putUserData(intent, userDTO)
        startActivityForResult(intent, REGISTRATION_REQUEST)
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
    }

    private fun putUserData(intent: Intent, userDTO: UserDTO) {
        intent.putExtra(USER_PARAMETER, userDTO)
    }

    override fun showIncorrectEmailText() {
        emailText!!.error = this.getString(R.string.incorrect_email)
    }

    override fun showIncorrectPasswordText() {
        passwordText!!.error = this.getString(R.string.incorrect_password)
    }

    override fun registration() {
        try {
            presenter!!.registration(userData)
        } catch (e: NumberFormatException) {
            showToast(R.string.incorrect_rating)
            enableButton()
        } catch (e: Exception) {
            showToast(R.string.incorrect_data)
            enableButton()
        }
    }

    override fun showProgress() {
        progressDialog = ProgressDialog.show(this, "", this.getString(R.string.please_wait))
    }

    override fun hideProgress() {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }

    override fun enableButton() {
        registrationButton!!.isEnabled = true
    }

    override fun disableButton() {
        registrationButton!!.isEnabled = false
    }

    @get:Throws(NumberFormatException::class)
    val userData: ExtendedUserDTO
        get() {
            val userData = ExtendedUserDTO()
            userData.name = nameText!!.text.toString()
            userData.surname = surnameText!!.text.toString()
            userData.patronymic = patronymicText!!.text.toString()
            userData.email = emailText!!.text.toString()
            userData.phoneNumber = mobileText!!.text.toString()
            userData.password = passwordText!!.text.toString()
            userData.reEnterPassword = reEnterPasswordText!!.text.toString()
            userData.rating = ratingText!!.text.toString().toInt()
            userData.birthday = birthday
            return userData
        }

    override fun setGenderSpinnerAdapter(genders: MutableList<String>) {
        genders.add(0, getString(R.string.chooseGender))
        val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, genders)
        genderSpinner!!.adapter = adapter
    }

    override fun setCoachSpinnerAdapter(coachesNames: MutableList<String>) {
        coachesNames.add(0, getString(R.string.chooseCoach))
        coachesNames.add(1, getString(R.string.absence))
        val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, coachesNames)
        coachSpinner!!.adapter = adapter
    }

    override fun setRankSpinnerAdapter(ranksNames: MutableList<String>) {
        ranksNames.add(0, getString(R.string.chooseRank))
        ranksNames.add(1, getString(R.string.absence))
        val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, ranksNames)
        rankSpinner!!.adapter = adapter
    }

    override fun setCountrySpinnerAdapter(countriesNames: MutableList<String>) {
        countriesNames.add(0, getString(R.string.chooseCountry))
        val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, countriesNames)
        countrySpinner!!.adapter = adapter
    }

    override fun onDateSet(view: DatePickerDialog, year: Int, monthOfYear: Int, dayOfMonth: Int,
                           yearEnd: Int, monthOfYearEnd: Int, dayOfMonthEnd: Int) {
        birthday = "$year-$monthOfYear-$dayOfMonth"
    }

    inner class GenderItemSelectedListener : OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
            getPresenter()!!.setSelectedGenderIndex(position)
        }

        override fun onNothingSelected(arg: AdapterView<*>?) {}
    }

    inner class CoachItemSelectedListener : OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
            getPresenter()!!.setSelectedCoachIndex(position)
        }

        override fun onNothingSelected(arg: AdapterView<*>?) {}
    }

    inner class RankItemSelectedListener : OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
            getPresenter()!!.setSelectedRankIndex(position)
        }

        override fun onNothingSelected(arg: AdapterView<*>?) {}
    }

    inner class CountryItemSelectedListener : OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
            getPresenter()!!.setSelectedCountryIndex(position)
        }

        override fun onNothingSelected(arg: AdapterView<*>?) {}
    }

    fun getPresenter(): RegistrationPresenter? {
        return presenter
    }

    override fun setConnectionStatus(connectivityStatus: Int) {
        presenter!!.setConnectivityStatus(connectivityStatus)
    }

    override fun showSnackBar(componentView: View, @StringRes message: Int) {
        snackbar = Snackbar
                .make(componentView, message, Snackbar.LENGTH_LONG)
        snackbar!!.setActionTextColor(Color.WHITE)
        val sbView = snackbar!!.view
        val textView = sbView.findViewById<TextView>(android.support.design.R.id.snackbar_text)
        textView.setTextColor(Color.WHITE)
        snackbar!!.show()
    }

    override fun showSnackBar(componentView: View, message: String) {
        snackbar = Snackbar
                .make(componentView, message, Snackbar.LENGTH_LONG)
        snackbar!!.setActionTextColor(Color.WHITE)
        val sbView = snackbar!!.view
        val textView = sbView.findViewById<TextView>(android.support.design.R.id.snackbar_text)
        textView.setTextColor(Color.WHITE)
        snackbar!!.show()
    }

    override fun loadSpinnersData() {
        presenter!!.loadSpinnersData()
    }

    override fun dialogConfirmButtonClicked() {
        if (connectivityStatus != TYPE_NOT_CONNECTED) {
            dismissAlertDialog()
            if (!presenter!!.spinnersAreLoaded()) {
                loadSpinnersData()
            }
        }
    }

    /**
     * REQUEST_IMAGE_GET is an intent code used for open the photo gallery
     */
    private val REQUEST_IMAGE_GET = 1

    /**
     * getImageFromFiles opens an intent to request a photo from the gallery
     * This function is called after the user clicks on the iv_add_avatar_btn
     */
    private val imageFromFiles: String
        private get() {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.type = "image/*"
            intent.addFlags(
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            if (intent.resolveActivity(this.applicationContext.packageManager) != null) {
                startActivityForResult(intent, REQUEST_IMAGE_GET)
            }
            return "0"
        }

    companion object {
        private var snackbar: Snackbar? = null
    }
}