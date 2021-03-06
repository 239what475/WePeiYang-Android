package com.yookiely.lostfond2.waterfall

import android.content.Context
import android.graphics.Typeface
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lostfond2.R
import com.twt.wepeiyang.commons.mta.mtaClick
import com.yookiely.lostfond2.service.Utils

class WaterfallTypeTableAdapter(private val waterfallActivity: WaterfallActivity,
                                val context: Context,
                                private val selectedItem: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class WaterFallTypeTableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val waterfallTypeItem: TextView = itemView.findViewById(R.id.tv_waterfall_type_item)
        val waterfallTypeLine: View = itemView.findViewById(R.id.vw_waterfall_type_line)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lf2_item_waterfall_type, parent, false)

        return WaterFallTypeTableViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as WaterFallTypeTableViewHolder

        viewHolder.apply {
            waterfallTypeItem.apply {
                text = Utils.getType(position)
                typeface = Typeface.DEFAULT
            }

            waterfallTypeLine.visibility = if (position == itemCount - 1 || position == itemCount - 2) View.GONE else View.VISIBLE

            if (position == selectedItem) {
                waterfallTypeItem.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            }

            itemView.setOnClickListener {
                mtaClick("lostfound2_首页 点击种类筛选${Utils.getType(position)}的次数")

                waterfallActivity.apply {
                    setWaterfallType(position)
                    waterfallTypeItem.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                    windowpop.dismiss()
                }
            }
        }
    }

    // 物品种类 = 13 + "全部种类"
    override fun getItemCount(): Int = 14
}