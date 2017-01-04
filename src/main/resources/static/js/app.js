//TODO UML
//TODO Add room
//TODO register url = /user : GET POST|PUT DELETE
//TODO room db table
//TODO profil & photo
//TODO notif toast & swal

"use strict";

/*
 WebSocket service for chatroom server
 */
function WebSocketService(url) {
    var webSocket, server;
    var serverLocation = url;

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
        console.log(server);
        webSocket = new WebSocket(server);
        callback();
    }

    //Disconnet from a room
    this.disconnect = function () {
        if (webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED) {
            webSocket.close();
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
    this.sendMessage = function (message) {
        webSocket.send(message);
    }

}

/*
 Search field service
 */
function SearchService() {
    var searchBar, search;
    searchBar = $(".search-bar");
    search = $("#search");

    //Show search field
    this.show = function () {
        searchBar.show();
    }

    //Hide search field
    this.hide = function () {
        searchBar.hide();
    }

}

/*
 Navigation Bar service with search field
 */
function NavBarService() {
    var loading = $("#loading");
    this.closeBtn = $("#close");

    var searchService = new SearchService();

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

}

/*
 Side navigation bar service
 */
function SideNavService() {
    var sideNavBar, sideNavBtn;
    sideNavBar = $("#slide-out");
    sideNavBtn = $(".button-collapse");

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
        room.material_select();
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
        initSelect();
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
        initSelect();
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

    //Get username TODO clean
    this.getUsername = function () {
        return (mail.val()).split("@")[0]; //TODO check get db username: if empty then do this line
    }

    //Get room
    this.getRoom = function () {
        return room.val();
    }

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
        //TODO clean console
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
                console.log("success");
            },
            error: function (data) {
                error();
                console.log("fail :", data);
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

    //Open Terms and Conditions of Service modal with captcha verification
    this.openCaptcha = function (callback) {
        if (registerForm.valid()) {
            swal({
                title: 'Terms and Conditions',
                html: '<p>By creating an account, you consent to the ' +
                '<a href="">Terms of Service</a> ' +
                'and the ' +
                '<a href="">Privacy Policy</a>.</p>' +
                '<div id="captcha" class="row"/>',
                type: 'info',
                background: '#546e7a',
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
                        background: '#546e7a',
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
            //TODO clean
            console.log('form ok');
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

    this.isSupported = function () {
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

    this.clearAll = function () {
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

    var getObject = function (key) {
        return session.getObject(key);
    };

}


/*
 Chat page service
 */
function ChatService(url) {
    //TODO clean var
    var session, mail, username, room, chat, conv, msg, bsend, msend, preventNewScroll, connected;
    preventNewScroll = false;
    connected = false;
    chat = $("#chat");
    conv = $("#conv");
    msg = $("#msg");
    bsend = $("#bsend");
    msend = $("#msend");

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
    var storeSession = function (user) {
        if (sessionService.isSupported()) {
            sessionService.setObject("user", user);
        } else {
            //TODO
        }
    }

    //Clear user session
    var clearSession = function () {
        if (sessionService.isSupported()) {
            sessionService.clearAll();
        } else {
            //TODO
        }
    }

    //Open user session
    var handleSession = function () {
        if (sessionService.isSupported()) {
            session = sessionService.getObject("user");
            if (session) {
                //TODO go to profil ?
                webSocketService.connect("IF", function () {
                    webSocketService.setHandler(handleMessage);
                    homeService.hide();
                    $('#login-room').text(username + "@" + room.toUpperCase());
                    sideNavService.init();
                    show();
                    connected = true;
                });
            }
        } else {
            //TODO
        }
    }

    //Open chatroom page
    var openChat = function () {
        //TODO clean timeout
        setTimeout(function () {
            navBarService.hideLoad();
            if (webSocketService.status() !== WebSocket.OPEN) {
                Materialize.toast("No connexion", 5000, "rounded");
            } else {
                webSocketService.setHandler(handleMessage);
                homeService.hide();
                $('#login-room').text(username + "@" + room.toUpperCase());
                sideNavService.init();
                show();
                connected = true;
            }
        }, 1000);
    }

    //Focus first empty visible enable input
    var autofocus = function () {
        //TODO update valid (& empty?)
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
            success: function () {
                alert("success");
                //TODO store session & clean username
                username = homeService.getUsername();
                navBarService.showLoad();
                webSocketService.connect(room, openChat);
            },
            error: function (data) {
                console.log(data);
                alert("fail");
                autofocus();
                //TODO notif
            }
        });
    }

    //Try enter chatroom
    var enterChat = function () {
        if (!connected) {
            mail = homeService.getMail();
            room = homeService.getRoom();
            //TODO if room null -> go to general chat or profil ?
            if ($.trim(room) && homeService.validForm()) {
                var session = {
                    mail: mail,
                    password: homeService.getPassword()
                }
                signin(session);
            } else {
                autofocus();
            }
        }
    }

    //Append message to chatroom
    var appendMessage = function (newMsg) {
        var sender, datetime, parag;
        var li = $('<li class="collection-item">');
        //TODO get username & user link
        var username = (newMsg.sender).split("@")[0];
        if (newMsg.sender == mail) {
            datetime = $('<small>' + newMsg.datetime + '</small>');
            sender = $('<strong class="right">' + username + '</strong>');
            parag = $('<p style="text-align: right">' + newMsg.message + '</p>');
            li.append(datetime).append(sender).append(parag);
        } else {
            sender = $('<strong>' + username + '</strong>');
            datetime = $('<small class="right">' + newMsg.datetime + '</small>');
            parag = $('<p>' + newMsg.message + '</p>');
            li.append(sender).append(datetime).append(parag);
        }
        conv.append(li);
    }

    //Handle message from server
    var handleMessage = function (e) {
        e.preventDefault();
        var msg = JSON.parse(e.data); // native API
        if (msg.sender == mail) {
            //TODO clean timeout
            setTimeout(function () {
                msend.hide();
                appendMessage(msg);
            }, 1000);
        } else {
            appendMessage(msg);
        }
    }

    //Send message in chatroom
    var sendMessage = function () {
        if ($.trim(msg.val())) {
            msend.show();
            //TODO check no connexion & hide loader + show message
            var newMsg = {
                message: msg.val().replace(/(?:\r\n|\r|\n)/g, '<br/>'),
                sender: mail,
            };

            webSocketService.sendMessage(JSON.stringify(newMsg));
            msg.val("").focus();
            scrollChat();
        }
    }

    //Exit
    var exit = function () {
        if (connected) {
            webSocketService.disconnect();
            sessionService.clearSession();
            hide();
            connected = false;
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

        var doc = $(document);

        (homeService.enterBtn).click(enterChat);

        (homeService.registerBtn).click(openRegistration);

        doc.on('keypress', function (e) {
            if (e.which == 13) {
                enterChat();
                return false;
            }
        });

        doc.keydown(function (e) {
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
                sendMessage(e);
                return false;
            }
        });

        bsend.click(sendMessage);

        registerService.refreshPasswordStrength();

        (registerService.validBtn).click(function () {
            registerService.openCaptcha(exit)
        });

        (navBarService.closeBtn).click(exit);

    }

}

$(window).on('load', function () {
    setTimeout(function () {
        $("body").addClass("loaded")
    }, 1000)
});

$(document).ready(function () {
    //TODO show loading
    //TODO autofocus();

    var chatApp = (function () {
        var particlesConfig = './lib/particlesjs-config.json';
        var url = "localhost:8080/chat/"; // TODO static server

        var chatService = new ChatService(url);
        chatService.init();

        //Init particlesjs
        var initParticles = function () {
            /* particlesJS.load(@dom-id, @path-json, @callback (optional)); */
            particlesJS.load('particles-js', particlesConfig);
        }();

        //return {}

    })();

});

/*
 (function ($) { $(function () {
 }); // end of document ready })(jQuery); // end of jQuery name space
 */
