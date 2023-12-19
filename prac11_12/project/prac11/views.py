from django.shortcuts import render
from django.http import HttpResponse


def hello_world(request, username=""):
    if username:
        message = f"Hello, {username}!"
    else:
        message = "Hello, World!"
    return HttpResponse(message)
