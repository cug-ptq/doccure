
function showIndexL_S_U(isLogin,isDoctor) {
    if (isLogin===true){
        $("#login-signup").hide();
        console.log(isLogin);
        //$("#userMenu").show();
    }
    else {
        //$("#login-signup").show();
        $("#userMenu").hide();
        console.log(isLogin);
    }
    if (isDoctor){
        $("#doctor_dashboard").show();
        $("#doctor_settings").show();
        $("#patient_dashboard").hide();
        $("#patient_settings").hide();
    }
    else {
        $("#doctor_dashboard").hide();
        $("#doctor_settings").hide();
        $("#patient_dashboard").show();
        $("#patient_settings").show();
    }
}