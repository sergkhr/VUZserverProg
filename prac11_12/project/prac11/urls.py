from django.contrib import admin
from django.urls import path
from .views import hello_world

urlpatterns = [
    path('<str:username>', hello_world),
    path('', hello_world),
]
