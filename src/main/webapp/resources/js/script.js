$(document).ready(function(e) {
        
   
    $("#goTopId").click(function() {
		event.preventDefault();
		
		$("html, body").animate({
			scrollTop:$("#myHeader").offset().top
		}, 800);
	});
	
	$("#goTopId").hide();
	$(window).scroll(function() {
		if ($(window).scrollTop() > 70) {
			if ($("#goTopId").is(":hidden"))
				$("#goTopId").show("slow");
		} else {
			$("#goTopId").hide("slow");
		}
	});
        
    
     statusButton(1);
      
});


   
    //Thông số vòng quay
            let duration = 5; //Thời gian kết thúc vòng quay
            let spins    = 15; //Quay nhanh hay chậm 3, 8, 15
            let theWheel = new Winwheel({
                'numSegments'  : 10,     // Chia 8 phần bằng nhau
                'outerRadius'  : 212,   // Đặt bán kính vòng quay
                'textFontSize' : 18,    // Đặt kích thước chữ
                'rotationAngle': 0,     // Đặt góc vòng quay từ 0 đến 360 độ.
                'segments'     :        // Các thành phần bao gồm màu sắc và văn bản.
                [
                   {'fillStyle' : '#eae56f', 'text' : '30 điểm','value':30},
                   {'fillStyle' : '#89f26e', 'text' : '5 điểm','value':5},
                   {'fillStyle' : '#eae56f', 'text' : '10 điểm','value':10},
                   {'fillStyle' : '#7de6ef', 'text' : '45 điểm','value':45},
                   {'fillStyle' : '#e7706f', 'text' : 'XUI XẺO','value':-1},
                   {'fillStyle' : '#eae56f', 'text' : '100 điểm', 'value':100},
                   {'fillStyle' : '#e7706f', 'text' : '2 điểm','value':2},
                   {'fillStyle' : '#89f26e', 'text' : 'XUI XẺO','value':-1},
                   {'fillStyle' : '#7de6ef', 'text' : '18 điểm', 'value':18},
                   {'fillStyle' : '#e7706f', 'text' : 'Mất điểm', 'value':-2}
                ],
                'animation' : {
                    'type'     : 'spinToStop',
                    'duration' : duration,
                    'spins'    : spins,
                    'callbackSound'    : playSound,     //Hàm gọi âm thanh khi quay
                    'soundTrigger'     : 'pin',         //Chỉ định chân là để kích hoạt âm thanh
                    'callbackFinished' : alertPrize,    //Hàm hiển thị kết quả trúng giải thưởng
                },
                'pins' :
                {
                    'number' : 32   //Số lượng chân. Chia đều xung quanh vòng quay.
                }
            });
            
            //Kiểm tra vòng quay
            let wheelSpinning = false;
            
            //Tạo âm thanh và tải tập tin tick.mp3.
            let audio = new Audio('./resources/music/tick.mp3');
            function playSound() {
                audio.pause();
                audio.currentTime = 0;
                audio.play();
            }
            
            //Hiển thị các nút vòng quay
            function statusButton(status) {
                if ( status==1 ) { //trước khi quay
                    
                    document.getElementById("formdapan:nhapdapan").classList.add("hide");
                    document.getElementById("formquay:spin_start").removeAttribute("disabled");
                    document.getElementById("btndoanra:spin_doanra").classList.remove("hide");
                    //document.getElementById("spin_reset").classList.add("hide");
                    
                } else if ( status==2 ) { //đang quay
                    document.getElementById("btndoanra:spin_doanra").classList.add("hide");
                    document.getElementById("formquay:spin_start").setAttribute("disabled", false);
                    //document.getElementById("spin_reset").classList.add("hide");
                }else {
                    alert('Các giá trị của status: 1, 2');
                }
            }
            
            
            //startSpin
            function startSpin() {
                document.getElementById("countdown").innerHTML = '';
                // Ensure that spinning can't be clicked again while already running.
                if (wheelSpinning == false) {
                    //CSS hiển thị button
                    statusButton(2);
                    
                    //Hàm bắt đầu quay
                    theWheel.startAnimation();

                    //Khóa vòng quay
                    wheelSpinning = true;
                }

            }
            //chon button doan
            function doanRa() {
                document.getElementById("formdapan:loaitl").value = 1;
                document.getElementById("btndoanra:spin_doanra").classList.add("hide");
                document.getElementById("formquay:spin_start").setAttribute("disabled", false);
                document.getElementById("formdapan:nhapdapan").classList.remove("hide");
            }
            
            //Result
            function alertPrize(indicatedSegment) {
                //alert("Chúc mừng bạn trúng: " + indicatedSegment.value);
                 
                 if(indicatedSegment.value==-1)
                 {
                     let au = new Audio('./resources/music/error.mp3');
                        au.pause();
                        au.currentTime = 0;
                        au.play();
                     document.getElementById("formxx:txtloaitruot").value = 0;
                     document.getElementById("formxx:btnxx").click();
                     resetWheel1();
                 }
                 else
                     if(indicatedSegment.value==-2)
                    {
                        let au = new Audio('./resources/music/error.mp3');
                           au.pause();
                           au.currentTime = 0;
                           au.play();
                        document.getElementById("formxx:txtloaitruot").value = -1;
                        document.getElementById("formxx:btnxx").click();
                        resetWheel1();
                    }
                 else
                 {
                     document.getElementById("formdapan:nhapdapan").classList.remove("hide");
                     document.getElementById("formdapan:diemquay").value = indicatedSegment.value;
                     //countdown 10s
                     countDown();
                 }
                 
                 //document.getElementById("formdapan:diemquay").value = indicatedSegment.text;
                //CSS hiển thị button
                //statusButton(3);
            }

            //resetWheel
            function resetWheel() {
                //CSS hiển thị button
                statusButton(1);
                
                theWheel.stopAnimation(false);  // Stop the animation, false as param so does not call callback function.
                theWheel.rotationAngle = 0;     // Re-set the wheel angle to 0 degrees.
                theWheel.draw();                // Call draw to render changes to the wheel.

                wheelSpinning = false;          // Reset to false to power buttons and spin can be clicked again.
                document.getElementById("formdapan:nhapdapan").classList.add("hide");
            }
            function resetWheel1() {
                //CSS hiển thị button
                statusButton(1);
                
                theWheel.stopAnimation(false);  // Stop the animation, false as param so does not call callback function.
                theWheel.rotationAngle = 0;     // Re-set the wheel angle to 0 degrees.
                //theWheel.draw();                // Call draw to render changes to the wheel.

                wheelSpinning = false;          // Reset to false to power buttons and spin can be clicked again.
                document.getElementById("formdapan:nhapdapan").classList.add("hide");
            }
            countdown = null;
            function countDown()
            {

                
                    
                        var seconds = 15;
                        countdown = setInterval(function() {
                        s = --seconds;
                        document.getElementById("countdown").innerHTML  = s +"s";
                        console.log(seconds);
                        if (seconds <= 0){
                            clearInterval(countdown);
                            document.getElementById("formxx:txtloaitruot").value = 1;
                            document.getElementById("formxx:btnxx").click();
                            resetWheel1();
                        }
                        }, 1000);
                        
                    
//                }
//                else
//                    clearInterval($('countdown').data('interval'));
            }
            
             stop = function(){
                    clearInterval(countdown);
            }
            
  function doantrung()
  {
      let au = new Audio('./resources/music/doantrung.mp3');
                           au.pause();
                           au.currentTime = 0;
                           //au.loop = true;
                           au.play();
  }
  function doansai()
  {
      let au = new Audio('./resources/music/tlsai.mp3');
                           au.pause();
                           au.currentTime = 0;
                           //au.loop = true;
                           au.play();
  }
  function endgame()
  {
      let au = new Audio('./resources/music/endgame.mp3');
                           au.pause();
                           au.currentTime = 0;
                           //au.loop = true;
                           au.play();
  }
 ///////////////////phao hoa/////////////////////
 const max_fireworks = 5,
            max_sparks = 50;
    let canvas = document.getElementById('canvastrung');
    let context = canvas.getContext('2d');
    let fireworks = [];
 function setPhaoHoa()
{
    
    let au = new Audio('./resources/music/phaohoa.mp3');
                           au.pause();
                           au.currentTime = 0;
                           au.loop = true;
                           au.play();
    for (let i = 0; i < max_fireworks; i++) {
        let firework = {
            sparks: []
        };
        for (let n = 0; n < max_sparks; n++) {
            let spark = {
                vx: Math.random() * 5 + .5,
                vy: Math.random() * 5 + .5,
                weight: Math.random() * .3 + .03,
                red: Math.floor(Math.random() * 2),
                green: Math.floor(Math.random() * 2),
                blue: Math.floor(Math.random() * 2)
            };
            if (Math.random() > .5)
                spark.vx = -spark.vx;
            if (Math.random() > .5)
                spark.vy = -spark.vy;
            firework.sparks.push(spark);
        }
        fireworks.push(firework);
        resetFirework(firework);
    }
    window.requestAnimationFrame(explode);
}
 
function resetFirework(firework) {
  firework.x = Math.floor(Math.random() * canvas.width);
  firework.y = canvas.height;
  firework.age = 0;
  firework.phase = 'fly';
}
 
function explode() {
  context.clearRect(0, 0, canvas.width, canvas.height);
  fireworks.forEach((firework,index) => {
    if (firework.phase == 'explode') {
        firework.sparks.forEach((spark) => {
        for (let i = 0; i < 10; i++) {
          let trailAge = firework.age + i;
          let x = firework.x + spark.vx * trailAge;
          let y = firework.y + spark.vy * trailAge + spark.weight * trailAge * spark.weight * trailAge;
          let fade = i * 20 - firework.age * 2;
          let r = Math.floor(spark.red * fade);
          let g = Math.floor(spark.green * fade);
          let b = Math.floor(spark.blue * fade);
          context.beginPath();
          context.fillStyle = 'rgba(' + r + ',' + g + ',' + b + ',1)';
          context.rect(x, y, 4, 4);
          context.fill();
        }
      });
      firework.age++;
      if (firework.age > 100 && Math.random() < .05) {
        resetFirework(firework);
      }
    } else {
      firework.y = firework.y - 10;
      for (let spark = 0; spark < 15; spark++) {
        context.beginPath();
        context.fillStyle = 'rgba(' + index * 50 + ',' + spark * 17 + ',0,1)';
        context.rect(firework.x + Math.random() * spark - spark / 2, firework.y + spark * 4, 4, 4);
        context.fill();
      }
      if (Math.random() < .001 || firework.y < 200) firework.phase = 'explode';
    }
  });
  window.requestAnimationFrame(explode);
}