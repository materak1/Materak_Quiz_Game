package com.example.materak_quiz_game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import utils.Game;
import utils.GameAdapter;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ArrayList<Game> arrayOfGames = new ArrayList<Game>();
        // Create the adapter to convert the array to views
        GameAdapter adapter = new GameAdapter(this, arrayOfGames);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.game_list);
        listView.setAdapter(adapter);

        Game new_game = new Game(1,0, 1, 1, 0, 1);
        adapter.add(new_game);
    }
}