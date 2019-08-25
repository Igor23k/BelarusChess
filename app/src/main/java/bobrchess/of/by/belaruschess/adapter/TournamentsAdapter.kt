package bobrchess.of.by.colibritweet.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.dto.TournamentDTO
import com.squareup.picasso.Picasso
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TournamentsAdapter(onTournamentClickListener: OnTournamentClickListener) : RecyclerView.Adapter<TournamentsAdapter.TournamentViewHolder>() {

    private val tournamentList = ArrayList<TournamentDTO>()
    private val imageList = ArrayList<String>()
    private val onTournamentClickListener: OnTournamentClickListener

    init {
        this.onTournamentClickListener = onTournamentClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TournamentViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.user_tournament_item_view, parent, false)
        return TournamentViewHolder(view)
    }

    override fun onBindViewHolder(holder: TournamentViewHolder, position: Int) {
        holder.bind(tournamentList[position])
    }

    override fun getItemCount(): Int {
        return tournamentList.size
    }

    fun setItems(tournaments: List<TournamentDTO>) {
        tournamentList.addAll(tournaments)
        notifyDataSetChanged()
    }

    fun clearItems() {
        tournamentList.clear()
        notifyDataSetChanged()
    }

    inner class TournamentViewHolder
    (itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Ваш ViewHolder должен содержать переменные для всех
        // View-компонентов, которым вы хотите задавать какие-либо свойства
        // в процессе работы пользователя со списком
        private val tournamentSmallImageView: ImageView
        private val tournamentNameTextView: TextView
        private val startDateTextView: TextView
        private val finishDateTextView: TextView
        private val tournamentImageView: ImageView
        private val countPointsTextView: TextView
        private val positionNumberTextView: TextView

        init {
            initTournamentList()
            tournamentSmallImageView = itemView.findViewById(R.id.profile_image_view)
            tournamentNameTextView = itemView.findViewById(R.id.tournament_name_text_view)
            startDateTextView = itemView.findViewById(R.id.start_date_text_view)
            finishDateTextView = itemView.findViewById(R.id.finish_date_text_view)
            tournamentImageView = itemView.findViewById(R.id.tournament_image_view)
            countPointsTextView = itemView.findViewById(R.id.count_points_text_view)
            positionNumberTextView = itemView.findViewById(R.id.position_number_text_view)

            itemView.setOnClickListener {
                val tournament = tournamentList.get(layoutPosition)
                onTournamentClickListener.onTournamentClick(tournament)
            }
        }

        fun bind(tournament: TournamentDTO) {
            var random = Random()
            tournamentNameTextView.text = tournament.name
            countPointsTextView.text = "11"//random.nextInt(7).toString() + 2
            positionNumberTextView.text = "12"//random.nextInt(30).toString() + 5

            // val StartDateFormatted = getFormattedDate(tournament.startDate.toString())
            startDateTextView.text = "Oct 21"
            finishDateTextView.text = "Oct 26"

            //   Picasso.with(itemView.context).load("https://www.w3schools.com/w3css/img_fjords.jpg").into(tournamentSmallImageView)

            //  val tweetPhotoUrl = "https://www.w3schools.com/w3css/img_fjords.jpg"
            // Picasso.with(itemView.context).load(tweetPhotoUrl).into(tournamentImageView)

            val avatarNumber = (0..6).random()
            Picasso.with(itemView.context).load(imageList[avatarNumber]/*user.imageUrl*/).into(tournamentImageView)
            Picasso.with(itemView.context).load(imageList[avatarNumber]/*user.imageUrl*/).into(tournamentSmallImageView)

            //  tournamentImageView.visibility = if (tweetPhotoUrl != null) View.VISIBLE else View.GONE  что это и нужно ли?
        }

        private fun initTournamentList() {
            imageList.add("https://www.imageup.ru/img152/3450439/5d0a75e13c3c5dac093874ec22358870.jpeg")
            imageList.add("https://www.imageup.ru/img127/3459217/london.jpg")
            imageList.add("https://www.imageup.ru/img127/3459220/melburn.jpg")
            imageList.add("https://www.imageup.ru/img127/3459221/paris.jpg")
            imageList.add("https://www.imageup.ru/img127/3459222/vena.jpg")
        }

        fun ClosedRange<Int>.random() =
                Random().nextInt(endInclusive - start) + start

        private fun getFormattedDate(rawDate: String): String {
            val utcFormat = SimpleDateFormat(TWITTER_RESPONSE_FORMAT, Locale.ROOT)
            val displayedFormat = SimpleDateFormat(MONTH_DAY_FORMAT, Locale.getDefault())
            try {
                val date = utcFormat.parse(rawDate)
                return displayedFormat.format(date)
            } catch (e: ParseException) {
                throw RuntimeException(e)
            }

        }
    }

    interface OnTournamentClickListener {
        fun onTournamentClick(tournament: TournamentDTO)
    }

    companion object {
        private val TWITTER_RESPONSE_FORMAT = "EEE MMM dd HH:mm:ss ZZZZZ yyyy" // Thu Oct 26 07:31:08 +0000 2017
        private val MONTH_DAY_FORMAT = "MMM d" // Oct 26
    }
}