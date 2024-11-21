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