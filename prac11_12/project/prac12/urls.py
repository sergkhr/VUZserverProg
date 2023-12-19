from django.contrib import admin
from django.urls import path

from .views import receive_statistics, file_stats_view

urlpatterns = [
    path('post', receive_statistics),
    path('get', file_stats_view),
]
