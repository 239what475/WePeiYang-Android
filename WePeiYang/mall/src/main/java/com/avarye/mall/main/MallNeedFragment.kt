package com.avarye.mall.main

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.avarye.mall.R
import com.avarye.mall.detail.DetailActivity
import com.avarye.mall.service.MallManager
import com.avarye.mall.service.ViewModel
import com.avarye.mall.service.detailLiveData
import com.avarye.mall.service.needLiveData
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemManager
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.mall_fragment_latest_need.*
import kotlinx.android.synthetic.main.mall_fragment_latest_need.view.*

/**
 * 最新求购
 */
class MallNeedFragment : Fragment() {
    private var page = 1
    private var totalPage = 1
    private var isLoading = false
    private val itemManager = ItemManager()
    private val viewModel = ViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.mall_fragment_latest_need, container, false)
        view.srl_main_need.apply {
            setColorSchemeResources(R.color.mallColorMain)
            //下拉刷新加载监听
            setOnRefreshListener {
                if (!isLoading) {
                    isLoading = true
                    resetPage()
                    itemManager.autoRefresh { removeAll { it is SaleItem } }
                    //redo
                    viewModel.getLatestNeed(page)
                    isRefreshing = false
                    Toasty.success(context, "已刷新").show()
                }
                isLoading = false
            }
        }

        view.rv_main_need.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = ItemAdapter(itemManager)
            //加载下一页监听
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!canScrollVertically(1) && page < totalPage) {
                        isLoading = true
                        //more
                        viewModel.getLatestNeed(++page)
                    }
                    isLoading = false
                }
            })
        }
        viewModel.getLatestNeed(page)
        bindNeed()
        return view
    }

    private fun bindNeed() {
        needLiveData.bindNonNull(this) { list ->
            totalPage = list[0].page
            if (totalPage == 0) {
                itemManager.autoRefresh { removeAll { it is SaleItem } }
                iv_need_null.visibility = View.VISIBLE
            } else {
                iv_need_null.visibility = View.GONE
                val items = mutableListOf<Item>().apply {
                    for (i in 1 until list.size) {
                        saleItem {
                            name.text = list[i].name
                            price.text = list[i].price
                            locate.text = MallManager.dealText(list[i].location)
                            card.setOnClickListener {
                                detailLiveData.postValue(list[i])
                                val intent = Intent(context, DetailActivity::class.java)
                                        .putExtra(MallManager.TYPE, MallManager.NEED)
                                startActivity(intent)
                            }
                        }
                    }
                }
                if (page == 1) {
                    itemManager.autoRefresh {
                        removeAll { it is SaleItem }
                        addAll(items)
                    }
                } else {
                    itemManager.addAll(items)
                }
            }
        }
    }

    private fun resetPage() {
        page = 1
        totalPage = 1
    }
}