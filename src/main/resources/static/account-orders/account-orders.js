import { checkLogin, createNavbar } from "../../useful-functions.js";
import * as Api from "../../api.js";


// 요소(element), input 혹은 상수
const ordersContainer = document.querySelector("#ordersContainer");
const modal = document.querySelector("#modal");
const modalBackground = document.querySelector("#modalBackground");
const modalCloseButton = document.querySelector("#modalCloseButton");
const deleteCompleteButton = document.querySelector("#deleteCompleteButton");
const deleteCancelButton = document.querySelector("#deleteCancelButton");

const sessionUser = sessionStorage.getItem("id");
if (sessionUser == null) {
  alert("로그인이 필요한 페이지입니다!");
  checkLogin();
}

addAllElements();
addAllEvents();

// 요소 삽입 함수들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllElements() {
  createNavbar();
  insertOrders();
}

// 여러 개의 addEventListener들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllEvents() {
  modalBackground.addEventListener("click", closeModal);
  modalCloseButton.addEventListener("click", closeModal);
  document.addEventListener("keydown", keyDownCloseModal);
  deleteCompleteButton.addEventListener("click", deleteOrderData);
  deleteCancelButton.addEventListener("click", cancelDelete);
}

// 페이지 로드 시 실행, 삭제할 주문 id를 전역변수로 관리함
let orderIdToDelete;
let totalPages = 0;
async function insertOrders(page = 0, size = 5) {
  const orders = await Api.get(`http://34.64.249.228:8080/orders/user/${sessionUser}?page=${page}&size=${size}`);

  totalPages = orders.totalPages;

  ordersContainer.innerHTML = "";

  console.log(orders);


  for (const order of orders.content) {
    const { id, orderDate, deliveryProcess } = order;
    let productName;
    try {
      productName = order.orderDetails[0].productName;
    }
    catch {
      if (productName == "") {
        productName = "undefined";
      }
    }
    const date = new Date(orderDate).toLocaleDateString('ko-KR');

    let process = '';

    if (deliveryProcess == 'preparing') {
      process = '준비중';
    }
    if (deliveryProcess == 'shipping') {
      process = '배송중';
    }
    if (deliveryProcess == 'complete') {
      process = '배달완료';
    }

    ordersContainer.insertAdjacentHTML(
      "beforeend",
      `
        <div class="columns orders-item" id="order-${id}">
          <div class="column is-2">${date}</div>
          <div class="column is-6 order-summary">${productName} 외 ${order.orderDetails.length - 1} 개의 제품</div>
          <div class="column is-2">${process}</div>
          <div class="column is-2">
            <button class="button" id="deleteButton-${id}" >주문 취소</button>
          </div>
        </div>
      `
    );

    const deleteButton = document.querySelector(`#deleteButton-${id}`);

    // Modal 창 띄우고, 동시에, 전역변수에 해당 주문의 id 할당
    deleteButton.addEventListener("click", () => {
      orderIdToDelete = id;
      if (deliveryProcess === 'preparing') {
        openModal();
      } else {
        alert("배송중이거나 배송이 완료된 제품은 취소가 불가능합니다!");
      }
    });
  }
}

function paging(newPage) {
  insertOrders(newPage, 5);
}

const prevPage = document.querySelector('#before');
const nextPage = document.querySelector('#next');
let curPage = 0;

prevPage.addEventListener('click', function () {
  if (curPage > 0) {
    curPage -= 1;
    paging(curPage);
  }
})

nextPage.addEventListener('click', function () {
  console.log(totalPages);
  if (curPage + 1 === totalPages) {
    alert("마지막 페이지입니다!");
  } else {
    curPage += 1;
    paging(curPage);
  }
});

// db에서 주문정보 삭제
async function deleteOrderData(e) {
  e.preventDefault();

  await fetch(`http://34.64.249.228:8080/orders/${orderIdToDelete}`, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
    }
  });

  // 삭제 성공
  alert("주문 정보가 삭제되었습니다.");

  // 삭제한 아이템 화면에서 지우기
  const deletedItem = document.querySelector(`#order-${orderIdToDelete}`);
  deletedItem.remove();

  // 전역변수 초기화
  orderIdToDelete = "";

  closeModal();
}

// Modal 창에서 아니오 클릭할 시, 전역 변수를 다시 초기화함.
function cancelDelete() {
  orderIdToDelete = "";
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
