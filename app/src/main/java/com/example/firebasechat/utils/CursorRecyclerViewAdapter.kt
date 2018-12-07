package com.example.firebasechat.utils

import android.content.Context
import android.database.Cursor
import android.database.DataSetObserver
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup

abstract class CursorRecyclerViewAdapter<VH: RecyclerView.ViewHolder>(
    private val context: Context,
    private val cursor: Cursor
): RecyclerView.Adapter<VH>() {

    private var isDataValid = true
    private val dataSetObserver = NotificationDataSetObserver()
    private val rowIdColumn = if (isDataValid) cursor.getColumnIndex("_ID") else -1

    init {
        cursor.registerDataSetObserver(dataSetObserver)
    }


    override fun getItemCount(): Int {
        if (isDataValid){
            return cursor.count
       }
        return 0
    }

    override fun getItemId(position: Int): Long {
        if (isDataValid && cursor.moveToPosition(position)){
            return cursor.getLong(rowIdColumn)
        }

        return 0
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        if (!isDataValid){
            throw IllegalStateException()
        }

        if (!cursor.moveToPosition(position)){
            throw IllegalStateException()
        }

        onBindViewHolder(holder, cursor)
    }

    abstract fun onBindViewHolder(holder: VH, cursor: Cursor)

    inner class NotificationDataSetObserver: DataSetObserver(){
        override fun onChanged() {
            super.onChanged()
            isDataValid = true
            notifyDataSetChanged()
        }

        override fun onInvalidated() {
            super.onInvalidated()
            isDataValid = false
            notifyDataSetChanged()
        }
    }
}