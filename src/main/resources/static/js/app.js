//TODO UML
//TODO Add room
//TODO profil & photo
//TODO clean console log

"use strict";

/*
 WebSocket service for chatroom server
 */
function WebSocketService(url) {
    var webSocket, server;
    var serverLocation = url;
    var maxAttempt = 50;

    //Connect to a room
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

    //Disconnet from a room
    this.disconnect = function (callback) {
        if (webSocket !== undefined) {
            if (webSocket.readyState === WebSocket.OPEN) {
                webSocket.close();
            }
            waitDisconnection(callback);
        }
    }

    //Get WebSocket State
    this.status = function () {
        return webSocket.readyState;
    }

    //Set on message handler
    this.setHandler = function (handler) {
        webSocket.onmessage = handler;
    }

    //Send message
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
            //Do nothing
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
                }, 100); // wait 100 milisecond for the connection...
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
                }, 10); // wait 10 milisecond for the disconnection...
        } else {
            maxAttempt = 50;
            showNetworkError();
        }
    }

}

/*
 Search field service
 */
function SearchService() {
    var searchBar, search, results, reset;
    searchBar = $(".search-bar");
    this.search = $("#search");
    this.reset = $('#reset');
    results = $("#results");

    //Show search field
    this.show = function () {
        searchBar.show();
    }

    //Hide search field
    this.hide = function () {
        searchBar.hide();
    }

    var searchRoom = function (callback) {
        $.ajax({
            type: "POST",
            url: "/messages",
            contentType: "application/json",
            dataType: 'JSON',
            data: JSON.stringify({message: $("#search").val()}),
            success: function (data) {
                console.log(data);
                callback(data);
            },
            error: function (err) {
                console.log(err);
            }
        });
    }

    var loadResults = function (rooms) {
        //TODO check
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
        //TODO results.empty();
    }

}

/*
 Navigation Bar service with search field
 */
function NavBarService() {
    var loading = $("#loading");
    this.closeBtn = $("#close");

    var searchService = new SearchService();
    this.search = searchService.search;
    this.reset = searchService.reset;

    //Show loading animation
    this.showLoad = function () {
        loading.show();
    }

    //Hide loading animation
    this.hideLoad = function () {
        loading.hide();
    }

    //Show close button
    this.showClose = function () {
        (this.closeBtn).show();
    }

    //Hide close button
    this.hideClose = function () {
        (this.closeBtn).hide();
    }

    //Show search field
    this.showSearch = function () {
        searchService.show();
    }

    //Hide search field
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

/*
 Side navigation bar service
 */
function SideNavService() {
    var sideNavBar, sideNavBtn;
    sideNavBar = $("#slide-out");
    sideNavBtn = $(".button-collapse");
    this.rooms = $('#rooms');

    //Fix for fixed side navigation on mobile
    var fixMobile = function () {
        $('.side-nav li a').on('click', function (e) {
            var windowsize = $(window).width();
            if (windowsize < 992) {
                sideNavBtn.sideNav('hide');
            }
        });
    }

    //Init side navigation
    this.init = function () {
        sideNavBtn.sideNav({
            menuWidth: 240, // Default is 240
            edge: 'left', // Choose the horizontal origin
            draggable: true, // Choose whether you can drag to open on touch screens
            closeOnClick: false // Closes side-nav on <a> clicks, useful for Angular/Meteor
        });
        fixMobile();
    };

    //Show side navigation
    this.show = function () {
        sideNavBar.show();
    }

    //Hide side navigation
    this.hide = function () {
        sideNavBar.hide();
    }

}

/*
 Chatrooms service
 */
function RoomService() {

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

    this.createRoom = function (name) {
        $.ajax({
            type: "POST",
            url: "/rooms",
            contentType: "application/json",
            dataType: 'JSON',
            data: JSON.stringify({name: name}),
            success: function (succ) {
                console.log(succ);
            },
            error: function (err) {
                console.log(err);
            }
        });
    }

}

/*
 Home page service
 */
function HomeService() {
    var home, mail, password, room, loginForm, loginValidator;
    home = $("#home");
    loginForm = $("#login-form");
    mail = $("#mail");
    password = $("#epassword");
    room = $("select");
    this.enterBtn = $("#enter");
    this.registerBtn = $("#registerBtn");

    var roomService = new RoomService();

    //Show home page
    this.show = function () {
        home.show();
    }

    //Hide home page
    this.hide = function () {
        home.hide();
    }

    //Init room select
    var initSelect = function () {
        roomService.getRooms(function (rooms) {
            rooms.forEach(function (item) {
                room.append('<option value="' + item.name + '">' + item.name + '</option>');
            });
            room.material_select();
        });
    };

    //Select switch to previous room
    this.previousRoom = function (e) {
        e.preventDefault();
        var current = $("#room > option:selected");
        if (!current.prev().is(":disabled")) {
            current.prop("selected", false);
            current.prev().prop("selected", true);
        } else {
            current.prop("selected", false);
            $("#room option:not([disabled])").last().prop("selected", true);
        }
        room.material_select();
    }

    //Select switch to next room
    this.nextRoom = function (e) {
        e.preventDefault();
        var current = $("#room > option:selected");
        if (!current.is(':last-child')) {
            current.prop("selected", false);
            current.next().prop("selected", true);
        } else {
            current.prop("selected", false);
            $("#room option:not([disabled])").first().prop("selected", true);
        }
        room.material_select();
    }

    //Validate login form
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
                room: "required",
            },
            //For custom messages
            messages: {
                mail: {
                    required: "An adress mail is required",
                },
                epassword: {
                    required: "A password is required",
                },
                room: "Please choose a room",
            },
        });
    };

    this.validForm = function () {
        return loginForm.valid();
    }

    //Get mail
    this.getMail = function () {
        return mail.val();
    }

    //Get password
    this.getPassword = function () {
        return password.val();
    }

    //Get room
    this.getRoom = function () {
        return room.val();
    }

    // roomService.createRoom("IF");
    // roomService.createRoom("ID");
    // roomService.createRoom("SITN");
    initSelect();
    validateLogin(loginForm);

}

/*
 Registration page service
 */
function RegisterService() {
    var userdata, register, pwd, registerForm, registerValidator, passwordS;
    register = $("#register");
    registerForm = $("#register-form");
    pwd = $("#rpassword");
    passwordS = $("#password-strength");
    this.validBtn = $("#valid");

    //Show registration page
    this.show = function () {
        register.show();
    }

    //Hide registration page
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
            //TODO Change label for progress
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

    //Init birthday date picker
    var initDatePicker = function () {
        var maxDate = new Date();
        maxDate.setFullYear(maxDate.getFullYear() - 10);
        $('.datepicker').pickadate({
            selectMonths: true, // Creates a dropdown to control month
            selectYears: 15, // Creates a dropdown of 10 years to control year
            max: maxDate, // The selected date must be in the past
        });
    };

    //Validate register form
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
            //For custom messages
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

    //Send registration data to server
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
        var captchaKey = '6LeHxwwUAAAAALBdEOEocIsmGVDzzmVYH3w_vVTT';
        //TODO compact mode if mobile
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

    //Open Terms and Conditions of Service modal with captcha verification
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
                            //TODO reset captcha ?
                            reject('Please vefiry the captcha');
                        }
                    })
                },
            }).then(function () {
                    //TODO color fix
                    swal({
                        title: 'Success',
                        text: 'A confirmation mail has been send to your inbox.',
                        type: 'success',
                    }).then(function () {
                        callback()
                    });
                }
                /*, function(dismiss) {
                 // dismiss can be 'cancel', 'overlay', 'close', and 'timer'
                 if (dismiss === 'cancel') {
                 swal(
                 'Cancelled',
                 'Your imaginary file is safe :)',
                 'error'
                 )
                 }
                 }*/
            );
        }
    }

    initDatePicker();
    validateRegister(registerForm);
}

//Form validator service
function ValidatorService() {

    // Compatibility extension for materialize.css
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
        return this.optional(element) || /[A-Za-z.\d-]+@[a-z.\d-]+\.[a-z]+/.test(value);
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
        //Storage extension for storing object in JSON strings
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

    //Show session handler error
    var showSessionError = function () {
        Materialize.toast("Warning : your browser doesn't handle HTML5 !", 5000, "rounded");
    }

}

/*
 Chat page service
 */
function ChatService(url) {
    var currentdate, userSession, mail, username, room, chat, conv, msg, bsend, msend, roomName, history, historyPage, preventNewScroll, connected;
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
    var navBarService = new NavBarService();
    var sideNavService = new SideNavService();
    var homeService = new HomeService();
    var registerService = new RegisterService();
    var sessionService = new SessionService();
    var webSocketService = new WebSocketService(url);

    //Show chat page
    var show = function () {
        chat.show();
        sideNavService.show();
        navBarService.showSearch();
        navBarService.showClose();
    }

    //Hide chat page
    var hide = function () {
        chat.hide();
        sideNavService.hide();
        navBarService.hideSearch();
        navBarService.hideClose();
    }

    //Open registration page
    var openRegistration = function () {
        homeService.hide();
        registerService.show();
        navBarService.showClose();
    }

    //Store user session
    var storeUserSession = function (user) {
        sessionService.storeObject("user", user);
    }

    //Store room session
    var storeRoomSession = function (room) {
        sessionService.storeObject("room", room);
    }

    //Open user session
    var openSession = function () {
        sessionService.handleSession(function () {
            userSession = sessionService.getObject("user");
            room = sessionService.getObject("room");
            mail = userSession.mail;
            //TODO check not empty object
            if (userSession && room) {
                //TODO signin token
                //TODO go to profil ?
                //TODO room session
                webSocketService.connect(room, function () {
                    //TODO request message optimisation (1 chat/room)
                    loadChatRoom();
                });
            }
        });
    }

    var loadChatRoom = function () {
        webSocketService.setHandler(handleMessage);
        roomName.text(room.toUpperCase());
        $('#name').text(username);
        $('#usermail').html(mail + "<i class='material-icons right'>arrow_drop_down</i>");
        homeService.hide();
        navBarService.hideLoad();
        sideNavService.init();
        show();
        connected = true;
    }

    //Open chatroom page
    var openChatRoom = function (room) {
        storeRoomSession(room);
        webSocketService.setHandler(handleMessage);
        loadChatRoom();
        showLastMessagePage();
    }

    //Focus first empty visible enable input
    var autofocus = function () {
        //TODO update valid (& empty?)
        //if (registerService)
        $('input:empty:visible:enabled:first').focus();
        // $('input:text[value=""],input:password[value=""]').first().focus()
    }

    var signin = function (user) {
        $.ajax({
            type: "POST",
            url: "/login",
            contentType: "application/json",
            dataType: 'JSON',
            data: JSON.stringify(user),
            success: function (data) {
                //TODO check application/json
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

    //Try enter chatroom
    var enterChat = function () {
        if (!connected) {
            room = homeService.getRoom();
            if ($.trim(room) && homeService.validForm()) {
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
        //TODO user link ?
        //TODO color ?
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

    //Append message to chatroom
    var appendMessage = function (newMsg) {
        conv.append(loadMessage(newMsg));
    }

    //Prepend message to chatroom
    var prependMessage = function (newMsg) {
        conv.prepend(loadMessage(newMsg));
    }

    //Handle message from server
    var handleMessage = function (e) {
        e.preventDefault();
        var msg = JSON.parse(e.data);
        if (msg.mail == mail) {
            msend.hide();
            appendMessage(msg);
        } else {
            appendMessage(msg);
        }
    }

    //Send message in room
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
                //TODO check no connexion & hide loader + show message
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

    //Exit
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

    var toggleFullscreen = function () {
        document.fullScreenElement && null !== document.fullScreenElement || !document.mozFullScreen && !document.webkitIsFullScreen ? document.documentElement.requestFullScreen ? document.documentElement.requestFullScreen() : document.documentElement.mozRequestFullScreen ? document.documentElement.mozRequestFullScreen() : document.documentElement.webkitRequestFullScreen && document.documentElement.webkitRequestFullScreen(Element.ALLOW_KEYBOARD_INPUT) : document.cancelFullScreen ? document.cancelFullScreen() : document.mozCancelFullScreen ? document.mozCancelFullScreen() : document.webkitCancelFullScreen && document.webkitCancelFullScreen()
    }

    //Auto scroll chat
    //TODO check usefull
    var scrollChat = function () {
        if (!preventNewScroll) { // if mouse is not over printer
            conv.stop().animate({
                scrollTop: conv[0].scrollHeight
                /* - convH */
            }, 1000); // SET SCROLLER TO BOTTOM
        }

        // PREVENT SCROLL TO BOTTOM WHILE READING OLD MESSAGES/
        conv.hover(function (e) {
            e.preventDefault();
            preventNewScroll = e.type == 'mouseenter' ? true : false;
            if (!preventNewScroll) {
                scrollChat();
            } // On mouseleave go to bottom
        });
    }

    //Init event listeners
    this.init = function () {

        $('.collapsible').collapsible();

        var body = $('body');

        (homeService.enterBtn).click(enterChat);

        (homeService.registerBtn).click(openRegistration);

        body.on('keypress', function (e) {
            if (e.which == 13) {
                enterChat();
                return false;
            }
        });

        body.keydown(function (e) {
            if (!connected) {
                if (e.which === 40 || e.which === 39) {
                    homeService.nextRoom(e);
                }
                if (e.which === 37 || e.which === 38) {
                    homeService.previousRoom(e);
                }
            } else {
                if (e.which === 27) {
                    exit(e);
                }
            }
        });

        msg.on('keypress', function (e) {
            // on Post click or 'enter' but allow new lines using shift+enter
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
            switchRoom($(e.target).text());
        });

        //TODO scroll spy event listener
        conv.on('click', '.history', function () {
            showLastMessagePage();
        });


        (navBarService.search).on('keypress', function (e) {
            if(e.which === 13){
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
        $("body").addClass("loaded")
    }, 1000)
});

$(document).ready(function () {
    //TODO autofocus();

    var chatApp = (function () {
        var particlesConfig = './lib/particlesjs-config.json';
        var url = "localhost:8080/chat/"; // TODO static server
        // var url = "2e03dca4.ngrok.io/chat/";

        var chatService = new ChatService(url);
        chatService.init();

        //Init particlesjs
        var initParticles = function () {
            /* particlesJS.load(@dom-id, @path-json, @callback (optional)); */
            particlesJS.load('particles-js', particlesConfig);
        }();

        swal.setDefaults({background: '#546e7a'});

        //return {}
    })();
});

/*
 (function ($) { $(function () {
 }); // end of document ready })(jQuery); // end of jQuery name space
 */
