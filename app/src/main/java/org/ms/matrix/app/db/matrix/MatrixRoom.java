package org.ms.matrix.app.db.matrix;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity(tableName = "_matrix_room")
public class MatrixRoom {


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int _id;

    @ColumnInfo(name = "_room_id")
    private String _room_id;
    @ColumnInfo(name = "_room_avatar")
    private String _room_avatar;
    @ColumnInfo(name = "_room_aliases")
    String _room_aliases;

    @ColumnInfo(name = "_room_displayname")
    String _room_displayname;
}
