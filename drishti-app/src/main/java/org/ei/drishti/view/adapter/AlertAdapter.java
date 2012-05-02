package org.ei.drishti.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.ei.drishti.R;
import org.ei.drishti.domain.Alert;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static org.ei.drishti.util.DateUtil.formattedDueDate;

public class AlertAdapter extends ArrayAdapter<Alert> {
    private List<Alert> alerts;
    private List<Alert> alertsUnfiltered;
    private List<WeakReference<OnDataSourceChangedListener>> onDataSourceChangedListeners;

    public AlertAdapter(Context context, int listItemResourceId, List<Alert> alerts) {
        super(context, listItemResourceId, alerts);
        this.alerts = alerts;
        this.alertsUnfiltered = new ArrayList<Alert>(alerts);
        this.onDataSourceChangedListeners = new ArrayList<WeakReference<OnDataSourceChangedListener>>();
    }

    public void updateAlerts(List<Alert> alerts) {
        this.alertsUnfiltered.clear();
        this.alertsUnfiltered.addAll(alerts);

        for (WeakReference<OnDataSourceChangedListener> dataSourceChangedListener : onDataSourceChangedListeners) {
            OnDataSourceChangedListener listener = dataSourceChangedListener.get();
            if (listener != null) {
                listener.dataSourceChanged();
            }
        }
    }

    public void refreshDisplayWithoutUpdatingAlerts(List<Alert> newAlertsToDisplay) {
        this.alerts.clear();
        this.alerts.addAll(newAlertsToDisplay);
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater viewInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = viewInflater.inflate(R.layout.list_item, null);
        }

        Alert alert = alerts.get(position);
        setTextView(view, R.id.beneficiaryName, alert.beneficiaryName());
        setTextView(view, R.id.dueDate, formattedDueDate(alert.dueDate()));
        setTextView(view, R.id.thaayiCardNo, alert.thaayiCardNo());
        setTextView(view, R.id.visitCode, alert.visitCode());

        return view;
    }

    private void setTextView(View v, int viewId, String text) {
        TextView beneficiaryName = (TextView) v.findViewById(viewId);
        beneficiaryName.setText(text);
    }

    public List<Alert> getAlerts() {
        return alertsUnfiltered;
    }

    public void setOnDataSourceChanged(OnDataSourceChangedListener onDataSourceChangedListener) {
        this.onDataSourceChangedListeners.add(new WeakReference<OnDataSourceChangedListener>(onDataSourceChangedListener));
    }

    public static interface OnDataSourceChangedListener {
        void dataSourceChanged();
    }
}