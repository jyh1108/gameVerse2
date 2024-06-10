let warnbox_id = $(".userID").closest('.field').find('.warn')
let warnbox_pw = $(".userPW").closest('.field').find('.warn')
let warnbox_pw_confirm = $(".userPW_confirm").closest('.field').find('.warn')
let warnbox_email = $(".userEmail").closest('.field').find('.warn')
let warnbox_nickname = $(".userNickname").closest('.field').find('.warn')

// 비밀번호 조건이 맞는지 확인하는 함수
function isPWCorrect() {
    let isGood_PW = function () {
        let pw = $(".userPW").val()
        let num = pw.search(/[0-9]/g)
        let eng = pw.search(/[a-z]/ig)
        let spe = pw.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi)

        if (pw.length < 8 || pw.length > 20) {
            // alert("8자리 ~ 20자리 이내로 입력해주세요.")
            return 1
        } else if (pw.search(/\s/) != -1) {
            // alert("비밀번호는 공백 없이 입력해주세요.")
            return 2
        } else if (num < 0 || eng < 0 || spe < 0) {
            // alert("영문,숫자, 특수문자를 혼합하여 입력해주세요.")
            return 3
        } else {
            // console.log("통과")
            return 0
        }
    }

    // console.log(`isGood_PW = ${isGood_PW()}`)

    if ($(".userPW").val() === "") {
        warnbox_pw.text("영문, 숫자, 특수문자를 혼합하여 8~20자리 사이로 입력")
        warnbox_pw.css("color", "gray")
    }
    else if (isGood_PW() === 1) {
        warnbox_pw.text("8자리 ~ 20자리 이내로 입력해주세요.")
        warnbox_pw.css("color", "red")
    }
    else if (isGood_PW() === 2) {
        warnbox_pw.text("비밀번호는 공백 없이 입력해주세요.")
        warnbox_pw.css("color", "red")
    }
    else if (isGood_PW() === 3) {
        warnbox_pw.text("영문,숫자, 특수문자를 혼합하여 입력해주세요.")
        warnbox_pw.css("color", "red")
    }
    else {
        warnbox_pw.text("사용 가능한 비밀번호입니다.")
        warnbox_pw.css("color", "blue")
    }
}

// 비밀번호와 비밀번호확인이 일치하는지 확인하는 함수
function isPW_ConfirmCorrect() {
    if ($(".userPW_confirm").val() === "") {
        warnbox_pw_confirm.text("")
    }
    else if ($(".userPW").val() === $(".userPW_confirm").val()) {
        warnbox_pw_confirm.text("비밀번호가 일치합니다.")
        warnbox_pw_confirm.css("color", "blue")
    }
    else {
        warnbox_pw_confirm.text("비밀번호가 일치하지 않습니다.")
        warnbox_pw_confirm.css("color", "red")
    }
}


// pw condition
$(".userPW").on("change", function () {
    isPWCorrect()
    isPW_ConfirmCorrect()
})


// pw-confirm condition
$(".userPW_confirm").on("change", function () {
    isPWCorrect()
    isPW_ConfirmCorrect()
})

//아이디 중복확인
$(document).ready(function () {
    // ID 중복 확인 버튼 클릭 이벤트
    $('#checkIdBtn').on('click', function () {
        var loginId = $('#loginId').val();

        if (loginId === '') {
            $('#idCheckResult').text('아이디를 입력해주세요.');
            return;
        }

        $.ajax({
            url: '/member/checkId',  // 중복 확인을 위한 서버 엔드포인트
            type: 'GET',
            data: { loginId: loginId },
            success: function (response) {
                if (response === 'OK') {
                    $('#idCheckResult').text('사용 가능한 아이디입니다.').css('color', 'green');
                } else {
                    $('#idCheckResult').text('이미 사용 중인 아이디입니다.').css('color', 'red');
                }
            },
            error: function () {
                $('#idCheckResult').text('아이디 중복 확인에 실패했습니다.').css('color', 'red');
            }
        });
    });
});