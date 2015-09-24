from django.contrib import admin
from blog.models import Author, Blog, Tag
 
 
# Register your models here.
class TagAdmin(admin.ModelAdmin):
    """docstring for AuthorAdmin"""
    list_display = ('tag_name', 'create_time')
 
class AuthorAdmin(admin.ModelAdmin):
    """docstring for AuthorAdmin"""
    list_display = ('name', 'email', 'website')
    search_fields = ('name',)
 
class BlogAdmin(admin.ModelAdmin):
    """docstring for BlogAdmin"""
    list_display = ('caption', 'id', 'author', 'publish_time')
    list_filter = ('publish_time',)
#     date_hierarchy = 'publish_time'
    ordering = ('-publish_time',)
    filter_horizontal = ('tags',)

admin.site.register(Tag, TagAdmin)
admin.site.register(Author, AuthorAdmin)
admin.site.register(Blog, BlogAdmin)
