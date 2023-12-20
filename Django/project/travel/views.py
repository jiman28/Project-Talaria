# 사용자에게 view를 랜더링 해주는 것이다.
# from django.shortcuts import render
# import numpy as np
from django.http import JsonResponse
# from django.views.decorators.csrf import csrf_exempt
# from django.conf import settings
import googlemaps
# from django.utils.decorators import method_decorator

import requests
from bs4 import BeautifulSoup
import os
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By   
import time 
from pandas import Series, DataFrame
from tqdm.notebook import tqdm
import numpy as np
import pandas as pd
from selenium.webdriver.chrome.service import Service as ChromeService
import datetime
from selenium.webdriver.support.ui import Select
import time
import csv
import requests
import os
from tqdm.notebook import tqdm
import time 
from pandas import Series, DataFrame
import numpy as np
import pandas as pd
from bs4 import BeautifulSoup
from selenium import webdriver
from selenium.webdriver.chrome.service import Service as ChromeService
from webdriver_manager.chrome import ChromeDriverManager
from sklearn.cluster import KMeans
import matplotlib.pyplot as plt
from sklearn import preprocessing
import random as random
import json
## 팝업창 자동 닫기 selenium 사용처가 많음





# Create your views here.

def model(request):

    userinfo = request.POST['user']
    userList = request.POST['userList']

    json_object = json.loads(userinfo)
    json_object_List = json.loads(userList)
    for i in json_object_List:
        i["userId"] = i["user"]["id"]
        del i["user"]
    userId = json_object[0]["user"]["id"]
    print(userId)
    print(json_object_List[0])

    # for i in json_object_List:
    times_data = pd.DataFrame(json_object_List)
    times_data = times_data.drop(labels=["id","reliability"],axis=1)
    print(times_data.head(100))
    arr = comb2(['culture','food','history','nature','religion','sights'])
    suggestion_list = []
    j = 1
    tmp = []
    for i in arr:
        
        estimator = KMeans(n_clusters = 4,n_init=10)
        ids = estimator.fit_predict(times_data[[i[0], i[1]]])
        dc = {0:[],1:[],2:[],3:[]}

        for index, A, B,C,D,E,F,K in times_data.itertuples():
            dc[ids[index]].append(K)
            if userId == K:
                userId = ids[index]

        if j % 3 != 0:
            tmp.extend(dc[userId])
        else:
            suggestion_list.append(set(tmp))
            tmp = []
        j = j + 1
    
    
    c = suggestion_list[0] #& suggestion_list[1] & suggestion_list[2]& suggestion_list[3]
    # c = suggestion_list[0] & suggestion_list[1] & suggestion_list[2]& suggestion_list[3]& suggestion_list[4]
    print("end")
    print(c)
    data = {"userList" : list(c)}
    return JsonResponse(data)


# 장고에서 데이터 뽑아서 보여주도록 하는 것
def findplace(request):

    search_name = request.POST['search']
    print("실행")
    list = info(search_name)
    findI = imagefind(search_name)    

    data = {
        'name' : list[0],
        'global_code' : list[1],
        'compound_code' : list[2],
        'address' : list[3],
        'lat' : list[4],
        'lng' : list[5],
        # 'image' : list[6],
        'image':findI[0],

        }

    return JsonResponse(data)

def info(name):
    try:
        gmaps_key = 'Your_Google_Map_Api_Key_Here'
        gmaps = googlemaps.Client(key = gmaps_key)
    
        place = gmaps.geocode(name,language='ko')
        test = place[0].get('plus_code')
        if test == None:
            print('I do not have plus_code')
            global_code = "GC없음"
            compound_code = "CC없음"
        else:
            global_code = place[0]['plus_code']['global_code']
            compound_code = place[0]['plus_code']['compound_code']

        spot_name = name
        address = place[0]['formatted_address']
        lat = place[0]['geometry']['location']['lat']
        lng = place[0]['geometry']['location']['lng']
        # image = 'image'

        return [spot_name, global_code , compound_code, address, lat, lng]
        # return [spot_name, global_code , compound_code, address, lat, lng, image]
    except:
        print("asdf")
        spot_name = "Null"
        global_code = "Null"
        compound_code = "Null"
        address = "Null"
        lat = "Null"
        lng = "Null"
        return [spot_name, global_code , compound_code, address, lat, lng]
    
def imagefind(name):

    options = webdriver.ChromeOptions()
    options.add_argument("headless")

    url = 'https://www.google.com/search?q='+name+'대표+사진'
    driver = webdriver.Chrome(service=Service('C:/source/python/web_scrap/driver/chromedriver.exe'), options=options)
    driver.get(url)

    try:
        driver.find_element(By.XPATH, '//div[@id="iur"]/div[2]/div[1]/div[1]/g-scrolling-carousel[1]/div[1]/div[1]/div[1]').click()
        time.sleep(4)
        image = BeautifulSoup(driver.page_source,'lxml').find('img', jsaction="VQAsE").get('src')
        return [image]
        
    except:
        image = "사진없음"
        return [image]
    


def comb2(arr):
    result = []
    for i in range(len(arr)):
        for j in arr[i + 1:]:
            result.append((arr[i], j))
    return result