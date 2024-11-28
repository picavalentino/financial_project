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

    // 초기 값 저장
    $("input[name='prodAirMin'], input[name='prodAirMax'], input[name='prodAirBgnYmd'], input[name='prodAirEndYmd'], input[name='prodNtslBgnYmd'], input[name='prodNtslEndYmd']").each(function () {
        $(this).attr("data-original-value", $(this).val());
    });

    // 수정 버튼 클릭
    $("#btn-register").on("click", function (e) {
        e.preventDefault(); // 기본 동작 방지
        const productId = $("input[name='prodSn']").val(); // 상품 ID
        if (!productId) {
            alert("상품 ID가 유효하지 않습니다.");
            return;
        }
        const updateUrl = `/product/detail/${productId}/update`; // 수정 요청 URL

        // 변경된 데이터 추출
        const changedData = { prodSn: productId }; // prodSn을 항상 포함
        $("input[name='prodAirMin'], input[name='prodAirMax'], input[name='prodAirBgnYmd'], input[name='prodAirEndYmd'], input[name='prodNtslBgnYmd'], input[name='prodNtslEndYmd']").each(function () {
            const originalValue = $(this).attr("data-original-value");
            const currentValue = $(this).val();
            if (originalValue !== currentValue) {
                changedData[$(this).attr("name")] = currentValue;
            }
        });
        if (Object.keys(changedData).length === 0) {
            alert("변경된 항목이 없습니다.");
            return;
        }

        // Ajax 요청
        $.ajax({
            url: updateUrl,
            type: "POST",
            contentType: "application/json", // Content-Type 설정
            data: JSON.stringify(changedData),
            beforeSend: function (xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken); // CSRF 헤더 설정
            },
            success: function (response) {
                console.log("수정 데이터: ", changedData);
                alert("수정되었습니다.");
                // 변경된 값 반영
                for (const key in changedData) {
                    $(`input[name='${key}']`).attr("data-original-value", changedData[key]);
                }
                location.reload(); // 성공 시 새로고침
            },
            error: function (xhr, status, error) {
                const errorMessage = xhr.responseJSON ? xhr.responseJSON.message : "알 수 없는 오류";
                console.error("수정 실패: ", errorMessage);
                alert(`수정에 실패했습니다: ${errorMessage}`);
            },
        });
    });

    // 판매 종료 버튼 클릭
    $("#btn-end").on("click", function (e) {
        e.preventDefault();
        if (!confirm("지금부터 해당 상품 판매를 중지하시겠습니까?")) {
            return;
        }

        const productId = $("input[name='prodSn']").val();
        if (!productId) {
            alert("상품 ID가 유효하지 않습니다.");
            return;
        }

        const deleteUrl = `/product/detail/${productId}/delete`;
        $.ajax({
            url: deleteUrl,
            type: "POST",
            beforeSend: function (xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function (response) {
                alert("판매중지되었습니다.");
                window.location.href = "/product/list"; // 목록 페이지 이동
            },
            error: function (xhr, status, error) {
                const errorMessage = xhr.responseJSON ? xhr.responseJSON.message : "알 수 없는 오류";
                console.error("판매 중지 실패: ", errorMessage);
                alert(`판매 중지에 실패했습니다: ${errorMessage}`);
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
