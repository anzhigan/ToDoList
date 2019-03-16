package anzhigun.ru.todo_test;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterForRecycleView extends RecyclerView.Adapter<AdapterForRecycleView.NoteHolder> {
    private final String LOGDIMA = "LogDima";
    private List<Task> tasks = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(Task task);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    class NoteHolder extends RecyclerView.ViewHolder{
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewPriority;
        private TextView textDateAndTime;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textDateAndTime = itemView.findViewById(R.id.text_view_dateandtime);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(tasks.get(position));
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Task currentTask = tasks.get(position);
        try{
            holder.textViewTitle.setText(currentTask.getTitle());
            holder.textViewDescription.setText(currentTask.getDescription());
            holder.textDateAndTime.setText(currentTask.getDateandtime());
            holder.textViewPriority.setText(String.valueOf(currentTask.getPriority()));
        }catch (NullPointerException e){
            Log.d(LOGDIMA, String.valueOf(e.getStackTrace()));
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(List<Task> tasks){
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public Task getNoteAt(int position){
        return tasks.get(position);
    }


}
