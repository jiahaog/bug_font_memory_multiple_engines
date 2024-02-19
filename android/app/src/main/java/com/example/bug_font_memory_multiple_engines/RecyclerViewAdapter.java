package com.example.bug_font_memory_multiple_engines;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.app.Activity;
import androidx.activity.ComponentActivity;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineGroup;
import io.flutter.embedding.android.FlutterView;
import io.flutter.embedding.android.ExclusiveAppComponent;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
  private static final int NUM_ITEMS = 16;
  private final Context context;
  private final ComponentActivity activity;
  private final FlutterEngineGroup flutterEngineGroup;

  RecyclerViewAdapter(Context context, ComponentActivity activity) {
    this.context = context;
    this.activity = activity;
    this.flutterEngineGroup = new FlutterEngineGroup(context);
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.row_item, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder viewHolder, int position) {
    FlutterView flutterView = new FlutterView(context);
    viewHolder.flutterViewContainer.addView(flutterView, MATCH_PARENT, MATCH_PARENT);

    FlutterEngine flutterEngine = flutterEngineGroup.createAndRunEngine(
        new FlutterEngineGroup.Options(context));

    FlutterViewEngine flutterViewEngine = new FlutterViewEngine(flutterEngine, activity);
    flutterEngine.getActivityControlSurface()
        .attachToActivity(flutterViewEngine, activity.getLifecycle());
    flutterView.attachToFlutterEngine(flutterEngine);
    activity.getLifecycle().addObserver(flutterViewEngine);
  }

  @Override
  public int getItemCount() {
    return NUM_ITEMS;
  }

  static final class ViewHolder extends RecyclerView.ViewHolder {

    final LinearLayout flutterViewContainer;

    ViewHolder(View itemView) {
      super(itemView);
      flutterViewContainer = itemView.findViewById(R.id.flutter_view_container);
    }
  }

  static final class FlutterViewEngine implements DefaultLifecycleObserver,
      ExclusiveAppComponent<Activity> {

    final FlutterEngine engine;
    final ComponentActivity activity;

    FlutterViewEngine(FlutterEngine engine, ComponentActivity activity) {
      this.engine = engine;
      this.activity = activity;
    }

    @Override
    public void onResume(LifecycleOwner owner) {
      engine.getLifecycleChannel().appIsResumed();
    }

    @Override
    public void onPause(LifecycleOwner owner) {
      engine.getLifecycleChannel().appIsInactive();
    }

    @Override
    public void onStop(LifecycleOwner owner) {
      engine.getLifecycleChannel().appIsPaused();
    }

    @Override
    public void detachFromFlutterEngine() {}

    @Override
    public Activity getAppComponent() {
      return activity;
    }
  }
}