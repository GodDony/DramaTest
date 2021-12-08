# 과제 구조

# 1. Clean Architectrue + MVVM + DI(Hilt) + Repository Pattern

- Presentation Layer.  
화면과 입력에 대한 처리 등 UI와 관련된 부분을 담당합니다.  
Domain 계층에 대한 의존성을 가지고 있습니다. (UseCase를 주입받아 데이터를 가져오게 되어 있습니다.)  

- Domain Layer.   
UseCase와 을 Model을 포함하고 있습니다. (각 계층마다 모델은 존재하나 Doamin 모델은 변경되면 안되는 영역)  
Presentation, Data 계층에 대한 의존성을 가지지 않고 독립적으로 분리되어 있습니다.  

- Data Layer.  
데이터베이스, 서버와의 통신등을 담당하고 있습니다.  
