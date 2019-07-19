package pt.jdinis.musicplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static pt.jdinis.musicplayer.PlayerService.SONG_ITEM;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {
    private static ArrayList<SongItem> songItems;
    private static SongHolder songHolder;

    public SongAdapter(ArrayList<SongItem> songItems) {
        SongAdapter.songItems = songItems;
    }

    public static int getItemsCount() {
        return songItems.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item, parent, false);
        songHolder = new SongHolder(v);
        return songHolder;
    }

    public SongItem getSongItem() {
        return songItems.get(songHolder.getAdapterPosition());
    }

    @Override
    public void onBindViewHolder(@NonNull SongHolder holder, int position) {
        SongItem currentSongItem = songItems.get(position);

        holder.songTitle.setText(currentSongItem.getTitle());
        holder.songAuthor.setText(currentSongItem.getArtist());
    }

    @Override
    public int getItemCount() {
        return songItems.size();
    }

    public static class SongHolder extends RecyclerView.ViewHolder {
        private static MediaPlayer mp;
        public ImageView songImageView;
        public TextView songTitle, songAuthor;

        @RequiresApi(api = Build.VERSION_CODES.O)
        public SongHolder(@NonNull final View itemView) {
            super(itemView);
            songImageView = itemView.findViewById(R.id.songImageView);
            songTitle = itemView.findViewById(R.id.songTitle);
            songAuthor = itemView.findViewById(R.id.songAuthor);

            itemView.setOnClickListener((v) -> {
                Intent startIntent = new Intent(v.getContext(), MusicPlayerActivity.class);
                startIntent.putExtra(SONG_ITEM, songItems.get(getAdapterPosition()));
                if (getAdapterPosition() - 1 > 0 && songItems.get(getAdapterPosition() - 1) != null)
                    startIntent.putExtra(SONG_ITEM + 1, songItems.get(getAdapterPosition()));
                if (getAdapterPosition() + 1 < getItemsCount() && songItems.get(getAdapterPosition() + 1) != null)
                    startIntent.putExtra(SONG_ITEM + 2, songItems.get(getAdapterPosition()));
                itemView.getContext().startActivity(startIntent);
            });
        }
    }
}
