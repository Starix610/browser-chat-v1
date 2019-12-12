window.app = {

    //服务器地址
    BASE_URL: "http://localhost:8080",
    BASE_WS_URL: "ws://localhost:8080",
    // BASE_URL: "http://139.196.102.109:9020",
    // BASE_WS_URL: "ws://139.196.102.109:9020",


    /**
     * 和后端的消息类型枚举对应
     */
    SINGLE: 0, 	// 单聊消息
    ROOM: 1,    // 聊天室消息
    GROUP: 2,   // 群聊消息
    SYSTEM: 3, 	// 系统消息
    FRIEND_REQUEST: 4,	// 好友请求
    STATUS_CHANGE: 5, //用户上下线通知消息
    CALL: 6,  //呼叫类型消息
    CALL_REPLY: 7, //呼叫回复消息


    // 消息对象
    MessageData: function (fromUid, username, toUid, content, type) {
        this.fromUid = fromUid;
        this.username = username;
        this.toUid = toUid;
        this.content = content;
        this.type = type;
        this.time = new Date().getTime();
    },



    /**
     * 判断字符串是否为空
     * @param {Object} str
     * true：不为空
     * false：为空
     */
    isNotNull: function(str) {
        if (str != null && str != "" && str != undefined) {
            return true;
        }
        return false;
    },


    /**
     * 聊天快照对象，保存朋友信息以及每次和朋友聊天的最后一条消息
     * @param {Object} friendId
     * @param {Object} msg
     * @param {Object} isRead	用于判断消息是否已读还是未读
     */
    ChatSnapshot: function(friendId, friendUsername, msg, isRead){
        this.friendId = friendId;
        this.friendUsername = friendUsername;
        this.msg = msg;
        this.isRead = isRead;
    },


    /**
     * 保存聊天用户列表
     * @param {Object} myId
     * @param {Object} friendId
     * @param {Object} msg
     * @param {Object} isRead
     */
    saveUserChatList: function(friendId, friendUsername, msg, isRead) {
        var chatKey = "chat-list-" + localStorage.getItem("uid");
        // 从本地缓存获取聊天list
        var chatListStr = localStorage.getItem(chatKey);
        var chatList;
        if (this.isNotNull(chatListStr)) {
            // 如果不为空
            chatList = JSON.parse(chatListStr);
            // 循环快照list，并且判断每个元素是否包含（匹配）friendId，如果匹配，则删除
            for (var i = 0 ; i < chatList.length ; i ++) {
                if (chatList[i].friendId == friendId) {
                    // 删除已经存在的friendId所对应的快照对象
                    chatList.splice(i, 1);
                    break;
                }
            }
        } else {
            // 如果为空，赋一个空的list
            chatList = [];
        }

        // 构建聊天快照对象
        var singleMsg = new this.ChatSnapshot(friendId,friendUsername, msg, isRead);

        // 向list中追加快照对象(在头部加)
        chatList.unshift(singleMsg);
        localStorage.setItem(chatKey, JSON.stringify(chatList));
    },

    /**
     * 获取聊天用户列表
     */
    getUserChatList: function() {
        var chatKey = "chat-list-" + localStorage.getItem("uid");
        // 从本地缓存获取聊天快照的list
        var chatListStr = localStorage.getItem(chatKey);
        var chatList;
        if (this.isNotNull(chatListStr)) {
            // 如果不为空
            chatList = JSON.parse(chatListStr);
        } else {
            // 如果为空，赋一个空的list
            chatList = [];
        }
        return chatList;
    },

    /**
     * 获取某个用户的聊天快照
     */
    getUserChatSnapshot: function(friendId) {
        var chatKey = "chat-list-" + localStorage.getItem("uid");
        // 从本地缓存获取聊天快照的list
        var chatListStr = localStorage.getItem(chatKey);
        var chatList;
        if (this.isNotNull(chatListStr)) {
            // 如果不为空
            chatList = JSON.parse(chatListStr);
            // 循环快照list，并且判断每个元素是否包含（匹配）friendId，如果匹配则返回
            for (var i = 0 ; i < chatList.length ; i ++) {
                if (chatList[i].friendId == friendId) {
                    return chatList[i];
                }
            }
        } else {
            // 如果为空直接返回空
            return null;
        }
    },

    /**
     * 单个聊天记录的对象
     * @param {Object} myId
     * @param {Object} friendId
     * @param {Object} msg
     * @param {Object} flag
     */
    ChatRecord: function(messageData, flag){
        this.messageData = messageData;
        this.flag = flag;
    },

    /**
     * 保存用户的聊天记录
     * @param {Object} messageData 消息对象
     * @param {Object} flag	判断本条消息是我发送的，还是朋友发送的，1:我  2:朋友
     * @param {Object} chatKey 消息在本地存储的key
     */
    saveChatRecord: function(messageData, flag, chatKey) {
        // 从本地缓存获取聊天记录是否存在
        var charRecordListStr = localStorage.getItem(chatKey);
        var chatRecordList;
        if (this.isNotNull(charRecordListStr)) {
            // 如果不为空
            chatRecordList = JSON.parse(charRecordListStr);
        } else {
            // 如果为空，赋一个空的list
            chatRecordList = [];
        }

        // 构建聊天记录对象
        var msgRecord = new this.ChatRecord(messageData, flag);

        // 向list中追加msg对象
        chatRecordList.push(msgRecord);

        localStorage.setItem(chatKey, JSON.stringify(chatRecordList));
    },

    /**
     * 获取用户聊天记录
     * @param {Object} friendId
     */
    getChatRecord: function(friendId) {
        var chatKey = '';
        //如果friendId为空则获取聊天室消息记录
        if (app.isNotNull(friendId)){
            chatKey = "single-chat-" + localStorage.getItem("uid") + "-" + friendId;
        } else {
            chatKey = "room-chat-" + localStorage.getItem("uid");
        }

        var chatRecordListStr = localStorage.getItem(chatKey);
        var charRecordList;
        if (this.isNotNull(chatRecordListStr)) {
            // 如果不为空
            charRecordList = JSON.parse(chatRecordListStr);
        } else {
            // 如果为空，赋一个空的list
            charRecordList = [];
        }

        return charRecordList;
    },

}