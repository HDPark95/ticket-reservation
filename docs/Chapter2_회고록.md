### 콘서트 예약 시스템 개발 회고록

3주간 진행한 콘서트 예약 시스템 개발 프로젝트를 진행하면서 아래 세 가지 내용을 완벽하진 않지만 어느 정도 이해하고 적용할 수 있게 되었습니다. 또한 코드를 작성할 때 먼저 이 세 가지를 고려하게 되었습니다.

1. 클린 아키텍처

2. 테스트 코드

3. 동시성 처리

클린 아키텍처를 적용하면서 비즈니스 로직이 외부 프레임워크나 라이브러리에 의존하지 않게 되었습니다. 또한 각 계층의 DTO를 분리하면서 인터페이스의 변화에 대응하기 쉬워졌습니다.

테스트 코드를 작성하면서 비즈니스 로직을 변경해도 기존의 기능이 잘 동작하는지 확인할 수 있었습니다. 어느 정도 사이드 이펙트가 발생할지 예측하는 것이 아닌 직접 눈으로 확인할 수 있었습니다.

단순히 성공하는 코드가 아닌 동시성 처리에 대해 고민하며, 조회 후 수정, 조회 후 등록 등의 작업에서 데이터베이스의 락을 어떻게 걸어야 할지 고려하게 되었습니다.







