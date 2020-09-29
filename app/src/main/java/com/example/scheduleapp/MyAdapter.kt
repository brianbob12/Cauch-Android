package com.example.scheduleapp



import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*


class MyAdapter(data: LinkedList<Task>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>(),ItemMoveCallback.ItemTouchHelperContract  {

    private var data:LinkedList<Task> =LinkedList<Task>()

    init {
        this.data = data
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.task_card_row
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.task_card_row, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.setText(data.get(position).getName())
    }

    override fun getItemCount(): Int {
        return data?.size!!;
    }




    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(data, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(data, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onRowSelected(myViewHolder: MyViewHolder?) {
        myViewHolder?.rowView?.setBackgroundColor(Color.GRAY);
    }

    override fun onRowClear(myViewHolder: MyViewHolder?) {
        myViewHolder?.rowView?.setBackgroundColor(Color.WHITE);
    }

    //viewholder for the list
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView
        var rowView: View

        init {
            rowView = itemView
            title = itemView.findViewById(R.id.mainTitle)
        }
    }


}