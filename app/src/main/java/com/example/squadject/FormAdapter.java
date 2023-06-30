package com.example.squadject;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

// FormAdapter.java
public class FormAdapter extends RecyclerView.Adapter<FormAdapter.ViewHolder> {

    private List<FormItem> formItems;
    private OnInviteClickListener inviteClickListener;

    public FormAdapter(List<FormItem> formItems, OnInviteClickListener inviteClickListener) {
        this.formItems = formItems;
        this.inviteClickListener = inviteClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_form, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FormItem item = formItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return formItems.size();
    }

    public interface OnInviteClickListener {
        void onInviteClick(String email);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView nameTextView;
        private TextView emailTextView;
        private TextView phoneTextView;
        private TextView collegeTextView;
        private TextView branchTextView;
        private TextView semesterTextView;
        private TextView skillsTextView;
        private Button sendInviteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageFJ);
            nameTextView = itemView.findViewById(R.id.nameFJ);
            emailTextView = itemView.findViewById(R.id.emailFJ);
            phoneTextView = itemView.findViewById(R.id.phoneFJ);
            collegeTextView = itemView.findViewById(R.id.collegeFJ);
            branchTextView = itemView.findViewById(R.id.branchFJ);
            semesterTextView = itemView.findViewById(R.id.semesterFJ);
            skillsTextView = itemView.findViewById(R.id.skillsFJ);
            sendInviteButton = itemView.findViewById(R.id.sendInvite);

            sendInviteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    FormItem item = formItems.get(position);
                    String email = item.getEmail();
                    inviteClickListener.onInviteClick(email);
                }
            });
        }

        public void bind(FormItem item) {
            Picasso.get().load(item.getProfilePicture()).into(imageView);
            nameTextView.setText(item.getFullName());
            emailTextView.setText(item.getEmail());
            phoneTextView.setText(item.getPhoneNumber());
            collegeTextView.setText(item.getCollege());
            branchTextView.setText(item.getBranch());
            semesterTextView.setText(item.getSemester());
            skillsTextView.setText(item.getSkills());
        }
    }
}