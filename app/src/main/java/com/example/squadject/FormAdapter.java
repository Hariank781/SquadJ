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

public class FormAdapter extends RecyclerView.Adapter<FormAdapter.ViewHolder> {

    private List<FormItem> formItems;

    public FormAdapter(List<FormItem> formItems) {
        this.formItems = formItems;
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
            imageView = itemView.findViewById(R.id.imageFJ);
            nameTextView = itemView.findViewById(R.id.nameFJ);
            emailTextView = itemView.findViewById(R.id.emailFJ);
            phoneTextView = itemView.findViewById(R.id.phoneFJ);
            collegeTextView = itemView.findViewById(R.id.collegeFJ);
            branchTextView = itemView.findViewById(R.id.branchFJ);
            semesterTextView = itemView.findViewById(R.id.semesterFJ);
            skillsTextView = itemView.findViewById(R.id.skillsFJ);
        }

        public void bind(FormItem item) {
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