// product-list.js

let currentSortColumn = null; // 현재 정렬 기준
let sortDirection = 'asc';   // 기본 정렬 방향 (오름차순)

// 정렬 함수
function sortTable(column) {
    if (currentSortColumn === column) {
        sortDirection = sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
        currentSortColumn = column;
        sortDirection = 'asc';
    }
    filterResults(1);
}

// 필터 초기화 및 전체 검색
function resetFilters(page = 1) {
    $('#prodCurrStcd').val('');
    $('#prodTyCd').val('');
    $('#prodPayTyCd').val('');
    $('#dateType').val('');
    $('input[name="searchBgnYmd"]').val('');
    $('input[name="searchEndYmd"]').val('');
    $('input[name="prodNm"]').val('');
    currentSortColumn = null;
    sortDirection = 'asc';
    filterResults(page);
}

// 조건 검색 및 정렬
function filterResults(page = 1) {
    const data = {
        prodCurrStcd: $('#prodCurrStcd').val(),
        prodTyCd: $('#prodTyCd').val(),
        prodPayTyCd: $('#prodPayTyCd').val(),
        dateType: $('#dateType').val(),
        searchBgnYmd: $('input[name="searchBgnYmd"]').val(),
        searchEndYmd: $('input[name="searchEndYmd"]').val(),
        prodNm: $('input[name="prodNm"]').val(),
        page,
        size: 8,
        sortColumn: currentSortColumn,
        sortDirection,
        ajax: true
    };

    $.ajax({
        url: window.location.origin + '/product/list',
        method: 'GET',
        data,
        success: function (response) {
            console.log(response);
            renderTable(response.list);
            updatePagination(response.totalPages, response.currentPage);
            console.log('Rendering data:', data);
        },
        error: function (error) {
            console.error('Error fetching filtered results:', error);
        }
    });
}

// 테이블 렌더링
function renderTable(data) {
    console.log('Rendering data:', data); // 데이터 확인
    const tableBody = $('#result-table-body');
    tableBody.empty();

    if (data && data.length > 0) {
        console.log('Populating table with data'); // 데이터 렌더링 시작 확인
        data.forEach(product => {
            const row = `
                <tr>
                    <td><input type="checkbox" class="row-checkbox"></td>
                    <td>${product.prodCd || 'N/A'}</td>
                    <td>${product.prodNm || 'N/A'}</td>
                    <td>${product.prodInstlAmtMin || 0}</td>
                    <td>${product.prodInstlAmtMax || 0}</td>
                    <td>${product.prodPayTyCd || 'N/A'}</td>
                    <td>${product.prodAirMin || 'N/A'}</td>
                    <td>${product.prodAirMax || 'N/A'}</td>
                    <td>${product.prodIntTaxTyCd || 'N/A'}</td>
                    <td>${product.prodNtslBgnYmd || 'N/A'} ~ ${product.prodNtslEndYmd || 'N/A'}</td>
                </tr>
            `;
            tableBody.append(row);
        });
    } else {
        console.log('No data available for rendering'); // 데이터가 없는 경우
        tableBody.append('<tr><td colspan="10">데이터가 없습니다.</td></tr>');
    }
}

// 페이지네이션 렌더링
function updatePagination(totalPages, currentPage) {
    const paginationContainer = $('.pagination-container');
    paginationContainer.empty();
    for (let i = 1; i <= totalPages; i++) {
        const activeClass = i === currentPage ? 'pagination-active' : '';
        const pageItem = `<button class="pagination-button ${activeClass}" data-page="${i}">${i}</button>`;
        paginationContainer.append(pageItem);
    }

    $('.pagination-button').off('click').on('click', function () {
        const page = $(this).data('page');
        filterResults(page);
    });
}

// Document Ready
$(document).ready(function () {
    // 검색 버튼 이벤트
    $('#searchForm').on('submit', function (e) {
        e.preventDefault();
        filterResults(1);
    });

    // 전체 검색 버튼 이벤트
    $('#btn-search-all').on('click', function () {
        resetFilters(1);
    });

    // 신규 등록 버튼 클릭
    $("#btn-register").on("click", function (e) {
        window.location.href = "/product/insert";
    });

    // 전체 선택 체크박스 클릭 이벤트
    $("#checkAll").on("click", function () {
        const isChecked = $(this).is(":checked");
        $(".row-checkbox").prop("checked", isChecked);
    });

    // 개별 체크박스 클릭 이벤트로 전체 체크박스 상태 업데이트
    $(".table-product").on("change", ".row-checkbox", function () {
        const total = $(".row-checkbox").length;
        const checked = $(".row-checkbox:checked").length;
        $("#checkAll").prop("checked", total === checked);
    });

    // 인쇄 버튼 클릭 이벤트
    $("#btn-print").on("click", function () {
        const selectedRows = $(".row-checkbox:checked").closest("tr");

        if (selectedRows.length === 0) {
            alert("최소한 하나의 행을 선택하세요.");
            return;
        }

        let printableContent = `
            <table border="1" style="border-collapse: collapse; width: 100%; text-align: center;">
                <thead>
                    <tr>
                        <th>상품코드</th>
                        <th>상품명</th>
                        <th>최소가입액</th>
                        <th>최대가입액</th>
                        <th>납입주기</th>
                        <th>최소적용이율</th>
                        <th>최대적용이율</th>
                        <th>이자과세</th>
                        <th>판매기간</th>
                    </tr>
                </thead>
                <tbody>
                    ${selectedRows.map(function () {
                        const cells = $(this).find("td:not(:first-child)");
                        return `<tr>${cells.map(function () {
                            return `<td>${$(this).html()}</td>`;
                        }).get().join("")}</tr>`;
                    }).get().join("")}
                </tbody>
            </table>
        `;

        const printWindow = window.open("", "_blank");
        printWindow.document.write(`
            <html>
            <head>
                <title>선택된 항목 인쇄</title>
            </head>
            <body>
                <h1>선택된 상품 목록</h1>
                ${printableContent}
            </body>
            </html>
        `);
        printWindow.document.close();
        printWindow.print();
    });

    // 정렬 버튼 이벤트 연결
    $('.sortable').on('click', function () {
        const column = $(this).data('column');
        sortTable(column);
    });
});
