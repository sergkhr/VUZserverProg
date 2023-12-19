from django.db import models


class DataDiagram(models.Model):
    hash_value = models.CharField(max_length=200)
    diagram_type = models.CharField(max_length=200)
    image = models.BinaryField()

    def __str__(self):
        return f"{self.hash_value} {self.diagram_type}"


class FileData(models.Model):
    filename = models.CharField(max_length=255)
    fileExt = models.CharField(max_length=10)
    filesize = models.IntegerField()
    id = models.CharField(max_length=24, primary_key=True)

    def __str__(self):
        return f"{self.filename} ({self.id})"
