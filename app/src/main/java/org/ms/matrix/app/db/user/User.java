package org.ms.matrix.app.db.user;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@Entity(tableName = "_matrix_user")
public class User {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int _id;
    @ColumnInfo(name = "_username")
    private String _username;
    @ColumnInfo(name = "_password")
    private String _password;
    @ColumnInfo(name = "_user_id")
    private String _user_id;
    @ColumnInfo(name = "_home_server")
    private String _home_server;
    @ColumnInfo(name = "_access_token")
    private String _access_token;
    @ColumnInfo(name = "_device_id")
    private String _device_id;
    @ColumnInfo(name = "_timestamp")
    private long _timestamp;


    public User() {
        _timestamp = System.currentTimeMillis();
    }
}
