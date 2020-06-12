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
@Entity(tableName = "_matrix_message_list")
public class MessageList {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    public int _id;

    @ColumnInfo(name = "_room_id")
    public String _room_id;

    // 消息类型
    @ColumnInfo(name = "_type")
    public String _type;

    @ColumnInfo(name = "_body")
    public String _body;

    // 发送者
    @ColumnInfo(name = "_sender")
    public String _sender;

    // 服务器时间
    @ColumnInfo(name = "_originserverts")
    public long _originserverts;

    // 存入时间
    @ColumnInfo(name = "_timestamp")
    public long _timestamp;


    // 是否已读
    @ColumnInfo(name = "_read")
    public boolean _read;


    @ColumnInfo(name = "_event_id")
    public String event_id;


}



