# Project Talaria

## 날씨API를 활용한 여행 계획 생성 및 머신러닝 기반 유저 추천 서비스
#### Team Name:  트래비앙
#### Team Member
- [Leejs-1014](https://github.com/Leejs-1014) : AI 개발, 백엔드 개발, 프론트엔드 개발, 데이터베이스 구축
- [idgod8451](https://github.com/idgod8451) : 백엔드 개발, 프론트엔드 개발, 서버 구축
- [NoelWON](https://github.com/NoelWON) : 백엔드 개발, 프론트엔드 개발, 데이터베이스 구축
- [jiman28](https://github.com/jiman28) : 백엔드 개발, 안드로이드 개발
#### Demonstration Video
- https://drive.google.com/file/d/15BRrBIHSfSPXBAsYwlD1Qxs_opiFTvps/view?usp=sharing


## Project Introduction
현재 여행 수요의 지속적인 증가에 대응하여 개인의 취향과 여행 목적에 부합하는 맞춤형 여행 계획 작성 서비스를 목표로 하고 있습니다. Google API와 날씨 API를 활용하여 사용자에게 정확하고 편리한 여행 정보를 제공하며, 머신러닝 기반의 군집화 알고리즘을 활용하여 사용자의 여행 성향과 유사한 그룹을 형성하여 사용자를 추천해줍니다.
사용자가 여행을 계획하는 과정에서 날씨 정보를 포함시켜 구체적인 계획수립을 도와주고 사용자가 선택한 관광지들을 토대로 여행 성향을 분석하여 성향이 비슷한 유저의 플랜을 확인 할 수 있습니다.
웹과 모바일을 동시에 서비스하여 사용자의 편의성을 도모하였습니다.


## Design focus (App)
- 본 프로젝트의 웹개발 부분과 같은 서비스 제공 (웹과 모바일 동시 서비스)


## Project Result
- '2023년 벤처·스타트업 아카데미 통합 프로젝트 발표회' 최우수상 수상


## Settings
- All API KEYs and other passwords were deleted. You must use your own Keys, ID and Password

#### Spring: STS4 => application.properties
- need to create MySQL DB named 'project'
- need MongoDB installed, DB name 'test'
- need MySQL ID & Password
- need OAuth 2.0 client ID & secret
- need [Google Map Api Key](https://mapsplatform.google.com/)
- need [OpenWeatherMap Api Key](https://openweathermap.org/api) -> WeatherByGPSApplication.java

#### Django = VS code
- need [Google Map Api Key](https://mapsplatform.google.com/)

#### Android: Android Studio => local.properties (SDK Location)
###### Please follow the instructions below
1. Go to your Project then Create a file with the following name: local.properties
2. Open the file and write the followings below:
   sdk.dir=C\:\\Users\\your_UserName_directory\\AppData\\Local\\Android\\Sdk
   GOOGLE_MAPS_API_KEY=Your_Google_Maps_Api_Key_Here
   BASE_URL=http://Your_IP_Address:8080/m/
   BASE_BOARD_SUMMERNOTE=http://Your_IP_Address:8080/
