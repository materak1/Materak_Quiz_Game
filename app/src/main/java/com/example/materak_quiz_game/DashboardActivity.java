package com.example.materak_quiz_game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import utils.Game;
import utils.GameAdapter;
import utils.GameDatabaseHelper;

public class DashboardActivity extends AppCompatActivity {
    GameAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        List<Game> gameModel = new ArrayList<>();
        // Create the adapter to convert the array to views
        adapter = new GameAdapter(this,R.layout.item_game,gameModel);
        // Attach the adapter to a ListView
        ListView listView = findViewById(R.id.game_list);
        listView.setAdapter(adapter);
        Intent intent = getIntent();
        final int user_id = intent.getIntExtra("ID", 0);
        Log.d("START USER ID", String.valueOf(user_id));
        Button new_game = findViewById(R.id.new_game);
        new_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBtn(user_id);
            }
        });

        GameDatabaseHelper helper = GameDatabaseHelper.getInstance(this);




        if (!(user_id > 0)) {
            reLogin();
        } else {
            try {
                List<Game> games = helper.getUserGames(user_id);
                Log.d("Game List Length", String.valueOf(games.size()));
                for (int x = 0; x < games.size(); x++) {
//                int game_id = games.get(x).getGame_id();
                    Game curr_game = games.get(x);
                    int game_id = curr_game.getGame_id();
                    int game_user_id = curr_game.getUser_id();
                    int game_score = curr_game.getScore();
                    Log.d("GAME ID", String.valueOf(game_id));
                    Log.d("USER ID", String.valueOf(game_user_id));
                    Log.d("SCORE", String.valueOf(game_score));
                    adapter.add(curr_game);
                }

//                List<Game> allGames = helper.getAllGames();
//                Log.d("All Game List Length", String.valueOf(allGames.size()));
//                for (int x = 0; x < allGames.size(); x++) {
////                int game_id = games.get(x).getGame_id();
//                    Game curr_game = allGames.get(x);
//                    int game_id = curr_game.getGame_id();
//                    int game_user_id = curr_game.getUser_id();
//                    int game_score = curr_game.getScore();
//                    Log.d("GAME ID", String.valueOf(game_id));
//                    Log.d("USER ID", String.valueOf(game_user_id));
//                    Log.d("SCORE", String.valueOf(game_score));
////                    adapter.add(curr_game);
//                }
            }
            catch(Exception e) {
                Log.d("Error", String.valueOf(e));
            }
//            adapter.add((Game) games);
        }
//        Game new_game = new Game(1,0, 1, 1, 0, 1);
//        adapter.add(new_game);
    }

    private void startBtn(int user_id) {
        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra("ID", user_id);
        startActivity(intent);
//        finish();
    }

    private void reLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
//        finish();
    }
}