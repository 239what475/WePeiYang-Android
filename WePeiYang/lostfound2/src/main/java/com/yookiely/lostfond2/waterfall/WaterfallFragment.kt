package com.yookiely.lostfond2.waterfall

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.lostfond2.R
import com.yookiely.lostfond2.service.MyListDataOrSearchBean
import com.yookiely.lostfond2.service.Utils
import kotlinx.android.synthetic.main.lf2_fragment_waterfall.*

class WaterfallFragment : Fragment(), WaterfallContract.WaterfallView {

    private lateinit var tableAdapter: WaterfallTableAdapter
    private val layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL) // GridLayoutManager(activity, 2)
    private var isLoading = false
    private var isRefresh = false
    private var campus = Utils.CAMPUS_BEI_YANG_YUAN
    private var beanList = mutableListOf<MyListDataOrSearchBean>()
    private var lostOrFound = Utils.STRING_LOST
    private var type = Utils.ALL_TYPE
    private var page = 1
    private var time = Utils.ALL_TIME
    private val waterfallPresenter = WaterfallPresenterImpl(this)
    private lateinit var waterfallRecyclerView: RecyclerView

    companion object {
        fun newInstance(type: String): WaterfallFragment {
            val args = Bundle()
            args.putString(Utils.INDEX_KEY, type)
            val fragment = WaterfallFragment()
            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.lf2_fragment_waterfall, container, false)
        val waterfallRefresh = view.findViewById<SwipeRefreshLayout>(R.id.sr_waterfall_refresh).apply {
            setColorSchemeColors(0x449ff1)
        }
        waterfallRecyclerView = view.findViewById<RecyclerView>(R.id.rv_waterfall_homepage)
        val waterfallNoRes = view.findViewById<LinearLayout>(R.id.ll_waterfall_no_res)

        campus = Utils.campus ?: Utils.CAMPUS_BEI_YANG_YUAN

        waterfallRecyclerView.layoutManager = layoutManager
        waterfallNoRes.visibility = View.GONE
        val bundle = arguments
        lostOrFound = bundle!!.getString(Utils.INDEX_KEY)
        tableAdapter = WaterfallTableAdapter(beanList, this.activity!!, lostOrFound)
        waterfallRecyclerView.adapter = tableAdapter
        waterfallRefresh.setOnRefreshListener(this::refresh)

        waterfallRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalCount = layoutManager.itemCount
                val array = IntArray(2)
                layoutManager.findLastCompletelyVisibleItemPositions(array)

                if (!isLoading && (totalCount <= array[1] + 2)) {
                    page++
                    isLoading = true
                    waterfallPresenter.apply {
                        if (type == Utils.ALL_TYPE) {
                            loadWaterfallData(lostOrFound, page, time)
                        } else {
                            loadWaterfallDataWithCondition(lostOrFound, page, type, time)
                        }
                    }
                }
            }
        })

        return view
    }

    override fun setWaterfallData(waterfallBean: List<MyListDataOrSearchBean>) {
        ll_waterfall_no_res.visibility = if (waterfallBean.isEmpty() && page == 1) {
            View.VISIBLE
        } else {
            View.GONE
        }

        if (isRefresh) {
            beanList.clear()
        }

        beanList.addAll(waterfallBean)
        if (isRefresh || !waterfallBean.isEmpty())
            tableAdapter.notifyDataSetChanged()
        sr_waterfall_refresh.isRefreshing = false
        isLoading = waterfallBean.isEmpty() && page == 1
        waterfallRecyclerView.scrollToPosition(layoutManager.itemCount)
        isRefresh = false
    }

    override fun loadWaterfallDataWithCondition(type: Int, time: Int) {
        this.type = type
        this.time = time
        page = 1
        isRefresh = true
        waterfallPresenter.loadWaterfallDataWithCondition(lostOrFound, page, this.type, this.time)
    }

    override fun onResume() {
        super.onResume()
        val newCampus = Utils.campus

        if (newCampus != null && campus != newCampus) {
            this.type = Utils.ALL_TYPE
            this.time = Utils.ALL_TIME
            campus = newCampus
            refresh()
        } else if (Utils.needRefreshWaterfall != 0) {
            Utils.needRefreshWaterfall--
            this.type = Utils.ALL_TYPE
            this.time = Utils.ALL_TIME
            refresh()
        } else if (beanList.isEmpty()) {
            this.type = Utils.ALL_TYPE
            this.time = Utils.ALL_TIME
            refresh()
        }
    }

    private fun refresh() {
        isLoading = true
        isRefresh = true
        page = 1
        waterfallPresenter.loadWaterfallDataWithCondition(lostOrFound, page, this.type, this.time)
    }
}
