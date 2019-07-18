package buildnlive.com.buildlive.adapters;

import android.app.AlertDialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import buildnlive.com.buildlive.R;
import buildnlive.com.buildlive.elements.Notification;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(Notification notification, int pos, View view);
    }

    private final List<Notification> items;
    private Context context;
    private final OnItemClickListener listener;

    public NotificationsAdapter(Context context, ArrayList<Notification> users, OnItemClickListener listener) {
        this.items = users;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public NotificationsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_notification, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(context, items.get(position), position, listener);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, quantity, label, details, date;
        private ImageView imageView;
        private LinearLayout buttons, reply, issue;
        private Button approve, reject, review, receive, notReceive, read, image, issued;
        private AlertDialog.Builder builder;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.level);
            details = view.findViewById(R.id.details);
            label = view.findViewById(R.id.name);
            title = view.findViewById(R.id.title);
            date = view.findViewById(R.id.date);
//            quantity = view.findViewById(R.id.quantity);
            buttons = view.findViewById(R.id.buttons);
            approve = view.findViewById(R.id.approve);
            image = view.findViewById(R.id.image);
            reject = view.findViewById(R.id.reject);
            review = view.findViewById(R.id.review);
            receive = view.findViewById(R.id.receive);
            notReceive = view.findViewById(R.id.not_receive);
            reply = view.findViewById(R.id.reply);
            read = view.findViewById(R.id.read_notification);
            issue = view.findViewById(R.id.issue);
            issued = view.findViewById(R.id.issued);
        }

        public void bind(final Context context, final Notification item, final int pos, final OnItemClickListener listener) {
            if (item.getLevel().equals("urgent")) {
                imageView.setVisibility(View.VISIBLE);
            }
            if (!item.getImage().isEmpty()) {
                image.setVisibility(View.VISIBLE);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onItemClick(item, pos, view);
                    }
                });
            }
            switch (item.getType()) {
                case "update":
                    reply.setVisibility(View.GONE);
                    buttons.setVisibility(View.GONE);
                    issue.setVisibility(View.GONE);
                    details.setText(item.getUser());
                    title.setText(item.getLabel());
                    label.setText(item.getText());
                    date.setText(item.getDate());
                    read.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onItemClick(item, pos, view);
                        }
                    });
                    break;
                case "issue":
                    reply.setVisibility(View.GONE);
                    read.setVisibility(View.GONE);
                    buttons.setVisibility(View.GONE);
                    details.setText(item.getUser());
                    title.setText(item.getLabel());
                    label.setText(item.getText());
                    date.setText(item.getDate());
                    issued.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onItemClick(item, pos, view);
                        }
                    });
                    break;
                case "reply":
                    issue.setVisibility(View.GONE);
                    read.setVisibility(View.GONE);
                    buttons.setVisibility(View.GONE);
                    details.setText(item.getUser());
                    title.setText(item.getLabel());
                    label.setText(item.getText());
                    date.setText(item.getDate());
                    notReceive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onItemClick(item, pos, view);
                        }

                    });
                    receive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onItemClick(item, pos, view);

                        }

                    });
                    break;
                case "approval":
                    issue.setVisibility(View.GONE);
                    read.setVisibility(View.GONE);
                    reply.setVisibility(View.GONE);
                    details.setText(item.getUser());
                    title.setText(item.getLabel());
                    label.setText(item.getText());
                    date.setText(item.getDate());
                    approve.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onItemClick(item, pos, view);
                        }

                    });
                    reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onItemClick(item, pos, view);
                        }

                    });
                    review.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onItemClick(item, pos, view);
                        }

                    });
                    break;
            }

        }
    }

}