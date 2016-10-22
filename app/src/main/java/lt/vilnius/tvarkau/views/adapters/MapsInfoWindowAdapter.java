package lt.vilnius.tvarkau.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import lt.vilnius.tvarkau.R;
import lt.vilnius.tvarkau.entity.Problem;
import lt.vilnius.tvarkau.utils.FormatUtils;

public class MapsInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    protected HashMap<String, Problem> problemHashMap;
    protected View view;
    protected Context context;

    @BindView(R.id.problem_map_info_window_content_title)
    protected TextView titleView;
    @BindView(R.id.problem_map_info_window_content_description)
    protected TextView descriptionView;
    @BindView(R.id.problem_map_info_window_content_status)
    protected TextView statusView;
    @BindView(R.id.problem_map_info_window_content_time)
    protected TextView timeView;
    @BindView(R.id.problem_map_info_window_content_thumb)
    protected ImageView thumbView;

    public MapsInfoWindowAdapter(Activity activity, HashMap<String, Problem> problemHashMap) {
        this.problemHashMap = problemHashMap;
        context = activity;
        view = activity.getLayoutInflater().inflate(R.layout.problem_map_info_window, null);

        ButterKnife.bind(this, view);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        Problem problem = problemHashMap.get(marker.getTitle());

        descriptionView.setText(problem.getDescription());

        titleView.setText(problem.getType());

        timeView.setText(FormatUtils.formatLocalDateTime(problem.getEntryDate()));

        problem.applyReportStatusLabel(problem.getStatus(), statusView);

        if (problem.getPhotos() != null) {
            thumbView.setVisibility(View.VISIBLE);
            Glide.with(context).load(problem.getPhotos()[0]).placeholder(R.drawable.ic_placeholder_list_of_reports).into(thumbView);
        } else {
            thumbView.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
