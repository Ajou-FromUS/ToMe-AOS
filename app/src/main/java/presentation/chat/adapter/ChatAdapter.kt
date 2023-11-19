package presentation.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tome_aos.R

class ChatAdapter(private val messageList: List<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val SEND_VIEW = 1
        private const val RECEIVE_VIEW = 2
    }
    inner class SendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sendTextView: TextView = itemView.findViewById(R.id.tv_send_message)
    }
    inner class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiveTextView: TextView = itemView.findViewById(R.id.tv_receive_message)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SEND_VIEW -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_send_message, parent, false)
                SendViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_receive_message, parent, false)
                ReceiveViewHolder(view)
            }
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]
        when (holder.itemViewType) {
            SEND_VIEW -> {
                val sendHolder = holder as SendViewHolder
                sendHolder.sendTextView.text = message
            }
            RECEIVE_VIEW -> {
                val receiveHolder = holder as ReceiveViewHolder
                receiveHolder.receiveTextView.text = message
            }
        }
    }
    override fun getItemCount(): Int {
        return messageList.size
    }
    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 1) {
            SEND_VIEW
        } else {
            RECEIVE_VIEW
        }
    }
}