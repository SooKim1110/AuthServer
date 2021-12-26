const authServerUrl = "http://localhost:8080"


let commonAjax = (url, data, method, success, error, complete) => {
    $.ajax({
        crossDomain: true,
        xhrFields: {
            withCredentials: true
        },
        url: url,
        data: data? JSON.stringify(data) : null,
        method: method,
        type: method,
        contentType: "application/json",
        success: success,
        error: error ? error: (e) => { console.log(e) },
        complete: complete
    })
}

let signup = () => {
    try {
        let url = authServerUrl + "/signup";
        let data = {
            "username": $("#username").val(),
            "password": $("#password").val(),
            "email": $("#email").val()
        }
        let method = "POST";
        let success = function (data) {
            $("#signup-result").append("회원가입이 완료되었습니다. 입력하신 이메일로 인증 메일이 전송되었습니다.");
        }
        let error = function (error) {
            $("#signup-result").append("회원가입에 실패했습니다.");
        }
        commonAjax(url, data, method, success, error, null);
    }
    catch(e){
        alert(e);
    }
}

let login = () => {
    try {
        let url = authServerUrl + "/login";
        let data = {
            "username": $("#username").val(),
            "password": $("#password").val(),
        }
        let method = "POST";
        let success = function (data) {
            window.location='/'
        }
        let error = function (error) {
            alert("error");
        }
        commonAjax(url, data, method, success, error, null);
    }
    catch(e){
        alert(e);
    }
}

let authenticateUser = () => {
    let url = authServerUrl + "/authenticate/user";
    let method = "GET";
    let success = () => {

    }
    let error = (e) => {
        window.location='/login'
    }
    commonAjax(url,null, method, success, error,null)
}

let checkAuthenticate = () => {
    let url = authServerUrl + "/authenticate";
    let method = "GET";
    let success = () => {
        let authorized = document.getElementById("authenticate");
        authorized.innerHTML = "<button id=\"logout-btn\" onClick=\"logout()\" class=\"btn btn-outline-danger my-2 my-sm-0 btn-sm\" type=\"button\">Logout</button>";
    }
    let error = (e) => {
        let authorized = document.getElementById("authenticate");
        authorized.innerHTML = "<button onclick=\"location.href='/login'\" class=\"btn btn-outline-info my-2 my-sm-0 btn-sm\" type=\"button\">Login</button>";
    }
    commonAjax(url,null, method,success,error,null)
}

let logout = () => {
    let url = authServerUrl + "/logoutuser";
    let method = "POST";
    let success = (result) => {
        window.location='/login'
    }
    let error = (e) => {
        console.log(e);
    }
    commonAjax(url,null,method ,success,error,null)
}


let getUserList = () => {
    let url = authServerUrl + "/admin";
    let method = "GET";
    let success = (result) => {
        updateTable(result)
    }
    let error = (e) => {
        window.location='/login'
    }
    commonAjax(url,null,method ,success,error,null)
}

let updateTable = (userList) => {
    let table = document.getElementById("userTable")
    for(let i = 1; i <= userList.length; i++) {
        let row = table.insertRow(i);
        let user = userList[i-1];
        let idxCell = row.insertCell(0);
        let nicknameCell = row.insertCell(1);
        let emailCell = row.insertCell(2);
        let roleCell = row.insertCell(3);
        let deleteCell = row.insertCell(4);

        idxCell.innerText = String(i);
        nicknameCell.innerText = user.username;
        emailCell.innerText = user.email;
        roleCell.innerText = user.role;
        deleteCell.innerHTML = "<td><button onclick=\"location.href='/'\" class=\"btn btn-outline-danger btn-sm\" type=\"button\">삭제</button></td>"
    }
}