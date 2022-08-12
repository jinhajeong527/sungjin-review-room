import env from "../env.js"

const loginModal = document.querySelector('.login__modal');
const loginModalBtn = document.querySelector('.login__modal__btn');
const loginBtn = document.querySelector('.login__modal .login__btn');
const doorIcon = document.querySelector('.login__modal .login__btn i');
const loginCloseBtn = document.querySelector('.login__modal .login__modal__close__btn');

loginBtn.addEventListener('mouseover', () => {
    doorIcon.classList.toggle('fa-door-closed');
    doorIcon.classList.toggle('fa-door-open');
})

loginBtn.addEventListener('mouseleave', () => {
    doorIcon.classList.toggle('fa-door-closed');
    doorIcon.classList.toggle('fa-door-open');
})

loginCloseBtn.addEventListener('click', () => {
    loginModal.style.display = 'none';
})

loginBtn.addEventListener('click', () => {
    /*todo!! check pattern by regex */
    
    let inputEmail = document.getElementById('email').value;
    let inputPassword = document.getElementById('password').value;
    let user = {
        email: inputEmail,
        password: inputPassword
    }

    login(user);
})

const NotVerifiedUserErrMsg = "This reviewer is not verified yet";
function login(user) {
    axios.post(`http://${env.api_address}/api/auth/signin`, user)
      .then(function (response) {
        console.log(response);
        //토큰을 쿠키에 저장한다면 브라우저에서 쿠키에 알아서 저장해줌
      })
      .catch(function (error) {
        console.log(error);
        //console.log(error.response.data.message);
        //console.log(error.response.data.verificationToken);
        /* AuthController 에서 이메일 인증 안한 리뷰어가 로그인 시에 DisabledException 발생. => badRequest()로 ResponseEntity 리턴하고 이는
        HTTP STATUS가 BAD_REQUEST가 되기 때문에 재인증 페이지 이동 로직은 error로 이동하게 된다. 
        */
        if (error.response.data.message == NotVerifiedUserErrMsg) {
            let token = error.response.data.verificationToken;
            window.location.href = `../reauth/reauth.html?token=${token}` 
        }
      });
}