package net.chmielowski.fiszki;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.Stream;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;

class WordsAdapter extends RecyclerView.Adapter<WordViewHolder> {

    private final List<WordGroup> words;

    WordsAdapter(Realm realm) {
        words = Stream.of(
                realm.where(WordGroup.class)
                     .findAllSorted(WordGroup.SCORE, Sort.DESCENDING))
                      .toList();
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                                        .inflate(R.layout.item_word, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        holder.bind(words.get(position));
    }

    @Override
    public int getItemCount() {
        return words.size();
    }
}
