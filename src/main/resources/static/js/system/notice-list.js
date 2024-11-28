// notice-list.js
// product-list.js

let currentSortColumn = null; // 현재 정렬 기준
let sortDirection = 'asc';   // 기본 정렬 방향 (오름차순)

function sortTable(column) {
    if (currentSortColumn === column) {
        sortDirection = sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
        currentSortColumn = column;
        sortDirection = 'asc';
    }
    updateSortIcons(column);
    filterResults(1);
}

function updateSortIcons(column) {
    $('.sortable').removeClass('sort-asc sort-desc');
    const header = $(`[onclick="sortTable('${column}')"]`);
    if (sortDirection === 'asc') {
        header.addClass('sort-asc');
    } else {
        header.addClass('sort-desc');
    }
}

// 정렬 초기화
function resetFilters(page = 1) {
    $('#inqCategory').val('');
    $('#keywordType').val('');
    $('input[name="keyword"]').val('');
    $('input[name="inqCreateAt"]').val('');
    currentSortColumn = null;
    sortDirection = 'desc';
    filterResults(page);
}

// 조건 검색 및 정렬
function filterResults(page = 1) {
    const data = {
        inqCategory: $('#inqCategory').val(),
        keywordType: $('#keywordType').val(),
        keyword: $('input[name="keyword"]').val(),
        inqCreateAt: $('input[name="inqCreateAt"]').val(),
        page,
        size: 8,
        sortColumn: currentSortColumn,
        sortDirection,
        ajax: true
    };

    $.ajax({
        url: window.location.origin + '/system/inquire',
        method: 'GET',
        data: data,
        success: function (response) {
            renderTable(response.list);
            updatePagination(response.totalPages, response.currentPage);
            updateProductSize(response.totalItems);
        },
        error: function (error) {
            console.error('Error fetching filtered results:', error);
        }
    });
}

function updateProductSize(size) {
    $('.product-size span').text(size);
}

// 테이블 렌더링
function renderTable(data) {
    console.log('Rendering data:', data);
    const tableBody = $('#result-table-body');
    tableBody.empty();

    if (data && data.length > 0) {
        console.log('Populating table with data');
        data.forEach(product => {
            // 답변 상태에 따라 클래스와 텍스트 설정
            const replyHtml = product.inqReply == '1'
                ? `<div class="reply" id="rep-completed">답변완료</div>`
                : `<div class="reply" id="rep-wait" style="cursor: pointer;" onclick="window.location.href='/inquire/detail/${product.inqId}'">답변대기</div>`;

            const row = $(`
                <tr class="table-row" style="cursor: pointer;">
                    <td>${product.inqId || 'N/A'}</td>
                    <td>${product.inqCategory || 'N/A'}</td>
                    <td>${product.inqAnonym == 1 ? '익명' : product.userId}</td>
                    <td>${product.inqTitle}</td>
                    <td>${product.formattedInqCreateAt}</td>
                    <td>${product.inqCheck}</td>
                    <td id="td-reply">
                        ${replyHtml}
                    </td>
                    <td>
                        <button type="button" class="${product.inqNotice == '2' ? 'btn-notice-register' : 'btn-notice-cancel'}">
                            ${product.inqNotice == '2' ? '등록' : '해제'}
                        </button>
                    </td>
                </tr>
            `);
            tableBody.append(row);
        });
    } else {
        console.log('No data available for rendering');
        tableBody.append('<tr><td colspan="8">데이터가 없습니다.</td></tr>');
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
    $('.pagination-button').on('click', function () {
        const page = $(this).data('page');
        filterResults(page);
    });
}

// 공지사항 등록/해제
function handleNoticeStcd()

// Document Ready
$(document).ready(function () {
    // 검색 버튼 이벤트
    $('#search-form').on('submit', function (e) {
        e.preventDefault();
        filterResults(1);
    });

    // 전체 검색 버튼 이벤트
    $('#btn-search-all').on('click', function () {
        resetFilters(1);
    });

    // 신규 등록 버튼 클릭
    $("#btn-register").on("click", function (e) {
        window.location.href = "/system/inquire/insert";
    });

    // 정렬 버튼 이벤트 연결
    $('.sortable').on('click', function () {
        const column = $(this).data('column');
        sortTable(column);
    });

    // 공지사항 등록 버튼
    $('.btn-notice-register').on('click', function(){
        alert('')
    })
});

