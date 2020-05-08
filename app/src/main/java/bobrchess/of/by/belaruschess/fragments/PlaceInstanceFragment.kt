package bobrchess.of.by.belaruschess.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomSheetDialog
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.dto.CountryDTO
import bobrchess.of.by.belaruschess.dto.PlaceDTO
import bobrchess.of.by.belaruschess.dto.extended.ExtendedPlaceDTO
import bobrchess.of.by.belaruschess.handler.BitmapHandler
import bobrchess.of.by.belaruschess.handler.EventHandler
import bobrchess.of.by.belaruschess.model.EventPlace
import bobrchess.of.by.belaruschess.presenter.impl.AddPlacePresenterImpl
import bobrchess.of.by.belaruschess.util.Util
import bobrchess.of.by.belaruschess.view.activity.AddPlaceContractView
import bobrchess.of.by.belaruschess.view.activity.PackageModel
import bobrchess.of.by.belaruschess.view.activity.impl.MainActivity
import kotlinx.android.synthetic.main.fragment_add_new_place.*
import org.springframework.util.StringUtils


/**
 *
 * PlaceInstanceFragment is a fragment class for adding/editing an instance of EventPlace
 * This fragment shows up, when the users wants to add a new EventPlace or edit an existing one
 * The fragment consists of several TextEdits to manage user data input
 *
 * This class inherits from android.support.v4.app.Fragment
 *
 */
class PlaceInstanceFragment : EventInstanceFragment(), AddPlaceContractView {

    /**
     * isEditedBirthday is a boolean flag to indicate whether this fragment is in "edit" mode aka. the user wants to edit an existing instance of EventPlace
     */
    private var isEditedPlace: Boolean = false

    /**
     * eventID is the index of the clicked item in EventListFragments RecyclerView, this is handy to get the birthday instance from the EventHandler
     */
    var eventID = -1

    /**
     * placeAvatarUri is a string to store the user picked image for the avatar
     */
    private var placeImageUri: String? = null

    /**
     * avatarImgWasEdited is a boolean flag to store the information whether the avatar img has been changed
     */
    private var avatarImgWasEdited = false

    /**
     * REQUEST_IMAGE_GET is an intent code used for open the photo gallery
     */
    private val REQUEST_IMAGE_GET = 1

    private val editName: EditText by lazy {
        view!!.findViewById<EditText>(R.id.e_add_place_name)
    }

    private val editCity: EditText by lazy {
        view!!.findViewById<EditText>(R.id.e_add_place_city)
    }

    private val editStreet: EditText by lazy {
        view!!.findViewById<EditText>(R.id.e_add_place_street)
    }

    private val editBuilding: EditText by lazy {
        view!!.findViewById<EditText>(R.id.e_add_place_building)
    }

    private val editCapacity: EditText by lazy {//todo добавить tours count
        view!!.findViewById<EditText>(R.id.e_add_place_capacity)
    }

    private val countrySpinner: Spinner by lazy {
        view!!.findViewById<Spinner>(R.id.s_countrySpinner)
    }


    inner class CountryItemSelectedListener : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            addPlacePresenter?.setSelectedCountryIndex(position)
        }

        override fun onNothingSelected(arg: AdapterView<*>) {

        }
    }

    private var addPlacePresenter: AddPlacePresenterImpl? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_place, container, false)
    }


    private var countryId: Int = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(false)

        countrySpinner.onItemSelectedListener = CountryItemSelectedListener()

        addPlacePresenter = AddPlacePresenterImpl(this)
        addPlacePresenter!!.setPackageModel(PackageModel(this.context!!))
        addPlacePresenter!!.attachView(this)
        addPlacePresenter!!.loadCountries()

        if (arguments != null) {
            isEditedPlace = true

            setToolbarTitle(context!!.resources.getString(R.string.toolbar_title_edit_place))

            eventID = (arguments!!.getInt(MainActivity.FRAGMENT_EXTRA_TITLE_EVENTID))
            EventHandler.getEventToEventIndex(eventID)?.let { place ->
                if (place is EventPlace) {
                    countryId = place.countryId!!

                    editCity.setText(place.city)
                    editName.setText(place.name)
                    placeImageUri = place.imageUri
                    editStreet.setText(place.street)
                    editBuilding.setText(place.building)
                    editCapacity.setText(place.capacity.toString())


                    //title.text = resources.getText(R.string.toolbar_title_edit_birthday)
                    btn_place_add_fragment_delete.visibility = Button.VISIBLE
                    //delete functionality
                    btn_place_add_fragment_delete.setOnClickListener {
                        val alertBuilder = AlertDialog.Builder(context)
                        alertBuilder.setTitle(resources.getString(R.string.alert_dialog_title_delete_place))
                        alertBuilder.setMessage(resources.getString(R.string.alert_dialog_body_message_place))

                        val contextTemp = context

                        // Set a positive button and its click listener on alert dialog
                        alertBuilder.setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                            addPlacePresenter!!.removePlace(eventID)
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

                this.updateImage()
            }
            //new place is going to be added
        } else {
            setToolbarTitle(context!!.resources.getString(R.string.toolbar_title_add_place))
            btn_place_add_fragment_delete.visibility = Button.INVISIBLE
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
                    if (isEditedPlace && placeImageUri != null && (EventHandler.getEventToEventIndex(eventID) as EventPlace).imageUri != null) {
                        iv_add_avatar_btn.setImageResource(R.drawable.ic_birthday_person)
                        avatarImgWasEdited = true
                        placeImageUri = null
                        BitmapHandler.removeBitmap(eventID, context!!)
                    } else {
                        iv_add_avatar_btn.setImageResource(R.drawable.ic_birthday_person)
                        placeImageUri = null
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

            placeImageUri = fullPhotoUri.toString()
            avatarImgWasEdited = true
        }
    }

    /**
     * acceptBtnPressed is a function which is called when the toolbars accept button is pressed
     */
    override fun acceptBtnPressed() {
        try {
            addPlacePresenter?.addPlace(getPlaceData())
        }catch (e: NumberFormatException) {
            showToast(R.string.incorrect_capacity)
        }
    }

    @Throws(NumberFormatException::class)
    private fun getPlaceData(): ExtendedPlaceDTO {
        val placeData = ExtendedPlaceDTO()
        placeData.id = eventID
        placeData.name = editName.text.toString()
        placeData.city = editCity.text.toString()
        placeData.street = editStreet.text.toString()
        placeData.building = editBuilding.text.toString()
        placeData.capacity = editCapacity.text.toString().toInt()
        placeData.image = placeImageUri
        return placeData
    }

    private fun updateImage() {
        if (this.iv_add_avatar_btn != null && this.eventID >= 0) {
            //load maybe already existent avatar photo
            EventHandler.getEventToEventIndex(eventID)?.let { event ->
                if (event is EventPlace && event.imageUri != null) {
                    this.iv_add_avatar_btn.setImageBitmap(BitmapHandler.getBitmapAt(eventID))
                    this.iv_add_avatar_btn.isEnabled = true
                }
            }
        }
    }

    private fun checkForLastDateSeperatorChar(dateString: String): Boolean {
        return (dateString.last().toString().matches("""\W""".toRegex()))
    }

    companion object {

        /**
         * BIRTHDAY_INSTANCE_FRAGMENT_TAG is the fragments tag as String
         */
        const val PLACE_INSTANCE_FRAGMENT_TAG = "PLACE_INSTANCE"

        /**
         * newInstance returns a new instance of PlaceInstanceFragment
         */
        @JvmStatic
        fun newInstance(): PlaceInstanceFragment {
            return PlaceInstanceFragment()
        }
    }

    override fun setCountrySpinnerAdapter(countries: MutableList<out CountryDTO>?) {
        val countriesNames = Util.getCountriesBasicData(countries)
        countriesNames.add(0, getString(R.string.chooseCountry))
        val adapter = ArrayAdapter<String>(this.context!!,
                android.R.layout.simple_spinner_dropdown_item, countriesNames)
        countrySpinner.adapter = adapter
        countrySpinner.setSelection(getCountryIndexById(countries, countryId) + 1)
    }

    private fun getCountryIndexById(list: MutableList<out CountryDTO>?, id: Int): Int {
        for ((i, value) in list!!.withIndex()) {
            if (value.id == id) {
                return i
            }
        }
        return -1
    }

    override fun startActivity(placeDTO: PlaceDTO?) {
    }

    override fun disableButton() {
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun setConnectionStatus(connectivityStatus: Int?) {
    }

    override fun showIncorrectPlaceNameText() {
    }

    override fun addPlace() {
    }

    override fun enableButton() {
    }

    override fun dialogConfirmButtonClicked() {

    }

    fun createPlaceEvent(placeDTO: PlaceDTO?) {

    }

    override fun removePlaceFromLocalStorage(id: Long) {
        EventHandler.removeEventByID(eventID, context!!, true)
        closeBtnPressed()
    }

    override fun placeRemoved(removedPlaceId: Int) {
        showSnackbar(R.string.place_deleted_notification)
        removePlaceFromLocalStorage(removedPlaceId.toLong())
    }

    override fun placeAdded(placeDTO: PlaceDTO?) {
        //create new instance from edit fields
        val placeEvent = EventPlace(
                placeDTO!!.id!!.toInt(),
                placeDTO.name!!
        )

        placeEvent.city = placeDTO.city
        placeEvent.street = placeDTO.street
        placeEvent.imageUri = placeDTO.image
        placeEvent.countryId = placeDTO.country?.id
        placeEvent.building = placeDTO.building
        placeEvent.capacity = placeDTO.capacity
        placeEvent.approved = placeDTO.approved

        //new place entry, just add a new entry in map
        if (!isEditedPlace) {
            EventHandler.addEvent(placeEvent, this.context!!, true)
            showSnackbar(R.string.place_added_notification)
            closeBtnPressed()
        } else {
            EventHandler.getEventToEventIndex(eventID)?.let { event ->
                if (event is EventPlace) {
                    EventHandler.changeEventAt(eventID, placeEvent, context!!, true)
                    showSnackbar(R.string.place_changed_notification)
                }
            }
            closeBtnPressed()
        }
    }
}
