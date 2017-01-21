"use strict";
function WebSocketService(url) {
    var webSocket, server;
    var serverLocation = url;
    var maxAttempt = 50;
    this.connect = function (room, callback) {
        if (webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED) {
            return;
        }
        if (window.location.protocol === 'https:') {
            server = "wss://" + serverLocation + room;
        } else {
            server = "ws://" + serverLocation + room;
        }
        webSocket = new WebSocket(server);
        waitConnection(function () {
            callback(room);
        });
    }
    this.disconnect = function (callback) {
        if (webSocket !== undefined) {
            if (webSocket.readyState === WebSocket.OPEN) {
                webSocket.close();
            }
            waitDisconnection(callback);
        }
    }
    this.status = function () {
        return webSocket.readyState;
    }
    this.setHandler = function (handler) {
        webSocket.onmessage = handler;
    }
    this.send = function (message) {
        waitConnection(function () {
            webSocket.send(JSON.stringify(message));
        });
    }
    var showNetworkError = function () {
        swal({
            title: 'Network Error',
            text: 'Please check your connection and reload page',
            type: 'error',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Reload',
            cancelButtonText: 'Stay',
        }).then(function () {
            window.location.reload();
        }, function (dismiss) {
        });
    }
    var waitConnection = function (callback) {
        if (maxAttempt >= 0) {
            setTimeout(
                function () {
                    if (webSocket.readyState === WebSocket.OPEN) {
                        if (callback != null) {
                            callback();
                        }
                        return;
                    } else {
                        console.log("Waiting connection..");
                        maxAttempt--;
                        waitConnection(callback);
                    }
                }, 100);
        } else {
            maxAttempt = 50;
            showNetworkError();
        }
    }
    var waitDisconnection = function (callback) {
        if (maxAttempt >= 0) {
            setTimeout(
                function () {
                    if (webSocket.readyState === WebSocket.CLOSED) {
                        if (callback != null) {
                            callback();
                        }
                        return;
                    } else {
                        console.log("Waiting disconnection..");
                        maxAttempt--;
                        waitConnection(callback);
                    }
                }, 10);
        } else {
            maxAttempt = 50;
            showNetworkError();
        }
    }
}
function SearchService() {
    var searchBar, search, results, reset;
    searchBar = $(".search-bar");
    this.search = $("#search");
    this.reset = $('#reset');
    results = $("#results");
    this.show = function () {
        searchBar.show();
    }
    this.hide = function () {
        searchBar.hide();
    }
    var getSearch = function () {
        return $("#search").val();
    }
    var searchRoom = function (callback) {
        $.ajax({
            type: "POST",
            url: "/messages",
            dataType: "text",
            data: "message=" + getSearch(),
            success: function (data) {
                callback(data);
            },
            error: function (err) {
                console.log(err);
            }
        });
    }
    var loadResults = function (data) {
        var rooms = JSON.parse(data);
        rooms.forEach(function (item) {
            var name = $('<div class="collapsible-header ">' + item.name + '</div>');
            var messages = $('<div class="collapsible-body"> <p>TODO</p> </div>');
            var result = $('<li></li>');
            result.append(name).append(messages);
            results.append(result);
        });
    }
    this.showResults = function () {
        searchRoom(function (data) {
            loadResults(data);
            $('.collapsible').collapsible();
            results.show();
        });
    }
    this.hideResults = function () {
        results.hide();
        results.empty();
    }
}
function NavBarService() {
    var loading = $("#loading");
    this.closeBtn = $("#close");
    var searchService = new SearchService();
    this.search = searchService.search;
    this.reset = searchService.reset;
    this.showLoad = function () {
        loading.show();
    }
    this.hideLoad = function () {
        loading.hide();
    }
    this.showClose = function () {
        (this.closeBtn).show();
    }
    this.hideClose = function () {
        (this.closeBtn).hide();
    }
    this.showSearch = function () {
        searchService.show();
    }
    this.hideSearch = function () {
        searchService.hide();
    }
    this.showResults = function () {
        searchService.showResults();
    }
    this.hideResults = function () {
        searchService.hideResults();
    }
}
function SideNavService() {
    var sideNavBar, sideNavBtn;
    sideNavBar = $("#slide-out");
    sideNavBtn = $(".button-collapse");
    this.rooms = $('#rooms');
    var fixMobile = function () {
        $('.side-nav li a').on('click', function (e) {
            var windowsize = $(window).width();
            if (windowsize < 992) {
                sideNavBtn.sideNav('hide');
            }
        });
    }
    var loadRooms = function (rooms) {
        $('#rooms').empty();
        rooms.forEach(function (item) {
            $('#rooms').append('<a class="collection-item waves-effect">' + item.name + '</a>');
        });
    }
    this.init = function (rooms) {
        sideNavBtn.sideNav({
            menuWidth: 240,
            edge: 'left',
            draggable: true,
            closeOnClick: false
        });
        fixMobile();
        loadRooms(rooms);
    };
    this.show = function () {
        sideNavBar.show();
    }
    this.hide = function () {
        sideNavBar.hide();
    }
}
function RoomService() {
    this.roomBtn = $('#add_room');
    this.getRooms = function (callback) {
        $.ajax({
            type: "GET",
            url: "/rooms",
            success: function (data) {
                callback(data);
            },
            error: function (err) {
                console.log(err);
            }
        });
    }
    var createRoom = function (name, user, success, error) {
        $.ajax({
            type: "POST",
            url: "/rooms",
            contentType: "application/json",
            dataType: 'JSON',
            data: JSON.stringify({name: name, user: user, date: new Date()}),
            success: function (succ) {
                success();
            },
            error: function (err) {
                error('An error occured. Please retry later.');
            }
        });
    }
    this.createNewRoom = function (mail, callback) {
        swal({
            title: 'Enter a room name',
            input: 'text',
            showCancelButton: true,
            confirmButtonText: 'OK',
            showLoaderOnConfirm: true,
            preConfirm: function (room) {
                return new Promise(function (resolve, reject) {
                    if ($.trim(room) && /^[a-zA-Z0-9-_.]+$/.test(room)) {
                        createRoom(room, mail, resolve, reject);
                    } else {
                        reject('Please enter a valid room name.');
                    }
                });
            },
        }).then(function (room) {
            swal({
                type: 'success',
                title: 'New room created :',
                html: room
            });
            callback();
        });
    }
}
function HomeService() {
    var home, mail, password, room, loginForm, loginValidator;
    home = $("#home");
    loginForm = $("#login-form");
    mail = $("#mail");
    password = $("#epassword");
    this.enterBtn = $("#enter");
    this.registerBtn = $("#registerBtn");
    this.show = function () {
        home.show();
    }
    this.hide = function () {
        home.hide();
    }
    var validateLogin = function (form) {
        loginValidator = form.validate({
            rules: {
                mail: {
                    required: true,
                    email: true
                },
                epassword: {
                    required: true,
                    minlength: 10
                },
            },
            messages: {
                mail: {
                    required: "An adress mail is required",
                },
                epassword: {
                    required: "A password is required",
                },
            },
        });
    };
    this.validForm = function () {
        return loginForm.valid();
    }
    this.getMail = function () {
        return mail.val();
    }
    this.getPassword = function () {
        return password.val();
    }
    validateLogin(loginForm);
}
function RegisterService() {
    var userdata, register, pwd, registerForm, registerValidator, passwordS;
    register = $("#register");
    registerForm = $("#register-form");
    pwd = $("#rpassword");
    passwordS = $("#password-strength");
    this.validBtn = $("#valid");
    this.show = function () {
        register.show();
    }
    this.hide = function () {
        register.hide();
    }
    var showPasswordS = function () {
        passwordS.show();
    }
    var hidePasswordS = function () {
        passwordS.hide();
    }
    var showPasswordStrength = function () {
        var password = getPassword();
        var meter = passwordS.find("div");
        var strength;
        if ($.trim(password)) {
            showPasswordS();
            strength = zxcvbn(password);
            switch (strength.score) {
                case 0:
                    $("#pstrength").text('Nonexistent');
                    break;
                case 1:
                    $("pstrength").text('Weak');
                    meter.css('background-color', '#f44336');
                    break;
                case 2:
                    $("#pstrength").text('Good');
                    meter.css('background-color', '#ff9800');
                    break;
                case 3:
                    $("#pstrength").text('Strong');
                    meter.css('background-color', '#ffeb3b');
                    break;
                case 4:
                    $("#pstrength").text('Insane');
                    meter.css('background-color', '#009688');
                    break;
                default:
                    break;
            }
            strength = strength.score * 100 / 4;
            meter.width(strength + "%");
        } else {
            hidePasswordS();
        }
    }
    this.refreshPasswordStrength = function () {
        pwd.on("input", showPasswordStrength);
    }
    var initDatePicker = function () {
        var maxDate = new Date();
        maxDate.setFullYear(maxDate.getFullYear() - 10);
        $('.datepicker').pickadate({
            selectMonths: true,
            selectYears: 15,
            max: maxDate,
        });
    };
    var validateRegister = function (form) {
        registerValidator = form.validate({
            rules: {
                email: {
                    required: true,
                    email: true,
                },
                rpassword: {
                    required: true,
                    minlength: 10,
                    passwordCheck: true,
                },
                rconfirm: {
                    required: true,
                    minlength: 10,
                    equalTo: "#rpassword",
                },
                username: {
                    maxlength: 30,
                },
                birthdate: {
                    date: true,
                },
                telephone: {
                    phone: true,
                },
            },
            messages: {
                email: {
                    required: "An adress mail is required",
                },
                rpassword: {
                    required: "A password is required",
                },
                rconfirm: {
                    required: "Confirm your password",
                    equalTo: "Your passwords don't match",
                },
                birthdate: {
                    date: "Enter a valid birthdate.",
                },
            },
        });
    };
    var getMail = function () {
        return $("#email").val();
    }
    var getPassword = function () {
        return pwd.val();
    }
    var getUsername = function () {
        return $("#username").val();
    }
    var getBirthdate = function () {
        return new Date($("#birthdate").val());
    }
    var getGender = function () {
        var male = $("#male").prop('checked');
        var female = $("#female").prop('checked');
        var res;
        if (male || female) {
            if (male) {
                res = "M";
            } else {
                res = "F";
            }
        } else {
            res = "";
        }
        return res;
    }
    var getPhone = function () {
        return $("#telephone").val();
    }
    var getUserData = function () {
        var data = {
            mail: getMail(),
            password: getPassword(),
            username: getUsername(),
            birthdate: getBirthdate(),
            gender: getGender(),
            phone: getPhone(),
        };
        return data;
    }
    var registerChat = function (response, success, error) {
        userdata = getUserData();
        userdata.captcha = response;
        $.ajax({
            type: "POST",
            url: "/register",
            contentType: "application/json",
            dataType: 'JSON',
            data: JSON.stringify(userdata),
            success: function () {
                success();
            },
            error: function (data) {
                error();
            }
        });
    }
    var initConfirm = function () {
        swal.disableConfirmButton();
        $('.swal2-confirm').wrap(function () {
            return '<div class="col s12 m6 tooltipped" data-position="top" data-tooltip="Please verify the captcha"/>';
        });
        $('.swal2-cancel').wrap(function () {
            return '<div class="col s12 m6"/>';
        });
        $('.tooltipped').tooltip({
            delay: 50
        });
    }
    var allowConfirm = function () {
        swal.enableConfirmButton();
        $('.tooltipped').tooltip('remove');
    }
    var renderCaptcha = function () {
        initConfirm();
        var captchaKey = '6LcepBIUAAAAADFMYhb4fas-oTTeTBqjlX765lLG';
        grecaptcha.render('captcha', {
            'sitekey': captchaKey,
            'callback': allowConfirm,
            'theme': 'dark',
        });
    }
    var captchaResponse = function () {
        var response = grecaptcha.getResponse();
        return response.length != 0 && response;
    }
    var validForm = function () {
        return registerForm.valid();
    }
    this.openCaptcha = function (callback) {
        if (validForm()) {
            swal({
                title: 'Terms and Conditions',
                html: '<p>By creating an account, you consent to the ' +
                '<a href="">Terms of Service</a> ' +
                'and the ' +
                '<a href="">Privacy Policy</a>.</p>' +
                '<div id="captcha" class="row"/>',
                type: 'info',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Agree',
                cancelButtonText: 'Cancel',
                confirmButtonClass: 'btn',
                cancelButtonClass: 'btn',
                buttonsStyling: false,
                onOpen: renderCaptcha,
                preConfirm: function () {
                    return new Promise(function (resolve, reject) {
                        var captcha = captchaResponse();
                        if (captcha) {
                            registerChat(captcha, resolve, reject);
                        } else {
                            reject('Please vefiry the captcha');
                        }
                    })
                },
            }).then(function () {
                    swal({
                        title: 'Success',
                        text: 'A confirmation mail has been send to your inbox.',
                        type: 'success',
                    }).then(function () {
                        callback()
                    });
                }
            );
        }
    }
    initDatePicker();
    validateRegister(registerForm);
}
function ValidatorService() {
    $.validator.setDefaults({
        errorClass: 'invalid',
        validClass: "valid",
        errorPlacement: function (error, element) {
            $(element)
                .closest("form")
                .find("label[for='" + element.attr("id") + "']")
                .attr('data-error', error.text());
        },
        submitHandler: function (form) {
        }
    });
    $.validator.methods.email = function (value, element) {
        return this.optional(element) || /[A-Za-z.\d-]+@[a-z.\d-]+\.[a-z]/.test(value);
    }
    $.validator.addMethod("passwordCheck", function (value, element) {
        return this.optional(element) || (/^[A-Za-z0-9\d=!\-@._*]*$/.test(value) // consists of only these
            &&
            /[a-z]/.test(value) // has a lowercase letter
            &&
            /\d/.test(value)); // has a digit
    }, "Your password must contain at least one number.");
    $.validator.addMethod("dateGreaterThan", function (value, element, params) {
        if (!/Invalid|NaN/.test(new Date(value))) {
            return new Date(value) > new Date($(params).val());
        }
        return isNaN(value) && isNaN($(params).val()) ||
            (Number(value) > Number($(params).val()));
    }, 'Must be greater than {0}.');
    $.validator.addMethod("phone", function (value, element) {
        return this.optional(element) || /^[+?\d\s-]+$/.test(value);
    }, "Enter a valid phone number.");
}

function SessionService() {
    var session = window.sessionStorage;
    var isSupported = function () {
        return session;
    };
    this.setItem = function (key, value) {
        return session.setItem(key, value);
    };
    this.getItem = function (key) {
        return session.getItem(key);
    };
    this.removeItem = function (key) {
        return session.removeItem(key);
    };
    var clearAll = function () {
        return session.clear();
    };
    var init = function () {
        Storage.prototype.setObject = function (key, value) {
            this.setItem(key, JSON.stringify(value));
        }
        Storage.prototype.getObject = function (key) {
            var value = this.getItem(key);
            return value && JSON.parse(value);
        }
    };
    init();
    var setObject = function (key, object) {
        return session.setObject(key, object);
    };
    this.getObject = function (key) {
        return session.getObject(key);
    };
    this.storeObject = function (name, object) {
        if (isSupported()) {
            setObject(name, object);
        } else {
            showSessionError();
        }
    }
    this.handleSession = function (callback) {
        if (isSupported()) {
            callback();
        } else {
            showSessionError();
        }
    }
    this.clearSession = function () {
        if (isSupported()) {
            clearAll();
        } else {
            showSessionError();
        }
    }
    var showSessionError = function () {
        Materialize.toast("Warning : your browser doesn't handle HTML5 !", 5000, "rounded");
    }
}
function ChatService(url) {
    var currentdate, userSession, mail, username, room, rooms, chat, conv, msg, bsend, msend, roomName, history, historyPage, preventNewScroll, connected;
    preventNewScroll = false;
    connected = false;
    chat = $("#chat");
    conv = $("#conv");
    msg = $("#msg");
    bsend = $("#bsend");
    msend = $("#msend");
    roomName = $('#room-name');
    history = $('#history')
    historyPage = -1;
    currentdate = new Date();
    ValidatorService();
    var roomService = new RoomService();
    var navBarService = new NavBarService();
    var sideNavService = new SideNavService();
    var homeService = new HomeService();
    var registerService = new RegisterService();
    var sessionService = new SessionService();
    var webSocketService = new WebSocketService(url);
    var show = function () {
        chat.show();
        sideNavService.show();
        navBarService.showSearch();
        navBarService.showClose();
    }
    var hide = function () {
        chat.hide();
        sideNavService.hide();
        navBarService.hideSearch();
        navBarService.hideClose();
    }
    var openRegistration = function () {
        homeService.hide();
        registerService.show();
        navBarService.showClose();
    }
    var storeUserSession = function (user) {
        sessionService.storeObject("user", user);
    }
    var storeRoomSession = function (room) {
        sessionService.storeObject("room", room);
    }
    var openSession = function () {
        sessionService.handleSession(function () {
            userSession = sessionService.getObject("user");
            room = sessionService.getObject("room");
            mail = userSession.mail;
            if (userSession && room) {
                webSocketService.connect(room, function () {
                    loadChatRoom();
                });
            }
        });
    }
    var loadChatRoom = function () {
        webSocketService.setHandler(handleMessage);
        roomName.text(room);
        $('#name').text(username);
        $('#usermail').html(mail + "<i class='material-icons right'>arrow_drop_down</i>");
        homeService.hide();
        navBarService.hideLoad();
        sideNavService.init(rooms);
        show();
        connected = true;
    }
    var openChatRoom = function (room) {
        storeRoomSession(room);
        webSocketService.setHandler(handleMessage);
        loadChatRoom();
        showLastMessagePage();
    }
    var autofocus = function () {
        $('input:empty:visible:enabled:first').focus();
    }
    var signin = function (user) {
        $.ajax({
            type: "POST",
            url: "/login",
            contentType: "application/json",
            dataType: 'JSON',
            data: JSON.stringify(user),
            success: function (data) {
                storeUserSession(data);
                mail = data.mail;
                if ($.trim(data.username)) {
                    username = data.username;
                } else {
                    username = (data.mail).split("@")[0];
                }
                webSocketService.connect(room, openChatRoom);
            },
            error: function (err) {
                console.log(err);
                navBarService.hideLoad();
                swal({
                    title: 'Authentication failed',
                    text: err.responseText,
                    type: "error",
                    animation: false,
                    customClass: 'animated shake',
                    timer: 3000,
                }).catch(swal.noop);
            }
        });
    }
    var enterChat = function () {
        if (!connected) {
            room = "General";
            if (homeService.validForm()) {
                var session = {
                    mail: homeService.getMail(),
                    password: homeService.getPassword()
                }
                navBarService.showLoad();
                signin(session);
            } else {
                autofocus();
            }
        }
    }
    var loadMessage = function (message) {
        var sender, datetime, parag;
        var li = $('<li class="collection-item">');
        var username = message.sender;
        if (message.mail === mail) {
            datetime = $('<small>' + message.date + '</small>');
            sender = $('<strong class="right">' + username + '</strong>');
            parag = $('<p style="text-align: right">' + message.message + '</p>');
            li.append(datetime).append(sender).append(parag);
        } else {
            sender = $('<strong>' + username + '</strong>');
            datetime = $('<small class="right">' + message.date + '</small>');
            parag = $('<p>' + message.message + '</p>');
            li.append(sender).append(datetime).append(parag);
        }
        return li;
    }
    var appendMessage = function (newMsg) {
        conv.append(loadMessage(newMsg));
    }
    var prependMessage = function (newMsg) {
        conv.prepend(loadMessage(newMsg));
    }
    var handleMessage = function (e) {
        e.preventDefault();
        var msg = JSON.parse(e.data);
        if (msg.mail == mail) {
            msend.hide();
        }
        appendMessage(msg);
    }
    var sendMessage = function () {
        if ($.trim(msg.val())) {
            if (webSocketService.status() === WebSocket.OPEN) {
                msend.show();
                var newMsg = {
                    message: msg.val().replace(/(?:\r\n|\r|\n)/g, '<br/>'),
                    sender: username,
                    mail: mail
                };
                webSocketService.send(newMsg);
                msg.val("").focus();
                scrollChat();
            } else {
                msend.hide();
            }
        }
    }
    var getLastMessagePage = function (num, callback) {
        $.ajax({
            type: "GET",
            url: "/messages/" + room + "/" + num,
            success: function (succ) {
                callback(succ);
            },
            error: function (err) {
                console.log(err);
            }
        });
    }
    var getHistoryPage = function () {
        historyPage += 1;
        return historyPage;
    }
    var showLastMessagePage = function () {
        getLastMessagePage(getHistoryPage(), function (data) {
            var messages = data.content;
            messages.forEach(function (message) {
                var datetime = Date.parse(message.date);
                if (datetime < currentdate) {
                    prependMessage(message);
                }
            });
            conv.prepend(history);
            if (data.last === true) {
                history.hide();
            } else {
                history.show();
            }
        });
    }
    var clearChatRoom = function () {
        conv.empty();
    }
    var switchRoom = function (nextRoom) {
        webSocketService.disconnect(function () {
            webSocketService.connect(nextRoom, function () {
                currentdate = new Date();
                webSocketService.setHandler(handleMessage);
                clearChatRoom();
                roomName.text(nextRoom);
                room = nextRoom;
                historyPage = -1;
                showLastMessagePage();
            });
        });
    }
    var exit = function () {
        if (connected) {
            webSocketService.disconnect(function () {
                sessionService.clearSession();
                hide();
                connected = false;
            });
        } else {
            navBarService.hideClose();
            registerService.hide();
        }
        homeService.show();
        autofocus();
    }
    var scrollChat = function () {
        if (!preventNewScroll) {
            conv.stop().animate({
                scrollTop: conv[0].scrollHeight
            }, 1000);
        }
        conv.hover(function (e) {
            e.preventDefault();
            preventNewScroll = e.type == 'mouseenter' ? true : false;
            if (!preventNewScroll) {
                scrollChat();
            }
        });
    }
    this.init = function () {
        $('.collapsible').collapsible();
        var body = $('body');
        roomService.getRooms(function (data) {
            rooms = data;
        });
        (homeService.enterBtn).click(enterChat);
        (homeService.registerBtn).click(openRegistration);
        body.on('keypress', function (e) {
            if (e.which == 13) {
                enterChat();
                return false;
            }
        });
        msg.on('keypress', function (e) {
            if (e.which === 13 && !e.shiftKey) {
                sendMessage();
                return false;
            }
        });
        bsend.click(sendMessage);
        registerService.refreshPasswordStrength();
        (registerService.validBtn).click(function () {
            registerService.openCaptcha(exit);
        });
        (sideNavService.rooms).click(function (e) {
            var nextRoom = $(e.target).text();
            if ($.trim(nextRoom)) {
                switchRoom(nextRoom);
            }
        });
        (roomService.roomBtn).click(function () {
            roomService.createNewRoom(mail, function () {
                roomService.getRooms(function (data) {
                    rooms = data;
                    sideNavService.init(rooms);
                });
            });
        });
        conv.on('click', '.history', function () {
            showLastMessagePage();
        });
        (navBarService.search).on('keypress', function (e) {
            if (e.which === 13) {
                e.preventDefault();
                navBarService.showResults();
            }
        });
        (navBarService.reset).click(navBarService.hideResults);
        (navBarService.closeBtn).click(exit);
    }
}
$(window).on('load', function () {
    setTimeout(function () {
        $("body").addClass("loaded");
    }, 1000)
});
$(document).ready(function () {
    var chatApp = (function () {
        var particlesConfig = './lib/particlesjs-config.json';
        var url = "localhost:8080/chat/";
        var chatService = new ChatService(url);
        chatService.init();
        var initParticles = function () {
            particlesJS.load('particles-js', particlesConfig);
        }();
        swal.setDefaults({background: '#546e7a'});
    })();
});