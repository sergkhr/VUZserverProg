import datetime
import hashlib
import io

from django.http import HttpResponse, JsonResponse
from django.shortcuts import render
from django.utils import timezone
import matplotlib
matplotlib.use('Agg')
from matplotlib import pyplot as plt
from .models import DataDiagram, FileData
import json
from django.views.decorators.csrf import csrf_exempt
from django.db.models import Count


@csrf_exempt
def receive_statistics(request):
    print("Recieved: " + request.body.decode('utf-8'))
    if request.method == 'POST':
        print(1)
        try:
            print(2)
            data = json.loads(request.body.decode('utf-8'))
            
            filename = '.'.join(data.get('filename', '').split('.')[0:-1])
            fileExt = data.get('filename', '').split('.')[-1]
            filesize = data.get('filesize', 0)
            file_id = data.get('id', '')

            if filename and filesize and file_id and fileExt:
                print(3)
                file_data = FileData(filename=filename, filesize=filesize, id=file_id, fileExt=fileExt)
                file_data.save()
                return JsonResponse({'status': 'success'})
            print(4)
            return JsonResponse({'status': 'failure'})
            
        except json.JSONDecodeError:
            print(5)
            return JsonResponse({'status': 'error', 'message': 'Invalid JSON format in the request body'}, status=400)

    print(6)
    return JsonResponse({'status': 'error', 'message': 'Invalid request method'}, status=405)


@csrf_exempt
def file_stats_view(request):
    if (request.method == 'GET'):
        file_type_counts = FileData.objects.values('fileExt').annotate(count=Count('fileExt'))
        hash_value = hashlib.sha256(json.dumps(list(file_type_counts)).encode('utf-8')).hexdigest()
        diagram = DataDiagram.objects.filter(hash_value=hash_value, diagram_type='extensions').first()
        if diagram is not None:
            return HttpResponse(diagram.image, content_type="image/png")
        
        fig, ax = plt.subplots(figsize=(20, 11))
        ax.pie([file['count'] for file in file_type_counts], labels=[file['fileExt'] for file in file_type_counts])
        ax.set_title('File extensions')
        fig.text(0.5, 0.01, str(timezone.localtime()), horizontalalignment='center', verticalalignment='bottom', transform=ax.transAxes)

        fig.tight_layout()
        image_bytes = io.BytesIO()
        plt.savefig(image_bytes, format='png')
        plt.close(fig)

        diagram = DataDiagram(hash_value=hash_value, diagram_type='extensions', image=image_bytes.getvalue())
        diagram.save()
        return HttpResponse(diagram.image, content_type="image/png")

    return JsonResponse({'status': 'error', 'message': 'Invalid request method'}, status=405)
