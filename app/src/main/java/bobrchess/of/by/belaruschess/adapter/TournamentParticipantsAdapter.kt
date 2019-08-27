package bobrchess.of.by.colibritweet.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.dto.UserDTO
import com.squareup.picasso.Picasso
import java.util.*

class TournamentParticipantsAdapter(onUserClickListener: OnUserClickListener) : RecyclerView.Adapter<TournamentParticipantsAdapter.UserViewHolder>() {

    private val userList = ArrayList<UserDTO>()
    private val onUserClickListener: OnUserClickListener

    init {
        this.onUserClickListener = onUserClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.tournament_participant_item_view, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList.get(position)
        holder.bind(user)
    }

    fun setItems(users: Collection<UserDTO>) {
        userList.addAll(users)
        notifyDataSetChanged()
    }

    fun clearItems() {
        userList.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userImageView: ImageView
        private val user_name_text_view: TextView
        private val user_rank_and_rating_text_view: TextView

        init {
            userImageView = itemView.findViewById(R.id.profile_image_view)
            user_name_text_view = itemView.findViewById(R.id.user_name_text_view)
            user_rank_and_rating_text_view = itemView.findViewById(R.id.user_rank_and_rating_text_view)

            itemView.setOnClickListener {
                val user = userList[layoutPosition]
                onUserClickListener.onUserClick(user)
            }
        }

        fun bind(user: UserDTO) {
            user_name_text_view.text = user.name + " " + user.surname
            user_rank_and_rating_text_view.text = user.rank!!.name + "    " + user.rating
            Picasso.with(itemView.context).load(user.image).into(userImageView)
        }
    }

    interface OnUserClickListener {
        fun onUserClick(user: UserDTO)
    }

    fun ClosedRange<Int>.random() =
            Random().nextInt(endInclusive - start) + start
}