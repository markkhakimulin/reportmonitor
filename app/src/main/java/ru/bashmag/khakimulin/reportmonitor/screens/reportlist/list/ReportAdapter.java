package ru.bashmag.khakimulin.reportmonitor.screens.reportlist.list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.bashmag.khakimulin.reportmonitor.R;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by Mark Khakimulin on 02.10.2018.
 * Email : mark.khakimulin@gmail.com
 */

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder>{


    private final PublishSubject<Integer> itemClicks = PublishSubject.create();
    ArrayList<ReportItem> objects = new ArrayList<>();

    public ReportAdapter(Context context) {

    }

    public Observable<Integer> observeClicks() {
        return itemClicks;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_report, viewGroup, false);
        return new ReportViewHolder(view ,itemClicks);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder reportViewHolder, int position) {
        ReportItem report = objects.get(position);
        reportViewHolder.bind(report);
    }

    @Override
    public int getItemCount() {
        if (objects != null && objects.size() > 0) {
            return objects.size();
        } else {
            return 0;
        }
    }
    public void replaceAll(ArrayList<ReportItem> list) {
        objects.clear();
        objects.addAll(list);
        notifyDataSetChanged();
    }

    public class ReportViewHolder extends RecyclerView.ViewHolder {

        View view;

        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvDesc)
        TextView tvDesc;
        @BindView(R.id.tvNew)
        TextView tvNew;

        public ReportViewHolder(View itemView, PublishSubject<Integer> clickSubject) {
            super(itemView);
            this.view = itemView;
            ButterKnife.bind(this, view);

            view.setOnClickListener(v -> clickSubject.onNext(getAdapterPosition()));
        }

        void bind(ReportItem reportItem) {

            tvName.setText(reportItem.name);
            tvDesc.setText(reportItem.desc);
            if (reportItem.isNew)
                tvNew.setVisibility(View.VISIBLE);
            else
                tvNew.setVisibility(View.GONE);
        }

    }

}
