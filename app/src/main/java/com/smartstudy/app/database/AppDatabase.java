//package com.smartstudy.app.database;
//
//import android.content.Context;
//
//import androidx.room.Database;
//import androidx.room.Room;
//import androidx.room.RoomDatabase;
//
//@Database(
//        entities = {NoteEntity.class, HighlightEntity.class},
//        version = 1,
//        exportSchema = false
//)
//public abstract class AppDatabase extends RoomDatabase {
//
//    private static volatile AppDatabase INSTANCE;
//
//    public abstract NoteDao noteDao();
//    public abstract HighlightDao highlightDao();
//
//    public static AppDatabase getInstance(Context context) {
//        if (INSTANCE == null) {
//            synchronized (AppDatabase.class) {
//                if (INSTANCE == null) {
//                    INSTANCE = Room.databaseBuilder(
//                                    context.getApplicationContext(),
//                                    AppDatabase.class,
//                                    "smartstudy_db"
//                            )
//                            .allowMainThreadQueries() // ✅ okay for uni project; later you can improve
//                            .build();
//                }
//            }
//        }
//        return INSTANCE;
//    }
//
//
//}

package com.smartstudy.app.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(
        entities = {
                NoteEntity.class,
                HighlightEntity.class
        },
        version = 2, // 🔴 bumped version because we added documentUri
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract NoteDao noteDao();
    public abstract HighlightDao highlightDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "smartstudy_db"
                            )
                            // ✅ REQUIRED because schema changed
                            .fallbackToDestructiveMigration()

                            // ✅ OK for university project
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
