package com.coin.market.home.all;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;

import com.coin.market.BaseFragment;
import com.coin.market.R;
import com.coin.market.event.NotifyEvent;
import com.coin.market.event.SearchNotify;
import com.coin.market.model.AltCoin;
import com.coin.market.model.GlobalMarketCap;
import com.coin.market.shared.MemoryShared;
import com.coin.market.sort.CustomComparator;
import com.vn.fa.adapter.multipleviewtype.IViewBinder;
import com.vn.fa.base.adapter.FaAdapter;
import com.vn.fa.widget.RecyclerViewWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;

/**
 * Created by t430 on 1/7/2018.
 */

public class AllCoinFargment extends BaseFragment implements AllCoinView,
        SwipeRefreshLayout.OnRefreshListener{
    @Bind(R.id.recyclerview)RecyclerViewWrapper recyclerViewWrapper;
    private FaAdapter faAdapter;
    @Override
    protected void initView(Bundle savedInstanceState) {
        presenter = new AllCoinPresenter();
        super.initView(savedInstanceState);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.line_divider));
        recyclerViewWrapper.addItemDecoration(itemDecorator);
        recyclerViewWrapper.setRefreshListener(this);
        ((AllCoinPresenter)presenter).loadData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_all_coin;
    }

    @Override
    public void showError(String message) {
        showToastMessage(message);
    }

    @Override
    public void loadDataToView(List<Object> coinList) {
        if (faAdapter == null){
            faAdapter = new FaAdapter();
            recyclerViewWrapper.setAdapter(faAdapter);
        }
        if (coinList != null && coinList.size() >0) {
            faAdapter.clear();
            List<IViewBinder> viewBinders = (List<IViewBinder>) (List) coinList;
            faAdapter.addAllDataObject(viewBinders);
        }
    }

    @Override
    public void onRefresh() {
        ((AllCoinPresenter)presenter).loadData();
    }

    @Override
    public void handleEvent(Object event) {
        super.handleEvent(event);
        if (event instanceof NotifyEvent){
            NotifyEvent evt = (NotifyEvent)event;
            if (evt.getType() == NotifyEvent.Type.CHANGE_SETTING){
                if (faAdapter != null)
                    faAdapter.notifyDataSetChanged();
            }
            if (evt.getType() == NotifyEvent.Type.CHANGE_SORT_SETTING){
                upDateSort();
            }
            if (evt.getType() == NotifyEvent.Type.ALL_REFRESH){
                onRefresh();
            }
        }
        if (event instanceof SearchNotify){
            SearchNotify searchNotify = (SearchNotify)event;
            doSearch(searchNotify.getQuery());
        }
    }
    private void upDateSort(){
        List<Object> coinList = new ArrayList<>();
        List<AltCoin> all = MemoryShared.getSharedInstance().getAltCoinList();
        if (faAdapter == null){
            faAdapter = new FaAdapter();
            recyclerViewWrapper.setAdapter(faAdapter);
        }
        if (all != null && all.size() >0 && MemoryShared.getSharedInstance().getGlobalMarketCap() != null) {
            faAdapter.clear();
            Collections.sort(all, new CustomComparator());
            coinList.add(0, MemoryShared.getSharedInstance().getGlobalMarketCap());
            coinList.addAll(all);
            List<IViewBinder> viewBinders = (List<IViewBinder>) (List) coinList;
            faAdapter.addAllDataObject(viewBinders);
        }
    }
    private void doSearch(String query){
        List<AltCoin> all = MemoryShared.getSharedInstance().getAltCoinList();
        List<AltCoin> result = new ArrayList<>();
        for (AltCoin altCoin:all
             ) {
            if (altCoin.getId().toLowerCase().contains(query.toLowerCase())
                    || altCoin.getName().toLowerCase().contains(query.toLowerCase())){
                altCoin.setType(AltCoin.Type.ALL_COIN);
                result.add(altCoin);
            }
        }
        if (faAdapter == null){
            faAdapter = new FaAdapter();
            recyclerViewWrapper.setAdapter(faAdapter);
        }
        if (result != null && result.size() >0) {
            faAdapter.clear();
            Collections.sort(result, new CustomComparator());
            List<IViewBinder> viewBinders = (List<IViewBinder>) (List) result;
            faAdapter.addAllDataObject(viewBinders);
        }
    }

}
