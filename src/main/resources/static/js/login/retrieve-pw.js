$(document).ready(function(){
    $("#btn-pw-cross").on("click", function(){
        $("#pw-modal").removeClass('show');
        window.location.href = "/login";
    })
    // 타이머 시작 시간 (2분)
    let timeLeft = 2 * 60;

    // 타이머를 업데이트하는 함수
    function updateTimer() {
        const minutes = Math.floor(timeLeft / 60); // 남은 분 계산
        const seconds = timeLeft % 60; // 남은 초 계산

        // 화면에 표시
        $("#pw-timer").text(
            `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`
        );

        // 시간이 0이 되면 타이머 중지
        if (timeLeft <= 0) {
            clearInterval(timerInterval);
            $('#pw-timer').css("display","none");
        } else {
            timeLeft--; // 시간 감소
        }
    }
    // 매초마다 updateTimer 함수 실행
    let timerInterval = setInterval(updateTimer, 1000);
    // 전화번호 숫자만
    $("#pw-phone2, #pw-phone3, #pw-input-code, #pw-user_id").on('keypress', function (event) {
        // 숫자 (0-9)만 입력 가능하게 설정
        if (event.which < 48 || event.which > 57) {
            // 백스페이스 (8), 탭 (9), 화살표 키 등은 허용
            if (event.which !== 8 && event.which !== 9) {
                event.preventDefault();
            }
        }
    });
    // 유효성
    $("#pw-phone1, #pw-phone2, #pw-phone3, #pw-user_id").on("input change", function() {
        $("#pw-msg-not-match").css("display", "none");
        $("#pw-user_telno").val("");
        $("#btn-reset, #btn-change").prop("disabled", true);
        $("#pw-btn-certify").val("인증");
        let code = $("#pw-input-code").val();
        if(code.length==6){
            $("#pw-btn-certify").prop("disabled", false);
        }else{
            $("#pw-btn-certify").prop("disabled", true);
        }
        let phone1 = $("#pw-phone1").val();
        let phone2 = $("#pw-phone2").val();
        let phone3 = $("#pw-phone3").val();
        let name = $("#pw-user_id").val();
        let phone = phone1 + "-" + phone2 + "-" + phone3;
        let phoneRegex = /^01[0-9]-\d{3,4}-\d{4}$/;
        if (phoneRegex.test(phone) && name.length>1){
            $("#pw-btn-send").prop("disabled", false);
            $("#pw-user_telno").val(phone);
            $("#pw-btn-resend").prop("disabled", false);
        } else{
            $("#pw-btn-send").prop("disabled", true);
            $("#pw-btn-resend").prop("disabled", true);
        }
    });
    // 인증하기 버튼 활성화
    $("#pw-input-code").on("input", function(){
        $("#pw-msg-not-match").css("display", "none");
        $("#pw-btn-certify").prop("disabled", true).val("인증");
        let code = $(this).val();
        if(code.length==6){
            $("#pw-btn-certify").prop("disabled", false);
        }else{
            $("#pw-btn-certify").prop("disabled", true);
        }
    })
    // 인증번호 전송
    $("#pw-btn-send, #pw-btn-resend").on("click", function(){
        $.ajax({
            type: 'GET',
            url: '/login/exist/pw',
            data: {
                id: $("#pw-user_id").val(),
                telno: $("#pw-user_telno").val()
            },
            success: function(response) {
                if(!response){
                    alert("일치하는 정보가 존재하지 않습니다.");
                }else{
                    $.ajax({
                        type: 'GET',
                        url: '/login/send-auth-code',
                        data: { telno: $("#pw-user_telno").val() },
                        success : function(response){
                            if(response){
                                if (response.endsWith("회")) {
                                    $("#pw-code-container").css("display", "block");
                                    $("#pw-btn-send").css("display", "none");
                                    $("#pw-btn-retrieve").css("display", "block");
                                    // 타이머 초기화
                                    clearInterval(timerInterval); // 이전 타이머 중지 (중복 방지)
                                    timeLeft = 2 * 60; // 시간을 초기화

                                    // 타이머 컨테이너 표시
                                    $('#pw-timer').css("display","block");

                                    // 타이머 시작
                                    timerInterval = setInterval(updateTimer, 1000);
                                    updateTimer();
                                }
                                if(response==="인증횟수 초과"){
                                    // 타이머 멈추기
                                    clearInterval(timerInterval);
                                    alert("인증횟수가 초과되었습니다. 24시간 뒤 시도해주세요.");
                                }
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
    $("#pw-btn-certify").on("click", function(){
        $.ajax({
            type: 'GET',
            url: '/login/check-auth-code',
            data: {
                telno: $("#pw-user_telno").val(),
                code: $("#pw-input-code").val()
            },
            success: function(response) {
                if(response){
                    $("#pw-btn-certify").prop("disabled", true).val("완료");
                    clearInterval(timerInterval);
                    $("#btn-reset, #btn-change").prop("disabled", false);
                }else{
                    $("#pw-msg-not-match").css("display", "flex");
                }
            },
            error: function() {
                alert("인증번호 확인중 오류가 발생했습니다. 다시 시도해주세요.");
            }
        });
    })
    // 비밀번호 초기화
    $("#btn-reset").on("click", function(){
        $.ajax({
            type: 'GET',
            url: '/login/reset-pw',
            data: {
                id: $("#pw-user_id").val()
            },
            success: function(response) {
                if(response){
                    alert("비밀번호가 초기화 되었습니다.\n 비밀번호 : "+response);
                    window.location.href = "/login";
                }else{
                    alert("비밀번호 초기화에 실패하였습니다. 다시 시도해주세요.");
                }
            },
            error: function() {
                alert("비밀번호 초기화에 실패하였습니다. 다시 시도해주세요.");
            }
        });
    })
})