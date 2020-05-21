package si.uni_lj.fri.pbd.miniapp3.database;

/*
 * DATABASE
 *
 * A helper class for database instantiation and access
 *
 */

import android.content.Context;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import si.uni_lj.fri.pbd.miniapp3.Constants;
import si.uni_lj.fri.pbd.miniapp3.database.dao.RecipeDao;
import si.uni_lj.fri.pbd.miniapp3.database.entity.RecipeDetails;

@androidx.room.Database(entities = {RecipeDetails.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {

    // a DAO reference
    public abstract RecipeDao recipeDao();
    private static volatile Database INSTANCE;

    public static Database getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (Database.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            Database.class, Constants.DB_NAME)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
