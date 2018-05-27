package bobrchess.of.by.colibritweet.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.dto.GameDTO
import com.squareup.picasso.Picasso
import java.util.*


// Унаследовали наш адаптер от RecyclerView.Adapter
// Здесь же указали наш собственный ViewHolder, который предоставит нам доступ к View-компонентам
class GamesAdapter(onGameClickListener: OnGameClickListener) : RecyclerView.Adapter<GamesAdapter.GameViewHolder>() {

    private val gamesList = ArrayList<GameDTO>()
    private val avatarList = ArrayList<String>()
    private val onGameClickListener: OnGameClickListener

    init {
        this.onGameClickListener = onGameClickListener
        initAvatarList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.game_item_view, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = gamesList[position]
        holder.bind(game)
    }

    fun setItems(games: Collection<GameDTO>) {
        gamesList.addAll(games)
        notifyDataSetChanged()
    }

    fun clearItems() {
        gamesList.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return gamesList.size
    }

    inner class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var firstPlayerNameTextView: TextView? = null
        private var secondPlayerNameTextView: TextView? = null
        private val countPointsFirstPlayerTextView: TextView
        private val countPointsSecondPlayerTextView: TextView
        private val firstPlayerImageView: ImageView
        private val secondPlayerImageView: ImageView
        init {
            firstPlayerNameTextView =  itemView.findViewById(R.id.first_player__name_text_view)
            secondPlayerNameTextView =  itemView.findViewById(R.id.second_player__name_text_view)
            countPointsFirstPlayerTextView = itemView.findViewById(R.id.count_points_first_player_text_view)
            countPointsSecondPlayerTextView = itemView.findViewById(R.id.count_points_second_player_text_view)
            firstPlayerImageView = itemView.findViewById(R.id.first_player__image_view)
            secondPlayerImageView = itemView.findViewById(R.id.second_player_image_view)

            itemView.setOnClickListener {
                val game = gamesList[layoutPosition]
                onGameClickListener.onGameClick(game)
            }
        }

        fun bind(game: GameDTO) {
            firstPlayerNameTextView!!.text = game.firstChessPlayer!!.name + " " + game.firstChessPlayer!!.surname
            secondPlayerNameTextView!!.text = game.secondChessPlayer!!.name + " " +  game.secondChessPlayer!!.surname
            countPointsFirstPlayerTextView.text = game.countPointsFirstPlayer.toString()
            countPointsSecondPlayerTextView.text = game.countPointsSecondPlayer.toString()
            var avatarNumber = (0..30).random()
            Picasso.with(itemView.context).load(avatarList[avatarNumber]/*user.imageUrl*/).into(firstPlayerImageView)
            avatarNumber = (0..30).random()
            Picasso.with(itemView.context).load(avatarList[avatarNumber]/*user.imageUrl*/).into(secondPlayerImageView)
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

    fun ClosedRange<Int>.random() =
            Random().nextInt(endInclusive - start) +  start

    interface OnGameClickListener {
        fun onGameClick(game: GameDTO)
    }
}