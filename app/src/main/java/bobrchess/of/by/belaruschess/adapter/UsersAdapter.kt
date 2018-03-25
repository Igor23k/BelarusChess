package bobrchess.of.by.colibritweet.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.colibritweet.pojo.User
import com.squareup.picasso.Picasso
import java.util.*


// Унаследовали наш адаптер от RecyclerView.Adapter
// Здесь же указали наш собственный ViewHolder, который предоставит нам доступ к View-компонентам
class UsersAdapter(onUserClickListener: OnUserClickListener) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {
    private val userList = ArrayList<User>()
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

    fun setItems(users: Collection<User>) {
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
            nickTextView = itemView.findViewById(R.id.user_nick_text_view)

            itemView.setOnClickListener {
                val user = userList.get(layoutPosition)
                onUserClickListener.onUserClick(user)
            }
        }

        fun bind(user: User) {
            nameTextView.text = user.name
            nickTextView.text = user.nick
            Picasso.with(itemView.context).load(user.imageUrl).into(userImageView)
        }
    }

    interface OnUserClickListener {
        fun onUserClick(user: User)
    }
}