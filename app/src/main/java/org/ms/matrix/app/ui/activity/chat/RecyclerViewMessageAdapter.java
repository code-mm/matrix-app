package org.ms.matrix.app.ui.activity.chat;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.ms.matrix.app.MatrixClient;
import org.ms.matrix.app.R;
import org.ms.matrix.app.db.matrix.MessageList;

import java.util.List;

public class RecyclerViewMessageAdapter extends RecyclerView.Adapter {


    private Context context;

    private List<MessageBean> messageBeanList;


    public RecyclerViewMessageAdapter(Context context, List<MessageBean> messageBeanList) {
        this.context = context;
        this.messageBeanList = messageBeanList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        switch (viewType) {

            case 0:
                return new ViewHolderMe(View.inflate(context, R.layout.item_recycler_view_message_me, null));
            case 1:
                return new ViewHolderOtherPeople(View.inflate(context, R.layout.item_recycler_view_message_otherpeople, null));

        }


        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int itemViewType = getItemViewType(position);


        if (itemViewType == 0) {

            ViewHolderMe holder1 = (ViewHolderMe) holder;
            holder1.textViewContent.setText(messageBeanList.get(position).getContent());
        } else {
            ViewHolderOtherPeople holder1 = (ViewHolderOtherPeople) holder;
            holder1.textViewContent.setText(messageBeanList.get(position).getContent());
        }


    }

    @Override
    public int getItemViewType(int position) {

        MessageBean messageBean = messageBeanList.get(position);

        if (messageBean.getSender().equals(MatrixClient.getInstance().getUserId())) {
            return 0;
        } else {
            return 1;
        }

    }

    @Override
    public int getItemCount() {
        return messageBeanList.size();
    }

    static class ViewHolderMe extends RecyclerView.ViewHolder {

        private TextView textViewContent;

        public ViewHolderMe(@NonNull View itemView) {
            super(itemView);
            textViewContent = itemView.findViewById(R.id.textViewContent);
        }
    }

    static class ViewHolderOtherPeople extends RecyclerView.ViewHolder {
        private TextView textViewContent;

        public ViewHolderOtherPeople(@NonNull View itemView) {
            super(itemView);
            textViewContent = itemView.findViewById(R.id.textViewContent);
        }
    }
}