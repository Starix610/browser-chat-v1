$(function () {

    $(".nav-primary li").click(function () {
        $('.nav li').removeClass("active");
        $(this).addClass("active");

        if ($(this).attr("id") == "index") {
            $("#index-panel").fadeIn();
            $("#single-chat-panel").hide();
            $("#room-chat-panel").hide();
            $("#call-panel").hide();
        }
        if ($(this).attr("id") == "single-chat") {
            $("#single-chat-panel").fadeIn();
            $("#index-panel").hide();
            $("#room-chat-panel").hide();
            $("#call-panel").hide();
            if ($("#chat-list").children().length == 0) {
                $("#chat-title").text("暂无聊天信息");
                $("#chat-title").removeAttr("style");
            }
        }
        if ($(this).attr("id") == "room-chat") {
            $("#room-chat-panel").fadeIn();
            $("#index-panel").hide();
            $("#single-chat-panel").hide();
            $("#call-panel").hide();

            getRoomChatReocrdFromStorge();
        }

        if ($(this).attr("id") == "call") {
            $("#call-panel").fadeIn();
            $("#index-panel").hide();
            $("#single-chat-panel").hide();
            $("#room-chat-panel").hide();
        }
    });

    $("#exit").click(function () {
        $.ajax({
            type: "GET",
            contentType: "application/x-www-form-urlencoded",
            url: app.BASE_URL + "/logout",
            dataType: "json",
            async: false,
            success: function (data) {
                if (data.code == 200) {
                    socket.close();
                    alert("退出成功！");
                    window.location.href = app.BASE_URL + "/login.html";
                } else {
                    alert(data.message);
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert("服务器异常");
            }
        });
    });


    //最近聊天用户列表点击选择用户
    $("body").on('click', ".list-group a", function () {
        $('.list-group a').removeClass("active");
        $(this).addClass("active");
        $("#chat-title").text("与[" + $(this).children("h4").text() + "]的聊天");
        $("#chat-list").next().show();

        //获取本地聊天记录并显示
        getSingleChatReocrdFromStorge($(this).attr("id"));

        //滚动条滑到底部
        adjustScrollBar("msgList");
    });


    //通过点击在线用户列表按钮去聊天
    $("body").on('click', 'button[name="user-list-send"]', function () {
        var friendId = $(this).parent().prevAll().eq(2).text();
        var friendUsername = $(this).parent().prevAll().eq(1).text();
        $('.nav li').removeClass("active");
        $("#single-chat").addClass("active");
        $("#single-chat-panel").fadeIn();
        $("#index-panel").hide();
        $("#room-chat-panel").hide();
        $("#call-panel").hide();

        // 渲染历史聊天记录
        getSingleChatReocrdFromStorge(friendId)

        var userChatSnapshot = app.getUserChatSnapshot(friendId);
        if (userChatSnapshot == null) {
            app.saveUserChatList(friendId, friendUsername, '...', 0);
        } else {
            app.saveUserChatList(friendId, friendUsername, userChatSnapshot.msg, 0);
        }

        loadUserChatList();

        $("#chat-list :first").addClass("active");
        $("#chat-title").text("与[" + $("#chat-list").children(".active").children("h4").text() + "]的聊天")
        $("#chat-list").next().show();

        //滚动条滑到底部
        adjustScrollBar("msgList");

    });

    $("#send-single").click(function () {
        sendSingleMsg();
    });

    $("#send-room").click(function () {
        sendRoomMsg()
    });

    $("#search").click(function () {
        searchUser();
    })

    //点击呼叫
    $("body").on('click', "button[name='call-btn']", function () {
        sendCallMsg($(this));
    });


})


function getUserInfo() {
    if (localStorage.getItem("uid") != null) {
        $("#username-label").text(localStorage.getItem("username"));
        $("#uid-label").text(localStorage.getItem("uid"));
        return;
    }
    $.ajax({
        type: "GET",
        contentType: "application/x-www-form-urlencoded",
        url: app.BASE_URL + "/userInfo",
        dataType: "json",
        async: false,
        success: function (data) {
            if (data.code == 200) {
                localStorage.setItem("uid", data.data.uid);
                localStorage.setItem("username", data.data.username);
                $("#username-label").text(localStorage.getItem("username"));
                $("#uid-label").text(localStorage.getItem("uid"));
            } else {
                alert(data.message);
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert("服务器异常");
        }
    });
}

function handlerSingleMsg(msgData) {
    var currentFromUid = $("#chat-list").children(".active").attr("id");
    //如果当前聊天窗口正好对应消息的发送者，则渲染消息
    if (msgData.fromUid == currentFromUid) {
        showSingleMsg(msgData);
    }
    //缓存聊天记录到本地
    app.saveChatRecord(msgData, 0, "single-chat-" + localStorage.getItem("uid") + "-" + msgData.fromUid);
    //保存聊天快照并调整该用户在聊天列表中的位置
    app.saveUserChatList(msgData.fromUid, msgData.username, msgData.content, 0);
    //重新加载聊天列表
    loadUserChatList();

    //保持原来的选中项选中
    if (app.isNotNull(currentFromUid)) {
        $("#chat-list").children("#" + currentFromUid).addClass("active");
    } else {
        //原来未选择则默认选中第一项
        $("#chat-list :first").addClass("active");
    }
}


function handlerRoomMsg(msgData) {
    //缓存聊天记录到本地
    app.saveChatRecord(msgData, 0, "room-chat-" + localStorage.getItem("uid"));
    showRoomMsg(msgData);
}


function loadUserChatList() {
    $("#chat-list").empty();
    var userChatList = app.getUserChatList();
    var html = '';
    for (var i = 0; i < userChatList.length; i++) {
        // html += "<a href=\"javascript:void(0);\" class=\"list-group-item\" id='" + userChatList[i].friendId + "'>" + userChatList[i].friendUsername + "---" + userChatList[i].msg + "</a>"
        html += "<a href=\"javascript:void(0);\" class=\"list-group-item\" id='" + userChatList[i].friendId + "'>\n" +
            "         <h4 class=\"list-group-item-heading\">" + userChatList[i].friendUsername + "</h4>\n" +
            "         <p class=\"list-group-item-text text-muted\">" + userChatList[i].msg + "</p>\n" +
            "    </a>"
    }
    $("#chat-list").append(html);
}


//发送单聊消息
function sendSingleMsg() {
    if ($("#singleMsg").val() == '') {
        new $.zui.Messager("不能发送空白消息", {
            type: 'warning',
            icon: 'info-sign',
            placement: 'center'
        }).show();
        return false;
    }

    var uid = localStorage.getItem("uid");
    var username = localStorage.getItem("username");
    var toUid = $("#chat-list").children(".active").attr("id");
    var msg = $("#singleMsg").val();
    var messageData = new app.MessageData(uid, username, toUid, msg, app.SINGLE);

    //保存聊天快照
    var friendUsername = $("#chat-list").children(".active").children("h4").text();
    app.saveUserChatList(toUid, friendUsername, msg, 0);
    //重新加载聊天列表(调整该用户在聊天列表中的位置)
    loadUserChatList();
    //保持第一项选中
    $("#chat-list :first").addClass("active");

    socket.send(JSON.stringify(messageData));
    $("#singleMsg").val('');

    //渲染消息到聊天窗口
    showSingleMsg(messageData);

    //缓存聊天记录到本地
    app.saveChatRecord(messageData, 0, "single-chat-" + localStorage.getItem("uid") + "-" + toUid);

}

//渲染单聊消息到聊天窗口
function showSingleMsg(msgData) {
    var time = new Date(msgData.time).toLocaleString();
    var html = '';
    if (msgData.fromUid == localStorage.getItem("uid")){
        html = "<div class=\"panel panel-default\" style=\"margin-bottom: 1px\">\n" +
            "       <div class=\"panel-heading\" style='color: green'>" + time + " <b>" + msgData.username + "</b>：</div>\n" +
            "       <div class=\"panel-body\" style='color: green'>" + msgData.content + "</div>\n" +
            "   </div>"
    } else {
        html = "<div class=\"panel panel-default\" style=\"margin-bottom: 1px\">\n" +
            "       <div class=\"panel-heading\">" + time + " <b>" + msgData.username + "</b>：</div>\n" +
            "       <div class=\"panel-body\">" + msgData.content + "</div>\n" +
            "   </div>"
    }

    $("#msgList").append(html);
    //滚动条滑到底部
    adjustScrollBar("msgList");
}


//发送聊天室消息
function sendRoomMsg() {
    if ($("#roomMsg").val() == '') {
        new $.zui.Messager("不能发送空白消息", {
            type: 'warning',
            icon: 'info-sign',
            placement: 'center'
        }).show();
        return false;
    }

    var uid = localStorage.getItem("uid");
    var username = localStorage.getItem("username");
    var msg = $("#roomMsg").val();
    var messageData = new app.MessageData(uid, username, null, msg, app.ROOM);

    socket.send(JSON.stringify(messageData));
    $("#roomMsg").val('');

    //渲染消息到聊天窗口
    showRoomMsg(messageData);

    //缓存聊天记录到本地
    app.saveChatRecord(messageData, 0, "room-chat-" + localStorage.getItem("uid"));

}

//渲染聊天室消息到聊天窗口
function showRoomMsg(msgData) {
    var time = new Date(msgData.time).toLocaleString();
    var html = '';
    if (msgData.fromUid == localStorage.getItem("uid")){
        html = "<div class=\"panel panel-default\" style=\"margin-bottom: 1px\">\n" +
            "       <div class=\"panel-heading\" style='color: green'>" + time + " <b>" + msgData.username + "</b>：</div>\n" +
            "       <div class=\"panel-body\" style='color: green'>" + msgData.content + "</div>\n" +
            "   </div>"
    } else {
        html = "<div class=\"panel panel-default\" style=\"margin-bottom: 1px\">\n" +
            "       <div class=\"panel-heading\">" + time + " <b>" + msgData.username + "</b>：</div>\n" +
            "       <div class=\"panel-body\">" + msgData.content + "</div>\n" +
            "   </div>"
    }

    $("#roomMsgList").append(html);
    //滚动条滑到底部
    adjustScrollBar("roomMsgList");
}


/**
 * 从本地缓存获取单聊聊天记录并渲染
 */

function getSingleChatReocrdFromStorge(friendId) {
    $("#msgList").empty();
    var chatRecordList = app.getChatRecord(friendId);
    if (chatRecordList == null || chatRecordList.length == 0) {
        return;
    }
    for (var i = 0; i < chatRecordList.length; i++) {
        showSingleMsg(chatRecordList[i].messageData)
    }
}

/**
 * 从本地缓存获取聊天室聊天记录并渲染
 */
function getRoomChatReocrdFromStorge() {
    $("#roomMsgList").empty();
    var chatRecordList = app.getChatRecord(null);
    if (chatRecordList == null || chatRecordList.length == 0) {
        return;
    }
    console.log("群聊聊天记录:" + chatRecordList)
    for (var i = 0; i < chatRecordList.length; i++) {
        showRoomMsg(chatRecordList[i].messageData)
    }
}


function loadUserList() {
    $.ajax({
        type: "GET",
        contentType: "application/x-www-form-urlencoded",
        url: app.BASE_URL + "/userList?currentUid=" + localStorage.getItem("uid"),
        dataType: "json",
        async: false,
        success: function (data) {
            if (data.code == 200) {
                var userList = data.data;
                if (userList == null) {
                    $("#no-user").show();
                    $("#user-list").empty();
                    return;
                }
                var html = "";
                $("#user-list").empty();
                for (var i = 0; i < userList.length; i++) {
                    var date = new Date(userList[i].loginTime).toLocaleString();
                    html += "<tr>\n" +
                        "            <td>" + userList[i].uid + "</td>\n" +
                        "            <td>" + userList[i].username + "</td>\n" +
                        "            <td>" + date + "</td>\n" +
                        "            <td>\n" +
                        "                <button class=\"btn btn-success btn-sm\" type=\"button\" name='user-list-send' id='" + userList[i].uid + "'>发消息</button>\n" +
                        "            </td>\n" +
                        "        </tr>"
                }
                $("#user-list").append(html);
                $("#no-user").hide();
            } else {
                alert(data.message);
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert("服务器异常");
        }
    });
}


function show_info_box(msg) {
    var tip_type = '';
    if (msg.includes('上线')) {
        tip_type = 'special';
    } else {
        tip_type = 'warning';
    }
    new $.zui.Messager('提示消息：' + msg, {
        type: tip_type,
        icon: 'info-sign'
    }).show();
}


//调整聊天窗口滚动条使其滑到底部
function adjustScrollBar(elementId) {
    var msgList = document.getElementById(elementId);
    msgList.scrollTop = msgList.scrollHeight;
}


function searchUser() {
    $.ajax({
        type: "GET",
        contentType: "application/x-www-form-urlencoded",
        url: app.BASE_URL + "/searchUser?keyword=" + $("#search-input").val(),
        dataType: "json",
        async: false,
        success: function (data) {
            if (data.code == 200) {
                $("#search-list").empty();
                if (data.data == null) {
                    $("#search-table").hide();
                    $("#no-result").show();
                } else {
                    $("#search-table").show();
                    $("#no-result").hide();
                    var html = '';
                    var userList = data.data;
                    for (var i = 0; i < userList.length; i++) {
                        var time = userList[i].loginTime == null ? '' : new Date(userList[i].loginTime).toLocaleString();
                        html += "<tr>\n" +
                            "        <td>" + userList[i].uid + "</td>\n" +
                            "        <td>" + userList[i].username + "</td>\n" +
                            "        <td>" + time + "</td>\n" +
                            "        <td>" + (userList[i].status == 1 ? '<span style="color: green">在线</span>' : '<span style="color: red">离线</span>') + "</td>\n" +
                            "        <td>\n" +
                            "            <button class=\"btn btn-info\" name='call-btn' " + (userList[i].status == 0 ? 'disabled' : '') + " type=\"button\" id='callBtn-" + userList[i].uid + "'>呼叫</button>\n" +
                            "        </td>\n" +
                            "    </tr>"
                    }
                    $("#search-list").append(html);
                }
            } else {
                alert(data.message);
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert("服务器异常");
        }
    });
}

function sendCallMsg(callBtn) {
    
    if (callBtn.parent().prevAll().eq(3).text() == localStorage.getItem("uid")){
        alert("你不能呼叫你自己！");
        return false;
    }
    
    if (callBtn.parent().prev().text() == '在线') {
        //显示确认弹窗
        fyAlert.alert({
            title:'提示信息',
            content: '你确定要呼叫该用户吗？',
            shadowClose: false,
            btns: {   //按钮组
                '确定' : function(obj){
                    obj.destory();
                    //等待动画
                    callBtn.addClass("load-indicator loading");
                    var uid = localStorage.getItem("uid");
                    var username = localStorage.getItem("username");
                    var toUid = callBtn.parent().prevAll().eq(3).text();
                    var msg = "我向你发起了一次呼叫";
                    var messageData = new app.MessageData(uid, username, toUid, msg, app.CALL);
                    socket.send(JSON.stringify(messageData));
                },
                '取消' : function(obj){
                    obj.destory();
                }
            },
        })
    }else {
        alert("不能呼叫离线的用户!");
    }
}


function handlerCall(msgData) {
    app.saveChatRecord(msgData, 0, "single-chat-" + localStorage.getItem("uid") + "-" + msgData.fromUid);
    //弹窗提示
    fyAlert.alert({
        title:'提示信息',
        content: '用户['+msgData.username+"]向你发起了一次聊天呼叫，是否接受？",
        shadowClose: false,
        btns: {   //按钮组
            '接受' : function(obj){
                obj.destory();

                var friendId = msgData.fromUid;
                var friendUsername = msgData.username;
                var msg = msgData.content;
                $('.nav li').removeClass("active");
                $("#single-chat").addClass("active");
                $("#single-chat-panel").fadeIn();
                $("#index-panel").hide();
                $("#room-chat-panel").hide();
                $("#call-panel").hide();

                //发送CALL回应消息
                var messageData = new app.MessageData(localStorage.getItem("uid"),localStorage.getItem("username"),friendId,"接受",app.CALL_REPLY);
                socket.send(JSON.stringify(messageData));

                // 渲染历史聊天记录
                getSingleChatReocrdFromStorge(friendId)

                app.saveUserChatList(friendId, friendUsername, msg, 0);

                loadUserChatList();

                $("#chat-list :first").addClass("active");
                $("#chat-title").text("与[" + $("#chat-list").children(".active").children("h4").text() + "]的聊天")
                $("#chat-list").next().show();
                //滚动条滑到底部
                adjustScrollBar("msgList");
            },
            '取消' : function(obj){
                obj.destory();
                //发送CALL回应消息
                var messageData = new app.MessageData(localStorage.getItem("uid"),localStorage.getItem("username"),msgData.fromUid,"拒绝",app.CALL_REPLY);
                socket.send(JSON.stringify(messageData));

            }
        },
    })
}


function handlerCallReply(msgData) {
    //弹窗提示CALL回应消息
    fyAlert.alert({
        title:'提示信息',
        content: "用户["+msgData.username+"]"+msgData.content+"了你的呼叫",
        shadowClose: false,
        closeBtn: false,
        btns: {   //按钮组
            '确定' : function(obj){
                obj.destory();
                //关闭等待动画
                var callBtn = $("#callBtn-"+msgData.fromUid);
                callBtn.removeClass("load-indicator loading");

                //对方接受则进入聊天界面
                if (msgData.content == '接受'){
                    var friendId = msgData.fromUid;
                    var friendUsername = msgData.username;
                    var msg = msgData.content;
                    $('.nav li').removeClass("active");
                    $("#single-chat").addClass("active");
                    $("#single-chat-panel").fadeIn();
                    $("#index-panel").hide();
                    $("#room-chat-panel").hide();
                    $("#call-panel").hide();

                    // 渲染历史聊天记录
                    getSingleChatReocrdFromStorge(friendId)

                    var userChatSnapshot = app.getUserChatSnapshot(friendId);
                    if (userChatSnapshot == null) {
                        app.saveUserChatList(friendId, friendUsername, '...', 0);
                    } else {
                        app.saveUserChatList(friendId, friendUsername, userChatSnapshot.msg, 0);
                    }

                    loadUserChatList();

                    $("#chat-list :first").addClass("active");
                    $("#chat-title").text("与[" + $("#chat-list").children(".active").children("h4").text() + "]的聊天")
                    $("#chat-list").next().show();
                    //滚动条滑到底部
                    adjustScrollBar("msgList");
                }

            }
        },
    })
}

