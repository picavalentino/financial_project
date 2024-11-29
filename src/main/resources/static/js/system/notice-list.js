// notice-list.js

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
            const replyHtml = product.inqReply == '1'
                ? `<div class="reply" id="rep-completed">답변완료</div>`
                : `<div class="reply" id="rep-wait" style="cursor: pointer;" onclick="window.location.href='/inquire/detail/${product.inqId}'">답변대기</div>`;

            const row = $(`<tr class="table-row" style="cursor: pointer;"></tr>`);
            row.append(`<td>${product.inqId || 'N/A'}</td>`);
            row.append(`<td>${product.inqCategory || 'N/A'}</td>`);
            row.append(`<td>${product.inqAnonym == 1 ? '익명' : product.userId}</td>`);
            row.append(`<td>${product.inqTitle}</td>`);
            row.append(`<td>${product.formattedInqCreateAt}</td>`);
            row.append(`<td>${product.inqCheck}</td>`);
            row.append(`<td id="td-reply">${replyHtml}</td>`);
            row.append(`
                <td>
                    <button type="button" class="${product.inqNotice == '1' ? 'btn-notice-cancel' : 'btn-notice-register'}">
                        ${product.inqNotice == '1' ? '해제' : '등록'}
                    </button>
                </td>
            `);

            // 행 클릭 이벤트 추가
            row.on('click', function () {
                const inqCategory = product.inqCategory;
                const inqId = product.inqId;
                window.location.href = `/inquire/detail/${inqId}`;
            });

            tableBody.append(row);
        });
    } else {
        console.log('No data available for rendering');
        tableBody.append('<tr><td colspan="8">데이터가 없습니다.</td></tr>');
    }
}

// 테이블 컬럼 클릭
function handleRowClick(row) {
    const inqCategory = row.getAttribute('data-inq-category');
    const inqId = row.getAttribute('data-inq-id');

    if (!inqId) {
        alert("게시글 ID가 유효하지 않습니다.");
        return;
    }
    window.location.href = `/inquire/detail/${inqId}`;
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

// Document Ready
$(document).ready(function () {
    // CSRF 토큰 가져오기
    let csrfToken = $('meta[name="_csrf"]').attr('content');
    let csrfHeader = $('meta[name="_csrf_header"]').attr('content');

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
    $('.btn-notice-register').on('click', function () {
        const inqId = $(this).data('inq-id');
        if (!inqId) {
            alert("게시글 ID가 유효하지 않습니다.");
            return;
        }
        if (!confirm("해당 게시글을 공지로 등록하시겠습니까?")) return;

        $.ajax({
            url: `/system/inquire/notice-register/${inqId}`,
            type: "POST",
            beforeSend: function (xhr) {
                    xhr.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function () {
                alert("공지 등록되었습니다.");
                window.location.reload(); // 페이지 새로고침
            },
            error: function (xhr) {
                const errorMessage = xhr.responseJSON?.message || "알 수 없는 오류";
                console.error("공지 등록 실패: ", errorMessage);
                alert(`공지 등록에 실패했습니다: ${errorMessage}`);
            },
        });
    });

    // 공지사항 해제 버튼
    $('.btn-notice-cancel').on('click', function () {
        const inqId = $(this).data('inq-id');
        if (!inqId) {
            alert("게시글 ID가 유효하지 않습니다.");
            return;
        }
        if (!confirm("공지 고정을 해제하시겠습니까?")) return;

        $.ajax({
            url: `/system/inquire/notice-cancel/${inqId}`,
            type: "POST",
            beforeSend: function (xhr) {
                   xhr.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function () {
                alert("공지 고정이 해제되었습니다.");
                window.location.reload(); // 페이지 새로고침
            },
            error: function (xhr) {
                const errorMessage = xhr.responseJSON?.message || "알 수 없는 오류";
                console.error("공지 해제 실패: ", errorMessage);
                alert(`공지 해제에 실패했습니다: ${errorMessage}`);
            },
        });
    });
});

