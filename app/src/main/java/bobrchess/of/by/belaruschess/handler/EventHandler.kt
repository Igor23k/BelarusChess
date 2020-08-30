package bobrchess.of.by.belaruschess.handler

import android.content.Context
import bobrchess.of.by.belaruschess.model.*
import bobrchess.of.by.belaruschess.view.activity.impl.MainActivity
import kotlinx.android.synthetic.main.fragment_event_list.*
import java.util.*

/**
 * EventHandler singleton object map to store all occurring EventDates (birthdays, anniversaries, etc.)
 * This is useful to compare all objects more easily, f.e. when you want to traverse all entries in event dates
 */
object EventHandler {

    /**
     * event_list a list used for sorted viewing of the maps content
     * the data is stored in pairs of EventDay and the index of this dataset in the map as an int
     */
    private var event_list: List<EventDate> = emptyList()

    private var event_map: MutableMap<Int, EventDate> = emptyMap<Int, EventDate>().toMutableMap()

    /**
     * addEvent adds a EventDay type to the map and has the possibility to write it to the shared preferences after adding it
     * this orders all events after the startDate automatically
     * also updates the Eventday list after every adding of a new event
     * @param event: EventDay
     * @param context: Context
     * @param writeAfterAdd: Boolean whether this event should be written to shared preferences after adding to list
     * @param addNewNotification : Boolean, whether a new notification should be created after adding this event
     * @param updateEventList : Boolean, whether to update the EventList, updating the EventList means sorting event values by their startDate
     * @param addBitmap : Boolean whether a new bitmap should be added
     */
    fun addEvent(
            event: EventDate,
            context: Context,
            writeAfterAdd: Boolean = true,
            addNewNotification: Boolean = true,
            updateEventList: Boolean = true,
            addBitmap: Boolean = true,
            sortList: Boolean = true

    ) {

        if (event !is Divider) {
            val cal = Calendar.getInstance()
            cal.time = event.eventDate
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.SECOND, 1)
            event.eventDate = cal.time
        }

        this.event_map[event.eventID] = event


        if (((event is EventTournament) || (event is EventUser) || (event is EventTournamentResult)) && addBitmap) {
            var imageUri: String? = null
            if (event is EventTournament) {
                imageUri = event.imageUri
            }
            if (event is EventUser) {
                imageUri = event.imageUri
            }
            if (event is EventTournamentResult) {
                imageUri = event.imageUri
            }
            Thread(Runnable {
                if (imageUri != null) {
                    /*BitmapHandler.addDrawable(
                            event.eventID,
                            Uri.parse(imageUri),
                            context,
                            readBitmapFromGallery = false,
                            //150dp because the app_bar height is 300dp
                            scale = MainActivity.convertDpToPx(context, 150f)
                    )*///сохранение карнтинки локально, пока что убрал
                }
                if (context is MainActivity) {
                    context.runOnUiThread {
                        if (context.recyclerView != null) {
                            context.recyclerView.adapter?.notifyDataSetChanged()
                        }
                    }
                }
            }).start()
        }

        //set hour of day from all other events except MonthDivider to 12h (month divider is at 0h), so when sorting month divider is always at first
        if (event !is Divider && addNewNotification) {
            NotificationHandler.scheduleNotification(context, event)
        }


        if (updateEventList) {
            if (sortList) {
                this.event_list = getSortedListBy()
            } else {
                this.event_list = getPlainList()
            }
        }

        if (writeAfterAdd) {
            IOHandler.writeEventToFile(event.eventID, event)
        }
    }

    /**
     * changeEventAt changes event at key position
     *
     * @param ID : Int
     * @param newEvent : EventDay
     */
    fun changeEventAt(
            ID: Int,
            newEvent: EventDate,
            context: Context,
            writeAfterChange: Boolean = false
    ) {
        getEventToEventIndex(ID)?.let { oldEvent ->
            newEvent.eventID = ID
            //set hour of day from all other events except monthdivider to 12h (month divider is at 0h), so when sorting month divider is always at first
            if (newEvent !is Divider) {
                val cal = Calendar.getInstance()
                cal.time = newEvent.eventDate
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.SECOND, 1)
                newEvent.eventDate = cal.time
            }

            NotificationHandler.cancelNotification(context, oldEvent)
            NotificationHandler.scheduleNotification(context, newEvent)

            this.event_map[ID] = newEvent

            if (newEvent is EventTournament) {
                if (newEvent.imageUri != null) {
                    //remove old drawable if one exists
                    if ((oldEvent as EventTournament).imageUri != null) {
                    }
                }
            }
            this.event_list = getSortedListBy()

            if (writeAfterChange) {
                IOHandler.writeEventToFile(ID, newEvent)
            }
        }
    }

    /**
     * removeEventByKey removes an event from the by using a key
     *
     * @param index : Int
     * @param context : Context
     * @param writeChange : Boolean
     */
    fun removeEventByID(index: Int, context: Context, writeChange: Boolean = false) {
        getEventToEventIndex(index)?.let { event ->

            if (event is EventTournament) {
                //BitmapHandler.removeBitmap(index, context) удаление локально, убрал пока что
            }

            NotificationHandler.cancelNotification(context, event)

            if (writeChange) {
                IOHandler.removeEventFromFile(event.eventID)
            }

            this.event_map.remove(index)
            this.event_list = this.getSortedListBy()
        }
    }

    fun clearData() {
        if (this.event_list.isNotEmpty()) {
            this.event_map.clear()
            this.event_list = getSortedListBy(EventDate.Identifier.Date)
        }
    }

    /**
     * getEventToEventIndex returns the value with type EventDay? to a given integer key
     * @param index : Int
     * @return EventDay?
     */
    fun getEventToEventIndex(index: Int): EventDate? {
        if (event_map.containsKey(index))
            return event_map[index]
        return null
    }

    fun deleteAllEntriesAndImages(context: Context, writeAfterAdd: Boolean) {
        this.event_list.forEach {
            NotificationHandler.cancelNotification(context, it)
        }
        this.clearData()
        //BitmapHandler.removeAllDrawables(context) удаление локально, убрал пока что
        if (writeAfterAdd) {
            IOHandler.clearSharedPrefEventData()
        }
    }

    /**
     * containsKey checks if the given key is present in the map
     *
     * @param index: Int
     * @return Boolean
     */
    private fun containsIndex(index: Int): Boolean {
        return event_map.containsKey(index)
    }

    fun getList(): List<EventDate> {
        return this.event_list
    }

    fun getEventsAsStringList(): String {
        var eventString = ""
        val tempList = event_list.toMutableList()
        tempList.forEach {
            //don't save Monthdividers bc they are created with the first start of the app
            if (it !is Divider) {
                //removing avatar image
                if (it is EventTournament) {
                    eventString += it.toStringWithoutImage() + "\n"
                } else {
                    eventString += it.toString() + "\n"
                }
            }
        }
        return eventString
    }

    /**
     * getSortValueListBy returns the map as a value list which is sorted by specific attributes given by an enum identifier
     * If the identifier is unknown, than an empty value list is returned
     *
     * @param identifier : SortIdentifier
     * @return List<EventDay>
     */
    private fun getSortedListBy(
            identifier: SortIdentifier = EventDate.Identifier.Date
    ): List<EventDate> {
        if (identifier == EventDate.Identifier.Date) {
            return event_map.values.sorted()
        } else {
            return emptyList()
        }
    }

    private fun getPlainList(): List<EventDate> {
        return event_map.values.toList()
    }

}