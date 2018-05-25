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
    private val avatarList = ArrayList<String>()
    private val onUserClickListener: OnUserClickListener

    init {
        this.onUserClickListener = onUserClickListener
        initAvatarList()
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
                val user = userList[layoutPosition]
                onUserClickListener.onUserClick(user)
            }
        }

        fun bind(user: UserDTO) {
            nameTextView.text = user.name
            nickTextView.text = user.surname
            val avatarNumber = (0..10).random()
            Picasso.with(itemView.context).load(avatarList[avatarNumber]/*user.imageUrl*/).into(userImageView)
        }
    }

    private fun initAvatarList(){
        avatarList.add("http://priscree.ru/img/3e250315e81d1e.jpg")
        avatarList.add("http://priscree.ru/img/5dc25ed76ea661.jpg")
        avatarList.add("http://priscree.ru/img/95cbe6b870f873.jpg")
        avatarList.add("http://priscree.ru/img/f85bddf83dfca1.jpg")
        avatarList.add("http://priscree.ru/img/9dd16d3aefc6cf.jpg")
        avatarList.add("http://priscree.ru/img/2748950ade3697.jpg")
        avatarList.add("http://priscree.ru/img/44e20348a43cc2.jpg")
        avatarList.add("http://priscree.ru/img/0169c41d3800b1.jpg")
        avatarList.add("http://priscree.ru/img/cbbb1c090bcea8.png")
        avatarList.add("http://priscree.ru/img/e76d8a0443e683.jpg")
    }



    interface OnUserClickListener {
        fun onUserClick(user: UserDTO)
    }

    fun ClosedRange<Int>.random() =
            Random().nextInt(endInclusive - start) +  start
}