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
    const header = $(`[data-column="${column}"]`);
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
    console.log("AJAX 요청: 페이지 번호 =", page);
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
        url: `${window.location.origin}/system/inquire`,
        method: 'GET',
        data: data,
        success: function (response) {
            console.log("서버 응답:", response);
            renderTable(response.list); // 테이블 렌더링
            updatePagination(response.totalPages, response.currentPage); // 페이지네이션 업데이트
            updateProductSize(response.totalItems); // 총 항목 수 업데이트
        },
        error: function (error) {
            console.error('AJAX 요청 오류:', error);
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

            // 날짜 형식 변환 함수
            const formatDate = (dateString) => {
                if (!dateString) return null;
                const date = new Date(dateString);
                const year = date.getFullYear();
                const month = String(date.getMonth() + 1).padStart(2, '0'); // 월(0-11이므로 +1)
                const day = String(date.getDate()).padStart(2, '0');
                const hours = String(date.getHours()).padStart(2, '0');
                const minutes = String(date.getMinutes()).padStart(2, '0');
                const seconds = String(date.getSeconds()).padStart(2, '0');
                return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
            };

            const formattedCreateAt = formatDate(product.inqCreateAt);
            const formattedUpdateAt = formatDate(product.inqUpdateAt);

            const row = $(`<tr class="table-row" style="cursor: pointer;"></tr>`);
            // 공지사항 여부에 따른 클래스 추가
            if (product.inqNotice == '1') {
                row.addClass('highlight-row');
            }
            row.append(`<td>${product.inqId || 'N/A'}</td>`);
            row.append(`<td>${product.inqCategory || 'N/A'}</td>`);
            row.append(`<td>${product.inqAnonym == 1 ? '익명' : product.userId}</td>`);
            row.append(`<td>${product.inqTitle}</td>`);
            row.append(`<td>${formattedCreateAt || 'N/A'}</td>`);
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
    paginationContainer.empty(); // 기존 페이지네이션 초기화

    const maxVisible = 5; // 한 번에 표시할 최대 페이지 번호 수
    const startPage = Math.max(1, currentPage - Math.floor(maxVisible / 2));
    const endPage = Math.min(totalPages, startPage + maxVisible - 1);

    // 이전 버튼
    if (currentPage > 1) {
        paginationContainer.append(
            `<button class="pagination-prev" data-page="${currentPage - 1}">&lt;</button>`
        );
    } else {
        paginationContainer.append(
            `<button class="pagination-prev" disabled>&lt;</button>`
        );
    }

    // 페이지 번호
    for (let i = startPage; i <= endPage; i++) {
        const activeClass = i === currentPage ? 'pagination-active' : '';
        paginationContainer.append(
            `<a class="pagination-number ${activeClass}" data-page="${i}">${i}</a>`
        );
    }

    // 다음 버튼
    if (currentPage < totalPages) {
        paginationContainer.append(
            `<button class="pagination-next" data-page="${currentPage + 1}">&gt;</button>`
        );
    } else {
        paginationContainer.append(
            `<button class="pagination-next" disabled>&gt;</button>`
        );
    }

    // 이벤트 바인딩
    $('.pagination-number, .pagination-prev, .pagination-next').off('click').on('click', function () {
        const page = $(this).data('page');
        filterResults(page); // AJAX 요청
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

