const enterBtn = document.querySelector('header .main .enterBtn');
const doorIcon = document.querySelector('header .main .enterBtn i');
enterBtn.addEventListener('mouseover', () => {
    doorIcon.classList.toggle('fa-door-closed');
    doorIcon.classList.toggle('fa-door-open');
})
enterBtn.addEventListener('mouseleave', () => {
    doorIcon.classList.toggle('fa-door-closed');
    doorIcon.classList.toggle('fa-door-open');
})