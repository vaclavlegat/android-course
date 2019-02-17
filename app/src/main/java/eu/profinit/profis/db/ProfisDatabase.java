package eu.profinit.profis.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import eu.profinit.profis.model.UtilizationItem;

@Database(entities = {UtilizationItem.class}, version = 1)
@TypeConverters({DateTypeConverter.class})
public abstract class ProfisDatabase extends RoomDatabase {
    public abstract UtilizationDao utilizationDao();

    private static ProfisDatabase db;

    public static ProfisDatabase getInstance(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(context, ProfisDatabase.class, "profis-db").build();
        }

        return db;
    }
}
