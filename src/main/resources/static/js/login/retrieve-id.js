$(document).ready(function(){
    // 타이머 시작 시간 (2분)
    let timeLeft = 2 * 60;

    // 타이머를 업데이트하는 함수
    function updateTimer() {
        const minutes = Math.floor(timeLeft / 60); // 남은 분 계산
        const seconds = timeLeft % 60; // 남은 초 계산

        // 화면에 표시
        $("#timer").text(
            `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`
        );

        // 시간이 0이 되면 타이머 중지
        if (timeLeft <= 0) {
            clearInterval(timerInterval);
        } else {
            timeLeft--; // 시간 감소
        }
    }
    // 매초마다 updateTimer 함수 실행
    let timerInterval = setInterval(updateTimer, 1000);

    // 모달창 닫기
    $("#btn-id-cross").on("click", function(){
        $("#id-modal").css("display", "none");
        window.location.href = "/login";
    })
    // 전화번호, 인증번호 숫자만
    $("#phone2, #phone3, #input-code").on('keypress', function (event) {
        // 숫자 (0-9)만 입력 가능하게 설정
        if (event.which < 48 || event.which > 57) {
            // 백스페이스 (8), 탭 (9), 화살표 키 등은 허용
            if (event.which !== 8 && event.which !== 9) {
                event.preventDefault();
            }
        }
    });
    // 유효성
    $("#phone1, #phone2, #phone3, #user_name").on("input change", function() {
        $("#msg-not-match").css("display", "none");
        $("#user_telno").val("");
        $("#btn-retrieve").prop("disabled", true);
        $("#btn-certify").val("인증");
        let code = $("#input-code").val();
        if(code.length==6){
            $("#btn-certify").prop("disabled", false);
        }else{
            $("#btn-certify").prop("disabled", true);
        }
        let phone1 = $("#phone1").val();
        let phone2 = $("#phone2").val();
        let phone3 = $("#phone3").val();
        let name = $("#user_name").val();
        let phone = phone1 + "-" + phone2 + "-" + phone3;
        let phoneRegex = /^01[0-9]-\d{3,4}-\d{4}$/;
        if (phoneRegex.test(phone) && name.length>1){
            $("#btn-send").prop("disabled", false);
            $("#user_telno").val(phone);
            $("#btn-resend").prop("disabled", false);
        } else{
            $("#btn-send").prop("disabled", true);
            $("#btn-resend").prop("disabled", true);
        }
    });
    // 인증하기 버튼 활성화
    $("#input-code").on("input", function(){
        $("#msg-not-match").css("display", "none");
        $("#btn-certify").prop("disabled", true).val("인증");
        let code = $(this).val();
        if(code.length==6){
            $("#btn-certify").prop("disabled", false);
        }else{
            $("#btn-certify").prop("disabled", true);
        }
    })
    // 인증번호 전송
    $("#btn-send, #btn-resend").on("click", function(){
        $.ajax({
            type: 'GET',
            url: '/login/exist/pw',
            data: {
                name: $("#user_name").val(),
                telno: $("#user_telno").val()
            },
            success: function(response) {
                if(!response){
                    alert("일치하는 정보가 존재하지 않습니다.");
                }else{
                    $.ajax({
                        type: 'GET',
                        url: '/login/send-auth-code',
                        data: { telno: $("#user_telno").val() },
                        success : function(response){
                            if(response){
                                $("#code-container").css("display", "block");
                                $("#btn-send").css("display", "none");
                                $("#btn-retrieve").css("display", "block");
                                alert(response);
                                if(response==="인증횟수 초과"){
                                    // 타이머 멈추기
                                    clearInterval(timerInterval);
                                    alert("인증횟수가 초과되었습니다. 24시간 뒤 시도해주세요.");
                                }
                                // 타이머 초기화
                                clearInterval(timerInterval); // 이전 타이머 중지 (중복 방지)
                                timeLeft = 2 * 60; // 시간을 초기화

                                // 타이머 컨테이너 표시
                                $('#timer').css("display","block");

                                // 타이머 시작
                                timerInterval = setInterval(updateTimer, 1000);
                                updateTimer();
                            }else{
                                alert("인증번호 전송 중 오류가 발생했습니다. 다시 시도해주세요.");
                            }
                        },
                        error: function() {
                            alert("인증번호 전송 중 오류가 발생했습니다. 다시 시도해주세요.");
                        }
                    });

                }
            },
            error: function() {
                alert("번호확인 중 오류가 발생했습니다. 다시 시도해주세요.");
            }
        });
    })
    // 인증번호 확인
    $("#btn-certify").on("click", function(){
        $.ajax({
            type: 'GET',
            url: '/login/check-auth-code',
            data: {
                telno: $("#user_telno").val(),
                code: $("#input-code").val()
            },
            success: function(response) {
                if(response){
                    $("#btn-certify").prop("disabled", true).val("완료");
                    clearInterval(timerInterval);
                    $("#btn-retrieve").prop("disabled", false);
                }else{
                    $("#msg-not-match").css("display", "flex");
                }
            },
            error: function() {
                alert("인증번호 확인중 오류가 발생했습니다. 다시 시도해주세요.");
            }
        });
    })
    // 사번 찾기
    $("#btn-retrieve").on("click", function(){
        $.ajax({
            type: 'GET',
            url: '/login/retrieve-id',
            data: {
                telno: $("#user_telno").val()
            },
            success: function(response) {
                alert("사원 번호 : "+ response);
                sessionStorage.setItem("id", response);
                window.location.href = "/login";
            },
            error: function() {
                alert("사번찾기중 오류가 발생했습니다. 다시 시도해주세요.");
            }
        });
    })
})