package com.minor.attendancemanager2.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.minor.attendancemanager2.R;

import java.util.ArrayList;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {
    ArrayList<SubjectModel> subjectModelArrayList;
    Context context;
    SubjectClickInterface subjectClickInterface;
    int lastpos = -1;

    public SubjectAdapter(ArrayList<SubjectModel> subjectModelArrayList,Context context,SubjectClickInterface subjectClickInterface){
        this.subjectModelArrayList = subjectModelArrayList;
        this.context = context;
        this.subjectClickInterface = subjectClickInterface;
    }

    @NonNull
    @Override
    public SubjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.subject_rv_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectAdapter.ViewHolder holder, int position) {
        SubjectModel subjectModel = subjectModelArrayList.get(position);
        holder.tvSubject.setText("Subject: " + subjectModel.getSubject());
        holder.tvLecture.setText("Lecture: " + subjectModel.getLecture());
        setAnimation(holder.itemView, position);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subjectClickInterface.onSubjectClick(position);
            }
        });
    }

    private void setAnimation(View itemView, int position) {
        if (position > lastpos) {
            // on below line we are setting animation.
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            itemView.setAnimation(animation);
            lastpos = position;
        }
    }

    @Override
    public int getItemCount() {
        return subjectModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvSubject,tvLecture;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLecture = itemView.findViewById(R.id.rvNum);
            tvSubject = itemView.findViewById(R.id.rvSubject);
            linearLayout = itemView.findViewById(R.id.llrow);
        }
    }

    public interface SubjectClickInterface{
        void onSubjectClick(int pos);
    }

}