package anzhigun.ru.todo_test;

import android.app.Application;
import android.os.AsyncTask;
import java.util.List;
import androidx.lifecycle.LiveData;


public class Repository {
    private DaoTask daoTask;
    private LiveData<List<Task>> allNotes;

    public Repository(Application app){
        //создаём БД
        TaskDatabase taskDatabase = TaskDatabase.getInstance(app);
        //инициализируем daoTask через созданную ранее БД
        daoTask = taskDatabase.taskDao();
        //заполняем соданный список Задач через daoTask
        allNotes = daoTask.getAllNotes();
    }

    public void insert(Task task){
        new InsertNoteAsyncTask(daoTask).execute(task);
    }

    public void update(Task task){
        new UpdateNoteAsyncTask(daoTask).execute(task);
    }

    public void delete(Task task){
        new DeleteNoteAsyncTask(daoTask).execute(task);
    }

    public void deleteAllNotes(){
        new DeleteAllNoteAsyncTask(daoTask).execute();
    }

    //передаём данные во ViewModel
    public LiveData<List<Task>> getAllNotes(){
        return allNotes;
    }

    private static class InsertNoteAsyncTask extends AsyncTask<Task, Void, Void>{
        private DaoTask daoTask;

        private InsertNoteAsyncTask(DaoTask daoTask){
            this.daoTask = daoTask;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            daoTask.insert(tasks[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Task, Void, Void>{
        private DaoTask daoTask;

        private UpdateNoteAsyncTask(DaoTask daoTask){
            this.daoTask = daoTask;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            daoTask.update(tasks[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Task, Void, Void>{

        private DaoTask daoTask;

        private DeleteNoteAsyncTask(DaoTask daoTask){
            this.daoTask = daoTask;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            daoTask.delete(tasks[0]);
            return null;
        }
    }

    private static class DeleteAllNoteAsyncTask extends AsyncTask<Void, Void, Void>{
        private DaoTask daoTask;
        private DeleteAllNoteAsyncTask(DaoTask daoTask){
            this.daoTask = daoTask;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            daoTask.deleteAllNotes();
            return null;
        }
    }

}
