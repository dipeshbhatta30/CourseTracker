package com.example.coursetracker.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.coursetracker.model.CalendarTask;
import com.example.coursetracker.model.Course;

@Database(entities = {Course.class, CalendarTask.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;
    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE courses ADD COLUMN credits INTEGER NOT NULL DEFAULT 3");
            database.execSQL("ALTER TABLE courses ADD COLUMN letterGrade TEXT DEFAULT 'N/A'");
        }
    };
    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `calendar_tasks` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT, `status` TEXT, `taskDate` TEXT)");
        }
    };

    public abstract CourseDao courseDao();
    public abstract TaskDao taskDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "course_db"
                    )
                    .addMigrations(MIGRATION_1_2)
                    .addMigrations(MIGRATION_2_3)
                    .build();
        }
        return instance;
    }
}