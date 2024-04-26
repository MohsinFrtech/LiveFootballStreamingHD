package com.depvt.live.football.tv.ui.app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depvt.live.football.tv.R
import com.depvt.live.football.tv.databinding.ItemLayoutEventsBinding
import com.depvt.live.football.tv.models.Event
import com.depvt.live.football.tv.ui.app.fragments.CategoryFragmentDirections
import com.depvt.live.football.tv.ui.app.fragments.EventFragmentDirections
import com.depvt.live.football.tv.utils.interfaces.NavigateData

class CategoryAdapter(
    val context: Context, private val navigateData: NavigateData, val list: List<Event?>,
    private val adType: String, private val adManager: com.depvt.live.football.tv.adsData.AdManager
) : ListAdapter<Event, CategoryAdapter.EventAdapterViewHolder>(CategoryAdapterDiffUtilCallback) {


//    private var bindingEvent: ItemLayoutEventsBinding? = null
    object CategoryAdapterDiffUtilCallback : DiffUtil.ItemCallback<Event>() {


        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventAdapterViewHolder {
        val bindingEvent:ItemLayoutEventsBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
            R.layout.item_layout_events,parent,false)
        return EventAdapterViewHolder(bindingEvent)
    }


    class EventAdapterViewHolder(private var binding: ItemLayoutEventsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindEvent(event: Event?) {
           binding.modelData = event
        }


    }

    override fun onBindViewHolder(holder: EventAdapterViewHolder, position: Int) {
//        holder.setIsRecyclable(false)
        holder.bindEvent(currentList[position])

        holder.itemView.setOnClickListener {

            val direction = EventFragmentDirections.actionEventToChannel(
                currentList[position],
                null,
                null
            )
            navigateData.navigation(direction)

        }
    }
}