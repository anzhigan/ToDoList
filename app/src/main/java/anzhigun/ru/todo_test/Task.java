package anzhigun.ru.todo_test;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

//создаём таблицу в БД через Room
@Entity(tableName = "task_table")
public class Task {
    //рандомные ключи
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private String dateandtime;
    private float priority;
    //устанавливаем значения в поля через конструктор

    public Task(String title, String description, String dateandtime, float priority) {
        this.title = title;
        this.description = description;
        this.dateandtime = dateandtime;
        this.priority = priority;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public String getDateandtime() {
        return dateandtime;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public float getPriority() {
        return priority;
    }
}
