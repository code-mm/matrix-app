package org.ms.matrix.app.db.matrix;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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
@Entity(tableName = "_buddy_user")
public class BuddyUser {


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int _id;

    @ColumnInfo(name = "_user_id")
    private String user_id;

    @ColumnInfo(name = "displayname")
    public String displayname;
    @ColumnInfo(name = "avatar_url")
    public String avatar_url;
    @ColumnInfo(name = "presence")
    public String presence;
    @ColumnInfo(name = "currently_active")
    public Boolean currently_active;
    @ColumnInfo(name = "lastActiveAgo")
    public Long lastActiveAgo;
    @ColumnInfo(name = "statusMsg")
    public String statusMsg;

}
