package com.juangm.myscrumboard_android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.juangm.myscrumboard_android.MainStages;
import com.juangm.myscrumboard_android.R;
import com.juangm.myscrumboard_android.adapters.CardsAdapter;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

//Fragment with "paused" cards list
public class PausedCardsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(
                R.layout.fragment_card_list, container, false);
        setupRecyclerView(rv);
        return rv;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        MainStages application = (MainStages) getActivity().getApplication();
        recyclerView.setItemAnimator(new SlideInLeftAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new CardsAdapter(application.pausedCards));
    }
}
