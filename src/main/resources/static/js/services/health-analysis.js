function changePatientEmailH(email) {
    if (email.indexOf(".")!==-1){
        patient_emailH = email;
        console.log(patient_emailH);
        if (endTime.length!==0 && beginTime.length!==0 && endTime>beginTime){
            if (healthDataTypeH.length!==0){
                getDataAjax();
            }
            else {
                Notiflix.Notify.Info("请选择数据项");
            }
        }
        else {
            Notiflix.Notify.Info("请正确选择时间或时间");
        }
    }
}
//改变历史数据类型
function changeDataTypeH(type) {
    healthDataTypeH = type;
    if (patient_emailH.length===0){
        Notiflix.Notify.Info("请选择病人");
    }
    else {
        if (endTime.length!==0 && beginTime.length!==0 && endTime>beginTime){
            getDataAjax();
        }
        else if (healthDataTypeH.length===0){
            Notiflix.Notify.Info("请选择数据项");
        }
        else {
            Notiflix.Notify.Info("请选择时间");
        }
    }
}

function changeBeginTime(time) {
    beginTime = timeDeal(time,5) + ":00";
    if (endTime.length!==0 && beginTime.length!==0 && endTime>beginTime){
        if (patient_emailH.length===0){
            Notiflix.Notify.Info("请选择病人");
        }
        else if (healthDataTypeH.length===0){
            Notiflix.Notify.Info("请选择数据项");
        }
        else {
            getDataAjax();
        }
    }
    else {
        Notiflix.Notify.Info("请正确选择时间");
    }
}
function changeEndTime(time) {
    endTime = timeDeal(time,5) + ":00";
    if (endTime.length!==0 && beginTime.length!==0 && endTime>beginTime){
        if (patient_emailH.length===0){
            Notiflix.Notify.Info("请选择病人");
        }
        else {
            getDataAjax();
        }
    }
}

function getDataAjax() {
    $.ajax({
        url: "/healthData/getHistory",
        type: "post",
        dataType: "json",
        data: {"email": patient_emailH, "healthDataType": healthDataTypeH, "beginTime": beginTime,"endTime":endTime,"internal":internal},
        success: function (data) {
            if (data.code === 1) {
                let healthDataInfo = JSON.parse(data.message);let formatter = "";
                let xData = [];let yData = [];let healthData = JSON.parse(healthDataInfo.data);
                if (healthData.length!==0){
                    if (healthDataTypeH==="血压"){
                        let yData1 = []; let yData2 = [];
                        for (let i = 0;i < healthData.length;i++){
                            xData.push(timeDeal(healthData[i].time));
                            yData1.push(healthData[i].health_data1.replace(/[^\d]/g,""));
                            yData2.push(healthData[i].health_data2.replace(/[^\d]/g,""));
                            formatter = healthData[i].health_data1.replace(/[0-9]/g,"")
                        }
                        yData.push(yData1);yData.push(yData2);
                        let nameOption = ["收缩压" + "变化曲线","舒张压"+"变化曲线"];
                        historyChart.setOption(getOptionDataHighLow(healthDataInfo.text,nameOption,xData,yData,"{value}"+formatter));
                    }
                    else {
                        for (let i = 0;i < healthData.length;i++){
                            xData.push(timeDeal(healthData[i].time));
                            yData.push(healthData[i].health_data1.replace(/[^\d]/g,""));
                            formatter = healthData[i].health_data1.replace(/[0-9]/g,"")
                        }
                        historyChart.setOption(getSingleOptionChart(healthDataInfo.text,healthDataInfo.nameOption,xData,yData,"{value}"+formatter))
                    }
                }
                else {
                    Notiflix.Notify.Info("病人不含有此数据记录后该时间段没有此数据记录");
                }
                //血压
            }
        }
    });
}

function getSingleOptionChart(text=" ",nameOption=" ",xData=[],yData=[],formatter="") {
    const colors = ['#808080','#5470C6'];
    return {
        title: {
            text: text
        },
        color: colors,
        tooltip: {
            trigger: 'none',
            axisPointer: {
                type: 'cross'
            }
        },
        legend: {},
        grid: {
            top: 70,
            bottom: 50
        },
        xAxis: [
            {
                type: 'category',
                name: '时间',
                axisTick: {
                    alignWithLabel: true
                },
                axisLine: {
                    onZero: false,
                    lineStyle: {
                        color: colors[0]
                    }
                },
                axisPointer: {
                    label: {
                        formatter: function (params) {
                            return (
                                'Value:  ' +
                                params.value +
                                (params.seriesData.length ? '：' + params.seriesData[0].data : '')
                            );
                        }
                    }
                },
                // prettier-ignore
                data: xData
            }
        ],
        yAxis: [
            {
                type: 'value',
                axisLabel:{
                    formatter:formatter
                }
            }
        ],
        series: [
            {
                name: nameOption,
                type: 'line',
                smooth: true,
                emphasis: {
                    focus: 'series'
                },
                lineStyle: {
                    normal:{
                        color: colors[1]
                    }
                },
                data: yData,
                markLine: {
                    data: [{ type: 'average', name: 'Avg' }]
                }
            }
        ]
    };
}

function getOptionDataHighLow(text,nameOption,xData,data,formatter) {
    const colors = ['#808080','#5470C6', '#e066ee'];
    let high = data[0];let low = data[1];
    return {
        title: {
            text: text
        },
        color: colors,
        tooltip: {
            trigger: 'none',
            axisPointer: {
                type: 'cross'
            }
        },
        legend: {},
        grid: {
            top: 70,
            bottom: 50
        },
        xAxis: [
            {
                type: 'category',
                name: '时间',
                axisTick: {
                    alignWithLabel: true
                },
                axisLine: {
                    onZero: false,
                    lineStyle: {
                        color: colors[0]
                    }
                },
                axisPointer: {
                    label: {
                        formatter: function (params) {
                            return (
                                'Value:  ' +
                                params.value +
                                (params.seriesData.length ? '：' + params.seriesData[0].data : '')
                            );
                        }
                    }
                },
                // prettier-ignore
                data: xData
            }
        ],
        yAxis: [
            {
                type: 'value',
                axisLabel:{
                    formatter:formatter
                }
            }
        ],
        series: [
            {
                name: nameOption[0],
                type: 'line',
                smooth: true,
                emphasis: {
                    focus: 'series'
                },
                lineStyle: {
                    normal:{
                        color: colors[1]
                    }
                },
                data: high,
                markLine: {
                    data: [{ type: 'average', name: 'Avg' }]
                }
            },
            {
                name: nameOption[1],
                type: 'line',
                smooth: true,
                emphasis: {
                    focus: 'series'
                },
                lineStyle: {
                    normal:{
                        color: colors[2]
                    }
                },
                data: low,
                markLine: {
                    data: [{ type: 'average', name: 'Avg' }]
                }
            }
        ]
    };
}

function timeDeal(time,last=8,T=false) {
    let timeArray;let result;
    if(time.indexOf("T")!==-1){
        timeArray = time.split("T");
        result = timeArray[0] + " " + timeArray[1].substr(0,last);
    }
    else{
        result = time;
        timeArray = time.split(" ");
    }
    if (T){
        return timeArray[0] + "T" + timeArray[1].substr(0,last);
    }
    return result;
}

$("#embody").click(function (){
    if (internal!==1) {
        internal--;
    }
    afterChangeInternal();
});
$("#vague").click(function (){
    if (internal!==10){
        internal++;
    }
    afterChangeInternal();
});

function afterChangeInternal() {
    if (patient_emailH.length!==0){
        if (endTime.length!==0 && beginTime.length!==0 && endTime>beginTime){
            if (healthDataTypeH.length!==0){
                getDataAjax();
            }
            else {
                Notiflix.Notify.Info("请选择数据项");
            }
        }
        else {
            Notiflix.Notify.Info("请正确选择时间");
        }
    }
    else {
        Notiflix.Notify.Info("请选择病人");
    }
}

//实时显示
function changePatientEmailM(email) {
    monitorChart.setOption(getSingleOptionChart());
    Notiflix.Notify.Info("请继续选择数据项");
    patient_emailM = email;
    $.ajax({
        url:"/healthData/cancel",
        type:"type",
        dataType:"json",
        success:function (data) {
            if (data.code){
                console.log(data.message);
            }
        }
    });
}

//改变实时类型
function changeDataTypeM(type) {
    yDataM = [];
    xDataM = [];
    healthDataTypeM = type;
    $.ajax({
        url: "/healthData/update",
        type: "post",
        dataType: "json",
        data: {"email":patient_emailM,"healthDateType":healthDataTypeM},
        success: function (data){
            if (data.code===-1){
                Notiflix.Notify.Info(data.message);
                monitorChart.clear();
            }
        }
    });
}

function monitorCharts(data) {
    if (data.code){
        let healthDataInfo = JSON.parse(data.message);let formatter = "";
        let healthData = JSON.parse(healthDataInfo.data);
        if (healthDataTypeM === "血压"){
            if (yDataM.length!==0){
                yDataM[0].shift();yDataM[1].shift();
            }
            else {
                yDataM.push([]);yDataM.push([]);
            }
            if (xDataM.length!==0){
                xDataM.shift();
            }
            for (let i = 0;i < healthData.length;i++){
                xDataM.push(timeDeal(healthData[i].time));
                yDataM[0].push(healthData[i].health_data1.replace(/[^\d]/g,""));
                yDataM[1].push(healthData[i].health_data2.replace(/[^\d]/g,""));
                formatter = healthData[i].health_data1.replace(/[0-9]/g,"")
            }
            let nameOption = ["收缩压" + "变化曲线","舒张压"+"变化曲线"];
            monitorChart.setOption(getOptionDataHighLow(healthDataInfo.text,nameOption,
                xDataM,yDataM,"{value}"+formatter));
        }
        else {
            yDataM.shift();
            for (let i = 0;i < healthData.length;i++){
                yDataM.push(healthData[i].health_data1.replace(/[^\d]/g,""));
                xDataM.push(timeDeal(healthData[i].time));
                formatter = healthData[i].health_data1.replace(/[0-9]/g,"")
            }
            monitorChart.setOption(getSingleOptionChart(healthDataInfo.text,healthDataInfo.nameOption,
                xDataM,yDataM,"{value}"+formatter));
        }
    }
    else {
        Notiflix.Notify.Info("此数据项没有实时数据");
    }
}

//appoint/patient
function getAppointPieChart(data) {
    return {
        title: {
            text: '我的预约类型比例',
            // subtext: 'Fake Data',
            left: 'center'
        },
        tooltip: {
            trigger: 'item'
        },
        legend: {
            orient: 'vertical',
            left: 'left'
        },
        series: [
            {
                name: 'Value',
                type: 'pie',
                radius: '50%',
                data: data,
                emphasis: {
                    itemStyle: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };
}

function getBarSpeciality(specialties,specialityNum) {
    return {
        title: {
            text: '各类医生被预约量',
            // subtext: 'Fake Data',
            left: 'center'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: [
            {
                type: 'category',
                data: specialties,
                axisTick: {
                    alignWithLabel: true
                }
            }
        ],
        yAxis: [
            {
                type: 'value',
                name: '数量'
            }
        ],
        series: [
            {
                name: 'Value',
                type: 'bar',
                barWidth: '60%',
                data: specialityNum
            }
        ]
    };
}

function setOtherCharts(data) {
    data = JSON.parse(data);
    let appointTypeNum = data.appointTypeNum;
    for (let i = 0;i < appointTypeNum.length;i++){
        appointTypeNum[i] = JSON.parse(appointTypeNum[i]);
    }
    appointTypeNumChart.setOption(getAppointPieChart(appointTypeNum));
    specialityChart.setOption(getBarSpeciality(data.specialties,data.specialityNum));
}