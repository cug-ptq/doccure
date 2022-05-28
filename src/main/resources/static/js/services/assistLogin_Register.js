//修改密码
$("#submitChangePassword").click(function () {
    let old_password = $("#old_password").val();
    let new_password = $("#new_password").val();
    let confirm_password = $("#confirm_password").val();
    $.ajax({
        url:"/changePassword",
        type:"post",
        dataType:"json",
        data:{"info":JSON.stringify({"old_password":old_password,"new_password":new_password,
            "confirm_password":confirm_password})},
        success:function (data) {
            console.log(data)
            if (data.code===1){
                Notiflix.Notify.Success(data.message);
            }
            else {
                Notiflix.Notify.Failure(data.message);
            }
        },
        error:function () {
            $("#messageChange").text("修改失败");
        }
    });
});

//登录
$("#submitLogin").click(function (){
    let email = $("#email").val();
    let password = $("#password").val();
    $.ajax({
        url:"/login/auth",
        type:"POST",
        dataType:"json",
        data:{"userInfo":JSON.stringify({"email":email,"password":password})},
        success:function (data){
            if (data.code===-1){
                Notiflix.Notify.Failure(data.message);
            }
            else {
                console.log(data);
                if (data.type === "admin"){
                    window.location.href = "doctor-apply";
                }else {
                    window.location.href = "/index";
                }
            }
        },
        error:function (data){
            Notiflix.Notify.Failure("请求失败");
        }
    });
});

//病人注册
$("#submitPatientSignup").click(function (){
    let username = $("#username").val();
    let email = $("#email").val();
    let password = $("#password").val();
    $.ajax({
        url:"/register/patient",
        type:"POST",
        dataType:"json",
        data:{"userInfo":JSON.stringify({"username":username,"email":email,"password":password})},
        success:function (data){
            if (data.code===-1){
                // $("#messagePatientSign").text(data.message);
                Notiflix.Notify.Failure(data.message);
            }
            else {
                window.location.href = "index";
            }
        },
        error:function (data){
            Notiflix.Notify.Failure("请求失败");
        }
    });
});

$("#submitDoctorApply").click(function () {
    let form = new FormData();
    let username = $("#username").val();
    let email = $("#email").val();
    let password = $("#password").val();
    let specialty = $("#specialty").find('option:selected').text();
    form.append("userInfo",JSON.stringify({"username":username,"email":email,"password":password,
        "specialty":specialty}));
    if ($("input#doctorImg")[0].files.length===0){
        Notiflix.Notify.Failure("未上传本人图片");
    }
    else if ($("input#doctorIdentifyFile")[0].files.length===0){
        Notiflix.Notify.Failure("未上传医生身份证明文件");
    }
    else {
        form.append("doctorImg",$('input#doctorImg')[0].files[0]);
        if ($("input#doctorIdentifyFile")[0].files.length!==0){
            form.append("doctorIdentifyFile",$('input#doctorIdentifyFile')[0].files[0]);
        }
        $.ajax({
            url: "/register/doctor",
            type: "post",
            dataType:"json",
            data:form,
            cache: false,
            processData: false,
            contentType: false,
            success:function (data){
                Notiflix.Notify.Success(data.message);
            },
            error:function () {
                Notiflix.Notify.Failure("请求失败");
            }
        });
    }
});

$("#queryDoctorApply").click(function (){
    let email = $("#query_email").val();
    let password = $("#query_password").val();
    $.ajax({
        url:"/query/registerResult",
        type:"post",
        dataType:"json",
        data:{"doctor_email":email,"doctor_password":password},
        success:function (data){
            console.log(data);
            if (data.code===1){
                Notiflix.Notify.Success(data.message);
            }
            else {
                if (data.type==="message"){
                    editor.txt.html(data.message);
                }
                else {
                    Notiflix.Notify.Info(data.message);
                }
            }
        }
    });
});