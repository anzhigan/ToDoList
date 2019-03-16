package anzhigun.ru.todo_test;

import android.app.Application;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

//здесь реализуем методы работы с БД
public class ViewModel extends AndroidViewModel {
    private Repository repository;
    private LiveData<List<Task>> allNotes;

    public ViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        allNotes = repository.getAllNotes();
    }

    public void insert(Task task){
        repository.insert(task);
    }

    public void update(Task task){
        repository.update(task);
    }

    public void delete(Task task){
        repository.delete(task);
    }

    public void deleteAllNotes(){
        repository.deleteAllNotes();
    }

    public LiveData<List<Task>> getAllNotes(){
        return allNotes;
    }
}
