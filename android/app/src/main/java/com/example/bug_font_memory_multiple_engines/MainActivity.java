package com.example.bug_font_memory_multiple_engines;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.activity.ComponentActivity;
import io.flutter.embedding.engine.FlutterEngineGroup;

public final class MainActivity extends ComponentActivity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), this);

    RecyclerView recyclerView = findViewById(R.id.recycler_view);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(recyclerViewAdapter);
  }
}