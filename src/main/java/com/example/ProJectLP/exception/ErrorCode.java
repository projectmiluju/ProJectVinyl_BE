package com.example.ProJectLP.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {


    LOGIN_REQUIRED(HttpStatus.BAD_REQUEST, "E_001", "로그인이 필요합니다."),
    LOGIN_NOTFOUND_MEMBER(HttpStatus.BAD_REQUEST,"E_002","사용자를 찾을수 없습니다."),
    LOGIN_EMPTY_USERNAME(HttpStatus.BAD_REQUEST, "E_003","아이디를 입력해 주세요."),
    LOGIN_EMPTY_PASSWORD(HttpStatus.BAD_REQUEST,"E_004","비밀번호를 입력해 주세요."),
    LOGIN_NOTFOUND_USERNAME(HttpStatus.BAD_REQUEST,"E_005","존재하지 않는 아이디이거나 비밀번호가 틀렸습니다."),
    LOGIN_NOTFOUND_PASSWORD(HttpStatus.BAD_REQUEST,"E_006","존재하지 않는 아이디이거나 비밀번호가 틀렸습니다."),

    VINYL_UPLOAD_FORBIDDEN(HttpStatus.BAD_REQUEST, "E_007", "바이닐 등록권한이 없는 사용자 입니다."),
    VINYL_DELETE_FORBIDDEN(HttpStatus.BAD_REQUEST, "E_008", "바이닐 삭제권한이 없는 사용자 입니다."),
    VINYL_MODIFY_FORBIDDEN(HttpStatus.BAD_REQUEST, "E_009", "바이닐 수정권한이 없는 사용자 입니다."),
    VINYL_NOTFOUND(HttpStatus.BAD_REQUEST,"E_010","찾을수 없는 바이닐 입니다."),

    VINYL_SONG_MODIFY_FORBIDDEN(HttpStatus.BAD_REQUEST, "E_011", "트랙리스트 수정권한이 없는 사용자 입니다."),

    VINYL_COMMENT_EMPTY_CONTENT(HttpStatus.BAD_REQUEST,"E_012", "바이닐 댓글 내용을 입력해 주세요."),
    VINYL_COMMENT_DELETE_FORBIDDEN(HttpStatus.BAD_REQUEST, "E_013", "바이닐 댓글 삭제권한이 없는 사용자 입니다."),
    VINYL_COMMENT_MODIFY_FORBIDDEN(HttpStatus.BAD_REQUEST, "E_014", "바이닐 댓글 수정권한이 없는 사용자 입니다."),
    VINYL_COMMENT_NOTFOUND(HttpStatus.BAD_REQUEST,"E_015","찾을수 없는 바이닐 댓글 입니다."),
    VINYL_COMMENT_EMPTY(HttpStatus.BAD_REQUEST,"E_016","바이닐 댓글이 존재하지 않습니다."),

    VINYL_LIKE_ALREADY(HttpStatus.BAD_REQUEST,"E_017","이미 좋아요한 바이닐 입니다."),
    VINYL_LIKE_NOT(HttpStatus.BAD_REQUEST,"E_018","좋아요 해놓지 않은 바이닐 입니다."),



    SIGNUP_EMPTY_USERNAME(HttpStatus.BAD_REQUEST, "E_019","아이디를 입력해 주세요."),
    SIGNUP_ALREADY_USERNAME(HttpStatus.BAD_REQUEST,"E_020","중복된 아아디 입니다."),
    SIGNUP_EMPTY_EMAIL(HttpStatus.BAD_REQUEST,"E_021","이메일을 입력해 주세요."),
    SIGNUP_ALREADY_EMAIL(HttpStatus.BAD_REQUEST,"E_022","중복된 이메일 입니다."),
    SIGNUP_EMPTY_PASSWORD(HttpStatus.BAD_REQUEST,"E_023","비밀번호를 입력해 주세요."),
    SIGNUP_EMPTY_PASSWORD_CHECK(HttpStatus.BAD_REQUEST,"E_024","비밀번호확인을 입력해 주세요."),
    SIGNUP_PASSWORD_CHECK(HttpStatus.BAD_REQUEST,"E_025","비밀번호와 비밀번호 확인이 일치하지 않습니다."),
    SIGNUP_EMPTY_EMAIL_CHECK(HttpStatus.BAD_REQUEST,"E_026","이메일 인증 코드를 입력해 주세요."),
    SIGNUP_EMAIL_CHECK(HttpStatus.BAD_REQUEST, "E_027", "이메일 인증 코드를 확인헤 주세요."),

    REFRESH_TOKEN_NOT_VALID(HttpStatus.BAD_REQUEST,"E_028","유효하지 않은 토큰입니다."),

    TOKEN_REISSUE(HttpStatus.BAD_REQUEST,"E_029","재로그인이 필요합니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorMsg;
}
