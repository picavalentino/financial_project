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
    listModal.style.display = "block";
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
    fetch(`/customer/counsel/getPagedData?page=${page}&size=${pageSize}`)
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
        'user_dept_cd',
        'counsel_category',
        'counsel_create_at',
        'counsel_update_at'
    ];

    counsels.forEach((counsel, i) => {
        const tr = document.createElement("tr");

        // counselFields 배열을 통해 각 필드를 순회
        counselFields.forEach((field, j) => {
            const td = document.createElement("td");

            if(field === 'counsel_create_at' || field === 'counsel_update_at'){
                if(counsel[field] === null){
                    td.textContent = counsel[field];
                } else {
                    const date = new Date(counsel[field]);

                    // 'yyyy-mm-dd' 형식으로 포맷
                    const formattedDate = date.toLocaleDateString('en-CA');
                    td.textContent = formattedDate;
                }
            } else if(field === 'counsel_category') {
                const counsel_category = counsel[field];
                td.textContent = counsel_category_f(counsel_category);
            } else if(j === 0){
                td.textContent = i + (currentPage - 1) * 10 + 1;
            } else {
                td.textContent = counsel[field];  // 동적으로 필드 값 삽입
            }

            tr.appendChild(td);  // tr에 td 추가

            // tr 클릭 시 모달 열기
            tr.addEventListener('click', () => {
                // 기존에 열려있는 모달을 닫기
                listModal.style.display = "none";

                // 새로운 모달 창에 내용 삽입
                document.getElementById("user_id").value = `${counsel.user_name}`;
                document.getElementById("user_dept_cd").value = `${counsel.user_dept_cd}`;

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

                document.getElementById("counsel_category").value = counsel_category_f(counsel.counsel_category);
                document.getElementById("counsel_content").textContent = `${counsel.counsel_content}`;

                document.getElementById("counsel_id").value = `${counsel.counsel_id}`;

                // 새 모달을 열기
                detailModal.style.display = "block";
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

function counsel_category_f(counsel_category){
    let result = '';
    if(counsel_category === '1'){
        result = '적금';
    } else if(counsel_category === '2'){
        result = '예금';
    } else if(counsel_category === '3'){
        result = '목돈적금';
    } else if(counsel_category === '4'){
        result = '대출';
    } else if(counsel_category === '5'){
        result = '기타';
    }
    return result;
}

// 뒤로가기 버튼 클릭
backBtn1.onclick = function() {
    detailModal.style.display = "none";
    listModal.style.display = "block";
}

backBtn2.onclick = function(event) {
    event.preventDefault();
    writeModal.style.display = "none";
    listModal.style.display = "block";
}

backBtn3.onclick = function(event) {
    event.preventDefault();
    updateModal.style.display = "none";
    detailModal.style.display = "block";
}

// 페이징 버튼 렌더링
function renderPagination(totalPages, currentPage) {
    pagination.innerHTML = '';  // 이전 내용 지우기

    // 이전 페이지 버튼 생성
    const prevLi = document.createElement("li");
    prevLi.classList.add("page-item");
    if (currentPage === 1) {
        prevLi.classList.add("disabled"); // 첫 페이지일 때 이전 버튼 비활성화
    }

    const prevA = document.createElement("a");
    prevA.classList.add("page-link");
    prevA.setAttribute("href", "#");
    prevA.innerHTML = "&lt;"; // 이전 페이지 기호

    prevLi.appendChild(prevA);

    // 이전 버튼 클릭 시, 첫 페이지가 아니면 페이지 로드
    prevLi.onclick = function() {
        if (currentPage > 1) {
            loadPageData(currentPage - 1);
        }
    };

    pagination.appendChild(prevLi);  // 이전 버튼 추가

    // 페이지 네비게이션 링크 생성
    for (let i = 1; i <= totalPages; i++) {
        const li = document.createElement("li");
        li.classList.add("page-item");

        const a = document.createElement("a");
        a.classList.add("page-link");
        a.setAttribute("href", "#");
        a.textContent = i;  // 링크 텍스트

        li.appendChild(a);

        li.onclick = function() {
            if (i !== currentPage) {
                loadPageData(i);
            }
        };

        if (i === currentPage) {
            li.classList.add("active");
            a.style.pointerEvents = "none";
        }

        pagination.appendChild(li);
    }

    // 다음 페이지 버튼 생성
    const nextLi = document.createElement("li");
    nextLi.classList.add("page-item");
    if (currentPage === totalPages) {
        nextLi.classList.add("disabled"); // 마지막 페이지일 때 다음 버튼 비활성화
    }

    const nextA = document.createElement("a");
    nextA.classList.add("page-link");
    nextA.setAttribute("href", "#");
    nextA.innerHTML = "&gt;"; // 다음 페이지 기호

    nextLi.appendChild(nextA);

    // 다음 버튼 클릭 시, 마지막 페이지가 아니면 페이지 로드
    nextLi.onclick = function() {
        if (currentPage < totalPages) {
            loadPageData(currentPage + 1);
        }
    };

    pagination.appendChild(nextLi);  // 다음 버튼 추가
}

// 상담작성 버튼 클릭
counselWriteBtn.onclick = function() {
    listModal.style.display = "none";

    document.getElementById("writer_user_id").value = "20160518007"; // 로그인된 user_id 넣기
    document.getElementById("cust_id").value = "20241118004"; // cust_id 넣기
    document.getElementById("writer_user_name").value = "손사원"; // 로그인된 user_name 넣기
    document.getElementById("writer_user_dept_cd").value = "3"; // 로그인된 user_dept_cd 넣기

    writeModal.style.display = "block";
}

// 상담작성 insert 버튼 클릭
insert_counsel_btn.onclick = function(event) {
    event.preventDefault();

    if(document.getElementById("insert_counsel_category").value === "선택"){
        alert("상담유형을 선택해주세요");
        return;
    }

    if(document.getElementById("insert_counsel_content").value.trim() === ""){
        alert("상담내용을 입력해주세요");
        return;
    }

    // AJAX 제출을 위한 FormData 객체 생성
    var formData = new FormData(document.getElementById("insertCounsel"));

    // Fetch API(AJAX)를 사용하여 양식 데이터 보내기
    fetch(document.getElementById("insertCounsel").action, {
        method: "POST",
        body: formData
    })
    .then(response => response.text())
    .then(data => {
        console.log("Form submitted successfully:", data);

        // 양식 제출이 완료된 후 모달 표시
        writeModal.style.display = "none";
        listModal.style.display = "block";
    })
    .catch(error => {
        console.error("Error submitting form:", error);
    });
}

// 상담수정 버튼 클릭
counsel_update_btn.onclick = function() {
    detailModal.style.display = "none";

    document.getElementById("update_user_name").value = document.getElementById("user_id").value;
    document.getElementById("update_user_dept_cd").value = document.getElementById("user_dept_cd").value;

    const el = document.getElementById("update_counsel_category");
    const len = el.options.length;
    const str = document.getElementById("counsel_category").value;
    for (let i=0; i<len; i++){
        if(el.options[i].textContent == str){
            el.options[i].selected = true;
        }
    }

    document.getElementById("update_counsel_content").textContent = document.getElementById("counsel_content").textContent;

    updateModal.style.display = "block";
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
        body: formData
    })
    .then(response => response.text())
    .then(data => {
        console.log("Form submitted successfully:", data);

        loadPageData(global_currentPage); // 이 함수가 먼저 다 실행된 뒤에 아래 코드 실행하는 방법



        // 양식 제출이 완료된 후 모달 표시
        updateModal.style.display = "none";
        detailModal.style.display = "block";
    })
    .catch(error => {
        console.error("Error submitting form:", error);
    });
}

// 상담삭제 버튼 클릭
counsel_delete.onclick = function() {
    detailModal.style.display = "none";

    deleteCouncel(document.getElementById("counsel_id").value);

    loadPageData(global_currentPage);

    listModal.style.display = "block";
}

function deleteCouncel(id) {
    fetch('http://localhost:4000/customer/counsel/deleteCounsel?id='+id)
        .then(response => response.text())
        .then(data => {
            alert(data);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

// POST 요청을 보내는 예시
//const message = { content: "Hello from JavaScript!" };
//
//fetch('http://localhost:8080/api/greeting', {
//    method: 'POST',
//    headers: {
//        'Content-Type': 'application/json',
//    },
//    body: JSON.stringify(message)  // JSON 형식으로 요청 본문(body)을 설정
//})
//    .then(response => response.json())  // 응답을 JSON으로 파싱
//    .then(data => {
//        console.log('Response:', data);
//        // 서버에서 응답 받은 데이터를 처리하는 로직
//    })
//    .catch(error => {
//        console.error('Error:', error);
//    });