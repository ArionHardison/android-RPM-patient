package com.midokter.app.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.midokter.app.R


class FaqAdapter(
    val context: Context,
    val expandableListTitle: List<String>,
    val list: HashMap<String, List<String>>
) : BaseExpandableListAdapter() {

    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return this.list.get(this.expandableListTitle.get(listPosition))!!
            .get(expandedListPosition);
    }


    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun getChildView(
        listPosition: Int, expandedListPosition: Int,
        isLastChild: Boolean, convertView: View?, parent: ViewGroup?
    ): View? {

        val expandedListText = getChild(listPosition, expandedListPosition) as String
        var convertView = convertView
        if (convertView == null) {
            val layoutInflater = this.context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_item_faq, null)

            val expandedListTextView = convertView
                .findViewById<View>(R.id.child_text) as TextView
            expandedListTextView.setText(expandedListText)
        }
        return convertView
    }

    override fun getChildrenCount(listPosition: Int): Int {
        return this.list.get(this.expandableListTitle.get(listPosition))!!.size
    }

    override fun getGroup(listPosition: Int): Any? {
        return this.expandableListTitle.get(listPosition);
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true;
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupCount(): Int {
        return this.expandableListTitle.size
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getGroupView(
        listPosition: Int, isExpanded: Boolean,
        convertView: View?, parent: ViewGroup?
    ): View? {
        var convertView = convertView
        val listTitle = getGroup(listPosition) as String?
        if (convertView == null) {
            val layoutInflater = this.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.item_faq_header, null)
        }
        if (isExpanded) {
            (convertView!!.findViewById<View>(R.id.img_up_down) as ImageView).setImageResource(R.drawable.ic_remove_24px)
            (convertView!!.findViewById<View>(R.id.divider) as View).setVisibility(View.GONE)
        } else {
            (convertView!!.findViewById<View>(R.id.img_up_down) as ImageView).setImageResource(R.drawable.ic_add_24px)
            (convertView!!.findViewById<View>(R.id.divider) as View).setVisibility(View.VISIBLE)
        }
        val listTitleTextView = convertView!!.findViewById<View>(R.id.txt_header_name) as TextView
        listTitleTextView.text = listTitle
        return convertView
    }
}