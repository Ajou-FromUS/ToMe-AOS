package presentation.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tome_aos.R
import data.dto.ChatDTO

class ChatAdapter(private val messageList: List<ChatDTO>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sendTextView: TextView = itemView.findViewById(R.id.tv_send_message)
        val receiveTextView: TextView = itemView.findViewById(R.id.tv_receive_message)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_send_message, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]
        val messageHolder = holder as ViewHolder

        messageHolder.sendTextView.visibility = if (message.sender == "SENDER") View.VISIBLE else View.GONE
        messageHolder.receiveTextView.visibility = if (message.sender == "RECEIVER") View.VISIBLE else View.GONE
        val content = if (message.sender == "SENDER" || message.sender == "RECEIVER") message.content else "Unknown sender"
        messageHolder.sendTextView.text = content
        messageHolder.receiveTextView.text = content
    }
    override fun getItemCount(): Int {
        return messageList.size
    }
}