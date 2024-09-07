console.log("Script Loaded");

let currentTheme = getTheme();
document.addEventListener("DOMContentLoaded", () => {
    changeTheme();
});

function changeTheme() {
    // set to web page
    document.querySelector("html").classList.add(currentTheme);
    // set the listener to change theme on button click

    const themeBtn = document.querySelector("#theme_btn");
    themeBtn.querySelector("span").textContent = currentTheme == "light" ? "Dark" : "Light";
    themeBtn.addEventListener("click", () => {
        const temptheme = currentTheme;
        if (currentTheme == "light")
            currentTheme = "dark";
        else
            currentTheme = "light";

        // update to localstorage
        setTheme(currentTheme);
        document.querySelector("html").classList.remove(temptheme);
        document.querySelector("html").classList.add(currentTheme);

        //theme button text change
        themeBtn.querySelector("span").textContent = currentTheme == "light" ? "Dark" : "Light";
    });
}

function setTheme(theme) {
    localStorage.setItem("theme", theme);
}

function getTheme() {
    theme = localStorage.getItem("theme");
    if (theme)
        return theme;
    else
        return "light";
}