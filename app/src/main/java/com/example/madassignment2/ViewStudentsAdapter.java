package com.example.madassignment2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madassignment2.Database.Student;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class ViewStudentsAdapter extends RecyclerView.Adapter<ViewStudentsAdapter.ViewHolder> {
    private List<Student> studentList;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;
    private Context thisContent;

    //Student adapter for the recycler view
    ViewStudentsAdapter(Context context, List<Student> studentList) {
        this.inflater = LayoutInflater.from(context);
        this.studentList = studentList;
        thisContent = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_layout_student, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String firstName = studentList.get(position).getFirstName();
        String lastName = studentList.get(position).getLastName();
        holder.studentFName.setText(firstName);
        holder.studentLName.setText(lastName);

        byte[] b = Base64.decode(studentList.get(position).getStudentPicture(), Base64.DEFAULT);
        Bitmap imageBitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        holder.studentPicture.setImageBitmap(imageBitmap);

    }

    public void clear() {
        int size = studentList.size();
        studentList.clear();
        notifyItemRangeRemoved(0, size);
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView studentFName;
        TextView studentLName;
        ImageView studentPicture;
        LinearLayout linearLayout;

        ViewHolder(View itemView) {
            //single student for the recycler view
            super(itemView);
            studentFName = itemView.findViewById(R.id.textViewStudentFName);
            studentLName = itemView.findViewById(R.id.textViewStudentLName);
            studentPicture = itemView.findViewById(R.id.flagViewStudentPic);
            studentFName.setOnClickListener(this);
            studentLName.setOnClickListener(this);
            studentPicture.setOnClickListener(this);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayoutStudent);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClickStudent(view, getAdapterPosition());
        }
    }

    public Student getItem(int id) {
        return studentList.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClickStudent(View view, int position);
    }
}

