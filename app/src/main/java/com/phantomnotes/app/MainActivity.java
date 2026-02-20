package com.phantomnotes.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.phantomnotes.app.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SQLManager databaseHelper;
    private RecyclerViewAdapter adapter;
    private final ArrayList<DatabaseObject> dbObjects = new ArrayList<>();

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Activity Lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        setSupportActionBar(binding.toolbar);

        databaseHelper = new SQLManager(this);

        setupRecyclerView();
        loadRecyclerViewData();

        binding.fab.setOnClickListener(view -> {
            Snackbar.make(view, "Created New Note", Snackbar.LENGTH_LONG).show();
            startActivity(new Intent(this, NotesActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshRecyclerView();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // RecyclerView
    private void setupRecyclerView() {
        adapter = new RecyclerViewAdapter(this);
        binding.content.recyclerView.setAdapter(adapter);

        binding.content.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.content.recyclerView.setAdapter(adapter);
        binding.content.recyclerView.setNestedScrollingEnabled(false);
    }

    private void loadRecyclerViewData() {
        adapter.submitList(databaseHelper.getData());
    }

    private void refreshRecyclerView() {
        loadRecyclerViewData();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Options Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, NotesActivity.class));
                return true;

            case R.id.action_settings2:
                // TODO: implement
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
