// promotionList.js

let currentSortColumn = null; // 현재 정렬 기준
let sortDirection = 'asc';   // 기본 정렬 방향 (오름차순)

// 테이블 정렬
function sortTable(column) {
    // 정렬 기준이 변경되었는지 확인
    if (currentSortColumn === column) {
        // 동일 기준일 경우 방향 전환 (오름차순 ↔ 내림차순)
        sortDirection = sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
        // 새로운 기준으로 정렬 초기화 (오름차순 시작)
        currentSortColumn = column;
        sortDirection = 'asc';
    }
    // 정렬 기준 및 방향을 적용하여 데이터 재조회
    filterResults(1); // 첫 페이지부터 시작
}

// 전체검색 (필터 및 정렬 상태 초기화)
function resetFilters(page = 1) { // 기본값으로 1페이지를 설정
    // 필터 조건 초기화
    $('#prgStcd').val(''); // 진행 상태 필터 초기화
    $('#dsTyCd').val(''); // 상품 유형 필터 초기화
    $('input[name="custNm"]').val(''); // 고객명 입력 필터 초기화
    $('input[name="userNm"]').val(''); // 담당자 입력 필터 초기화
    $('input[name="prodNm"]').val(''); // 상품명 입력 필터 초기화

    currentSortColumn = null; // 정렬 상태 초기화
    sortDirection = 'asc'; // 기본 정렬 방향을 오름차순으로 설정

    filterResults(page); // 초기화된 필터와 정렬 상태를 기준으로 데이터 재조회
}

// 엔터키 감지 함수
function checkEnterKey(event) {
    if (event.key === "Enter") {
       event.preventDefault(); // 기본 엔터키 동작(예: 폼 제출)을 막음
       filterResults(); // 공통 함수 호출
    }
}

// 조건검색
function filterResults(page = 1) {
    const prgStcd = $('#prgStcd').val(); // 진행 상태 선택값 가져오기
    const dsTyCd = $('#dsTyCd').val(); // 상품 유형 선택값 가져오기
    const custNm = $('input[name="custNm"]').val(); // 고객명 입력값
    const userNm = $('input[name="userNm"]').val(); // 담당자 입력값
    const prodNm = $('input[name="prodNm"]').val(); // 상품명 입력값

    // Ajax 요청
    $.ajax({
        url: '/promotion/list',
        method: 'GET',
        data: {
            prgStcd: prgStcd, // 진행 상태 필터 값
            dsTyCd: dsTyCd, // 상품 유형 필터 값
            custNm: custNm, // 고객명 필터 값
            userNm: userNm, // 담당자 필터 값
            prodNm: prodNm, // 상품명 필터 값
            page: page, // 페이지 번호
            size: 10, // 페이지당 데이터 개수
            sortColumn: currentSortColumn, // 정렬 기준
            sortDirection: sortDirection, // 정렬 방향
            ajax: true // AJAX 요청임을 명시
        },
        success: function(response) {

            console.log('Response:', response); // 응답 데이터 확인

            // 기존 테이블 데이터를 초기화
            const tableBody = $('#result-table-body');
            tableBody.empty();

            // 응답 데이터를 기반으로 새로운 테이블 데이터 렌더링
            if (response.PromotionList && response.PromotionList.length > 0) {
                response.PromotionList.forEach(productDesign => {

                    console.log('Product Design:', productDesign); // 데이터 확인

                    const row = `
                        <tr data-dsgn-sn="${productDesign.dsgnSn}" onclick="handleRowClick(this, event)">
                            <td><input type="checkbox" class="row-checkbox"></td>
                            <td>${productDesign.custNm}</td>
                            <td>${productDesign.userNm}</td>
                            <td>${getProductType(productDesign.dsTyCd)}</td>
                            <td>${productDesign.prodNm}</td>
                            <td>${getProgressStatus(productDesign.prgStcd)}</td>
                            <td>${productDesign.mtrDate}</td>
                            <td>${productDesign.mtrAmt}</td>
                        </tr>
                    `;
                    tableBody.append(row); // 테이블에 행 추가
                });
            } else {
                console.log('No data available.'); // 데이터가 없을 때
                tableBody.append('<tr><td colspan="8">데이터가 없습니다.</td></tr>');
            }

            // 페이지네이션 갱신
            updatePagination(
                response.totalPages,
                response.currentPage,
                response.startPage,
                response.endPage);

        },
        error: function(error) {
            console.error('Error fetching filtered results:', error);
        }
    });
}

// 상품 유형 코드 → 이름 변환
function getProductType(dsTyCd) {
    const types = {
        '1': '적금설계',
        '2': '목돈마련설계',
        '3': '예금설계',
        '4': '대출설계'
    };
    return types[dsTyCd] || '알 수 없음'; // 일치하는 코드가 없을 경우 '알 수 없음' 반환
}

// 진행 상태 코드 → 이름 변환
function getProgressStatus(prgStcd) {
    const statuses = {
        '0': '제안중',
        '1': '추가상담필요',
        '2': '가입대기',
        '3': '가입완료',
        '4': '만기예정',
        '5': '취소/보류',
        '6': '만기완료',
        '9': '해지'
    };
    return statuses[prgStcd] || '알 수 없음';
}

// 페이지네이션 업데이트
function updatePagination(totalPages, currentPage, startPage, endPage) {
    const paginationControls = $('#paginationControls');
    paginationControls.empty(); // 기존 페이지네이션 요소 초기화

    // 새로운 페이지네이션 요소 생성
    const paginationDiv = $('<div class="pagination"></div>');
    paginationControls.append(paginationDiv);

    // 이전 페이지 버튼
    if (currentPage > 1) {
        paginationDiv.append(`<button class="pagination-prev pagination-button" onclick="filterResults(${currentPage - 1})">&lt;</button>`);
    } else {
        paginationDiv.append(`<button class="pagination-prev pagination-button" disabled>&lt;</button>`);
    }

    // 페이지 번호
    for (let i = startPage; i <= endPage; i++) {
        if (i === currentPage) {
            paginationDiv.append(`<a href="#" class="pagination-number pagination-active" disabled>${i}</button>`);
        } else {
            paginationDiv.append(`<a href="#" class="pagination-number" onclick="filterResults(${i})">${i}</a>`);
        }
    }

    // 다음 페이지 버튼
    if (currentPage < totalPages) {
        paginationDiv.append(`<button class="pagination-number" onclick="filterResults(${currentPage + 1})">&gt;</a>`);
    } else {
        paginationDiv.append(`<button class="pagination-next pagination-button">&gt;</button>`);
    }
}

// 테이블 행 클릭 시 금융계산기 페이지로 이동
function handleRowClick(row, event) {
    // 클릭된 요소가 체크박스인 경우, 이벤트 처리 중단
    if (event.target.closest('td').querySelector('input[type="checkbox"]')) {
        event.stopPropagation();
        return;
    }

    // 클릭한 행의 data-dsgn-sn 값 가져오기
    const dsgnSn = row.getAttribute('data-dsgn-sn');

    // 금융계산기 페이지로 이동
    if (dsgnSn) {
        window.location.href = `/promotion/cal/detail?dsgnSn=${dsgnSn}`;
    } else {
        console.error('설계번호가 없습니다.');
    }
}

// 전체 선택 및 개별 체크박스 동기화
$(document).ready(() => {
    const selectAllCheckbox = $('#selectAll');

    // 상단 체크박스 클릭 시
    selectAllCheckbox.on('change', function () {
        const isChecked = this.checked;
        $('#result-table-body .row-checkbox').prop('checked', isChecked);
    });

    // 개별 체크박스 클릭 시
    $(document).on('change', '#result-table-body .row-checkbox', function () {
        const allChecked = $('#result-table-body .row-checkbox').length === $('#result-table-body .row-checkbox:checked').length;
        selectAllCheckbox.prop('checked', allChecked);
    });
});

// 인쇄
function printCheckedRows() {
    // 체크된 행의 데이터 수집
    const checkedRows = [];
    $('#result-table-body .row-checkbox:checked').each(function () {
        const row = $(this).closest('tr'); // 체크박스의 부모 행
        const rowData = {
            custNm: row.find('td:nth-child(2)').text(),
            userNm: row.find('td:nth-child(3)').text(),
            dsTyCd: row.find('td:nth-child(4)').text(),
            prodNm: row.find('td:nth-child(5)').text(),
            prgStcd: row.find('td:nth-child(6)').text(),
            mtrDt: row.find('td:nth-child(7)').text(),
            mtrAmt: row.find('td:nth-child(8)').text()
        };
        checkedRows.push(rowData);
    });

    if (checkedRows.length === 0) {
        alert('인쇄할 데이터를 선택하세요.');
        return;
    }

    // 인쇄 데이터 확인용 로그
    console.log('Checked Rows:', checkedRows);

    // 인쇄용 HTML 생성
    const printContent = generatePrintContent(checkedRows);

    // 새 창에 인쇄 데이터 표시
    const printWindow = window.open('', '_blank');
    printWindow.document.write(printContent);
    printWindow.document.close();
    printWindow.focus();
    printWindow.print();
    printWindow.close();
}

function generatePrintContent(rows) {
    let content = `
        <html>
        <head>
            <title>인쇄</title>
            <style>
                body {
                    font-family: Arial, sans-serif;
                }
                table {
                    width: 100%;
                    border-collapse: collapse;
                    margin: 20px 0;
                }
                th, td {
                    border: 1px solid #ccc;
                    padding: 8px;
                    text-align: left;
                }
                th {
                    background-color: #f4f4f4;
                }
            </style>
        </head>
        <body>
            <h1>상품 설계 조회</h1>
            <table>
                <thead>
                    <tr>
                        <th>고객명</th>
                        <th>담당자</th>
                        <th>상품유형</th>
                        <th>상품명</th>
                        <th>진행상태</th>
                        <th>만기일자</th>
                        <th>만기금액(잔액)</th>
                    </tr>
                </thead>
                <tbody>
    `;

    rows.forEach(row => {
        content += `
            <tr>
                <td>${row.custNm}</td>
                <td>${row.userNm}</td>
                <td>${row.dsTyCd}</td>
                <td>${row.prodNm}</td>
                <td>${row.prgStcd}</td>
                <td>${row.mtrDt}</td>
                <td>${row.mtrAmt}</td>
            </tr>
        `;
    });

    content += `
                </tbody>
            </table>
        </body>
        </html>
    `;

    return content;
}

// 인쇄 버튼 이벤트
$(document).on('click', '.btn-print', printCheckedRows);

// 페이지 로드 시 오류 메시지를 alert로 표시
window.onload = function() {
    const errorMessage = /*[[${errorMessage != null}]]*/ false ? '[[${errorMessage}]]' : null;
    if (errorMessage) {
        alert(errorMessage);
    }
};

// 상태 버튼 클릭 시 진행 상태 업데이트
$(document).on('click', '.btn-refresh', function () {
    // 버튼 비활성화 및 스타일 변경
    const button = $(this);

    button.prop('disabled', true);
    button.find('i').removeClass('bi-arrow-repeat').addClass('bi-arrow-clockwise spinner-icon');

    // CSRF 토큰 가져오기
    const csrfToken = $('meta[name="_csrf"]').attr('content');
    const csrfHeader = $('meta[name="_csrf_header"]').attr('content');

    // 진행 상태 갱신 요청
    $.ajax({
        url: '/promotion/update-statuses',
        method: 'POST',
        headers: {
            [csrfHeader]: csrfToken // CSRF 토큰 추가
        },
        success: function (response) {
            // 성공 메시지 출력
            alert(response);

            // 상태 갱신 성공 시 테이블 다시 로드
            filterResults(1); // 첫 페이지부터 데이터 갱신
        },
        error: function (xhr, status, error) {
            // 에러 메시지 출력
            alert('진행 상태를 갱신하는 중 오류가 발생했습니다.');
            console.error('Error:', error);
        },
        complete: function () {
        // 버튼 원래 상태로 복원
        button.prop('disabled', false);
        button.find('i').removeClass('bi-arrow-clockwise spinner-icon').addClass('bi-arrow-repeat');
        }
    });
});