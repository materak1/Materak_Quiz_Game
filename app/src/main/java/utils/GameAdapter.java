package utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.materak_quiz_game.R;

import java.util.ArrayList;
import java.util.List;

public class GameAdapter extends ArrayAdapter<Game> {
//    public GameAdapter(Context context, ArrayList<Game> games) {
//        super(context, 0, games);
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        Game game = getItem(position);
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_game, parent, false);
//        }
//        TextView tvId = convertView.findViewById(R.id.tv_gameId);
//        TextView tvQ1 = convertView.findViewById(R.id.tv_question_1);
//        TextView tvQ2 = convertView.findViewById(R.id.tv_question_2);
//        TextView tvQ3 = convertView.findViewById(R.id.tv_question_3);
//        TextView tvQ4 = convertView.findViewById(R.id.tv_question_4);
//        TextView tvQ5 = convertView.findViewById(R.id.tv_question_5);
//        TextView tvScore = convertView.findViewById(R.id.tv_score);
//        tvId.append(String.valueOf(game.user_id));
//        tvQ1.append(String.valueOf(game.question_1));
//        tvQ2.append(String.valueOf(game.question_2));
//        tvQ3.append(String.valueOf(game.question_3));
//        tvQ4.append(String.valueOf(game.question_4));
//        tvQ5.append(String.valueOf(game.question_5));
//        tvScore.append(game.score);
//        return convertView;
//    }

    public GameAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Game> objects) {
        super(context,resource,objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Game game = getItem(position);
        convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_game
                , parent, false);

        TextView tvId = convertView.findViewById(R.id.tv_gameId);
//        TextView tvQ1 = convertView.findViewById(R.id.tv_question_1);
//        TextView tvQ2 = convertView.findViewById(R.id.tv_question_2);
//        TextView tvQ3 = convertView.findViewById(R.id.tv_question_3);
//        TextView tvQ4 = convertView.findViewById(R.id.tv_question_4);
//        TextView tvQ5 = convertView.findViewById(R.id.tv_question_5);
        TextView tvScore = convertView.findViewById(R.id.tv_score);
        tvId.append(String.valueOf(game.game_id));
//        tvQ1.append(String.valueOf(game.question_1));
//        tvQ2.append(String.valueOf(game.question_2));
//        tvQ3.append(String.valueOf(game.question_3));
//        tvQ4.append(String.valueOf(game.question_4));
//        tvQ5.append(String.valueOf(game.question_5));
        tvScore.append(String.valueOf(game.score));
        return convertView;
    }
}