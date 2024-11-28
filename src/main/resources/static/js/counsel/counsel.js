// CSRF 토큰 가져오기
const csrfToken = $('meta[name="_csrf"]').attr('content');
const csrfHeader = $('meta[name="_csrf_header"]').attr('content');

// 상담 목록 관련 변수
const listModal = document.getElementById("myModal");
const openModalBtn = document.getElementById("openModalBtn");
const closeModalBtn = document.getElementById("closeModalBtn");
const counselList = document.getElementById("counselList");
const pagination = document.getElementById("pagination");

// 상담 상세 관련 변수
const detailModal = document.getElementById("detailModal");
const backBtn1 = document.getElementById("backBtn1");
const closeDetailModalBtn = document.getElementById("closeDetailModalBtn");

const updateModal = document.getElementById("updateModal");
const counsel_update_btn = document.getElementById("counsel_update");
const counsel_update_btn_2 = document.getElementById("counsel_update_btn_2");
const closeUpdateModalBtn = document.getElementById("closeUpdateModalBtn");

const counsel_delete_btn = document.getElementById("counsel_delete");

// 상담 작성 관련 변수
const writeModal = document.getElementById("writeModal");
const counselWriteBtn = document.getElementById("counselWriteBtn");
const closeWriteModalBtn = document.getElementById("closeWriteModalBtn");
const backBtn2 = document.getElementById("backBtn2");

let global_currentPage = 1;  // 현재 페이지
const pageSize = 10;   // 한 페이지에 표시할 데이터 수

// 모달 열기
openModalBtn.onclick = function() {
    loadPageData(global_currentPage);  // 페이지 데이터 로드
    listModal.style.display = "flex";
}

// 상담 목록 모달 닫기
closeModalBtn.onclick = function() {
    listModal.style.display = "none";
}

// 상담 상세 모달 닫기
closeDetailModalBtn.onclick = function() {
    detailModal.style.display = "none";
}

// 상담 작성 모달 닫기
closeWriteModalBtn.onclick = function() {
    writeModal.style.display = "none";
}

// 상담 수정 모달 닫기
closeUpdateModalBtn.onclick = function() {
    updateModal.style.display = "none";
}

// 모달 외부 클릭 시 닫기
window.onclick = function(event) {
    if (event.target == listModal) {
        listModal.style.display = "none";
    }
}

// 페이지 데이터 로드
function loadPageData(page) {
    return fetch(`/customer/counsel/getPagedData?page=${page}&size=${pageSize}`)
        .then(response => response.json())
        .then(data => {
            renderUserList(data.content, data.number);
            renderPagination(data.totalPages, data.number); // undefined
        })
        .catch(error => console.log('Error:', error));
}

// 사용자 목록 렌더링
function renderUserList(counsels, currentPage) {
    counselList.innerHTML = '';  // 이전 내용 지우기
    // 각 필드 이름을 배열로 정의
    const counselFields = [
        'counsel_id',
        'user_name',
        'user_dept_nm',
        'counsel_category_nm',
        'counsel_create_at',
        'counsel_update_at'
    ];

    counsels.forEach((counsel, i) => {
        const tr = document.createElement("tr");

        // 각 필드를 순회하면서 항목을 표시
        counselFields.forEach((field, j) => {
            const td = document.createElement("td");

            // 번호 열 출력
            if (j === 0) {
                td.textContent = i + (currentPage - 1) * 10 + 1;
            }
            // 날짜 항목을 포맷하여 출력
            else if (field === 'counsel_create_at' || field === 'counsel_update_at') {
                if(counsel[field] === null) {
                    td.textContent = counsel[field];
                } else {
                    const date = new Date(counsel[field]);
                    // 'yyyy-mm-dd' 형식으로 포맷
                    const formattedDate = date.toLocaleDateString('en-CA');
                    td.textContent = formattedDate;
                }
            }
            // 동적으로 필드 값 삽입
            else {
                td.textContent = counsel[field];
            }

            tr.appendChild(td);  // tr에 td 추가

            // tr 클릭 시 모달 열기
            tr.addEventListener('click', () => {
                // 기존에 열려있는 모달을 닫기
                listModal.style.display = "none";

                // 새로운 모달 창에 내용 삽입
                document.getElementById("user_id").value = `${counsel.user_name}`;
                document.getElementById("user_dept_cd").value = `${counsel.user_dept_cd}`;
                document.getElementById("user_dept_nm").value = `${counsel.user_dept_nm}`;

                const create_date = new Date(counsel.counsel_create_at);
                // 'yyyy-mm-dd' 형식으로 포맷
                const formattedDate_c = create_date.toLocaleDateString('en-CA');
                document.getElementById("counsel_create_at").value = formattedDate_c;

                document.getElementById("counsel_update_at").value = "";
                if(counsel.counsel_update_at !== null){
                    const update_date = new Date(counsel.counsel_update_at);
                    // 'yyyy-mm-dd' 형식으로 포맷
                    const formattedDate_u = update_date.toLocaleDateString('en-CA');
                    document.getElementById("counsel_update_at").value = formattedDate_u;
                }

                document.getElementById("counsel_category").value = `${counsel.counsel_category}`;
                document.getElementById("counsel_category_nm").value = `${counsel.counsel_category_nm}`;
                // document.getElementById("counsel_category").value = counsel_category_f(counsel.counsel_category);
                document.getElementById("counsel_content").textContent = `${counsel.counsel_content}`;
                document.getElementById("counsel_id").value = `${counsel.counsel_id}`;

                // 새 모달을 열기
                detailModal.style.display = "flex";
            });
        });

        // 생성한 tr을 테이블에 추가
        counselList.appendChild(tr);

        // 마지막 요소일 때 추가 작업
        if (i === counsels.length - 1) {
            for(let j = i+1; j < 10; j++){
                const tr_end = document.createElement("tr");
                counselFields.forEach((field, k) => {
                    const td = document.createElement("td");

                    if(k === 0){
                        td.textContent = j + (currentPage - 1) * 10 + 1;
                    }

                    tr_end.appendChild(td);  // tr에 td 추가
                });
                counselList.appendChild(tr_end);
            }
        }
    });
}

// 뒤로가기 버튼 클릭
backBtn1.onclick = function() {
    detailModal.style.display = "none";
    listModal.style.display = "flex";
}

backBtn2.onclick = function(event) {
    event.preventDefault();
    writeModal.style.display = "none";
    listModal.style.display = "flex";
}

backBtn3.onclick = function(event) {
    event.preventDefault();
    updateModal.style.display = "none";
    detailModal.style.display = "flex";
}

// 페이지네이션
function renderPagination(totalPages, currentPage) {
    const pagination = document.getElementById("pagination");
    pagination.innerHTML = "";

    // 이전 버튼 생성
    const prevButton = document.createElement("button");
    prevButton.classList.add("custom-pagination-prev", "custom-pagination-button");
    prevButton.innerHTML = "&lt;";
    if (currentPage === 1) {
        prevButton.disabled = true;
    }
    prevButton.onclick = function () {
        if (currentPage > 1) {
            loadPageData(currentPage - 1);
        }
    };
    pagination.appendChild(prevButton);

    // 페이지 번호 생성
    for (let i = 1; i <= totalPages; i++) {
        const pageLink = document.createElement("button");
        pageLink.classList.add("custom-pagination-number");
        pageLink.textContent = i;
        if (i === currentPage) {
            pageLink.classList.add("custom-pagination-active");
            pageLink.disabled = true;
        }
        pageLink.onclick = function () {
            if (i !== currentPage) {
                loadPageData(i);
            }
        };
        pagination.appendChild(pageLink);
    }

    // 다음 버튼 생성
    const nextButton = document.createElement("button");
    nextButton.classList.add("custom-pagination-next", "custom-pagination-button");
    nextButton.innerHTML = "&gt;";
    if (currentPage === totalPages) {
        nextButton.disabled = true;
    }
    nextButton.onclick = function () {
        if (currentPage < totalPages) {
            loadPageData(currentPage + 1);
        }
    };
    pagination.appendChild(nextButton);
}

// 상담작성 버튼 클릭 (작성 모달)
counselWriteBtn.onclick = function() {
    listModal.style.display = "none";

    // 모달을 열기 전에 모든 입력 필드를 초기화
    document.getElementById("insert_counsel_category").value = "선택";
    document.getElementById("insert_counsel_content").value = "";

    document.getElementById("writer_user_id").value = "20160518007"; // 로그인된 user_id 넣기
    document.getElementById("cust_id").value = "20241118004"; // cust_id 넣기
    document.getElementById("writer_user_name").value = "손사원"; // 로그인된 user_name 넣기
    document.getElementById("writer_user_dept_cd").value = "3"; // 로그인된 user_dept_cd 넣기
    document.getElementById("writer_user_dept_nm").value = "영업2부"; // 로그인된 user_dept_cd 넣기

    writeModal.style.display = "flex";
}

// 상담작성 insert 버튼 클릭
insert_counsel_btn.onclick = function(event) {
    event.preventDefault();

    // 상담유형을 선택했는지 확인
    if (document.getElementById("insert_counsel_category").value.trim() === "선택") {
        alert("상담유형을 선택해주세요");
        return;
    }

    // 상담내용이 비어있는지 확인
    if(document.getElementById("insert_counsel_content").value.trim() === ""){
        alert("상담내용을 입력해주세요");
        return;
    }

    // AJAX 제출을 위한 FormData 객체 생성
    var formData = new FormData(document.getElementById("insertCounsel"));

    // Fetch API(AJAX)를 사용하여 양식 데이터 보내기
    fetch(document.getElementById("insertCounsel").action, {
        method: "POST",
        headers: {
            [csrfHeader]: csrfToken // CSRF 토큰 추가
        },
        body: formData
    })
        .then((response) => response.text())
        .then((data) => {
            alert(data);
            console.log("Form submitted successfully:", data);

            // 등록 후 목록 새로고침
            return loadPageData(global_currentPage);
        })
        .then(() => {
            // 등록 완료 후 모달 닫기 및 목록 모달 열기
            writeModal.style.display = "none";
            listModal.style.display = "flex";

            // 필드 초기화
            document.getElementById("insert_counsel_category").value = "선택";
            document.getElementById("insert_counsel_content").value = "";
    })
    .catch(error => {
        console.error("Error submitting form:", error);
    });
}

// 상담수정 버튼 클릭 (수정 모달)
counsel_update_btn.onclick = function() {
    detailModal.style.display = "none";

    // 수정 모달 열기 전에 필드 초기화
    document.getElementById("update_user_name").value = "";
    document.getElementById("update_user_dept_cd").value = "";
    document.getElementById("update_user_dept_nm").value = "";
    document.getElementById("update_counsel_category").value = "";
    document.getElementById("update_counsel_content").value = "";

    // 기존 데이터를 설정
    document.getElementById("update_user_name").value = document.getElementById("user_id").value;
    document.getElementById("update_user_dept_cd").value = document.getElementById("user_dept_cd").value;
    document.getElementById("update_user_dept_nm").value = document.getElementById("user_dept_nm").value;

    // 상담유형 설정
    const el = document.getElementById("update_counsel_category");
    const counselCategoryValue = document.getElementById("counsel_category").value;
    el.value = counselCategoryValue;

    document.getElementById("update_counsel_content").value = document.getElementById("counsel_content").textContent;

    updateModal.style.display = "flex";
}

// 상담수정 버튼 클릭 2
counsel_update_btn_2.onclick = function(event) {
    event.preventDefault();

    if(document.getElementById("update_counsel_content").value.trim() === ""){
        alert("상담내용을 입력해주세요");
        return;
    }

    // AJAX 제출을 위한 FormData 객체 생성
    var formData = new FormData(document.getElementById("updateCounsel"));

    // Fetch API(AJAX)를 사용하여 양식 데이터 보내기
    fetch(document.getElementById("updateCounsel").action, {
        method: "POST",
        headers: {
            [csrfHeader]: csrfToken // CSRF 토큰 추가
        },
        body: formData
    })
    .then(response => response.json())
    .then(updatedData => {
        console.log("Form submitted successfully:", updatedData);

        alert("상담 수정이 완료되었습니다.");

        // 최신 데이터를 상세 모달에 업데이트
        updateDetailModal(updatedData);

        // 리스트도 갱신
        loadPageData(global_currentPage).then(() => {
            updateModal.style.display = "none"; // 수정 모달 닫기
            detailModal.style.display = "flex"; // 상세 모달 다시 열기
        });
    })
    .catch(error => {
        console.error("Error submitting form:", error);
        alert("상담 수정 중 오류가 발생했습니다.");
    });
};

// 상세 모달 업데이트
function updateDetailModal(data) {
    document.getElementById("user_id").value = data.user_name;
    document.getElementById("user_dept_cd").value = data.user_dept_cd;
    document.getElementById("user_dept_nm").value = data.user_dept_nm;

    const createDate = new Date(data.counsel_create_at).toLocaleDateString('en-CA');
    document.getElementById("counsel_create_at").value = createDate;

    if (data.counsel_update_at) {
        const updateDate = new Date(data.counsel_update_at).toLocaleDateString('en-CA');
        document.getElementById("counsel_update_at").value = updateDate;
    } else {
        document.getElementById("counsel_update_at").value = "";
    }

    document.getElementById("counsel_category").value = data.counsel_category;
    document.getElementById("counsel_category_nm").value = data.counsel_category_nm;
    document.getElementById("counsel_content").textContent = data.counsel_content;
    document.getElementById("counsel_id").value = data.counsel_id;
}

// 상담삭제 버튼 클릭
counsel_delete.onclick = function() {
    const counselId = document.getElementById("counsel_id").value;

    // 삭제 처리
    deleteCouncel(counselId)
        .then(() => {
            // 페이지 데이터를 최신화하고 모달을 열기
            return loadPageData(global_currentPage);
        })
        .then(() => {
            detailModal.style.display = "none"; // 상세 모달 닫기
            listModal.style.display = "flex"; // 목록 모달 열기
        })
        .catch(error => {
            console.error("Error deleting counsel:", error);
        });
}

// 상담 삭제 : 기존 Get 매핑 -> DELETE 매핑 변경
/*
function deleteCouncel(id) {
    return fetch('/customer/counsel/deleteCounsel?id=' + id)
        .then(response => {
            if (!response.ok) {
                throw new Error("Failed to delete counsel");
            }
            return response.text();
        })
        .then(data => {
            alert(data);
        });
}*/

function deleteCouncel(id) {
    console.log("Counsel ID:", id); // 디버깅 용
    return fetch(`/customer/counsel/deleteCounsel?id=${id}`, {
        method: "DELETE",
        headers: {
            [csrfHeader]: csrfToken, // CSRF 토큰 추가
        },
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Failed to delete counsel");
        }
        return response.text();
    })
    .then(data => {
        alert(data);
    });
}