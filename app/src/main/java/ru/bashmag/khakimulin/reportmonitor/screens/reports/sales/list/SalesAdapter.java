package ru.bashmag.khakimulin.reportmonitor.screens.reports.sales.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import ru.bashmag.khakimulin.reportmonitor.R;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.sales.SalesData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverData;

/**
 * Created by Mark Khakimulin on 01.11.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class SalesAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<SalesData> data = new ArrayList<>();
    private final PublishSubject<Integer> groupClicks = PublishSubject.create();

    public SalesAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<SalesData> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(groupPosition).getTurnoverList().size();
    }

    @Override
    public SalesData getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public TurnoverData getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).getTurnoverList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ChildViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_turnover_child, parent, false);
            holder = new ChildViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        holder.bind(getChild(groupPosition, childPosition));
        return convertView;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_turnover_parent, parent, false);

            holder = new GroupViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }

        holder.bind(getGroup(groupPosition));

/*
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupClicks.onNext(groupPosition);
            }
        });
*/

        return convertView;
    }

    public Observable<Integer> observeGroupsClicks() {
        return groupClicks;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public class GroupViewHolder {
        View view;

        @BindView(R.id.store)
        TextView store;

        @BindView(R.id.employee)
        TextView employee;

        @BindView(R.id.position)
        TextView position;

        @BindView(R.id.group)
        TextView group;

        @BindView(R.id.plan)
        TextView plan;

        @BindView(R.id.fact)
        TextView fact;

        DecimalFormat df;


        public GroupViewHolder(View itemView) {
            this.view = itemView;
            ButterKnife.bind(this, view);

            df = new DecimalFormat("#.#");
            df.setRoundingMode(RoundingMode.CEILING);


        }

        void bind(SalesData reportItem) {
            store.setText(reportItem.storeTitle);
            employee.setText(reportItem.employee);
            position.setText(reportItem.position);
            fact.setText(String.valueOf(df.format(reportItem.getTurnoverFact())));
            plan.setText(String.valueOf(df.format(reportItem.getTurnoverPlan())));
            group.setText("Общее");

        }
    }
    public class ChildViewHolder {
        View view;

        @BindView(R.id.group)
        TextView group;

        @BindView(R.id.plan)
        TextView plan;

        @BindView(R.id.fact)
        TextView fact;

        DecimalFormat df;


        public ChildViewHolder(View itemView) {
            this.view = itemView;
            ButterKnife.bind(this, view);

            df = new DecimalFormat("#.#");
            df.setRoundingMode(RoundingMode.CEILING);

        }

        void bind(TurnoverData reportItem) {
            fact.setText(String.valueOf(df.format(reportItem.fact)));
            plan.setText(String.valueOf(df.format(reportItem.plan)));
            group.setText(reportItem.group);

        }
    }
}