const loginModal = document.querySelector('.login__modal');
const loginModalBtn = document.querySelector('.login__modal__btn');
const loginBtn = document.querySelector('.login__modal .login__btn');
const doorIcon = document.querySelector('.login__modal .login__btn i');
const loginCloseBtn = document.querySelector('.login__modal .login__modal__close__btn');

loginModalBtn.addEventListener('click', () => {
    loginModal.style.display = 'flex';
});

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
    /*todo!! password 암호화 with random salt -> 서버에서 */
    
    let inputEmail = document.getElementById('email').value;
    let inputPassword = document.getElementById('password').value;
    let user = {
        email: inputEmail,
        password: inputPassword
    }

    login(user);
})

function login(user) {
    axios.post('http://localhost:5500/api/auth/signin', user)
      .then(function (response) {
        console.log(response);
      })
      .catch(function (error) {
        console.log(error);
      });
}