$("#submitBook").click(function (){
    if (!$("#terms_accept").is(":checked")){
        $("#messageBooking").text("请勾选是否确认预约信息");
    }
    else {
        let date = $("#date").val();
        let time = $("#time").val();
        if (date.length!==0&&time.length!==0){
            if (compareTime(date,time)){
                let appointTye = 0;
                if ($("#sign").is(":checked")){
                    appointTye = "签约";
                    booking(date,time,appointTye);
                }
                else if ($("#treat").is(":checked")){
                    appointTye = "就诊";
                    booking(date,time,appointTye);
                }
                else if ($("#visit").is(":checked")){
                    appointTye = "家访";
                    booking(date,time,appointTye);
                }
                else {
                    Notiflix.Notify.Failure("请勾选预约类型");
                }
            }
            else {
                Notiflix.Notify.Failure("请选择正确时间");
            }
        }
        else {
            Notiflix.Notify.Failure("请选择时间");
        }
    }
});
function booking(date,time,appointType) {
    $.ajax({
        url:"patient/booking",
        type:"post",
        dataType:"json",
        data:{"appointInfo":JSON.stringify({"date":date,"time":time,
                "appointType":appointType,"doctor_email":doctorInfo.email})},
        success:function (data){
            if (data.code === -1){
                Notiflix.Notify.Failure(data.message);
            }
            else {
                Notiflix.Notify.Success(data.message);
            }
        },
        error:function (){

        }
    });
}
function compareTime(date,time) {
    //day month year
    let year_month_day = date.split("-");
    let day = ""; let month = "";
    if (year_month_day[1][0] === "0"){
        month = year_month_day[1][1];
    }
    else {month=year_month_day[1];}
    if (year_month_day[2][0] === "0"){
        day = year_month_day[2][1];
    }
    else {day=year_month_day[2];}
    let appointTime = year_month_day[0]+"/"+month+"/"+day+" "+
        time;
    let nowTime = new Date();
    let now = nowTime.getFullYear()+"/"+nowTime.getMonth()+"/"+
        nowTime.getDay()+" "+nowTime.getHours()+":"+nowTime.getMinutes()+":"+nowTime.getSeconds();
    return now <= appointTime;
}