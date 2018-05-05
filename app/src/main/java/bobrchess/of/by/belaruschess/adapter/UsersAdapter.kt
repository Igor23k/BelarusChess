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


// Унаследовали наш адаптер от RecyclerView.Adapter
// Здесь же указали наш собственный ViewHolder, который предоставит нам доступ к View-компонентам
class UsersAdapter(onUserClickListener: OnUserClickListener) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {
    private val userList = ArrayList<UserDTO>()
    private val onUserClickListener: OnUserClickListener

    init {
        this.onUserClickListener = onUserClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.user_item_view, parent, false)
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
        private val nameTextView: TextView
        private val nickTextView: TextView

        init {
            userImageView = itemView.findViewById(R.id.profile_image_view)
            nameTextView = itemView.findViewById(R.id.user_name_text_view)
            nickTextView = itemView.findViewById(R.id.user_rating_text_view)

            itemView.setOnClickListener {
                val user = userList.get(layoutPosition)
                onUserClickListener.onUserClick(user)
            }
        }

        fun bind(user: UserDTO) {
            nameTextView.text = user.name
            nickTextView.text = user.surname
            Picasso.with(itemView.context).load("http://priscree.ru/img/5f1585e4e674e0.jpg"/*user.imageUrl*/).into(userImageView)
        }
    }

    interface OnUserClickListener {
        fun onUserClick(user: UserDTO)
    }
}