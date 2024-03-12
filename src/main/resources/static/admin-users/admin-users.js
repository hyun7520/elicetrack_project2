import { addCommas, checkAdmin, createNavbar } from "../useful-functions.js";
import * as Api from "../api.js";

// 요소(element), input 혹은 상수
const usersCount = document.querySelector("#usersCount");
const adminCount = document.querySelector("#adminCount");
const usersContainer = document.querySelector("#usersContainer");
const modal = document.querySelector("#modal");
const modalBackground = document.querySelector("#modalBackground");
const modalCloseButton = document.querySelector("#modalCloseButton");
const deleteCompleteButton = document.querySelector("#deleteCompleteButton");
const deleteCancelButton = document.querySelector("#deleteCancelButton");

checkAdmin();
addAllElements();
addAllEvents();

// 요소 삽입 함수들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllElements() {
  createNavbar();
  insertUsers();
}

// 여러 개의 addEventListener들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllEvents() {
  modalBackground.addEventListener("click", closeModal);
  modalCloseButton.addEventListener("click", closeModal);
  document.addEventListener("keydown", keyDownCloseModal);
  deleteCompleteButton.addEventListener("click", deleteUserData);
  deleteCancelButton.addEventListener("click", cancelDelete);
}

// 페이지 로드 시 실행, 삭제할 회원 id를 전역변수로 관리함
let userIdToDelete;
// async function insertUsers() {
//   let page = 0;
//   let size = 10; // 페이지당 사용자 수를 변수로 관리
//
//   document.getElementById('page1').addEventListener('click', () => {
//     if (page > 0) {
//       page--;
//       getUsers(page, size);
//     }
//   });
//
//   document.getElementById('page2').addEventListener('click', () => {
//     page++;
//     getUsers(page, size);
//   });
//
//   const usersCount = await Api.get(`/users/count-user`);
//   const adminCount = await Api.get(`/users/count-admin`);
//   // const usersCount = usersCountResponse.data; // 가정: API 응답 구조는 { data: 값 }
//   // const adminCount = adminCountResponse.data;
//
//   // 총 요약에 값 삽입
//   document.getElementById('usersCount').innerText = usersCount.toString();
//   document.getElementById('adminCount').innerText = adminCount.toString();
//
//   async function getUsers(page, size) {
//     const response = await Api.get(`/users?page=${page}&size=${size}`);
//     const users = response.content; // 가정: API 응답 구조는 { content: 사용자 목록 }
//
//     // 기존 목록 초기화
//     const usersContainer = document.getElementById('usersContainer');
//     usersContainer.innerHTML = `
//       <div class="columns notification is-info is-light is-mobile orders-top">
//         <div class="column is-2">가입날짜</div>
//         <div class="column is-2">이메일</div>
//         <div class="column is-2">이름</div>
//         <div class="column is-2">권한</div>
//         <div class="column is-2">관리</div>
//       </div>
//     `; // 기본 컬럼 헤더 다시 그리기
//
//     for (const user of users) {
//       const { id, email, nickname, admin, createdAt } = user;
//       const date = createdAt;
//
//       usersContainer.insertAdjacentHTML(
//           "beforeend",
//           `
//           <div class="columns orders-item" id="user-${id}">
//             <div class="column is-2">${date}</div>
//             <div class="column is-2">${email}</div>
//             <div class="column is-2">${nickname}</div>
//             <div class="column is-2">
//               <div class="select">
//                 <select id="roleSelectBox-${id}">
//                   <option class="has-background-link-light has-text-link" ${admin ? "" : "selected"} value="USER">일반사용자</option>
//                   <option class="has-background-danger-light has-text-danger" ${admin ? "selected" : ""} value="ADMIN">관리자</option>
//                 </select>
//               </div>
//             </div>
//             <div class="column is-2">
//               <button class="button" id="deleteButton-${id}">회원정보 삭제</button>
//             </div>
//           </div>
//         `
//       );
//       // 나머지 이벤트 리스너 및 로직은 기존대로 유지
//     }
//   }
//
//   // 초기 페이지 로드 시 첫 번째 페이지 데이터 가져오기
//   await getUsers(page, size);
// }

// async function insertUsers() {
//   let page = 0;
//   let size = 10;
//   const users = await Api.get("/users/all");
//
//   // 총 요약에 활용
//   const usersCount = await Api.get(`/users/count-user`);
//   const adminCount = await Api.get(`/users/count-admin`);
//
//   for (const user of users) {
//     const { id, email, nickname, admin, createdAt } = user;
//     const date = createdAt;
//
//     summary.usersCount += 1;
//
//     if (admin === true) {
//       summary.adminCount += 1;
//     }
//
//     usersContainer.insertAdjacentHTML(
//         "beforeend",
//         `
//         <div class="columns orders-item" id="user-${id}">
//           <div class="column is-2">${date}</div>
//           <div class="column is-2">${email}</div>
//           <div class="column is-2">${nickname}</div>
//           <div class="column is-2">
//             <div class="select" >
//               <select id="roleSelectBox-${id}">
//                 <option
//                   class="has-background-link-light has-text-link"
//                   ${admin === true ? "selected" : ""}
//                   value="USER">
//                   일반사용자
//                 </option>
//                 <option
//                   class="has-background-danger-light has-text-danger"
//                   ${admin === true ? "selected" : ""}
//                   value="ADMIN">
//                   관리자
//                 </option>
//               </select>
//             </div>
//           </div>
//           <div class="column is-2">
//             <button class="button" id="deleteButton-${id}" >회원정보 삭제</button>
//           </div>
//         </div>
//       `
//     );
//
//     // 요소 선택
//     const roleSelectBox = document.querySelector(`#roleSelectBox-${id}`);
//     const deleteButton = document.querySelector(`#deleteButton-${id}`);
//
//     // 권한관리 박스에, 선택되어 있는 옵션의 배경색 반영
//     const index = roleSelectBox.selectedIndex;
//     roleSelectBox.className = roleSelectBox[index].className;
//
//     // 이벤트 - 권한관리 박스 수정 시 바로 db 반영
//     roleSelectBox.addEventListener("change", async () => {
//
//       if(roleSelectBox.value === "USER"){
//         alert("관리자는 일반 회원으로 바꿀 수 없습니다.");
//         roleSelectBox.value = "ADMIN";
//         const index = roleSelectBox.selectedIndex;
//         roleSelectBox.className = roleSelectBox[index].className;
//         return;
//       }
//
//       // 선택한 옵션의 배경색 반영
//       const index = roleSelectBox.selectedIndex;
//       roleSelectBox.className = roleSelectBox[index].className;
//
//       // api 요청
//       await Api.patch("/users/role", id, true);
//     });
//
//     // 이벤트 - 삭제버튼 클릭 시 Modal 창 띄우고, 동시에, 전역변수에 해당 주문의 id 할당
//     deleteButton.addEventListener("click", () => {
//       userIdToDelete = id;
//       openModal();
//     });
//   }
//
//   // 총 요약에 값 삽입
//   usersCount.innerText = addCommas(usersCount);
//   adminCount.innerText = addCommas(adminCount);
// }

async function insertUsers(page = 0, size = 10) {
  const usersResponse = await Api.get(`http://localhost:8080/users?page=${page}&size=${size}`);
  const users = usersResponse.content;

  // 총 사용자 수와 관리자 수는 한 번만 불러오면 되므로, 첫 페이지 로드 시에만 호출
  if (page === 0) {
    const usersCount = await Api.get(`http://localhost:8080/users/count-user`);
    const adminCount = await Api.get(`http://localhost:8080/users/count-admin`);

    // 총 요약에 값 삽입
    document.getElementById('usersCount').innerText = addCommas(usersCount);
    document.getElementById('adminCount').innerText = addCommas(adminCount);
  }

  // 사용자 목록 초기화 (새 페이지 로드 시 이전 목록 삭제)
  usersContainer.innerHTML = '';

  for (const user of users) {
    const { id, email, nickname, admin, createdAt } = user;
    const date = createdAt;

    usersContainer.insertAdjacentHTML(
        "beforeend",
        `
      <div class="columns orders-item" id="user-${id}">
        <div class="column is-2">${date}</div>
        <div class="column is-2">${email}</div>
        <div class="column is-2">${nickname}</div>
        <div class="column is-2">
          <div class="select" >
            <select id="roleSelectBox-${id}">
              <option
                class="has-background-link-light has-text-link"
                ${admin ? "selected" : ""}
                value="USER">
                일반사용자
              </option>
              <option
                class="has-background-danger-light has-text-danger"
                ${admin ? "selected" : ""}
                value="ADMIN">
                관리자
              </option>
            </select>
          </div>
        </div>
        <div class="column is-2">
          <button class="button" id="deleteButton-${id}" >회원정보 삭제</button>
        </div>
      </div>
      `
    );

    const roleSelectBox = document.querySelector(`#roleSelectBox-${id}`);
    const deleteButton = document.querySelector(`#deleteButton-${id}`);

    roleSelectBox.addEventListener("change", async () => {
      if(roleSelectBox.value === "USER"){
        alert("관리자는 일반 회원으로 바꿀 수 없습니다.");
        roleSelectBox.value = "ADMIN";

        return;
      }

      const isAdmin = true;

      // api 요청
      await Api.patch(`http://localhost:8080/users/${id}/role`,"", isAdmin);
    });

    deleteButton.addEventListener("click", () => {
      userIdToDelete = id;
      openModal();
    });
  }
}

// 페이지 변경 함수
function changePage(newPage) {
  insertUsers(newPage, 10); // 새 페이지 번호와 함께 사용자 목록을 다시 불러옴
}
const prevPageButton = document.getElementById('page1');
const nextPageButton = document.getElementById('page2');
let currentPage = 0;

prevPageButton.addEventListener('click', function() {
  if (currentPage > 0) { // 첫 페이지보다 클 때만 이전 페이지로 이동
    currentPage -= 1;
    changePage(currentPage);
  }
});

nextPageButton.addEventListener('click', function() {
  currentPage += 1;
  changePage(currentPage);
});


// db에서 회원정보 삭제
async function deleteUserData(e) {
  e.preventDefault();

  try {
    await Api.delete("http://localhost:8080/users", userIdToDelete);

    // 삭제 성공
    alert("회원 정보가 삭제되었습니다.");

    // 삭제한 아이템 화면에서 지우기
    const deletedItem = document.querySelector(`#user-${userIdToDelete}`);
    deletedItem.remove();

    // 전역변수 초기화
    userIdToDelete = "";

    closeModal();
  } catch (err) {
    alert(`회원정보 삭제 과정에서 오류가 발생하였습니다: ${err}`);
  }
}

// Modal 창에서 아니오 클릭할 시, 전역 변수를 다시 초기화함.
function cancelDelete() {
  userIdToDelete = "";
  closeModal();
}

// Modal 창 열기
function openModal() {
  modal.classList.add("is-active");
}

// Modal 창 닫기
function closeModal() {
  modal.classList.remove("is-active");
}

// 키보드로 Modal 창 닫기
function keyDownCloseModal(e) {
  // Esc 키
  if (e.keyCode === 27) {
    closeModal();
  }
}
