package net.chmielowski.fiszki;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

class WordViewHolder extends RecyclerView.ViewHolder {
    WordViewHolder(View view) {
        super(view);
    }

    void bind(WordGroup group) {
        bindField(R.id.word, group.words.first().foreign);
        bindField(R.id.score, String.valueOf(group.score));
    }

    private void bindField(int view, String text) {
        final TextView textView = itemView.findViewById(view);
        textView.setText(text);
    }
}
