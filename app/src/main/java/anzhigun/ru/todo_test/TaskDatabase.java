package anzhigun.ru.todo_test;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Task.class},version = 1,exportSchema = false)
public abstract class TaskDatabase extends RoomDatabase {

    private static TaskDatabase instance;
    public abstract DaoTask taskDao();

    // инициализируем и отправляем экземпляр TaskDatabase в Repository
    public static synchronized TaskDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TaskDatabase.class, "task_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private DaoTask daoTask;
        private PopulateDbAsyncTask(TaskDatabase db){
            //создаём daoTask
            daoTask = db.taskDao();
        }

        //Заполняем при первом включении
        @Override
        protected Void doInBackground(Void... voids) {
            daoTask.insert(new Task("Позвонить маме", "smth", "12:30",1));
            daoTask.insert(new Task("Накормить кота", "smth", "15:16",5));
            daoTask.insert(new Task("Болеть за Реал", "smth", "1 марта, 23:00",6));
            return null;
        }
    }
}
