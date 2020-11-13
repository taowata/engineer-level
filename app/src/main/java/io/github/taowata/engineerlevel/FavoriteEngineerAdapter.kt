package io.github.taowata.engineerlevel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.taowata.engineerlevel.data.Engineer
import io.github.taowata.engineerlevel.databinding.FavoriteEngineerItemBinding

class FavoriteEngineerAdapter(
    private val itemClickAction: (Engineer) -> Unit
): RecyclerView.Adapter<FavoriteEngineerAdapter.FavoriteEngineerViewHolder>() {

    private var engineers: List<Engineer> = mutableListOf()

    class FavoriteEngineerViewHolder(private val binding: FavoriteEngineerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Engineer, onItemClick: (Engineer) -> Unit) {
            binding.engineerName.text = item.name
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): FavoriteEngineerViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoriteEngineerItemBinding.inflate(layoutInflater, parent, false)
                return FavoriteEngineerViewHolder(
                    binding
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteEngineerViewHolder {
        return FavoriteEngineerViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: FavoriteEngineerViewHolder, position: Int) {
        val current = engineers[position]
        holder.bind(current, itemClickAction)
    }

    override fun getItemCount(): Int = engineers.size

    fun setEngineers(engineers: List<Engineer>) {
        this.engineers = engineers
        notifyDataSetChanged()
    }
}