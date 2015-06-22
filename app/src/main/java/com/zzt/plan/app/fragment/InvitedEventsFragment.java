package com.zzt.plan.app.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import com.zzt.plan.app.Config;
import com.zzt.plan.app.R;
import com.zzt.plan.app.activity.LoginActivity;
import com.zzt.plan.app.adapter.InvitedEventsAdapter;
import com.zzt.plan.app.entity.EventPlanEntity;
import com.zzt.plan.app.net.LoadInvitations;

import java.util.List;

/**
 * Created by zzt on 15-6-22.
 */
public class InvitedEventsFragment extends LazyFragment {
    private String token;
    private String account;
    private boolean isPrepared;
    private ListView lvEvents;
    private InvitedEventsAdapter adapter;

    public InvitedEventsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        account = Config.getCachedAccount(getActivity());
        token = Config.getCachedToken(getActivity());

        View rootView = inflater.inflate(R.layout.fragment_events, container, false);
        lvEvents = (ListView) rootView.findViewById(R.id.lv_events);
        adapter = new InvitedEventsAdapter(getActivity());
        lvEvents.setAdapter(adapter);
        isPrepared = true;
        return rootView;
    }

    @Override
    protected void lazyLoad() {
        if (isPrepared && isVisible) {
            loadInvitedEvents();
        } else return;
    }

    private void loadInvitedEvents() {
        final ProgressDialog pd = ProgressDialog.show(getActivity(), getString(R.string.now_loading), getString(R.string.please_waite));

        new LoadInvitations(account, token, new LoadInvitations.SuccessCallback() {
            @Override
            public void onSuccess(List<EventPlanEntity> invitations) {
                pd.dismiss();
                adapter.clear();
                adapter.addAll(invitations);
            }
        }, new LoadInvitations.FailCallback() {
            @Override
            public void onFail() {
                onFail(Config.RESULT_STATUS_FAIL);
            }

            @Override
            public void onFail(int failCode) {
                pd.dismiss();
                switch (failCode) {
                    case Config.RESULT_STATUS_FAIL:
                        Toast.makeText(getActivity(), R.string.loading_failed, Toast.LENGTH_LONG).show();
                        break;
                    case Config.RESULT_STATUS_INVALID_TOKEN:
                        Toast.makeText(getActivity(), R.string.invalid_token_please_login_again, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        break;
                }
            }
        });
    }

}
