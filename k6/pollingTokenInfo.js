import http from 'k6/http';

export let options = {
    vus: 100, // 동시 사용자 10명
    duration: '30s', // 30초 동안 테스트 실행
};

export default function () {
    let token = "815f0a39-37d0-40ee-ab83-92ffc43cb5a3";

    let headers = {
        'Content-Type': 'application/json',
        'X-Waiting-Token': token
    };

    let res = http.get('http://localhost:8080/api/v1/token', { headers: headers });
}