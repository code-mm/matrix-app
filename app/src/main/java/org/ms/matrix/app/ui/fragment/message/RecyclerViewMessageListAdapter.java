package org.ms.matrix.app.ui.fragment.message;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.ms.matrix.app.R;
import org.ms.matrix.app.ui.activity.chat.ChatActivity;
import org.w3c.dom.Text;

import java.util.List;

public class RecyclerViewMessageListAdapter extends RecyclerView.Adapter<RecyclerViewMessageListAdapter.ViewHolder> {


    private Context context;
    private List<RecyclerViewMessageListBean> recyclerViewMessageListBeans;


    public RecyclerViewMessageListAdapter(Context context, List<RecyclerViewMessageListBean> recyclerViewMessageListBeans) {
        this.context = context;
        this.recyclerViewMessageListBeans = recyclerViewMessageListBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = View.inflate(context, R.layout.item_recycler_view_message_list, null);

        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewRoomName.setText(recyclerViewMessageListBeans.get(position).getRoomName());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("roomid", recyclerViewMessageListBeans.get(position).getRoomId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return recyclerViewMessageListBeans.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewRoomAvatar;
        private TextView textViewRoomName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewRoomAvatar = itemView.findViewById(R.id.imageViewRoomAvatar);
            textViewRoomName = itemView.findViewById(R.id.textViewRoomName);
        }
    }
}
