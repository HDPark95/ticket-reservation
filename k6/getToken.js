import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
  vus: 300,
  duration: '5m',
};

export default function () {
  const seatId = 1;
  const headers = {
    'Content-Type': 'application/json',
    'X-Waiting-Token': '32cfd9d3-4555-4baa-8422-92e95f058e57'
  };

  const reserveRes = http.get('http://localhost:8080/api/v1/token', { headers });

  check(reserveRes, { '토큰 조회 성공': (r) => r.status === 201 || r.status === 400 });
}
