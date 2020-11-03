$(document).ready(function(e) {
        

      runChartPoint();
     runChartCauhoi();
     runChartUser();
});
////////////////////////////////////// Ve so do/////////////////////////
function runChartPoint()
        {
            $.ajax({
                type: 'GET',
    //            headers : {
    //                Accept : "application/json; charset=utf-8",
    //                "Content-Type" : "application/json; charset=utf-8"
    //            },
                url: 'viewjson.xhtml?loai=1',
                dataType: "json",
                success: function (result) {
                    google.charts.load('current', {
                        'packages': ['corechart']
                    });
                    google.charts.setOnLoadCallback(function () {
                        drawChartPoint(result);
                    });
                }
            });
        }

        function drawChartPoint(result) {

            var data = new google.visualization.DataTable();
            data.addColumn('string', 'Ngày');
            data.addColumn('number', 'Đúng');
            data.addColumn('number', 'Đoán');
            data.addColumn('number', 'Sai');
//            var dataArray = [];
//            $.each(result, function(i, obj) {
//                dataArray.push([ obj.month, obj.dung, obj.doan, obj.sai ]);
//            });
            
            for (var i = 0; i < result.length; i++) {
                data.addRow([result[i].day, result[i].dung,result[i].doan, result[i].sai]);
                
            }
           
            var piechart_options = {
                title : 'Thống kê tổng điểm trong tháng với 3 loại(trả lời đúng, trả lời sai, đoán được ô chữ) ',
                height : 700
            };
            var piechart = new google.visualization.LineChart(document
                    .getElementById('chartpoint_div'));
            piechart.draw(data, piechart_options);

        }
        
        //////////////////////////////////////
        function runChartCauhoi()
        {
            $.ajax({
                type: 'GET',
    //            headers : {
    //                Accept : "application/json; charset=utf-8",
    //                "Content-Type" : "application/json; charset=utf-8"
    //            },
                url: 'viewjson.xhtml?loai=2',
                dataType: "json",
                success: function (result) {
                    google.charts.load('current', {
                        'packages': ['corechart']
                    });
                    google.charts.setOnLoadCallback(function () {
                        drawChartCauhoi(result);
                    });
                }
            });
        }

        function drawChartCauhoi(result) {

            var data = new google.visualization.DataTable();
            data.addColumn('string', 'Ngày');
            data.addColumn('number', 'Đúng');
            data.addColumn('number', 'Đoán');
            data.addColumn('number', 'Sai');
//            var dataArray = [];
//            $.each(result, function(i, obj) {
//                dataArray.push([ obj.month, obj.dung, obj.doan, obj.sai ]);
//            });
            
            for (var i = 0; i < result.length; i++) {
                data.addRow([result[i].day, result[i].dung,result[i].doan, result[i].sai]);
                
            }
           
            var piechart_options = {
                title : 'Thống kê số lượng câu hỏi trong tháng với 3 loại(trả lời đúng, trả lời sai, đoán được ô chữ) ',
                height : 700
            };
            var piechart = new google.visualization.LineChart(document
                    .getElementById('chartcauhoi_div'));
            piechart.draw(data, piechart_options);

        }
        //////////////////////////////////////////
        function runChartUser()
        {
            $.ajax({
                type: 'GET',
    //            headers : {
    //                Accept : "application/json; charset=utf-8",
    //                "Content-Type" : "application/json; charset=utf-8"
    //            },
                url: 'viewjson.xhtml?loai=3',
                dataType: "json",
                success: function (result) {
                    google.charts.load('current', {
                        'packages': ['table']
                    });
                    google.charts.setOnLoadCallback(function () {
                        drawChartUser(result);
                    });
                }
            });
        }

        function drawChartUser(result) {

            var data = new google.visualization.DataTable();
            data.addColumn('string', 'Ngày');
            data.addColumn('string', 'Thành viên nhiều điểm');
            data.addColumn('number', 'Tổng điểm');
            data.addColumn('string', 'Thành viên trả lời đúng nhiều');
            data.addColumn('number', 'Tổng câu');
//            var dataArray = [];
//            $.each(result, function(i, obj) {
//                dataArray.push([ obj.month, obj.dung, obj.doan, obj.sai ]);
//            });
            
            for (var i = 0; i < result.length; i++) {
                data.addRow([result[i].day,result[i].userPoint, result[i].point,result[i].userCount, result[i].count]);
                
            }
           
            var piechart_options = {
                title : 'Thống kê thành viên trả lời nhiều câu và đạt nhiều điểm nhất ',
                height : 'auto',
                width : 'auto'
            };
            var table = new google.visualization.Table(document
                    .getElementById('table_div'));
            table.draw(data, piechart_options);

        }
        ////////////////////////////
        function runAllChart()
        {
            runChartPoint();
            runChartCauhoi();
            runChartUser();
        }
   ////////////////////////////Hết vẽ sơ đồ/////////////////////////////