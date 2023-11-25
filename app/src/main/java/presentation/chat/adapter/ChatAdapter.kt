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
        if (message.sender == "SENDER") {
            messageHolder.sendTextView.visibility = View.VISIBLE
            messageHolder.sendTextView.text = message.content
            messageHolder.receiveTextView.visibility = View.GONE
        } else if (message.sender == "RECEIVER") {
            messageHolder.sendTextView.visibility = View.GONE
            messageHolder.receiveTextView.visibility = View.VISIBLE
            messageHolder.receiveTextView.text = message.content
        } else {
            messageHolder.receiveTextView.visibility = View.VISIBLE
            messageHolder.receiveTextView.text = message.content
            messageHolder.sendTextView.visibility = View.GONE
        }
    }
    override fun getItemCount(): Int {
        return messageList.size
    }
}