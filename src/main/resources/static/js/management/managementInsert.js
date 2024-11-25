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
$(".btn-submit").on("click", function (e) {

    const userIdField = document.getElementById('user_id');

    if (!userIdField.value) {
        e.preventDefault(); // 폼 제출 방지
        alert("사원번호를 생성해주세요."); // 경고 메시지
        return;
        }
    });
});