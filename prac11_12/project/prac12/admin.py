from django.contrib import admin
from .models import FileData


@admin.register(FileData)
class FileDataAdmin(admin.ModelAdmin):
    list_display = ('filename', 'filesize', 'id', 'fileExt')
    search_fields = ('filename', 'id')
