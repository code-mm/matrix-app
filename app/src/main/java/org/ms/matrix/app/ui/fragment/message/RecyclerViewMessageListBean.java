package org.ms.matrix.app.ui.fragment.message;


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
public class RecyclerViewMessageListBean {

    // 房间ID
    private String roomId;

    // 房间名称
    private String roomName;

    // 房间头像
    private String roomAvatar;
    // 最后一条消息
    private String latestMessage;
    // 未读数
    private int latestMessageUnreadNumber;


}
