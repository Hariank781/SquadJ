package com.example.squadject;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class JoinAdapter extends RecyclerView.Adapter<JoinAdapter.ViewHolder> {

    private List<JoinItem> joinItems;

    public JoinAdapter(List<JoinItem> joinItems) {
        this.joinItems = joinItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_join, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JoinItem item = joinItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return joinItems.size();
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageJF);
            nameTextView = itemView.findViewById(R.id.nameJF);
            emailTextView = itemView.findViewById(R.id.emailJF);
            phoneTextView = itemView.findViewById(R.id.phoneJF);
            collegeTextView = itemView.findViewById(R.id.collegeJF);
            branchTextView = itemView.findViewById(R.id.branchJF);
            semesterTextView = itemView.findViewById(R.id.semesterJF);
            skillsTextView = itemView.findViewById(R.id.skillsJF);
        }

        public void bind(JoinItem item) {
            // Set data to views here
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