package org.ms.matrix.app.ui.fragment.room;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.ms.matrix.app.R;
import org.ms.matrix.app.db.matrix.MatrixRoom;

import java.util.List;

public class RecyclerViewRoomsAdapter extends RecyclerView.Adapter<RecyclerViewRoomsAdapter.ViewHolder> {


    private Context context;
    private List<MatrixRoom> matrixRooms;


    public RecyclerViewRoomsAdapter(Context context, List<MatrixRoom> matrixRooms) {
        this.context = context;
        this.matrixRooms = matrixRooms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_recycler_view_room_list, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MatrixRoom room = matrixRooms.get(position);

        if (room.get_room_displayname() != null) {
            holder.textViewRoomName.setText(room.get_room_displayname()+"");
        }


    }

    @Override
    public int getItemCount() {
        return matrixRooms.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewRoomName;
        private ImageView imageViewRoomAvatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRoomName=itemView.findViewById(R.id.textViewRoomName);
            imageViewRoomAvatar=itemView.findViewById(R.id.imageViewRoomAvatar);
        }
    }
}
