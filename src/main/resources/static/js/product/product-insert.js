// formatNumber 함수 정의
function formatNumber(input) {
    // 기존 입력값에서 숫자만 추출
    const value = input.value.replace(/[^0-9]/g, '');
    // 천 단위 콤마 추가
    if (value) {
        input.value = new Intl.NumberFormat('ko-KR').format(value);
    } else {
        input.value = ''; // 빈 문자열 처리
    }
}

$(document).ready(function () {
    // CSRF 토큰 가져오기
    let csrfToken = $('meta[name="_csrf"]').attr('content');
    let csrfHeader = $('meta[name="_csrf_header"]').attr('content');

    // 필드 유효성 검사 함수
    function validateForm() {
        let isValid = true;
        let errorMessage = "";
        let firstInvalidField = null; // 가장 먼저 비어 있는 필드를 찾기 위한 변수

        // 모든 input, select 태그 순회하며 값 확인
        $("#product-form").find("input, select").each(function () {
            const value = $(this).val().trim();
            if (!value) {
                if (!firstInvalidField) {
                    // 비어 있는 필드의 레이블 텍스트 가져오기
                    const label = $(this).closest("tr").find("th label").text();
                    firstInvalidField = label;
                }
            }
        });
        if (firstInvalidField) {
            isValid = false;
            errorMessage = `${firstInvalidField} 필드를 입력해주세요.`;
            alert(errorMessage);
        }
        return isValid;
    }

    // 등록 버튼 클릭
    $("#btn-register").on("click", function (e) {
        e.preventDefault(); // 기본 동작 방지
        // 유효성 검사 실행
        if (!validateForm()) {
            return;
        }
        // 가입금액에서 콤마 제거
        $("#prodInstlAmtMin").val(function () {
            return $(this).val().replace(/,/g, '');
        });
        $("#prodInstlAmtMax").val(function () {
            return $(this).val().replace(/,/g, '');
        });

        // 폼 데이터를 JavaScript 객체로 수집
        const formData = {
            prodNm: $("#prodNm").val(),
            prodTyCd: $("#prodTyCd").val(),
            prodSbstgTyCd: $("#prodSbstgTyCd").val(),
            prodInstlAmtMin: $("#prodInstlAmtMin").val(),
            prodInstlAmtMax: $("#prodInstlAmtMax").val(),
            prodPayTyCd: $("#prodPayTyCd").val(),
            prodAirMin: $("#prodAirMin").val(),
            prodAirMax: $("#prodAirMax").val(),
            prodAirBgnYmd: $("#prodAirBgnYmd").val(),
            prodAirEndYmd: $("#prodAirEndYmd").val(),
            prodIntTaxTyCd: $("#prodIntTaxTyCd").val(),
            prodCurrStcd: $("#prodCurrStcd").val(),
            prodNtslBgnYmd: $("#prodNtslBgnYmd").val(),
            prodNtslEndYmd: $("#prodNtslEndYmd").val(),
            staffName: $("#staffName").val(),
            userId: $("#userId").val(),
        };
        const insertUrl = "/product/insert"; // 등록 요청 URL
        console.log("등록 데이터: ", formData);

        $.ajax({
            url: insertUrl,
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(formData),
            beforeSend: function (xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken); // CSRF 헤더 설정
            },
            success: function (response) {
                alert("신규 상품이 등록되었습니다.");
                window.location.href = "/product/list"; // 목록 페이지로 이동
            },
            error: function (xhr, status, error) {
                console.error("등록 실패: ", error);
                alert("상품 등록에 실패했습니다.");
            },
        });
    });

    // 초기화 버튼 클릭
    $("#btn-reset").on("click", function (e) {
        e.preventDefault(); // 기본 동작 방지
        document.getElementById("product-form").reset(); // 폼 초기화
    });

    // 목록 버튼 클릭
    $("#btn-list").on("click", function (e) {
        e.preventDefault(); // 기본 동작 방지
        window.location.href = "/product/list"; // 목록 페이지로 이동
    });

    // 모달 열기
    $("#btn-search-icon").on("click", function () {
        $("#staffSearchModal").fadeIn();
    });

    // 모달 닫기 버튼
    $(".close").on("click", function () {
        $("#staffSearchModal").fadeOut();
    });

    // 모달 외부 클릭 시 닫기
    $(window).on("click", function (event) {
        if ($(event.target).is("#staffSearchModal")) {
            $("#staffSearchModal").fadeOut();
        }
    });

    // 직원 검색 버튼 클릭
    $("#staffSearchButton").on("click", function () {
        const searchTerm = $("#staffSearchInput").val().trim();
        if (!searchTerm) {
            alert("검색어를 입력하세요.");
            return;
        }

        // 서버로 Ajax 요청 보내기
        $.ajax({
            url: "/product/user/search", // API 엔드포인트
            type: "GET",
            data: { name: searchTerm }, // 요청 파라미터
            success: function (response) {
                const resultsContainer = $("#staffSearchResults");
                resultsContainer.empty();
                if (response.length === 0) {
                    resultsContainer.append("<p>검색 결과가 없습니다.</p>");
                } else {
                    // 테이블 생성
                    const table = $(`
                        <table class="staff-table">
                            <thead>
                                <tr>
                                    <th>사원번호</th>
                                    <th>사원이름</th>
                                </tr>
                            </thead>
                            <tbody></tbody>
                        </table>
                    `);
                    // 결과 추가
                    response.forEach((item) => {
                        const row = $(`
                            <tr class="result-item" data-id="${item.id}">
                                <td>${item.id}</td>
                                <td>${item.name}</td>
                            </tr>
                        `);
                        // 행 클릭 이벤트 추가
                        row.on("click", function () {
                            $("#staffName").val(item.name); // 입력 필드에 이름 설정
                            $("#userId").val(item.id);
                            $("#staffSearchModal").fadeOut(); // 모달 닫기
                        });

                        table.find("tbody").append(row);
                    });
                    resultsContainer.append(table);
                }
            },
            error: function (xhr, status, error) {
                console.error("검색 실패: ", error);
                alert("검색에 실패했습니다. 다시 시도해주세요.");
            },
        });
    });

    // 직원 이름 검색 인풋 필드에서 엔터 키 입력 처리
    $("#staffSearchInput").on("keypress", function (event) {
        if (event.key === "Enter") {
            event.preventDefault(); // 기본 Enter 키 동작(폼 제출) 방지
            $("#staffSearchButton").click(); // 검색 버튼 클릭 이벤트 트리거
        }
    });
});
