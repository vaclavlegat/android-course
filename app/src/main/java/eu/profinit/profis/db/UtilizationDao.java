package eu.profinit.profis.db;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import eu.profinit.profis.model.UtilizationItem;

@Dao
public interface UtilizationDao {
    @Query("SELECT * FROM utilization order by date desc")
    List<UtilizationItem> getAll();

    @Query("SELECT * FROM utilization WHERE id = :id")
    UtilizationItem getById(long id);

    @Insert
    void insert(UtilizationItem profisItem);

    @Delete
    void delete(UtilizationItem profisItem);
}
