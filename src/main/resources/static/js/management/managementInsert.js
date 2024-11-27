document.addEventListener("DOMContentLoaded", function() {
        const yearMonthSelect = document.getElementById("yearMonthSelect");
        const currentDate = new Date();
        let month = currentDate.getMonth(); // 0부터 시작하므로 0 = 1월
        let year = currentDate.getFullYear();

        // 최근 6개월 옵션 추가
        for (let i = 0; i < 6; i++) {
            if (month < 0) {
                month = 11; // 12월
                year -= 1;
            }

            const monthText = (month + 1).toString().padStart(2, '0'); // 월을 2자리로 맞춤
            const optionText = `${year}년 ${monthText}월`;
            const optionValue = `${year}-${monthText}`;

            const option = document.createElement("option");
            option.value = optionValue;
            option.textContent = optionText;

            yearMonthSelect.appendChild(option);
            month--;
        }
    });

document.getElementById('btn-reset').addEventListener('click', function() {
      // 검색 폼의 모든 값을 초기화
      document.getElementById('searchForm').reset();
      // 폼을 다시 제출하여 초기 상태로 이동
      window.location.href = '/management/insert';
   });

document.getElementById('user_birthday').addEventListener('input', function (e) {
    const input = e.target.value;
    const regex = /^\d{4}-\d{2}-\d{2}$/;

    // 기본 형식 검사
    if (!regex.test(input) && input !== '') {
        e.target.setCustomValidity('날짜 형식은 yyyy-mm-dd 입니다.');
        return;
    }

    // 날짜 범위 검사
    const parts = input.split('-');
    const year = parseInt(parts[0], 10);
    const month = parseInt(parts[1], 10);
    const day = parseInt(parts[2], 10);

    if (year < 1900 || year > 2024) {
        e.target.setCustomValidity('년도는 1900년부터 2024년까지 입력 가능합니다.');
    } else if (month < 1 || month > 12) {
        e.target.setCustomValidity('월은 01부터 12까지 입력 가능합니다.');
    } else if (day < 1 || day > 31) {
        e.target.setCustomValidity('일은 01부터 31까지 입력 가능합니다.');
    } else if ((month === 4 || month === 6 || month === 9 || month === 11) && day > 30) {
        e.target.setCustomValidity('입력한 월에는 30일까지 존재합니다.');
    } else if (month === 2) {
        // 윤년 여부 검사
        const isLeapYear = (year % 4 === 0 && year % 100 !== 0) || (year % 400 === 0);
        if (day > 29 || (day === 29 && !isLeapYear)) {
            e.target.setCustomValidity('2월에는 윤년의 경우 29일, 그 외에는 28일까지 입력 가능합니다.');
        } else {
            e.target.setCustomValidity('');
        }
    } else {
        e.target.setCustomValidity('');
    }
});

$(document).ready(function () {
    $("#btn-register").on("click", function () {

        // 서버로부터 데이터 가져오기
        $.ajax({
            type: 'GET',
            url: '/management/employee/insertModal',
            success: function (response) {
                // 부서 정보 채우기
                let departmentSelect = $('#department');
                departmentSelect.empty();
                departmentSelect.append('<option value="" selected>부서</option>');
                response.departmentList.forEach(function (department) {
                    departmentSelect.append('<option value="' + department.user_dept_cd + '">' + department.dept_name + '</option>');
                });

                // 직위 정보 채우기
                let positionSelect = $('#position');
                positionSelect.empty();
                positionSelect.append('<option value="" selected>직위</option>');
                response.jobPositionList.forEach(function (position) {
                    positionSelect.append('<option value="' + position.user_jbps_ty_cd + '">' + position.position_name + '</option>');
                });

                // 권한 정보 채우기
                let authSelect = $('#auth');
                authSelect.empty();
                authSelect.append('<option value="" selected>권한</option>');
                response.authList.forEach(function (auth) {
                    authSelect.append('<option value="' + auth.user_auth_cd + '">' + auth.auth_name + '</option>');
                });

                // 상태 정보 채우기
                let statusSelect = $('#status');
                statusSelect.empty();
                statusSelect.append('<option value="" selected>상태</option>');
                response.statusList.forEach(function (status) {
                    statusSelect.append('<option value="' + status.user_status + '">' + status.status_name + '</option>');
                });
            },
            error: function (xhr, status, error) {
                 console.error('Error fetching employee data:', error);
            }
        });
    });

    $(".btn-userIdGenerate").on("click", function() {
        let joiningDate = $("#user_jncmp_ymd").val(); // 입사일자 값 가져오기

        if (!joiningDate) {
            alert("입사일자를 먼저 입력해주세요.");
            return;
        }

        // 서버로부터 사원번호 생성 요청
        $.ajax({
            type: "POST",
            url: "/management/generate/userId",
            data: { joiningDate: joiningDate },
            success: function(user_id) {
                $("#user_id").val(user_id); // 생성된 사원번호를 입력 필드에 채움
            },
            error: function() {
                alert("사원번호를 생성하는데 실패했습니다.");
            }
        });
    });

    $(".btn-submit").on("click", function (e) {
        const userIdField = document.getElementById('user_id');

        if (!userIdField.value) {
            e.preventDefault(); // 폼 제출 방지
            alert("사원번호를 생성해주세요."); // 경고 메시지
            return;
        }
    });

    $(".btn-close").on("click", function () {
        // 모달이 닫힌 후 페이지를 리디렉션
        window.location.href = '/management/insert';
    });
});

//// 모든 readonly 요소에 대해 선택 방지
document.querySelectorAll('input[readonly]').forEach(input => {
    input.addEventListener('focus', (e) => e.target.blur()); // 포커스 제거
    input.addEventListener('mousedown', (e) => e.preventDefault()); // 클릭 방지
    input.addEventListener('selectstart', (e) => e.preventDefault()); // 텍스트 선택 방지
});