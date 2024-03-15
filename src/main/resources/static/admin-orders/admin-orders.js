import { addCommas, checkAdmin, createNavbar } from "../useful-functions.js";
import * as Api from "../api.js";
// import {
//   title
// } from "../../../../../../../../Library/Application Support/JetBrains/IntelliJIdea2023.3/javascript/extLibs/http_sdk.amazonaws.com_js_aws-sdk-2.410.0";

// 요소(element), input 혹은 상수
const ordersCount = document.querySelector("#ordersCount");
const prepareCount = document.querySelector("#prepareCount");
const deliveryCount = document.querySelector("#deliveryCount");
const completeCount = document.querySelector("#completeCount");
const ordersContainer = document.querySelector("#ordersContainer");
const modal = document.querySelector("#modal");
const modalBackground = document.querySelector("#modalBackground");
const modalCloseButton = document.querySelector("#modalCloseButton");
const deleteCompleteButton = document.querySelector("#deleteCompleteButton");
const deleteCancelButton = document.querySelector("#deleteCancelButton");

// const isAdmin = sessionStorage.getItem("isAdmin");
// if (isAdmin == false) {
//   window.alert("로그인 해주세요!");;
// }
let curPage = 0;

addAllElements();
addAllEvents();
checkAdmin();

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
  const orders = await Api.get(`http://34.64.249.228:8080/orders?page=${page}&size=${size}`);

  totalPages = orders.totalPages;

  ordersContainer.innerHTML = "";

  const summary = {
    ordersCount: 0,
    prepareCount: 0,
    deliveryCount: 0,
    completeCount: 0,
  };

  for (const order of orders.content) {
    console.log(order);
    const { id, orderBy, orderDate, deliveryProcess, totalCost } = order;

    let summaryTitle;
    try {
      summaryTitle = order.orderDetails[0].productName;
    }
    catch {
      if (summaryTitle == "") {
        summaryTitle = "undefined";
      }
    }

    const date = new Date(orderDate).toLocaleDateString('ko-KR');

    console.log(summaryTitle)
    console.log(deliveryProcess)


    summary.ordersCount += 1;

    if (deliveryProcess === "preparing") {
      summary.prepareCount += 1;
    } else if (deliveryProcess === "shipping") {
      summary.deliveryCount += 1;
    } else if (deliveryProcess === "complete") {
      summary.completeCount += 1;
    }

    ordersContainer.insertAdjacentHTML(
      "beforeend",
      `
        <div class="columns orders-item" id="order-${id}">
          <div class="column is-2">${date}</div>
          <div class="column is-2">${orderBy}</div>
          <div class="column is-4 order-summary">${summaryTitle}</div>
          <div class="column is-2">${addCommas(totalCost)}</div>
          <div class="column is-2">
            <div class="select" >
              <select id="statusSelectBox-${id}">
                <option 
                  class="has-background-danger-light has-text-danger"
                  ${deliveryProcess === "preparing" ? "selected" : ""} 
                  value="preparing">
                  preparing
                </option>
                <option 
                  class="has-background-primary-light has-text-primary"
                  ${deliveryProcess === "shipping" ? "selected" : ""} 
                  value="shipping">
                  shipping
                </option>
                <option 
                  class="has-background-grey-light"
                  ${deliveryProcess === "complete" ? "selected" : ""} 
                  value="complete">
                  complete
                </option>
              </select>
            </div>
          </div>
          <div class="column is-2">
            <button class="button" id="deleteButton-${id}" >주문 취소</button>
          </div>
        </div>
      `
    );

    // 요소 선택
    const statusSelectBox = document.querySelector(`#statusSelectBox-${id}`);
    const deleteButton = document.querySelector(`#deleteButton-${id}`);

    // 상태관리 박스에, 선택되어 있는 옵션의 배경색 반영
    const index = statusSelectBox.selectedIndex;
    statusSelectBox.className = statusSelectBox[index].className;

    // 이벤트 - 상태관리 박스 수정 시 바로 db 반영
    statusSelectBox.addEventListener("change", async () => {
      const newStatus = statusSelectBox.value;
      const data = { "orderProcess": "confirmed", "deliveryProcess": `${newStatus}` };

      console.log(data);

      // 선택한 옵션의 배경색 반영
      const index = statusSelectBox.selectedIndex;
      statusSelectBox.className = statusSelectBox[index].className;

      // api 요청
      await fetch(`http://34.64.249.228:8080/orders/${id}/order-status`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(data)
      });
      location.reload(true);
    });

    // 이벤트 - 삭제버튼 클릭 시 Modal 창 띄우고, 동시에, 전역변수에 해당 주문의 id 할당
    deleteButton.addEventListener("click", () => {
      orderIdToDelete = id;
      if (deliveryProcess === 'preparing') {
        openModal();
      } else {
        alert("배송중이거나 배송이 완료된 제품은 취소가 불가능합니다!");
      }
    });
  }

  // 총 요약 값 삽입
  ordersCount.innerText = addCommas(summary.ordersCount);
  prepareCount.innerText = addCommas(summary.prepareCount);
  deliveryCount.innerText = addCommas(summary.deliveryCount);
  completeCount.innerText = addCommas(summary.completeCount);
}

// db에서 주문정보 삭제
async function deleteOrderData(e) {
  e.preventDefault();

  try {
    const result = await fetch(`http://34.64.249.228:8080/orders/${orderIdToDelete}`, {
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
  } catch (err) {
    alert(`주문정보 삭제 과정에서 오류가 발생하였습니다: ${err}`);
  }
}

function paging(newPage) {
  insertOrders(newPage, 5);
}

const prevPage = document.querySelector('#before');
const nextPage = document.querySelector('#next');

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
