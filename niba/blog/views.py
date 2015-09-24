from django.shortcuts import render
 
# Create your views here.
from django.shortcuts import render
from django.template import loader,Context
from django.http import HttpResponse
from blog.models import Blog
  
# Create your views here.
  
def archive(request):
    posts = Blog.objects.all()
    t = loader.get_template("archive.html")
    c = Context({'posts':posts})
    return HttpResponse(t.render(c))
