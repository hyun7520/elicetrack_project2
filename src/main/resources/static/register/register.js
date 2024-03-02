import * as Api from "../api.js";
import { validateEmail, createNavbar } from "../useful-functions.js";

// 요소(element), input 혹은 상수
const nicknameInput = document.querySelector("#nicknameInput");
const emailInput = document.querySelector("#emailInput");
const passwordInput = document.querySelector("#passwordInput");
const passwordConfirmInput = document.querySelector("#passwordConfirmInput");
const submitButton = document.querySelector("#submitButton");
const postcodeInput = document.querySelector('#postcode');
const addressInput = document.querySelector('#address');
const detailAddressInput = document.querySelector('#detailAddress');
const extraAddressInput = document.querySelector('#extraAddress');


addAllElements();
addAllEvents();

// html에 요소를 추가하는 함수들을 묶어주어서 코드를 깔끔하게 하는 역할임.
async function addAllElements() {
  createNavbar();
}

// 여러 개의 addEventListener들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllEvents() {
  submitButton.addEventListener("click", handleSubmit);
}

// Daum Postcode API 불러오기
function loadPostcodeScript() {
  return new Promise((resolve, reject) => {
    const script = document.createElement('script');
    script.src = '//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js';
    script.onload = resolve;
    script.onerror = reject;
    document.head.appendChild(script);
  });
}

loadPostcodeScript().then(() => {
  window.execDaumPostcode = function() {
    new daum.Postcode({
      oncomplete: function(data) {
        // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

        // 각 주소의 노출 규칙에 따라 주소를 조합한다.
        // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
        var addr = ''; // 주소 변수
        var extraAddr = ''; // 참고항목 변수

        //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
        if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
          addr = data.roadAddress;
        } else { // 사용자가 지번 주소를 선택했을 경우(J)
          addr = data.jibunAddress;
        }

        // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
        if(data.userSelectedType === 'R'){
          // 법정동명이 있을 경우 추가한다. (법정리는 제외)
          // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
          if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
            extraAddr += data.bname;
          }
          // 건물명이 있고, 공동주택일 경우 추가한다.
          if(data.buildingName !== '' && data.apartment === 'Y'){
            extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
          }
          // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
          if(extraAddr !== ''){
            extraAddr = ' (' + extraAddr + ')';
          }
          // 조합된 참고항목을 해당 필드에 넣는다.
          document.getElementById("extraAddress").value = extraAddr;

        } else {
          document.getElementById("extraAddress").value = '';
        }

        // 우편번호와 주소 정보를 해당 필드에 넣는다.
        document.getElementById('postcode').value = data.zonecode;
        document.getElementById("address").value = addr;
        // 커서를 상세주소 필드로 이동한다.
        document.getElementById("detailAddress").focus();
      }
    }).open();
  };
}).catch((error) => {
  console.error('Daum Postcode API를 로드하는데 실패했습니다.', error);
});


// 회원가입 진행
async function handleSubmit(e) {
  e.preventDefault();

  const nickname = nicknameInput.value;
  const email = emailInput.value;
  const password = passwordInput.value;
  const passwordConfirm = passwordConfirmInput.value;
  const postcode = postcodeInput.value;
  const address = addressInput.value;
  const detailAddress = detailAddressInput.value;
  const extraAddress = extraAddressInput.value;

  // 잘 입력했는지 확인
  const isnicknameValid = nickname.length >= 2;
  const isEmailValid = validateEmail(email);
  const isPasswordValid = password.length >= 4;
  const isPasswordSame = password === passwordConfirm;

  if (!isnicknameValid || !isPasswordValid) {
    return alert("닉네임은 2글자 이상, 비밀번호는 4글자 이상이어야 합니다.");
  }

  if (!isEmailValid) {
    return alert("이메일 형식이 맞지 않습니다.");
  }

  if (!isPasswordSame) {
    return alert("비밀번호가 일치하지 않습니다.");
  }

  // 이메일 중복 확인
  try {
    const duplicateCheckResult = await Api.get(`/users/checkEmail?email=${email}`);
    console.log(duplicateCheckResult);
    if (duplicateCheckResult) {
      return alert("이미 사용 중인 이메일입니다.");
    }
  } catch (err) {
    console.error(err.stack);
    alert(`이메일 중복 확인 중 문제가 발생하였습니다. 확인 후 다시 시도해 주세요: ${err.message}`);
    return;
  }

  // 회원가입 api 요청
  try {
    const data = { nickname, email, password, postcode, address, detailAddress, extraAddress };

    await Api.post("/users/sign-up", data);

    alert(`정상적으로 회원가입되었습니다.`);
    // 로그인 페이지 이동
    window.location.href = "/login";
  } catch (err) {
    console.error(err.stack);
    alert(`문제가 발생하였습니다. 확인 후 다시 시도해 주세요: ${err.message}`);
  }
}
