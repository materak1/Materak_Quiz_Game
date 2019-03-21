package utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.materak_quiz_game.R;

import java.util.ArrayList;

public class GameAdapter extends ArrayAdapter<Game> {
    public GameAdapter(Context context, ArrayList<Game> games) {
        super(context, 0, games);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Game game = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_game, parent, false);
        }
        TextView tvId = (TextView) convertView.findViewById(R.id.tv_gameId);
        TextView tvQ1 = (TextView) convertView.findViewById(R.id.tv_question_1);
        TextView tvQ2 = (TextView) convertView.findViewById(R.id.tv_question_2);
        TextView tvQ3 = (TextView) convertView.findViewById(R.id.tv_question_3);
        TextView tvQ4 = (TextView) convertView.findViewById(R.id.tv_question_4);
        TextView tvQ5 = (TextView) convertView.findViewById(R.id.tv_question_5);
        TextView tvScore = (TextView) convertView.findViewById(R.id.tv_score);
        tvId.setText(game.user_id);
        tvQ1.setText(game.question_1);
        tvQ2.setText(game.question_2);
        tvQ3.setText(game.question_3);
        tvQ4.setText(game.question_4);
        tvQ5.setText(game.question_5);
        tvScore.setText(game.score);
        return convertView;
    }
}