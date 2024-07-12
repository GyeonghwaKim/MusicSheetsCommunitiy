$("li>div>#nav-modifyBtn").on("click", function () {
    var index = $("li>div>#nav-modifyBtn").index(this);

    $("li>#nav-modifyDiv").eq(index).toggle();
});

