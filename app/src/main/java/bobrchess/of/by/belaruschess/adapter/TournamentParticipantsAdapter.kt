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
class TournamentParticipantsAdapter(onUserClickListener: OnUserClickListener) : RecyclerView.Adapter<TournamentParticipantsAdapter.UserViewHolder>() {

    private val userList = ArrayList<UserDTO>()
    private val avatarList = ArrayList<String>()
    private val onUserClickListener: OnUserClickListener

    init {
        this.onUserClickListener = onUserClickListener
        initAvatarList()
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
        avatarList.add("http://priscree.ru/img/cbbb17685dd8a9.jpg")
        avatarList.add("http://priscree.ru/img/013c16d74f1cd6.jpg")
        avatarList.add("http://priscree.ru/img/013c1ca2d5a510.jpg")
        avatarList.add("http://priscree.ru/img/7180e245e24566.jpg")
        avatarList.add("http://priscree.ru/img/722a716ca92a0d.jpg")
        avatarList.add("http://priscree.ru/img/752eac33141eb4.jpg")
        avatarList.add("http://priscree.ru/img/3804bfaf17169c.jpg")
        avatarList.add("http://priscree.ru/img/3804b8a5b874a7.jpg")
        avatarList.add("http://priscree.ru/img/3804b3cad12fd8.jpg")
        avatarList.add("http://priscree.ru/img/c4a98f942fb210.jpg")
        avatarList.add("http://priscree.ru/img/d14195087d41d8.jpg")
        avatarList.add("http://priscree.ru/img/a62a0bb34ba8d2.jpg")
        avatarList.add("http://priscree.ru/img/7c8aa7a1077d5f.jpg")
        avatarList.add("http://priscree.ru/img/12906cab3343fb.jpg")
        avatarList.add("http://priscree.ru/img/7a20ffc7ea595e.jpg")
        avatarList.add("http://priscree.ru/img/18064ef9e07a27.jpg")
        avatarList.add("http://priscree.ru/img/69c41ee4659246.jpg")
        avatarList.add("http://priscree.ru/img/5a5a2e1421c5bf.jpg")
        avatarList.add("http://priscree.ru/img/5a5a216979f035.jpg")
        avatarList.add("http://priscree.ru/img/1f2b8c263c4758.jpg")
    }



    interface OnUserClickListener {
        fun onUserClick(user: UserDTO)
    }

    fun ClosedRange<Int>.random() =
            Random().nextInt(endInclusive - start) +  start
}