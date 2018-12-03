package ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.list;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.bashmag.khakimulin.reportmonitor.R;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverReportData;
import rx.Observable;
import rx.subjects.PublishSubject;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * Created by Mark Khakimulin on 26.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class TurnoverAdapter extends RecyclerView.Adapter<TurnoverAdapter.ReportViewHolder>{

    private final PublishSubject<TurnoverData> monthFactClicks = PublishSubject.create();
    private final PublishSubject<TurnoverData> monthPlanClicks = PublishSubject.create();
    private final PublishSubject<TurnoverData> dailyFactClicks = PublishSubject.create();
    private final PublishSubject<TurnoverData> dailyPlanClicks = PublishSubject.create();

    private final PublishSubject<ConversionData> conversionClicks = PublishSubject.create();
    private final PublishSubject<ConversionData> fullnessClicks = PublishSubject.create();

    private final PublishSubject<String> storesClicks = PublishSubject.create();

    private ArrayList<TurnoverReportData> objects = new ArrayList<>();

    Context context;


    public TurnoverAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public TurnoverAdapter.ReportViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_turnover, viewGroup, false);
        return new TurnoverAdapter.ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TurnoverAdapter.ReportViewHolder reportViewHolder, int position) {
        TurnoverReportData report = objects.get(position);
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

    public void replaceAll(ArrayList<TurnoverReportData> list) {
        objects.clear();
        objects.addAll(list);
        notifyDataSetChanged();
    }

    public Observable<TurnoverData> observeMonthFactClicks() {
        return monthFactClicks;
    }
    public Observable<TurnoverData> observeMonthPlanClicks() {
        return monthPlanClicks;
    }
    public Observable<TurnoverData> observeDailyFactClicks() {
        return dailyFactClicks;
    }
    public Observable<TurnoverData> observeDailyPlanClicks() {
        return dailyPlanClicks;
    }
    public Observable<ConversionData> observeConversionClicks() {
        return conversionClicks;
    }
    public Observable<ConversionData> observeFullnessClicks() {
        return fullnessClicks;
    }
    public Observable<String> observeStoresClicks() {
        return storesClicks;
    }

    public class ReportViewHolder extends RecyclerView.ViewHolder {

        View view;

        @BindView(R.id.store)
        TextView store;

        @BindView(R.id.month_plan)
        TextView monthPlan;

        @BindView(R.id.month_fact)
        TextView monthFact;

        @BindView(R.id.daily_plan)
        TextView dailyPlan;

        @BindView(R.id.daily_fact)
        TextView dailyFact;

        @BindView(R.id.conversion)
        TextView conversion;

        @BindView(R.id.fullness)
        TextView fullness;

        @BindView(R.id.daily_plan_container)
        ViewGroup dailyPlanContainer;

        @BindView(R.id.daily_fact_container)
        ViewGroup dailyFactContainer;

        @BindView(R.id.conversion_container)
        ViewGroup conversionContainer;

        @BindView(R.id.fullness_container)
        ViewGroup fullnessContainer;

        @BindView(R.id.exchange)
        TextView exchange;

        public ReportViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            ButterKnife.bind(this, view);

            dailyPlanContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dailyPlanClicks.onNext(objects.get(getAdapterPosition()).turnoverLast);
                }
            });
            dailyFactContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dailyFactClicks.onNext(objects.get(getAdapterPosition()).turnoverLast);
                }
            });
            conversionContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    conversionClicks.onNext(objects.get(getAdapterPosition()).conversionLast);
                }
            });
            fullnessContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fullnessClicks.onNext(objects.get(getAdapterPosition()).conversionLast);
                }
            });
            store.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    storesClicks.onNext(objects.get(getAdapterPosition()).turnover.storeId);
                }
            });
        }

        void bind(TurnoverReportData reportItem) {

            DecimalFormat df = new DecimalFormat("#,###,###.##");
            df.setRoundingMode(RoundingMode.CEILING);

            store.setText(reportItem.turnover.storeTitle);
            monthPlan.setText(String.valueOf(df.format(reportItem.turnover.plan)));
            monthFact.setText(String.valueOf(df.format(reportItem.turnover.fact)));
            dailyPlan.setText(String.valueOf(df.format(reportItem.turnoverLast.plan)));

            if (reportItem.turnoverLast.fact > 0) {
                dailyFact.setText(String.valueOf(df.format(reportItem.turnoverLast.fact)));
            }else {
                dailyFact.setText("нет");
            }

            Drawable imageDown = context.getResources().getDrawable( R.drawable.arrow_down );
            imageDown.setBounds( 0, 0, imageDown.getIntrinsicWidth(), imageDown.getIntrinsicHeight() );

            Drawable imageUp= context.getResources().getDrawable( R.drawable.arrow_up );
            imageUp.setBounds( 0, 0, imageUp.getIntrinsicWidth(), imageUp.getIntrinsicHeight() );

            if (reportItem.turnover.fact >= reportItem.turnover.plan) {
                monthFact.setCompoundDrawables(null,null,imageUp,null);
                monthFact.setTextColor(context.getResources().getColor(R.color.colorRight));
            } else {
                monthFact.setCompoundDrawables(null,null,imageDown,null);
                monthFact.setTextColor(context.getResources().getColor(R.color.colorWrong));
            }

            if (reportItem.turnoverLast.fact >= reportItem.turnoverLast.plan) {
                dailyFact.setCompoundDrawables(null,null,imageUp,null);
                dailyFact.setTextColor(context.getResources().getColor(R.color.colorRight));
            } else {
                dailyFact.setCompoundDrawables(null,null,imageDown,null);
                dailyFact.setTextColor(context.getResources().getColor(R.color.colorWrong));
            }

            if (reportItem.conversionLast.visitors > 0) {
                conversion.setText(String.valueOf(df.format((float) reportItem.conversionLast.cheques / (float) reportItem.conversionLast.visitors * 100)));
            } else {
                conversion.setText("нет");
            }
            if (reportItem.conversionLast.cheques > 0) {
                fullness.setText(String.valueOf(df.format((float) reportItem.conversionLast.items / (float) reportItem.conversionLast.cheques * 100)));
            } else {
                fullness.setText("нет");
            }

            setActual(reportItem.turnoverLast.actual > 0 || reportItem.turnover.actual > 0);
        }

        public void setActual(Boolean isActual) {
            exchange.setVisibility(isActual?INVISIBLE:VISIBLE);
        }

    }
}
