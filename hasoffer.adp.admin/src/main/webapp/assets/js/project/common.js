
/**
 * 接口调用公共js
 */

var eva = {
    init: function () {
        this.user.initUserLogin();
    },
    hostDomain: "http://eva.only2only.cn",
    localHostDomain: "http://www.e.com",
    debug: true,
    apiList: {
        login: '/api/login',
        evaluation: '/api/evaluation/self/list',
        evaluation_question_list: '/api/evaluation/self/question/list/',
    },
    get: function (url, data, beforeCallback, completeCallback) {
        // set token param
        if (eva.user.isLogin()) {
            data.token = eva.user.token;
        }
        $.ajax({
            url: url,
            type: "get",
            dataType: "json",
            data: data,
            timeout: 15000,
            beforeSend: beforeCallback,
            error: function (event, XMLHttpRequest, ajaxOptions, thrownError) {
                console.log('event:' + event);
                console.log('XMLHttpRequest:' + XMLHttpRequest);
                console.log('ajaxOptions:' + ajaxOptions);
                console.log('thrownError:' + thrownError);
            },
            success: completeCallback

        });
    },
    post: function (url, data, beforeCallback, completeCallback) {
        // set token param
        if (eva.user.isLogin()) {
            data.token = eva.user.token;
        }
        $.ajax({
            url: url,
            type: "post",
            dataType: "json",
            data: data,
            timeout: 15000,
            beforeSend: beforeCallback,
            error: function (event, XMLHttpRequest, ajaxOptions, thrownError) {
                console.log('event:' + event);
                console.log('XMLHttpRequest:' + XMLHttpRequest);
                console.log('ajaxOptions:' + ajaxOptions);
                console.log('thrownError:' + thrownError);
            },
            success: completeCallback

        });
    },
    /**
     * user handler
     */
    user: {
        token: null,
        uid: null,
        cookie_user_auth: "eva_iUoYu_auth_jUkeL",
        isLogin: function () {
            var userInfo = eva.user.getUserInfo();
            if (userInfo == null) {
                return false;
            }
            if (!userInfo.uid) {
                return false;
            }
            return true;
        },
        getUid: function () {
            var userInfo = eva.user.getUserInfo();
            if (!userInfo.uid) {
                return false;
            }
            return userInfo.uid;
        },
        getToken: function () {
            var userInfo = eva.user.getUserInfo();
            if (!userInfo.token) {
                return false;
            }
            return userInfo.token;
        },
        getUserInfo: function () {
            var userinfo = eva.infoCookie.get(eva.user.cookie_user_auth);
            return JSON.parse(userinfo);
        },
        setToken: function (data) {
            if (typeof data != "object") {
                return false;
            }
            var cstr = JSON.stringify(data.userinfo);
            eva.infoCookie.set(eva.user.cookie_user_auth, cstr);
            if (eva.infoCookie.get(eva.user.cookie_user_auth)) {
                return true;
            }
            return false;
        },
        initUserLogin: function () {
            if (!this.isLogin()) {
                return false;
            }
            this.uid = this.getUid();
            this.token = this.getToken();
        }
    },
    /**
     * cookie handler
     */
    infoCookie: {
        set: function (name, data, ext) {
            if (!name || !data) {
                return false;
            }
            ext = ext ? ext : {expires: 7, path: "/"};
            $.cookie(name, data, ext);
            return true;
        },
        get: function (name) {
            if (!name) {
                return false;
            }
            return $.cookie(name);
        },
        del: function (name) {
            if (!name) {
                return false;
            }
            $.cookie(name, null, {path: "/", expires: -1});
            return true;
        }
    },
    /**
     * 消息提示
     * @param {type} msg    提示内容
     * @param {type} type true 成功 false 失败
     * @param {type} time 100   显示层停留的时间，不显示定义，可以直接更改
     * @returns {undefined}
     */
    showMsg: function (msg, type) {
        var cTime = arguments[2];
        layer.msg(
                msg,
                {
                    offset: 100,
                    shift: type ? 5 : 6,
                    shadeClose: true,
                    time: cTime ? cTime : 800,
                }
        );
    },
}