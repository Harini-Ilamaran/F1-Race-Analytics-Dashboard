const menuToggle =
    document.getElementById("menuToggle");

const navbar =
    document.getElementById("navbar");

if(menuToggle && navbar){

    menuToggle.addEventListener(
        "click",
        () => {

            navbar.classList.toggle(
                "active"
            );

            if(navbar.classList.contains("active")){
                menuToggle.innerHTML = "✕";
            }
            else{
                menuToggle.innerHTML = "☰";
            }
        }
    );
}