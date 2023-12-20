from django.urls import path
# view 파일도 임포트 해줘야 한다.
# 현재경로에 views를 가져온다
from . import views 


app_name= 'travel'

urlpatterns = [
    path('popup/', views.findplace, name='findplace'),   # views 파일에 show 함수를 호출한다.
    path('popup/', views.info, name='info'),   # views 파일에 show 함수를 호출한다.
     path('model/', views.model, name='model'),   # views 파일에 show 함수를 호출한다.
]