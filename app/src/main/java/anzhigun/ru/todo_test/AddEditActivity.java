package anzhigun.ru.todo_test;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Objects;
import androidx.appcompat.app.AppCompatActivity;

public class AddEditActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_ID =
            "ru.anzhigun.todo_test.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "ru.anzhigun.todo_test.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "ru.anzhigun.todo_test.EXTRA_DESCRIPTION";
    public static final String EXTRA_STARS =
            "ru.anzhigun.todo_test.EXTRA_STARS";
    public static final String EXTRA_DATEANDTIME =
            "ru.anzhigun.todo_test.EXTRA_DATEANDTIME";

    //создаём поля, которые будет заполнять пользователь
    //при добавлении новой задачи в AddEditActivity
    private EditText editTextTitle;
    private EditText editTextDescription;
    private String dataOfDateAndTime;
    Calendar dateAndTime = Calendar.getInstance();
    Button dateButton;
    Button timeButton;
    Button notificationButton;
    private RatingBar ratingbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        //Находим наши вьюхи
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        ratingbar = (RatingBar) findViewById(R.id.ratingBar);
        dateButton = (Button) findViewById(R.id.dateButton);
        timeButton = (Button) findViewById(R.id.timeButton);
        notificationButton = (Button) findViewById(R.id.notificationButton);
        dateButton.setOnClickListener(this);
        timeButton.setOnClickListener(this);
        notificationButton.setOnClickListener(this);

        setInitialDateTime();

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Добавить Задачу");

        Intent intent = getIntent();

        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Изменить Задачу");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
        }
    }

    private void saveNote(){

        //если поле title не заполненно
        if(editTextTitle.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Введите задачу", Toast.LENGTH_SHORT).show();
            return;
        }

        //если поле dataOfDateAndTime не заполненно
        if(dataOfDateAndTime.trim().isEmpty()){
            Toast.makeText(this, "Введите время выполнения", Toast.LENGTH_SHORT).show();
            return;
        }

        //Возвращаем запрошенные MainActivity данные в MainActivity
        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, editTextTitle.getText().toString());
        data.putExtra(EXTRA_DESCRIPTION, editTextDescription.getText().toString());
        data.putExtra(EXTRA_STARS, ratingbar.getRating());
        data.putExtra(EXTRA_DATEANDTIME, dataOfDateAndTime);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if(id != 1){
            data.putExtra(EXTRA_ID, id);
        }

        startAlarm(dateAndTime);

        setResult(RESULT_OK, data);
        finish();

    }

    //устанавливаем будильник
    private void startAlarm(Calendar dateAndTime){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, Receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, dateAndTime.getTimeInMillis(), pendingIntent);
    }

    // перевод начальных даты и времени в String
    private void setInitialDateTime() {
        dataOfDateAndTime = DateUtils.formatDateTime(this, dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE
                | DateUtils.FORMAT_SHOW_YEAR
                | DateUtils.FORMAT_SHOW_TIME);
    }

    // установка обработчика выбора времени
    TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            setInitialDateTime();
        }
    };

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dateButton:
                new DatePickerDialog(AddEditActivity.this, d,
                        dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH),
                        dateAndTime.get(Calendar.DAY_OF_MONTH))
                        .show();
                break;
            case R.id.timeButton:
                new TimePickerDialog(AddEditActivity.this, t,
                            dateAndTime.get(Calendar.HOUR_OF_DAY),
                            dateAndTime.get(Calendar.MINUTE), true)
                            .show();
                break;
            case R.id.notificationButton:
                    startAlarm(dateAndTime);
                    Toast.makeText(this, "Мы напомним вам о Задаче", Toast.LENGTH_SHORT).show();
                break;

            }
    }

    //создём меню для сохранения новой Задачи
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    //когда пользователь нажмёт на иконку "сохранить"
    //вызовется метод saveNote()
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_note:
                saveNote();
                Toast.makeText(this, Float.toString(ratingbar.getRating()), Toast.LENGTH_SHORT).show();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }



}

