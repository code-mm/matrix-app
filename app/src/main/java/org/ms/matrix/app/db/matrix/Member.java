package org.ms.matrix.app.db.matrix;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(tableName = "_member")
public class Member {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int _id;

    @ColumnInfo(name = "_room_id")
    private String _room_id;

    @ColumnInfo(name = "_member_id")
    private String _member_id;

    @ColumnInfo(name = "_avatar_url")
    private String _avatar_url;

    @ColumnInfo(name = "_displayname")
    private String _displayname;

    @ColumnInfo(name = "_name")
    private String _name;

}
