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
async function insertOrders() {
  const orders = await Api.get(`http://localhost:8080/orders/user/${sessionUser}`);
  console.log(orders.content);

  for (const order of orders.content) {
    const { id, orderDate, firstProductName, deliveryProcess } = order;
    // const date = orderDate.split("T")[0];
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
          <div class="column is-6 order-summary">${firstProductName}</div>
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
      openModal();
    });
  }
}

async function deleteData() {
  await fetch(`http://localhost:8080/orders/${orderIdToDelete}`, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
    }
  });
}

// db에서 주문정보 삭제
async function deleteOrderData(e) {
  e.preventDefault();

  try {
    await deleteData();
    // 삭제 성공
    alert("주문 정보가 삭제되었습니다.");

    // 삭제한 아이템 화면에서 지우기
    const deletedItem = document.querySelector(`#order-${orderIdToDelete}`);
    deletedItem.remove();

    // 전역변수 초기화
    orderIdToDelete = "";

    closeModal();
  } catch (err) {
    alert(`주문정보 삭제 과정에서 오류가 발생하였습니다: ${err}`);
  }
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
