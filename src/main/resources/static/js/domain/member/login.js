// 로그인 실패 시 오류를 띄우는 기능

let isLoginAble = true // 로그인 가능여부 조건 나중에 수정

if (isLoginAble === false) {
    $(".message_box").append(`
        <div class="alert alert-danger" role="alert">
            아이디나 비밀번호가 잘못되었습니다.
        </div>
    `)
}






