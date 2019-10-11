package bobrchess.of.by.belaruschess.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import bobrchess.of.by.belaruschess.R
import kotlinx.android.synthetic.main.card_view_help.view.*

class HelpAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class HelpInstance {
        EventBirthday,
        AnnualEvent,
        OneTimeEvent,
        Reason,
        Support,
        Notifications
    }

    class AboutCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val itemList = listOf(
        HelpInstance.Reason,
        HelpInstance.EventBirthday,
        HelpInstance.AnnualEvent,
        HelpInstance.OneTimeEvent,
        HelpInstance.Notifications,
        HelpInstance.Support
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): RecyclerView.ViewHolder {
        val cardView =
            LayoutInflater.from(parent.context).inflate(R.layout.card_view_help, parent, false)
        return AboutCardViewHolder(cardView)
    }

    override fun getItemCount(): Int {
        return this.itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return this.itemList[position].ordinal
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        //change visibility of textview on image view click
        holder.itemView.constrLayout_help_title.setOnClickListener {
            holder.itemView.tv_card_view_help_content.let {
                if (it.visibility == TextView.GONE) {
                    it.visibility = TextView.VISIBLE
                    holder.itemView.iv_expand_text.animate().rotation(180f)
                } else {
                    it.visibility = TextView.GONE
                    holder.itemView.iv_expand_text.animate().rotation(0f)
                }
            }
        }
        when (itemList[position]) {
            HelpInstance.Reason -> {
                holder.itemView.tv_card_view_help_title.text =
                    context.resources.getText(R.string.incorrect_rating)
                holder.itemView.tv_card_view_help_content.text =
                    context.resources.getText(R.string.incorrect_rating)
            }
            HelpInstance.EventBirthday -> {
                holder.itemView.tv_card_view_help_title.text =
                    context.resources.getText(R.string.incorrect_rating)
                holder.itemView.tv_card_view_help_content.text =
                    context.resources.getText(R.string.incorrect_rating)
            }
            HelpInstance.AnnualEvent -> {
                holder.itemView.tv_card_view_help_title.text =
                    context.resources.getText(R.string.incorrect_rating)
                holder.itemView.tv_card_view_help_content.text =
                    context.resources.getText(R.string.incorrect_rating)

            }
            HelpInstance.OneTimeEvent -> {
                holder.itemView.tv_card_view_help_title.text =
                    context.resources.getText(R.string.incorrect_rating)
                holder.itemView.tv_card_view_help_content.text =
                    context.resources.getText(R.string.incorrect_rating)
            }
            HelpInstance.Support -> {
                holder.itemView.tv_card_view_help_title.text =
                    context.resources.getText(R.string.incorrect_rating)
                holder.itemView.tv_card_view_help_content.text =
                    context.resources.getText(R.string.incorrect_rating)
            }
            HelpInstance.Notifications -> {
                holder.itemView.tv_card_view_help_title.text =
                    context.resources.getText(R.string.incorrect_rating)
                holder.itemView.tv_card_view_help_content.text =
                    context.resources.getText(R.string.incorrect_rating)
            }
        }
    }
}