package anzhigun.ru.todo_test;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MainActivity extends AppCompatActivity{
    //Создаём модель нашего view
    private ViewModel viewModel;
    private final int ADD_TASK_REQUEST = 1;
    private final int EDIT_TASK_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //реализуем кнопку добавления задачи
        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                startActivityForResult(intent, ADD_TASK_REQUEST);
            }
        });

        //Создаем recyclerView и адаптер для него
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        final AdapterForRecycleView adapterForRecycleView = new AdapterForRecycleView();
        recyclerView.setAdapter(adapterForRecycleView);

        //инициализируем модель view, передавая текущий activity
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        viewModel.getAllNotes().observe(this, new Observer<List<Task>>() {
            //чекаем все изменения в activity
            @Override
            public void onChanged(@Nullable List<Task> tasks) {
                adapterForRecycleView.setTasks(tasks);
            }
        });

        //реализуем элементы свайпа для удаления
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                viewModel.delete(adapterForRecycleView.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Задача выполнена!", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapterForRecycleView.setOnItemClickListener(new AdapterForRecycleView.OnItemClickListener() {
            @Override
            public void onItemClick(Task task) {
                Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                intent.putExtra(AddEditActivity.EXTRA_ID, task.getId());
                intent.putExtra(AddEditActivity.EXTRA_TITLE, task.getTitle());
                intent.putExtra(AddEditActivity.EXTRA_DESCRIPTION, task.getDescription());
                intent.putExtra(AddEditActivity.EXTRA_STARS, task.getPriority());
                intent.putExtra(AddEditActivity.EXTRA_DATEANDTIME, task.getDateandtime());
                startActivityForResult(intent, EDIT_TASK_REQUEST);
            }
        });
    }


    //реализуем получение данных из AddEditActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //вызываем AddEditActivity для получения данных
        //благодаря onActivityResult
        if(requestCode == ADD_TASK_REQUEST && resultCode == RESULT_OK){
            String title = data.getStringExtra(AddEditActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditActivity.EXTRA_DESCRIPTION);
            String dateAndTime = data.getStringExtra(AddEditActivity.EXTRA_DATEANDTIME);
            float priority = data.getFloatExtra(AddEditActivity.EXTRA_STARS, 322);

            //добавляем новый Task
            Task task = new Task(title, description, dateAndTime, priority);
            viewModel.insert(task);

            Toast.makeText(this, "Задача сохранена", Toast.LENGTH_SHORT).show();
        }else if((requestCode == EDIT_TASK_REQUEST && resultCode == RESULT_OK)) {
            int id = data.getIntExtra(AddEditActivity.EXTRA_ID, -1);
            if(id == -1){
                Toast.makeText(this, "Задача не может быть обновлена", Toast.LENGTH_LONG).show();
                return;
            }

            String title = data.getStringExtra(AddEditActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditActivity.EXTRA_DESCRIPTION);
            String dateAndTime = data.getStringExtra(AddEditActivity.EXTRA_DATEANDTIME);
            int priority = data.getIntExtra(AddEditActivity.EXTRA_STARS, 1);

            Task task = new Task(title, description, dateAndTime, priority);
            task.setId(id);
            viewModel.update(task);
            Toast.makeText(this, "Задача обновлена", Toast.LENGTH_LONG).show();
        } else{
            Toast.makeText(this, "Задача не сохранена", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all_notes:
                viewModel.deleteAllNotes();
                Toast.makeText(this, "Все задачи удалены", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //Реализуем уведомления



}
